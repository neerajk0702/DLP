package dlp.bluelupin.dlp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import dlp.bluelupin.dlp.Activities.LanguageActivity;
import dlp.bluelupin.dlp.Adapters.NavigationMenuAdapter;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.Fragments.CourseFragment;
import dlp.bluelupin.dlp.Fragments.WebFragment;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.AccountServiceRequest;
import dlp.bluelupin.dlp.Services.BackgroundSyncService;
import dlp.bluelupin.dlp.Services.IAsyncWorkCompletedCallback;
import dlp.bluelupin.dlp.Services.ServiceCaller;
import dlp.bluelupin.dlp.Utilities.CustomProgressDialog;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.Utility;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int PERMISSION_REQUEST_CODE = 1;
    private Toolbar toolbar;
    public TextView title, question, question_no, totalQuestion;
    public FrameLayout downloadContainer;
    private static MainActivity mainActivity;
    private CustomProgressDialog customProgressDialog;
    public static MainActivity getInstace() {
        return mainActivity;
    }
    private TextView name, email;
    AlertDialog alert;
    public ImageView splashImage;
    private LinearLayout ques_layout;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    GregorianCalendar calendar;
    private long timeIntervel = 18000000;
    SharedPreferences offlineAccountPref;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
        Typeface VodafoneExB = Typeface.createFromAsset(this.getAssets(), "fonts/VodafoneExB.TTF");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        title.setTypeface(VodafoneExB);
        question = (TextView) toolbar.findViewById(R.id.question);
        question_no = (TextView) toolbar.findViewById(R.id.question_no);
        totalQuestion = (TextView) toolbar.findViewById(R.id.totalQuestion);
        ques_layout = (LinearLayout) toolbar.findViewById(R.id.question_layout);
        question_no.setTypeface(VodafoneExB);
        question.setTypeface(VodafoneExB);
        totalQuestion.setTypeface(VodafoneExB);
        customProgressDialog = new CustomProgressDialog(this, R.mipmap.syc);
        if (Utility.isTablet(this)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        offlineAccountPref = getSharedPreferences("offlineAccountPref", Context.MODE_PRIVATE);
        //alertForProgressBar();

        Utility.setLangRecreate(MainActivity.this, Utility.getLanguageCodeFromSharedPreferences(MainActivity.this));
        downloadContainer = (FrameLayout) findViewById(R.id.downloadContainer);
        final DbHelper dbhelper = new DbHelper(this);
        splashImage = (ImageView) findViewById(R.id.splashImage);
        //splashImage.setVisibility(View.VISIBLE);

         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Typeface VodafoneRg = FontManager.getFontTypeface(this, "fonts/VodafoneRg.ttf");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
        //navigationView.setItemIconTintList(null);

        //View header = navigationView.getHeaderView(0);
        Typeface custom_fontawesome = FontManager.getFontTypeface(this, "fonts/fontawesome-webfont.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        View header = findViewById(R.id.navHader);
        name = (TextView) header.findViewById(R.id.name);
        email = (TextView) header.findViewById(R.id.email);
        TextView logOut = (TextView) header.findViewById(R.id.logOut);
        name.setTypeface(VodafoneExB);
        email.setTypeface(VodafoneRg);
        logOut.setTypeface(materialdesignicons_font);
        logOut.setText(Html.fromHtml("&#xf343;"));
        TextView createBy = (TextView) findViewById(R.id.createBy);
        createBy.setTypeface(VodafoneRg);
        LinearLayout poweredBylayout = (LinearLayout) findViewById(R.id.poweredBylayout);

        setProfileDetails();
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper dbhelper = new DbHelper(MainActivity.this);
                dbhelper.deleteAccountData();
                dbhelper.deleteFavoriteData();
                String msg = getString(R.string.logout);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(MainActivity.this, LanguageActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                finish();
            }
        });

        //registerReceiver();
        String localFolderPath = Consts.outputDirectoryLocation;
        File outputFolder = new File(localFolderPath);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        setMenuLayout();

        poweredBylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                WebFragment fragment = WebFragment.newInstance("http://bluelup.in/pwrdby", "");
                fragmentManager.beginTransaction().setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right)
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
                closeDrawer();//nav drawer close
            }
        });
        invokeServiceForBackgroundUpdate(); // app already ran once; put all updates in background
        setUpCourseFragment();
        //readExternalFilesAsync();

        if (offlineAccountPref != null) {
            boolean firstTimeLaunchVersion = offlineAccountPref.getBoolean("AccountCreate", false);
            if (firstTimeLaunchVersion) {
                callCreateAccountService();
            }
        }
    }

    //alert for progress bar
    public void alertForProgressBar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.alertAnimation;
        View view = alert.getLayoutInflater().inflate(R.layout.progressbar_alert, null);
        alert.setCustomTitle(view);
        alert.setCanceledOnTouchOutside(false);
    }

    //set user profile
    public void setProfileDetails() {
        DbHelper dbhelper = new DbHelper(MainActivity.this);
        AccountData accountData = dbhelper.getAccountData();
        if (accountData != null && !accountData.equals("")) {
            name.setText(accountData.getName());
            email.setText(accountData.getEmail());
        }
    }

   /* private void readExternalFilesAsync() {
        Utility.verifyStoragePermissions((Activity) MainActivity.this);
        //splashImage.setVisibility(View.VISIBLE);
        String messageText = getString(R.string.import_data);
        if (Utility.getSelectFolderPathFromSharedPreferences(MainActivity.this) != null) {
            messageText = getString(R.string.import_data_from) + " " + Utility.getSelectFolderPathFromSharedPreferences(MainActivity.this);
        }
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(MainActivity.this, messageText, R.mipmap.syc);
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                CardReaderHelper cardReaderHelper = new CardReaderHelper(MainActivity.this);
                String folderLocation = Utility.getSelectFolderPathFromSharedPreferences(MainActivity.this);//Consts.outputDirectoryLocation;
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
                //alert.show();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                customProgressDialog.dismiss();
                //alert.dismiss();
                if (Utility.isOnline(MainActivity.this)) {
                    callSync();
                } else {
                    Utility.alertForErrorMessage(getString(R.string.online_msg), MainActivity.this);
                    //setUpCourseFragment();
                }

            }
        }.execute(null, null, null);

    }*/


    @Override
    protected void onPause() {
        super.onPause();
        if (customProgressDialog != null) {
            if (customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();
            }
        }
    }



    //set slider item value
    public void setMenuLayout() {
        Typeface VodafoneRgBd = Typeface.createFromAsset(getAssets(), "fonts/VodafoneRgBd.ttf");

        ListView menuList = (ListView) findViewById(R.id.lst_menu_items);
        menuList.setDivider(null);
        List<String> itemList = new ArrayList<String>();
        itemList.add("Home");
        itemList.add("Notification");
        itemList.add("Favorites");
        itemList.add("Downloads");
        itemList.add("Profile");
        itemList.add("Change Language");
        itemList.add("Change Downloads Folder");
        itemList.add("Terms of use");
        itemList.add("Refer a Friend");
        itemList.add("Certificates");
        //itemList.add("About Us");

        List<String> menuIconList = new ArrayList<String>();
        menuIconList.add("f2dc");
        menuIconList.add("f09c");
        menuIconList.add("f4ce");
        menuIconList.add("f1da");
        menuIconList.add("f631");
        menuIconList.add("f493");
        menuIconList.add("f24d");
        menuIconList.add("f219");
        menuIconList.add("f219");
        menuIconList.add("f219");
       // menuIconList.add("f2fd");

        List<String> displayNameList = new ArrayList<String>();
        displayNameList.add(getString(R.string.home));
        displayNameList.add(getString(R.string.notification));
        displayNameList.add(getString(R.string.Favorites));
        displayNameList.add(getString(R.string.Downloads));
        displayNameList.add(getString(R.string.Profile));
        displayNameList.add(getString(R.string.Change_Language));
        displayNameList.add(getString(R.string.Change_Folder));
        displayNameList.add(getString(R.string.Terms_of_use));
        displayNameList.add(getString(R.string.referfriend));
        displayNameList.add(getString(R.string.certificates));
       // displayNameList.add(getString(R.string.about_us));
        NavigationMenuAdapter navigationMenuAdapter = new NavigationMenuAdapter(MainActivity.this, itemList, menuIconList, displayNameList);
        menuList.setAdapter(navigationMenuAdapter);
    }

    private void setUpCourseFragment() {
        if (Consts.playYouTubeFlag) {//for play online you tube video device back press handel
            FragmentManager fragmentManager = getSupportFragmentManager();
            CourseFragment fragment = CourseFragment.newInstance("", "");
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    //.addToBackStack(null)
                    .commitAllowingStateLoss();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
      /*  // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.notification) {
            //item.setIcon(R.drawable.ic_media_play);
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, LanguageActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
        } else if (id == R.id.zip) {
            Utility.verifyStoragePermissions(this);

            CardReaderHelper cardReaderHelper = new CardReaderHelper(MainActivity.this);
            String SDPath = Utility.getSelectFolderPathFromSharedPreferences(MainActivity.this);// get this location from sharedpreferance;
            if (SDPath != null && !SDPath.equals("")) {
                cardReaderHelper.readDataFromSDCard(SDPath);
            } else {
                cardReaderHelper.readDataFromSDCard(Consts.inputDirectoryLocation);
            }

        } else if (id == R.id.favorites) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            FavoritesFragment fragment = FavoritesFragment.newInstance("", "");
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right);
            transaction.replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.selectFolder) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            SelectLocationFragment fragment = SelectLocationFragment.newInstance("", "");
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right);
            transaction.replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.logout) {
            DbHelper dbhelper = new DbHelper(MainActivity.this);
            dbhelper.deleteAccountData();
            Toast.makeText(MainActivity.this, "You have been logged out.", Toast.LENGTH_LONG).show();
            Intent mainIntent = new Intent(MainActivity.this, LanguageActivity.class);
            startActivity(mainIntent);
            finish();
        }*/


        return true;
    }

    //close drawer after item select
    public void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        //return true;
    }

    public void setScreenTitle(String heading) {
        if (heading != null) {
            this.setTitle(Html.fromHtml(heading));
            title.setText(Html.fromHtml(heading));
        }
    }

    public void setShowQuestionIconOption(boolean on) {
        if (on) {
            ques_layout.setVisibility(View.VISIBLE);
        } else {
            ques_layout.setVisibility(View.GONE);
        }
    }

    public void hideSplashImage(Boolean flag) {
        if (flag) {
            splashImage.setVisibility(View.GONE);
        } else {
            splashImage.setVisibility(View.VISIBLE);
        }

    }

    public void setdownloadContainer(boolean on) {
        if (on) {
            downloadContainer.setVisibility(View.VISIBLE);
        } else {
            downloadContainer.setVisibility(View.GONE);
        }
    }

   /* public void callSync() {
        callContentAsync();
        callResourceAsync();
        callMediaAsync();
        callQuizzesAsync();
        callQuizzesQuestionsAsync();
        callQuizzesQuestionsOptionsAsync();
        callContentQuizAsync();
        callMedialanguageLatestAsync();
    }*/

   /* private Boolean contentCallDone = false, resourceCallDone = false, mediaCallDone = false,mediaCallLatest=false;

    private void sendMessageIfAllCallsDone() {
        if (contentCallDone && resourceCallDone && mediaCallDone && mediaCallLatest) {
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "Sync Call done in Splash");
            }
            if (customProgressDialog != null && customProgressDialog.isShowing()) {
                customProgressDialog.dismiss();
            }
            // alert.dismiss();
             setUpCourseFragment();//uncomment done to solve course image issue
        }
    }

    private void callContentAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(MainActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.URL_CONTENT_LATEST);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(MainActivity.this);
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
        DbHelper db = new DbHelper(MainActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.URL_LANGUAGE_RESOURCE_LATEST);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData for URL_LANGUAGE_RESOURCE_LATEST: " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(MainActivity.this);
        sc.getAllResource(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
               *//* Log.d(Consts.LOG_TAG, "MainActivity: callResourceAsync success result: " + isComplete);
                DbHelper db = new DbHelper(MainActivity.this);
                List<Data> data = db.getResources();
                Log.d(Consts.LOG_TAG, "MainActivity: callResourceAsync data_item count: " + data.size());*//*
                resourceCallDone = true;
                sendMessageIfAllCallsDone();
            }
        });
    }

    private void callMediaAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(MainActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.URL_MEDIA_LATEST);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData for URL_MEDIA_LATEST: " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(MainActivity.this);
        sc.getAllMedia(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
               *//* Log.d(Consts.LOG_TAG, "MainActivity: callMediaAsync success result: " + isComplete);
                DbHelper db = new DbHelper(MainActivity.this);
                List<Data> data = db.getAllMedia();
                Log.d(Consts.LOG_TAG, "MainActivity: callMediaAsync data_item count: " + data.size());*//*
                mediaCallDone = true;
                sendMessageIfAllCallsDone();
            }
        });
    }

    private void callMedialanguageLatestAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(MainActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.MediaLanguage_Latest);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(MainActivity.this);
        sc.getAllMedialanguageLatestContent(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
              *//*  if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "MainActivity: callMedialanguageLatestAsync success result: " + isComplete);
                }
                DbHelper db = new DbHelper(MainActivity.this);
                List<Data> data = db.getAllMedialanguageLatestDataEntity();
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "MainActivity: callMedialanguageLatestAsync data_item count: " + data.size());
                }*//*
                mediaCallLatest=true;
                sendMessageIfAllCallsDone();
            }
        });

    }

    //get all Quizzes
    private void callQuizzesAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(MainActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.Quizzes);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(MainActivity.this);
        sc.getAllQuizzes(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
              *//*  if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "MainActivity: callMedialanguageLatestAsync success result: " + isComplete);
                }
                DbHelper db = new DbHelper(MainActivity.this);
                List<Data> data = db.getAllMedialanguageLatestDataEntity();
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "MainActivity: callMedialanguageLatestAsync data_item count: " + data.size());
                }*//*
            }
        });
    }

    //get all Quizzes Questions
    private void callQuizzesQuestionsAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(MainActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.QuizzesQuestions);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(MainActivity.this);
        sc.getAllQuizzesQuestions(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {

            }
        });
    }

    //get all Quizzes Options
    private void callQuizzesQuestionsOptionsAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(MainActivity.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.QuizzesOptions);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "MainActivity: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(MainActivity.this);
        sc.getAllQuizzesQuestionsOptions(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {

            }
        });
    }

    //get all Content Quiz
    private void callContentQuizAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        ServiceCaller sc = new ServiceCaller(MainActivity.this);
        sc.ContentQuiz(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {

            }
        });
    }*/

    //call create account service when user in online mode
    private void callCreateAccountService() {

        int languageId = Utility.getLanguageIdFromSharedPreferences(this);
        final DbHelper dbhelper = new DbHelper(MainActivity.this);
        AccountData data = dbhelper.getAccountData();
        AccountServiceRequest accountServiceRequest = new AccountServiceRequest();
        if (data != null) {
                accountServiceRequest.setName(data.getName());
                accountServiceRequest.setEmail(data.getEmail());
                accountServiceRequest.setPhone(data.getPhone());
                accountServiceRequest.setPreferred_language_id(languageId);
                if (Utility.isOnline(this)) {
                    ServiceCaller sc = new ServiceCaller(MainActivity.this);
                    sc.CreateAccount(accountServiceRequest, new IAsyncWorkCompletedCallback() {
                        @Override
                        public void onDone(String workName, boolean isComplete) {

                            if (isComplete) {
                                if (offlineAccountPref != null) {
                                    SharedPreferences.Editor editor = offlineAccountPref.edit();
                                    editor.putBoolean("AccountCreate",false);//when data save into server
                                    editor.commit();
                                }
                                if (Consts.IS_DEBUG_LOG) {
                                    Log.d(Consts.LOG_TAG, " callCreateAccountService in offline mode success result: " + isComplete);
                                }
                                // Toast.makeText(MainActivity.this, getString(R.string.otp_sent), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
        }
    }
    //call service at every 5 hours of intervel
    private void invokeServiceForBackgroundUpdate() {

        calendar = (GregorianCalendar) Calendar.getInstance();
        if (Consts.IS_DEBUG_LOG) {
            Log.d(Consts.LOG_TAG, "starting invokeServiceForBackgroundUpdate" + Calendar.getInstance().getTime());
        }

        Intent myIntent = new Intent(MainActivity.this, BackgroundSyncService.class);
        Messenger messenger = new Messenger(new Handler() {
            public void handleMessage(Message message) {
                if (Consts.IS_DEBUG_LOG)
                    Log.d(Consts.LOG_TAG, "handleMessage invokeServiceForBackgroundUpdate " + Calendar.getInstance().getTime());
                Object path = message.obj;
                if (message.arg1 == RESULT_OK) {
                    // Toast.makeText(SplashActivity.this, "Done Sync", Toast.LENGTH_LONG).show();
                    if (Consts.IS_DEBUG_LOG)
                        Log.d(Consts.LOG_TAG, "Sync: Messenger: handleMessage");

                } else {
                    // Toast.makeText(SplashActivity.this, "sync failed.", Toast.LENGTH_LONG).show();
                    if (Consts.IS_DEBUG_LOG)
                        Log.d(Consts.LOG_TAG, "Sync Messenger: handleMessage failed");
                }

            }

            ;
        });
        myIntent.putExtra("MESSENGER", messenger);
        //myIntent.setData(Uri.parse("http://"));//
        myIntent.putExtra("appName", "some data_item");
        pendingIntent = PendingIntent.getService(MainActivity.this, 0,
                myIntent, 0);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                timeIntervel, pendingIntent);
    }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ContentServiceRequest request = new ContentServiceRequest();
//                callContentAsync();
//                callResourceAsync();
//                callMediaAsync();
//            };
////         Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//        });


    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(MainActivity.this, "Destroy call", Toast.LENGTH_LONG).show();
    }*/
    /**
     * To be semantically or contextually correct, maybe change the name
     * and signature of this function to something like:
     *
     * private void showBackButton(boolean show)
     * Just a suggestion.
     */

    public void enableViews(boolean enable) {

        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if(enable) {
            //You may not want to open the drawer on swipe from the left in this case
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            // Remove hamburger
            toggle.setDrawerIndicatorEnabled(false);
            // Show back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if(!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { // Doesn't have to be onBackPressed
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            //You must regain the power of swipe for the drawer.
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            // Remove back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }

        // So, one may think "Hmm why not simplify to:
        // .....
        // getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        // mDrawer.setDrawerIndicatorEnabled(!enable);
        // ......
        // To re-iterate, the order in which you enable and disable views IS important #dontSimplify.
    }
}


