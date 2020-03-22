package dlp.bluelupin.dlp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import dlp.bluelupin.dlp.Adapters.LanguageAdapter;
import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.MainActivity;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.Models.LanguageData;
import dlp.bluelupin.dlp.Models.LogsDataRequest;
import dlp.bluelupin.dlp.Models.ProfileUpdateServiceRequest;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Services.AppController;
import dlp.bluelupin.dlp.Services.IAsyncWorkCompletedCallback;
import dlp.bluelupin.dlp.Services.ServiceCaller;
import dlp.bluelupin.dlp.Utilities.CustomProgressDialog;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.LocationUtility;
import dlp.bluelupin.dlp.Utilities.LogAnalyticsHelper;
import dlp.bluelupin.dlp.Utilities.Utility;

/**
 * Created by Neeraj on 7/22/2016.
 */
public class LanguageActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView languageIcon, chooseLanguage, description;
    private Spinner spinner;
    private TextView tickIcon, done, title;
    private LinearLayout doneLayout;
    List<LanguageData> data;
    private String name_string;
    CustomProgressDialog customProgressDialog;
    SharedPreferences InstallationLogprefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
        setContentView(R.layout.activity_language);
        Consts.playYouTubeFlag = true;//for play online you tube video device back press handel
        if (Utility.isTablet(this)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();

        //Check Google play service
//        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
//        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
//
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Log.e(LOG_TAG, "This device is not supported.");
//                finish();
//            }
//        }
//
//        Log.i(TAG, "InstanceID token: " + FirebaseInstanceId.getInstance().getToken());
        //get device token
        String token = FirebaseInstanceId.getInstance().getToken();
        Utility.setDeviceIDIntoSharedPreferences(LanguageActivity.this, token);

        // Log and toast
        String msg = getString(R.string.msg_token_fmt, token);
        Log.d(Consts.LOG_TAG, msg);
        //Toast.makeText(LanguageActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    //get all id's
    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) toolbar.findViewById(R.id.title);
        // rootActivity.setShowQuestionIconOption(false);

        Typeface custom_fontawesome = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/fontawesome-webfont.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        Typeface VodafoneExB = FontManager.getFontTypeface(this, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(this, "fonts/VodafoneRg.ttf");

        languageIcon = (TextView) findViewById(R.id.languageIcon);
        chooseLanguage = (TextView) findViewById(R.id.chooseLanguage);
        description = (TextView) findViewById(R.id.description);
        done = (TextView) findViewById(R.id.done);
        doneLayout = (LinearLayout) findViewById(R.id.doneLayout);
        doneLayout.setOnClickListener(this);
        tickIcon = (TextView) findViewById(R.id.tickIcon);
        tickIcon.setTypeface(custom_fontawesome);
        title.setTypeface(VodafoneExB);
        done.setTypeface(VodafoneRg);
        languageIcon.setTypeface(custom_fontawesome);
        chooseLanguage.setTypeface(VodafoneExB);
        description.setTypeface(VodafoneRg);
        languageIcon.setText(Html.fromHtml("&#xf1ab;"));
        tickIcon.setText(Html.fromHtml("&#xf00c;"));

        spinner = (Spinner) findViewById(R.id.spinner);
        //spinner.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);

        /*List<String> list = new ArrayList<String>();
        list.add("English");
        list.add("Hindi");
        list.add("Telugu");
        list.add("Tamil");
        list.add("Kannada");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.language_item, data_item);*/

        DbHelper db = new DbHelper(LanguageActivity.this);
        data = db.getAllLanguageDataEntity();
        if (data != null) {
            LanguageAdapter languageAdapter = new LanguageAdapter(this, data);
            //languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(languageAdapter);
            int languagePos = Utility.getLanguagePositionFromSharedPreferences(this);
            spinner.setSelection(languagePos);//set default value
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setLanguage(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        InstallationLogprefs = getSharedPreferences("InstallationLog", Context.MODE_PRIVATE);
        if (InstallationLogprefs != null) {
            boolean Installation = InstallationLogprefs.getBoolean("Installation", false);
            if (!Installation) {
                logInstallationDetails();
            }
        }

    }
    //set select language into sharepreferences
    private void setLanguage(int langpos) {
        if (data != null) {
            String StringCode = data.get(langpos).getCode();
            String[] parts = StringCode.split("-");
            String code = parts[0];
            String part2 = parts[1];
            Utility.setLanguageIntoSharedPreferences(this, data.get(langpos).getId(), code, langpos);
            LogAnalyticsHelper analyticsHelper = new LogAnalyticsHelper(this);
            analyticsHelper.logSetUserProperty("Language", code);
        }
       /* switch (langpos) {
            case 0: //English
                Utility.setLanguageIntoSharedPreferences(this, EnumLanguage.en);
                return;
            case 1: //Hindi
                Utility.setLanguageIntoSharedPreferences(this, EnumLanguage.hi);
                return;
            case 2: //telugu
                Utility.setLanguageIntoSharedPreferences(this, EnumLanguage.te);
                return;
            case 3: //tamil
                Utility.setLanguageIntoSharedPreferences(this, EnumLanguage.ta);
                return;
            case 4: //Kannada
                Utility.setLanguageIntoSharedPreferences(this, EnumLanguage.kn);
                return;
            default: //By default set to english
                Utility.setLanguageIntoSharedPreferences(this, EnumLanguage.en);
                return;
        }*/
    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.doneLayout:
//                Intent intent=new Intent(this,AccountSettingsActivity.class);
//                startActivity(intent);
//                v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_animation));//onclick animation
//                break;
//        }

        checkRegistered();
        DbHelper dbhelper = new DbHelper(LanguageActivity.this);
        AccountData accountData = dbhelper.getAccountData();
        if (accountData != null && !accountData.equals("")) {
            callProfileUpdateService();
        }
    }

    private void checkRegistered() {
        DbHelper dbhelper = new DbHelper(LanguageActivity.this);
        AccountData accountData = dbhelper.getAccountData();
        if (accountData != null && !accountData.equals("")) {
            if (accountData.getIsVerified() == 1) {
                Intent mainIntent = new Intent(LanguageActivity.this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                finish();
            } else {
                Intent mainIntent = new Intent(LanguageActivity.this, AccountSettingsActivity.class);
                startActivity(mainIntent);
                finish();
            }
        } else {
            Intent mainIntent = new Intent(LanguageActivity.this, AccountSettingsActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }

    //call create account service
    private void callProfileUpdateService() {
        customProgressDialog = new CustomProgressDialog(LanguageActivity.this, R.mipmap.syc);

        int languageId = Utility.getLanguageIdFromSharedPreferences(LanguageActivity.this);
        DbHelper dbhelper = new DbHelper(LanguageActivity.this);
        AccountData accountData = dbhelper.getAccountData();
        if (accountData != null && !accountData.equals("")) {
            name_string = accountData.getName();
        }

        ProfileUpdateServiceRequest ServiceRequest = new ProfileUpdateServiceRequest();

        ServiceRequest.setLanguage_id(languageId);
        ServiceRequest.setName(name_string);
        if (Utility.isOnline(LanguageActivity.this)) {
            customProgressDialog.show();
            ServiceCaller sc = new ServiceCaller(LanguageActivity.this);
            sc.updateProfile(ServiceRequest, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {

                    if (isComplete) {
                        if (Consts.IS_DEBUG_LOG) {
                            Log.d(Consts.LOG_TAG, " callProfileUpdateService success result: " + isComplete);
                        }
                        Toast.makeText(LanguageActivity.this, getString(R.string.profile_update), Toast.LENGTH_LONG).show();

                        customProgressDialog.dismiss();
                    } else {
                        Toast.makeText(LanguageActivity.this, getString(R.string.profile_not_updated), Toast.LENGTH_LONG).show();
                        customProgressDialog.dismiss();
                    }

                }
            });
        } else {
            Toast.makeText(LanguageActivity.this, getString(R.string.online_msg), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (customProgressDialog != null && customProgressDialog.isShowing()) {
            customProgressDialog.dismiss();
        }
    }
//first time install logInstallationDetails
    private void logInstallationDetails() {
        LogAnalyticsHelper analyticsHelper = new LogAnalyticsHelper(LanguageActivity.this);
        Bundle bundle = new Bundle();
        bundle.putString("Date_Time", Utility.getCurrentDateTime());
        bundle.putString("Application_Version", Utility.getAppVersion(LanguageActivity.this) + "");
        bundle.putString("Mobile_Make", AppController.getInstance().deviceName());
        bundle.putString("Mobile_Model", AppController.getInstance().deviceModel());
        bundle.putString("Current_Language_Selected", Utility.getLanguageIdFromSharedPreferences(LanguageActivity.this) + "");
        bundle.putString("Language_Name_Code", Utility.getLanguageCodeFromSharedPreferences(LanguageActivity.this));
        bundle.putString("Action_Type", "Text");
        analyticsHelper.logEvent("Installation", bundle);

        LogsDataRequest ServiceRequest = new LogsDataRequest();
        ServiceRequest.setLogEventName("Installation");
        ServiceRequest.setDate_Time(Utility.getCurrentDateTime());
        ServiceRequest.setApplication_Version(Utility.getAppVersion(LanguageActivity.this) + "");
        ServiceRequest.setMobile_Make(AppController.getInstance().deviceName());
        ServiceRequest.setMobile_Model(AppController.getInstance().deviceModel());
        ServiceRequest.setCurrent_Language_Selected(Utility.getLanguageIdFromSharedPreferences(LanguageActivity.this) + "");
        ServiceRequest.setLanguage_Name_Code(Utility.getLanguageCodeFromSharedPreferences(LanguageActivity.this));
        ServiceRequest.setLatitude(String.valueOf(LocationUtility.currentLatitude));
        ServiceRequest.setLongitude(String.valueOf(LocationUtility.currentLongitude));
        ServiceRequest.setOS_Version(AppController.getInstance().OSInfo());

        if (Utility.isOnline(LanguageActivity.this)) {
            ServiceCaller sc = new ServiceCaller(LanguageActivity.this);
            sc.logsRequest(ServiceRequest, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {

                    if (isComplete) {

                        if (InstallationLogprefs != null) {
                            SharedPreferences.Editor editor = InstallationLogprefs.edit();
                            editor.putBoolean("Installation", true);
                            editor.commit();
                        }
                        if (Consts.IS_DEBUG_LOG) {
                            Log.d(Consts.LOG_TAG, " LogsDataRequest success result: " + isComplete);
                        }
                    }
                }
            });
        }
    }
}