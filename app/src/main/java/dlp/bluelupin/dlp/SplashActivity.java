package dlp.bluelupin.dlp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.ApplicationVersionResponse;
import dlp.bluelupin.dlp.Models.CacheServiceCallData;
import dlp.bluelupin.dlp.Models.ContentServiceRequest;
import dlp.bluelupin.dlp.Services.IAsyncWorkCompletedCallback;
import dlp.bluelupin.dlp.Services.IServiceSuccessCallback;
import dlp.bluelupin.dlp.Services.ServiceCaller;
import dlp.bluelupin.dlp.Services.ServiceHelper;
import dlp.bluelupin.dlp.Utilities.CardReaderHelper;
import dlp.bluelupin.dlp.Utilities.CustomProgressDialog;
import dlp.bluelupin.dlp.Utilities.LocationUtility;
import dlp.bluelupin.dlp.Utilities.Utility;
import dlp.bluelupin.dlp.runtimepermission.PermissionResultCallback;
import dlp.bluelupin.dlp.runtimepermission.PermissionUtils;

/**
 * Created by Neeraj on 7/22/2016.
 */
public class SplashActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback, PermissionResultCallback {

    private Context context;
    private CustomProgressDialog customProgressDialog;
    private boolean force_update;
    private String version;
     AlertDialog alert;
    PermissionUtils permissionUtils;
    ArrayList<String> permissions = new ArrayList<>();
    private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        context = SplashActivity.this;
        Consts.playYouTubeFlag = true;//for play online you tube video device back press handel
        if (Utility.isTablet(this)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        runTimePermission();
        LocationUtility.getmFusedLocationClient(context);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
    private void runTimePermission() {
        permissionUtils = new PermissionUtils(SplashActivity.this);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WAKE_LOCK);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionUtils.check_permission(permissions, "Storage Services Permissions are required for this App.", REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
    }

    public void MoveNextScreen() {
//        Utility.verifyStoragePermissions((Activity) SplashActivity.this);
        if (Consts.IS_DEBUG_LOG) {
            Log.d(Consts.LOG_TAG, "calling start ");
        }
        DbHelper dbHelper = new DbHelper(context);
        AccountData sd = dbHelper.getAccountData();
        if (determineFirstTimeLaunch()) {
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "firstTimeLaunch " + Utility.getAppVersion(context) + "");
            }
            copyDBWhenFirstRun();
            markFirstTimeLaunch();
        } //determineFirstTimeLaunch
        callGetAllLanguage();
    }

    private Boolean determineFirstTimeLaunch() {
        SharedPreferences prefs = context.getSharedPreferences("appState", Context.MODE_PRIVATE);
        if (prefs != null) {
            String firstTimeLaunchVersion = prefs.getString("firstTimeLaunch", null);
            if (firstTimeLaunchVersion != null) {
                return false;
            }
        }
        return true;
//
    }


    private void markFirstTimeLaunch() {
        SharedPreferences prefs = context.getSharedPreferences("appState", Context.MODE_PRIVATE);
        if (prefs != null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("firstTimeLaunch", Utility.getAppVersion(context) + "");
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "firstTimeLaunch " + Utility.getAppVersion(context) + "");
            }
            editor.commit();
        }
    }
    //check user registered or not
    private void checkRegistered() {

        DbHelper dbhelper = new DbHelper(SplashActivity.this);
        AccountData accountData = dbhelper.getAccountData();
        if (accountData != null && !accountData.equals("")) {
            if (accountData.getIsVerified() == 1) {
                //invokeServiceForBackgroundUpdate(); // app already ran once; put all updates in background
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            } else {
                dbhelper.deleteAccountData();
                // app running for the first time
                Intent mainIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(mainIntent);
                finish();
            }
        } else {
            // app running for the first time
            Intent mainIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }


    private void copyDBWhenFirstRun() {
        String db_fileName = Consts.dataBaseName;
        File dbFile = context.getDatabasePath(db_fileName);
        dbFile.delete(); // because this is first time delete the DB
        final String outFileName = dbFile.getPath();//"/data/data/dlp.bluelupin.dlp/databases/" + db_fileName;// Consts.outputDirectoryLocation + db_fileName; //DB_PATH + NAME;
        try {
            if (!dbFile.exists()) {
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "DB DOES NOT exists. Copying DB from asset. DB Path is " + outFileName);
                }
                //this.getWritableDatabase().close();
                //Open your local db as the input stream

                final InputStream myInput = context.getAssets().open(db_fileName, Context.MODE_PRIVATE);

                //Open the empty db as the output stream
                final OutputStream myOutput = new FileOutputStream(outFileName);
                //final FileOutputStream myOutput = context.openFileOutput(outFileName, Context.MODE_PRIVATE);

                //transfer bytes from the inputfile to the outputfile
                final byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                //Close the streams
                myOutput.flush();
                ((FileOutputStream) myOutput).getFD().sync();
                myOutput.close();
                myInput.close();
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "DB file copied Successfully " + outFileName);
                }
            }
        } catch (IOException ex) {
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "Exception!!!. DB asset DOES  NOT exists!! " + ex.getMessage());
            }
        } catch (Exception ex) {
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "Exception!!!. DB asset DOES  NOT exists!! " + ex.getMessage());
            }
        }

    }


    private void init() {
        //set language
        Utility.setLangRecreate(SplashActivity.this, Utility.getLanguageCodeFromSharedPreferences(SplashActivity.this));
        checkRegistered();
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                *//* Create an Intent that will start the Menu-Activity. *//*
                checkRegistered();
            }
        }, 800);*/
    }

    //Language service call
    public void callGetAllLanguage() {
        if (Utility.isOnline(SplashActivity.this)) {
            final ServiceHelper sh = new ServiceHelper(SplashActivity.this);
            sh.calllanguagesService(new IServiceSuccessCallback<String>() {
                @Override
                public void onDone(final String callerUrl, String d, String error) {
                    //DbHelper db = new DbHelper(SplashActivity.this);
                    //List<LanguageData> data = db.getAllLanguageDataEntity();
                    //init();
                    readExternalFilesAsync();
                }
            });

        } else {
            readExternalFilesAsync();
            // init();
        }
    }

    //alert for error message
    public void alertForErrorMessage(String errorMessage, Context mContext) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        Typeface VodafoneExB = Typeface.createFromAsset(mContext.getAssets(), "fonts/VodafoneExB.TTF");
        final AlertDialog alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        View view = alert.getLayoutInflater().inflate(R.layout.custom_error_alert, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setTypeface(VodafoneExB);
        TextView ok = (TextView) view.findViewById(R.id.Ok);
        ok.setTypeface(VodafoneExB);
        LinearLayout layout_titleBar = (LinearLayout) view.findViewById(R.id.alert_layout_titleBar);
        View divider = (View) view.findViewById(R.id.divider);
        GradientDrawable drawable = (GradientDrawable) ok.getBackground();
        drawable.setStroke(5, Color.parseColor("#e60000"));// set stroke width and stroke color
        drawable.setColor(Color.parseColor("#e60000")); //
        title.setText(errorMessage);
        alert.setCustomTitle(view);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }


    public void checkForceUpdate() {

       /* new AsyncTask<Void, String, String>() {
            @Override
            protected String doInBackground(Void... voids) {

                String newVersion = null;
                try {
                    newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName() + "&hl=en")
                            .timeout(30000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get()
                            .select("div[itemprop=softwareVersion]")
                            .first()
                            .ownText();
                    return newVersion;
                } catch (Exception e) {
                    return newVersion;
                }
            }

            @Override
            protected void onPostExecute(String onlineVersion) {
                super.onPostExecute(onlineVersion);
                if (onlineVersion != null && !onlineVersion.isEmpty()) {
                    if (Float.valueOf(Utility.getAppVersionName(context)) < Float.valueOf(onlineVersion)) {
                        //show dialog
                        showUpdateDialog(context);
                    } else {
                        MoveNextScreen();
                    }
                }
                else {
                    MoveNextScreen();
                }

                Log.d("update", "Current version " + Utility.getAppVersionName(context) + "playstore version " + onlineVersion);
            }
        }.execute();*/


        if (Utility.isOnline(SplashActivity.this)) {
            final Float currentAppVersion = Float.valueOf(Utility.getAppVersionName(context));
            final ServiceHelper sh = new ServiceHelper(SplashActivity.this);
            final String callerUrl = "";
            sh.callVersionApiService(version, new IServiceSuccessCallback<ApplicationVersionResponse>() {
                        @Override
                        public void onDone(String callerUrl, ApplicationVersionResponse result, String error) {
                            if(result != null && result.getApp_version()!=null && result.getApp_version().getVersion()!=null) {

                                Float serverAppVersion = Float.valueOf(result.getApp_version().getVersion());
                                if(currentAppVersion<serverAppVersion){
                                    showUpdateDialog(context);
                                }
                                else {
                                    MoveNextScreen();
                                }

                            }else{
                                MoveNextScreen();
                            }
                        }
                    }
            );
        }
        else {
            MoveNextScreen();
        }
    }


    private void showUpdateDialog(final Context context) {
        //alert for error message

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        Typeface VodafoneExB = Typeface.createFromAsset(context.getAssets(), "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = Typeface.createFromAsset(context.getAssets(), "fonts/VodafoneRg.ttf");
        Typeface VodafoneRgBd = Typeface.createFromAsset(context.getAssets(), "fonts/VodafoneRgBd.ttf");
        alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        View view = alert.getLayoutInflater().inflate(R.layout.custom_update_alert, null);
        TextView title = (TextView) view.findViewById(R.id.textMessage);
        TextView title2 = (TextView) view.findViewById(R.id.textMessage2);
        title.setTypeface(VodafoneRgBd);
        title2.setTypeface(VodafoneRg);
        Button ok = (Button) view.findViewById(R.id.buttonUpdate);
        Button buttonCancel = (Button) view.findViewById(R.id.buttonCancel);
        ok.setTypeface(VodafoneExB);
        buttonCancel.setTypeface(VodafoneExB);
        alert.setCustomTitle(view);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "dlp.bluelupin.dlp" + "&hl=en"));
                context.startActivity(intent);
                alert.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                MoveNextScreen();
            }
        });
        alert.show();
        alert.setCanceledOnTouchOutside(false);
    }


    private void readExternalFilesAsync() {

        String messageText = getString(R.string.import_data);
        if (Utility.getSelectFolderPathFromSharedPreferences(SplashActivity.this) != null) {
            messageText = getString(R.string.import_data_from) + " " + Utility.getSelectFolderPathFromSharedPreferences(SplashActivity.this);
        }
        customProgressDialog = new CustomProgressDialog(SplashActivity.this, messageText, R.mipmap.syc);
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                CardReaderHelper cardReaderHelper = new CardReaderHelper(SplashActivity.this);
                String folderLocation = Utility.getSelectFolderPathFromSharedPreferences(SplashActivity.this);//Consts.outputDirectoryLocation;
                if (folderLocation != null) {
                    cardReaderHelper.ReadAppDataFolder(folderLocation);
                }
                return true;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (customProgressDialog != null) {
                    customProgressDialog.show();
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (Utility.isOnline(SplashActivity.this)) {
                    callSync();
                } else {
                    if (customProgressDialog != null && customProgressDialog.isShowing()) {
                        customProgressDialog.dismiss();
                    }
                    init();
                }

            }
        }.execute(null, null, null);

    }

    public void callSync() {
        callContentAsync();
        callResourceAsync();
        callMediaAsync();
        callQuizzesAsync();
        callQuizzesQuestionsAsync();
        callQuizzesQuestionsOptionsAsync();
        callContentQuizAsync();
        callMedialanguageLatestAsync();
    }

    private Boolean contentCallDone = false, resourceCallDone = false, mediaCallDone = false, mediaCallLatest = false;

    private void sendMessageIfAllCallsDone() {
        if (contentCallDone && resourceCallDone && mediaCallDone && mediaCallLatest) {
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "Sync Call done in Splash");
            }
            if (customProgressDialog != null && customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();
            }
            init();
        }
    }

    private void callContentAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(SplashActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.URL_CONTENT_LATEST);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(SplashActivity.this);
        sc.getAllContent(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                Log.d(Consts.LOG_TAG, "MainActivity: callContentAsync success result: " + isComplete);
                //DbHelper db = new DbHelper(MainActivity.this);
                //List<Data> data = db.getDataEntityByParentId(null);
                // Log.d(Consts.LOG_TAG, "MainActivity: data_item count: " + data.size());
                contentCallDone = true;
                sendMessageIfAllCallsDone();
            }
        });
    }

    private void callResourceAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(SplashActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.URL_LANGUAGE_RESOURCE_LATEST);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData for URL_LANGUAGE_RESOURCE_LATEST: " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(SplashActivity.this);
        sc.getAllResource(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
               /* Log.d(Consts.LOG_TAG, "MainActivity: callResourceAsync success result: " + isComplete);
                DbHelper db = new DbHelper(MainActivity.this);
                List<Data> data = db.getResources();
                Log.d(Consts.LOG_TAG, "MainActivity: callResourceAsync data_item count: " + data.size());*/
                resourceCallDone = true;
                sendMessageIfAllCallsDone();
            }
        });
    }


    private void callMediaAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(SplashActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.URL_MEDIA_LATEST);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData for URL_MEDIA_LATEST: " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(SplashActivity.this);
        sc.getAllMedia(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
               /* Log.d(Consts.LOG_TAG, "MainActivity: callMediaAsync success result: " + isComplete);
                DbHelper db = new DbHelper(MainActivity.this);
                List<Data> data = db.getAllMedia();
                Log.d(Consts.LOG_TAG, "MainActivity: callMediaAsync data_item count: " + data.size());*/
                mediaCallDone = true;
                sendMessageIfAllCallsDone();
            }
        });
    }

    private void callMedialanguageLatestAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(SplashActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.MediaLanguage_Latest);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(SplashActivity.this);
        sc.getAllMedialanguageLatestContent(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
              /*  if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "MainActivity: callMedialanguageLatestAsync success result: " + isComplete);
                }
                DbHelper db = new DbHelper(MainActivity.this);
                List<Data> data = db.getAllMedialanguageLatestDataEntity();
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "MainActivity: callMedialanguageLatestAsync data_item count: " + data.size());
                }*/
                mediaCallLatest = true;
                sendMessageIfAllCallsDone();
            }
        });

    }

    //get all Quizzes
    private void callQuizzesAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(SplashActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.Quizzes);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(SplashActivity.this);
        sc.getAllQuizzes(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
              /*  if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "MainActivity: callMedialanguageLatestAsync success result: " + isComplete);
                }
                DbHelper db = new DbHelper(MainActivity.this);
                List<Data> data = db.getAllMedialanguageLatestDataEntity();
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "MainActivity: callMedialanguageLatestAsync data_item count: " + data.size());
                }*/
               // mediaCallDone = true;
                sendMessageIfAllCallsDone();
            }
        });
    }

    //get all Quizzes Questions
    private void callQuizzesQuestionsAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(SplashActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.QuizzesQuestions);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(SplashActivity.this);
        sc.getAllQuizzesQuestions(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                sendMessageIfAllCallsDone();
            }
        });
    }

    //get all Quizzes Options
    private void callQuizzesQuestionsOptionsAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(SplashActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.QuizzesOptions);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(SplashActivity.this);
        sc.getAllQuizzesQuestionsOptions(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                sendMessageIfAllCallsDone();
            }
        });
    }

    //get all Content Quiz
    private void callContentQuizAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        ServiceCaller sc = new ServiceCaller(SplashActivity.this);
        sc.ContentQuiz(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                sendMessageIfAllCallsDone();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
        if (alert != null && alert.isShowing()) {
            alert.dismiss();
        }
    }

    @Override
    public void PermissionGranted(int request_code) {
        if (Utility.isOnline(context)) {
            checkForceUpdate();
        } else {
            MoveNextScreen();
        }
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {

    }

    @Override
    public void PermissionDenied(int request_code) {

    }

    @Override
    public void NeverAskAgain(int request_code) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            if (Utility.isOnline(context)) {
                checkForceUpdate();
            } else {
                MoveNextScreen();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onResume() {
        super.onResume();
        LocationUtility.startLocationUpdates(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationUtility.stopLocationUpdates();
    }
}
