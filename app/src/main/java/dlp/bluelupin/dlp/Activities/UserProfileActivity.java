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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    private TextView done, certificateName, mainName, mainemail;
    private EditText mobileNo;
    ImageView leftArrow, certificateimg;
    private Context context;
    private TextView nameLable, emailLable, email, phoneLable, phone, lanLable, save;
    private Spinner spinner;
    private EditText enterName;
    private List<LanguageData> data;
    private String name_string;
    LinearLayout shareviewcertificate;
    private static final String INTRO_CARD1 = "intro_card_1";
    private static final String INTRO_CARD2 = "intro_card_2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    public void init() {
        if (Utility.isTablet(context)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Typeface VodafoneExB = FontManager.getFontTypeface(context, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(context, "fonts/VodafoneRg.ttf");
        nameLable = (TextView) this.findViewById(R.id.nameLable);
        emailLable = (TextView) this.findViewById(R.id.emailLable);
        email = (TextView) this.findViewById(R.id.email);
        phoneLable = (TextView) this.findViewById(R.id.phoneLable);
        phone = (TextView) this.findViewById(R.id.phone);
        lanLable = (TextView) this.findViewById(R.id.lanLable);
        save = (TextView) this.findViewById(R.id.save);
        enterName = (EditText) this.findViewById(R.id.enterName);
        done = this.findViewById(R.id.done);
        certificateName = this.findViewById(R.id.certificateName);
        mainName = this.findViewById(R.id.mainName);
        mainemail = this.findViewById(R.id.mainemail);
        leftArrow = this.findViewById(R.id.leftArrow);
        certificateimg = this.findViewById(R.id.certificateimg);
        shareviewcertificate = this.findViewById(R.id.shareviewcertificate);

        emailLable.setTypeface(VodafoneExB);
        nameLable.setTypeface(VodafoneExB);
        phoneLable.setTypeface(VodafoneExB);
        lanLable.setTypeface(VodafoneExB);
        save.setTypeface(VodafoneRg);
        spinner = (Spinner) this.findViewById(R.id.spinner);
        spinner.getBackground().setColorFilter(Color.parseColor("#4a4d4e"), PorterDuff.Mode.SRC_ATOP);
        save.setOnClickListener(this);
        DbHelper dbhelper = new DbHelper(context);
        data = dbhelper.getAllLanguageDataEntity();
        if (data != null) {
            LanguageAdapter languageAdapter = new LanguageAdapter(context, data);
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(languageAdapter);
            int languagePos = Utility.getLanguagePositionFromSharedPreferences(context);
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
        AccountData accountData = dbhelper.getAccountData();
        if (accountData != null && !accountData.equals("")) {
            enterName.setText(accountData.getName());
            email.setText(accountData.getEmail());
            phone.setText(accountData.getPhone());
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
            case R.id.save:
                if (isValidate()) {
                    callProfileUpdateService();
                }
                break;
        }
    }

    // ----validation -----
    private boolean isValidate() {
        String numberRegex = "[0-9]+";
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        // String emailReg = "^[A-Za-z0-9_.]+(\\.[_A-Za-z0-9]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        name_string = enterName.getText().toString().trim();
        if (name_string.length() == 0) {
            Utility.alertForErrorMessage(getString(R.string.enter_name), context);
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
            showIntro(shareviewcertificate, INTRO_CARD2, "Share Certificate", Focus.ALL);
    }
}