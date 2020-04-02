package dlp.bluelupin.dlp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import dlp.bluelupin.dlp.Adapters.LanguageAdapter;
import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.MainActivity;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.LanguageData;
import dlp.bluelupin.dlp.Models.ProfileUpdateServiceRequest;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Services.IAsyncWorkCompletedCallback;
import dlp.bluelupin.dlp.Services.ServiceCaller;
import dlp.bluelupin.dlp.Utilities.CustomProgressDialog;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.Utility;
import dlp.bluelupin.dlp.shwocaseview.animation.MaterialIntroListener;
import dlp.bluelupin.dlp.shwocaseview.shape.Focus;
import dlp.bluelupin.dlp.shwocaseview.shape.FocusGravity;
import dlp.bluelupin.dlp.shwocaseview.view.MaterialIntroView;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener, MaterialIntroListener {

    private TextView done, certificateName, mainName, mainemail,title;
    ImageView leftArrow, certificateimg;
    private Context context;
    private TextView nameLable, emailLable, phoneLable, phone, lanLable, save,language;
    private EditText enterName,email;
    private List<LanguageData> data;
    private String name_string,email_string;
    LinearLayout shareviewcertificate;
    private static final String INTRO_CARD1 = "intro_card_1";
    private static final String INTRO_CARD2 = "intro_card_2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_user_profile);
        context=UserProfileActivity.this;
        init();
    }

    public void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title =  toolbar.findViewById(R.id.title);
        leftArrow =  toolbar.findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (Utility.isTablet(context)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Typeface VodafoneExB = FontManager.getFontTypeface(context, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(context, "fonts/VodafoneRg.ttf");
        nameLable = (TextView) this.findViewById(R.id.nameLable);
        emailLable = (TextView) this.findViewById(R.id.emailLable);
        email = this.findViewById(R.id.email);
        phoneLable = (TextView) this.findViewById(R.id.phoneLable);
        phone = (TextView) this.findViewById(R.id.phone);
        lanLable = (TextView) this.findViewById(R.id.lanLable);
        save = (TextView) this.findViewById(R.id.save);
        enterName = (EditText) this.findViewById(R.id.enterName);
        done = toolbar.findViewById(R.id.done);
        done.setOnClickListener(this);
        done.setTypeface(VodafoneExB);
        title.setTypeface(VodafoneExB);
        certificateName = this.findViewById(R.id.certificateName);
        mainName = this.findViewById(R.id.mainName);
        mainName.setTypeface(VodafoneExB);
        mainemail = this.findViewById(R.id.mainemail);
        mainemail.setTypeface(VodafoneRg);
        certificateimg = this.findViewById(R.id.certificateimg);
        shareviewcertificate = this.findViewById(R.id.shareviewcertificate);
        shareviewcertificate.setOnClickListener(this);
        emailLable.setTypeface(VodafoneExB);
        nameLable.setTypeface(VodafoneExB);
        phoneLable.setTypeface(VodafoneExB);
        lanLable.setTypeface(VodafoneExB);
        save.setTypeface(VodafoneRg);
        language =  this.findViewById(R.id.language);
        DbHelper dbhelper = new DbHelper(context);
        data = dbhelper.getAllLanguageDataEntity();
        if (data != null) {
            int languagePos = Utility.getLanguagePositionFromSharedPreferences(context);
            String languagecode = Utility.getLanguageCodeFromSharedPreferences(context);
            language.setText(data.get(languagePos).getName());
        }
        AccountData accountData = dbhelper.getAccountData();
        if (accountData != null && !accountData.equals("")) {
            enterName.setText(accountData.getName());
            email.setText(accountData.getEmail());
            phone.setText(accountData.getPhone());
            mainName.setText(accountData.getName());
            mainemail.setText(accountData.getEmail());
        }

        showIntro(done, INTRO_CARD1, getString(R.string.save), Focus.ALL);
    }

    private void setLanguage(int langpos) {
        if (data != null) {
            String StringCode = data.get(langpos).getCode();
            String[] parts = StringCode.split("-");
            String code = parts[0];
            String part2 = parts[1];
            Utility.setLanguageIntoSharedPreferences(context, data.get(langpos).getId(), code, langpos);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                if (isValidate()) {
                    callProfileUpdateService();
                }
                break;
            case R.id.shareviewcertificate:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;
        }
    }

    // ----validation -----
    private boolean isValidate() {
        String numberRegex = "[0-9]+";
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        // String emailReg = "^[A-Za-z0-9_.]+(\\.[_A-Za-z0-9]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        name_string = enterName.getText().toString().trim();
        email_string = email.getText().toString().trim();
        if (name_string.length() == 0) {
            Utility.alertForErrorMessage(getString(R.string.enter_name), context);
            return false;
        }else if (email_string.length() == 0) {
            Utility.alertForErrorMessage(getString(R.string.enter_e_mail), this);
            return false;
        } else if (!email_string.matches(emailRegex)) {
            Utility.alertForErrorMessage(getString(R.string.enter_valid_mail), this);
            return false;
        }
        return true;
    }

    //call create account service
    private void callProfileUpdateService() {
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(context, R.mipmap.syc);

        int languageId = Utility.getLanguageIdFromSharedPreferences(context);

        ProfileUpdateServiceRequest ServiceRequest = new ProfileUpdateServiceRequest();
        ServiceRequest.setName(name_string);
        ServiceRequest.setLanguage_id(languageId);
        ServiceRequest.setEmail(email_string);
        if (Utility.isOnline(context)) {
            customProgressDialog.show();
            ServiceCaller sc = new ServiceCaller(context);
            sc.updateProfile(ServiceRequest, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        if (Consts.IS_DEBUG_LOG) {
                            Log.d(Consts.LOG_TAG, " callProfileUpdateService success result: " + isComplete);
                        }
                        Toast.makeText(context, getString(R.string.profile_update), Toast.LENGTH_LONG).show();
                        checkRegistered();
                        customProgressDialog.dismiss();
                    } else {
                        Utility.alertForErrorMessage(getString(R.string.profile_not_updated), context);
                        customProgressDialog.dismiss();
                    }

                }
            });
        } else {
            Utility.alertForErrorMessage(context.getString(R.string.online_msg), context);
        }
    }

    private void checkRegistered() {
        DbHelper dbhelper = new DbHelper(context);
        AccountData accountData = dbhelper.getAccountData();
        if (accountData != null && !accountData.equals("")) {
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();

        } else {
            Intent mainIntent = new Intent(context, AccountSettingsActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }

    public void showIntro(View view, String id, String text, Focus focusType) {
        new MaterialIntroView.Builder(UserProfileActivity.this)
                .enableDotAnimation(true)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(focusType)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .setListener(this)
                .performClick(true)
                .setInfoText(text)
                .setTarget(view)
                .setUsageId(id) //THIS SHOULD BE UNIQUE ID
                .show();
    }

    @Override
    public void onUserClicked(String materialIntroViewId) {
        if (materialIntroViewId == INTRO_CARD1)
            showIntro(shareviewcertificate, INTRO_CARD2, getString(R.string.Share), Focus.ALL);
    }
}