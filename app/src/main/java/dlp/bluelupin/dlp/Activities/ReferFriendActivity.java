package dlp.bluelupin.dlp.Activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import dlp.bluelupin.dlp.R;

public class ReferFriendActivity extends AppCompatActivity {

    private TextView viewpastInvite,invite;
    private EditText mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);
    }
}
