package dlp.bluelupin.dlp.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.SplashActivity;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.Utility;
import dlp.bluelupin.dlp.WelcomeActivity;
import dlp.bluelupin.dlp.shwocaseview.animation.MaterialIntroListener;
import dlp.bluelupin.dlp.shwocaseview.shape.Focus;
import dlp.bluelupin.dlp.shwocaseview.shape.FocusGravity;
import dlp.bluelupin.dlp.shwocaseview.view.MaterialIntroView;

public class ReferFriendActivity extends AppCompatActivity implements  MaterialIntroListener {

    private TextView viewpastInvite, invite;
    private EditText mobileNo;
    private ImageView leftArrow;
    private static final String INTRO_FOCUS_1 = "intro_focus_1";
    private static final String INTRO_FOCUS_2 = "intro_focus_2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_refer_friend);
        init();
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        leftArrow = toolbar.findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (Utility.isTablet(ReferFriendActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        viewpastInvite = findViewById(R.id.viewpastInvite);
        invite = findViewById(R.id.invite);
        mobileNo = findViewById(R.id.mobileNo);
        viewpastInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(ReferFriendActivity.this, InviteFriendList.class);
                startActivity(mainIntent);
            }
        });

        showIntro(invite, INTRO_FOCUS_1, getString(R.string.Invite), Focus.NORMAL, FocusGravity.LEFT);
    }




    public void showIntro(View view, String id, String text, Focus focusType, FocusGravity focusGravity) {
        new MaterialIntroView.Builder(ReferFriendActivity.this)
                .enableDotAnimation(true)
                .setFocusGravity(focusGravity)
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
        if (materialIntroViewId == INTRO_FOCUS_1)
          showIntro(viewpastInvite, INTRO_FOCUS_2, getString(R.string.viewpastinvitestr), Focus.MINIMUM, FocusGravity.CENTER);
    }

}
