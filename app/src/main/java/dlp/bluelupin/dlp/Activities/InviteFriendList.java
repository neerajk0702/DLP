package dlp.bluelupin.dlp.Activities;

import android.content.Context;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dlp.bluelupin.dlp.Adapters.MyInviteFragmentRecyclerViewAdapter;
import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.Utility;
import dlp.bluelupin.dlp.WelcomeActivity;

public class InviteFriendList extends AppCompatActivity {

    private TextView viewpastInvite, invite;
    private EditText mobileNo;
    private ImageView leftArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.fragment_invitefragment_list);
        init();
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        leftArrow =  toolbar.findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (Utility.isTablet(InviteFriendList.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        List<Data> dataList=new ArrayList<>();
        for(int i=0;i<=30;i++){
            Data data=new Data();
            data.setMediaId(i);
            dataList.add(data);

        }
        RecyclerView recyclerView=findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(InviteFriendList.this));
        recyclerView.setAdapter(new MyInviteFragmentRecyclerViewAdapter(InviteFriendList.this,dataList));
    }
}

