package dlp.bluelupin.dlp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import dlp.bluelupin.dlp.Adapters.LanguageAdapter;
import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.MainActivity;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.AccountServiceRequest;
import dlp.bluelupin.dlp.Models.LanguageData;
import dlp.bluelupin.dlp.Models.ProfileUpdateServiceRequest;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Services.IAsyncWorkCompletedCallback;
import dlp.bluelupin.dlp.Services.ServiceCaller;
import dlp.bluelupin.dlp.Utilities.CustomProgressDialog;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.LocationUtility;
import dlp.bluelupin.dlp.Utilities.Utility;
import dlp.bluelupin.dlp.shwocaseview.animation.MaterialIntroListener;
import dlp.bluelupin.dlp.shwocaseview.shape.Focus;
import dlp.bluelupin.dlp.shwocaseview.shape.FocusGravity;
import dlp.bluelupin.dlp.shwocaseview.view.MaterialIntroView;


public class NewUserActivity extends AppCompatActivity implements View.OnClickListener, MaterialIntroListener {
    private TextView title, leftArrow;
    private TextView save, skip;


    //  private EditText enterName, enterEmail;
    private EditText enterName, enterEmail;
    String name_string, email_string;
    private static final String INTRO_CARD1 = "intro_card_1";
    private static final String INTRO_CARD2 = "intro_card_2";

    CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
        setContentView(R.layout.activity_new_user);
        if (Utility.isTablet(this)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        init();
    }

    //get all id's
    private void init() {
        LocationUtility.getmFusedLocationClient(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        leftArrow = (TextView) toolbar.findViewById(R.id.leftArrow);
        title = (TextView) toolbar.findViewById(R.id.title);
        Typeface custom_fontawesome = FontManager.getFontTypeface(this, "fonts/fontawesome-webfont.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(this, "fonts/materialdesignicons-webfont.otf");
        Typeface VodafoneExB = FontManager.getFontTypeface(this, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(this, "fonts/VodafoneRg.ttf");
        leftArrow.setTypeface(materialdesignicons_font);
        leftArrow.setOnClickListener(this);
        int textColor = Color.parseColor("#e60000");
        save = (TextView) findViewById(R.id.save);
        skip = (TextView) findViewById(R.id.skip);
        save.setOnClickListener(this);
        title.setTypeface(VodafoneExB);

        //cancel.setTypeface(VodafoneRg);
        save.setTypeface(VodafoneRg);
        leftArrow.setText(Html.fromHtml("&#xf04d;"));
        enterName = findViewById(R.id.name);
        enterEmail = findViewById(R.id.email);
        showIntro(save, INTRO_CARD1, getString(R.string.Verifyotp), Focus.ALL);
    }


    //call update account service
    private void callProfileUpdateService() {
        customProgressDialog = new CustomProgressDialog(NewUserActivity.this, R.mipmap.syc);

        int languageId = Utility.getLanguageIdFromSharedPreferences(NewUserActivity.this);

        ProfileUpdateServiceRequest ServiceRequest = new ProfileUpdateServiceRequest();

        ServiceRequest.setLanguage_id(languageId);
        ServiceRequest.setName(name_string);
        ServiceRequest.setEmail(email_string);
        if (Utility.isOnline(NewUserActivity.this)) {
            customProgressDialog.show();
            ServiceCaller sc = new ServiceCaller(NewUserActivity.this);
            sc.updateProfile(ServiceRequest, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {

                    if (isComplete) {
                        if (Consts.IS_DEBUG_LOG) {
                            Log.d(Consts.LOG_TAG, " callProfileUpdateService success result: " + isComplete);
                        }
                        Toast.makeText(NewUserActivity.this, getString(R.string.profile_update), Toast.LENGTH_LONG).show();
                        Intent intentOtp = new Intent(NewUserActivity.this, MainActivity.class);
                        intentOtp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intentOtp);
                        customProgressDialog.dismiss();
                    } else {
                        Toast.makeText(NewUserActivity.this, getString(R.string.profile_not_updated), Toast.LENGTH_LONG).show();
                        customProgressDialog.dismiss();
                    }

                }
            });
        } else {
            Toast.makeText(NewUserActivity.this, getString(R.string.online_msg), Toast.LENGTH_LONG).show();
        }
    }

//    private void setLanguage(int langpos) {
//        if (data != null) {
//            String StringCode = data.get(langpos).getCode();
//            String[] parts = StringCode.split("-");
//            String code = parts[0];
//            String part2 = parts[1];
//            Utility.setLanguageIntoSharedPreferences(this, data.get(langpos).getId(), code, langpos);
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }

    //to hide keyboard from otside touch
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    // ----validation -----
    private boolean isValidate() {
        String numberRegex = "[0-9]+";
        String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
//         String emailReg = "^[A-Za-z0-9_.]+(\\.[_A-Za-z0-9]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        name_string = enterName.getText().toString().trim();
        email_string = enterEmail.getText().toString().trim();
        if (name_string.length() == 0) {
            Utility.alertForErrorMessage(getString(R.string.enter_name), this);
            return false;
        } else if (email_string.length() == 0) {
            Utility.alertForErrorMessage(getString(R.string.enter_e_mail), this);
            return false;
        } else if (!email_string.matches(emailRegex)) {
            Utility.alertForErrorMessage(getString(R.string.enter_valid_mail), this);
            return false;
        }


        return true;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftArrow:
                Intent intent = new Intent(this, LanguageActivity.class);
                startActivity(intent);
                break;
            case R.id.save:
                if (isValidate()) {
                    callProfileUpdateService();
                    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click_animation));//onclick animation
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationUtility.startLocationUpdates(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationUtility.stopLocationUpdates();
    }


    public void showIntro(View view, String id, String text, Focus focusType) {
        new MaterialIntroView.Builder(NewUserActivity.this)
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
            showIntro(skip, INTRO_CARD2, "Yo can Skip", Focus.ALL);
    }
}
