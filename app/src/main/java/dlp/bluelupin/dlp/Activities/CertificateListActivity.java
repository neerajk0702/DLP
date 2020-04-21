package dlp.bluelupin.dlp.Activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dlp.bluelupin.dlp.Adapters.CertificatesAdapter;
import dlp.bluelupin.dlp.Adapters.MyInviteFragmentRecyclerViewAdapter;
import dlp.bluelupin.dlp.Models.Certificates;
import dlp.bluelupin.dlp.Models.ContentData;
import dlp.bluelupin.dlp.Models.Invitations;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Services.IAsyncWorkCompletedCallbackWithContentData;
import dlp.bluelupin.dlp.Services.ServiceCaller;
import dlp.bluelupin.dlp.Utilities.CustomProgressDialog;
import dlp.bluelupin.dlp.Utilities.Utility;

public class CertificateListActivity extends AppCompatActivity {

    private TextView viewpastInvite, invite;
    private EditText mobileNo;
    private ImageView leftArrow;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.certificate_list_activity_layout);
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

        if (Utility.isTablet(CertificateListActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        recyclerView=findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(CertificateListActivity.this));
        callCertificateListService();
    }


    //call certificate list Service
    private void callCertificateListService() {
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(CertificateListActivity.this, R.mipmap.syc);
        if (Utility.isOnline(CertificateListActivity.this)) {
            customProgressDialog.show();
            ServiceCaller sc = new ServiceCaller(CertificateListActivity.this);
            sc.certificateList( new IAsyncWorkCompletedCallbackWithContentData() {
                @Override
                public void onDone(ContentData result, boolean isComplete) {
                    if (isComplete) {
                        List<Certificates> invitations=result.getCertificates();
                        if(invitations!=null && invitations.size()>0) {
                            recyclerView.setAdapter(new CertificatesAdapter(CertificateListActivity.this, invitations));
                        }else {
                            Toast.makeText(CertificateListActivity.this, getString(R.string.notcomplete), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }else{
                        Toast.makeText(CertificateListActivity.this, getString(R.string.notcomplete), Toast.LENGTH_LONG).show();
                        finish();
                    }
                    customProgressDialog.dismiss();
                }
            });
        } else {
            Utility.alertForErrorMessage(CertificateListActivity.this.getString(R.string.online_msg), CertificateListActivity.this);
        }
    }
}


