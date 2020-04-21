package dlp.bluelupin.dlp.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Models.AccountServiceRequest;
import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.Models.StatusUpdateService;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Services.IAsyncWorkCompletedCallback;
import dlp.bluelupin.dlp.Services.ServiceCaller;
import dlp.bluelupin.dlp.SplashActivity;
import dlp.bluelupin.dlp.Utilities.CustomProgressDialog;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.Utility;
import dlp.bluelupin.dlp.WelcomeActivity;

public class ReferFriendActivity extends AppCompatActivity {

    private TextView viewpastInvite, invite;
    private EditText mobileNo;
    private ImageView leftArrow;
    private String pnone_no_string;
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
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidate()){
                    callInviteService();
                }

            }
        });
    }
    //call invite  Updat Service
    private void callInviteService() {
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(ReferFriendActivity.this, R.mipmap.syc);
        AccountServiceRequest ServiceRequest = new AccountServiceRequest();
        ServiceRequest.setPhone(pnone_no_string);
        if (Utility.isOnline(ReferFriendActivity.this)) {
            customProgressDialog.show();
            ServiceCaller sc = new ServiceCaller(ReferFriendActivity.this);
            sc.inviteFriend(ServiceRequest, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        Toast.makeText(ReferFriendActivity.this, getString(R.string.Invitefriend), Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ReferFriendActivity.this, getString(R.string.Invitefriendnot), Toast.LENGTH_LONG).show();
                    }
                    customProgressDialog.dismiss();
                }
            });
        } else {
            Utility.alertForErrorMessage(ReferFriendActivity.this.getString(R.string.online_msg), ReferFriendActivity.this);
        }
    }


    private boolean isValidate() {
        String numberRegex = "[0-9]+";
        //String emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        // String emailReg = "^[A-Za-z0-9_.]+(\\.[_A-Za-z0-9]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pnone_no_string = mobileNo.getText().toString().trim();

        if (pnone_no_string.length() == 0) {
            Utility.alertForErrorMessage(getString(R.string.enter_phone_no), this);
            return false;
        } else if (pnone_no_string.length() != 10) {
            Utility.alertForErrorMessage(getString(R.string.enter_valid_phone), this);
            return false;
        } else if (pnone_no_string.length() > 10) {
            Utility.alertForErrorMessage(getString(R.string.enter_valid_phone), this);
            return false;
        } else if (!pnone_no_string.matches(numberRegex)) {
            Utility.alertForErrorMessage(getString(R.string.enter_valid_phone), this);
            return false;
        }
        return true;
    }
}
