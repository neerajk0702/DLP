package dlp.bluelupin.dlp.Fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dlp.bluelupin.dlp.Activities.LanguageActivity;
import dlp.bluelupin.dlp.Adapters.ContentRecycleAdapter;
import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.MainActivity;
import dlp.bluelupin.dlp.Models.Content_status;
import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.Models.ProfileUpdateServiceRequest;
import dlp.bluelupin.dlp.Models.StatusUpdateService;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentFragment extends Fragment implements MaterialIntroListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int parentId;
    private String contentTitle;
    TextView mark_complete;

    private static final String INTRO_CARD1 = "intro_card_1";

    public ContentFragment() {
        // Required empty public constructor
    }


    public static ContentFragment newInstance(int parentId, String contentTitle) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, parentId);
        args.putString(ARG_PARAM2, contentTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentId = getArguments().getInt(ARG_PARAM1);
            contentTitle = getArguments().getString(ARG_PARAM2);
        }
    }

    private Context context;
    private TextView content_title;
    View view;
    int completion_status=0;
    LinearLayout statusLayout,markLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_content, container, false);
        context = getActivity();
        if (Utility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
        return view;
    }

    private void init() {
        LocationUtility.getmFusedLocationClient(context);
        MainActivity rootActivity = (MainActivity) getActivity();
        rootActivity.setScreenTitle(contentTitle);
        //rootActivity.hideSplashImage(true);
        rootActivity.setShowQuestionIconOption(false);
        Typeface custom_fontawesome = FontManager.getFontTypeface(context, "fonts/fontawesome-webfont.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        Typeface VodafoneExB = FontManager.getFontTypeface(context, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(context, "fonts/VodafoneRg.ttf");
//        content_title = (TextView) view.findViewById(R.id.content_title);
//        content_title.setTypeface(VodafoneExB);


        DbHelper db = new DbHelper(context);
        List<Data> dataList = db.getDataEntityByParentId(parentId);
        if (dataList.size() == 0) {
            view = view.inflate(context, R.layout.no_record_found_fragment, null);
            TextView noRecordIcon = (TextView) view.findViewById(R.id.noRecordIcon);
            noRecordIcon.setTypeface(materialdesignicons_font);
            noRecordIcon.setText(Html.fromHtml("&#xf187;"));
        } else {
            statusLayout=view.findViewById(R.id.statusLayout);
            markLayout=view.findViewById(R.id.markLayout);
            Content_status service = db.getcontentStatusEntityById(parentId);//get content status data by parentID
            if (service!=null) {
                completion_status=service.getCompletion_status();
                if(completion_status==0){
                    markLayout.setVisibility(View.VISIBLE);
                    callStatusUpdatService(1);//browsed = 1
                }else if(completion_status==1){
                    statusLayout.setVisibility(View.GONE);
                    markLayout.setVisibility(View.VISIBLE);
                }else if(completion_status==2){
                    markLayout.setVisibility(View.GONE);
                    statusLayout.setVisibility(View.VISIBLE);
                }
            }else{
                markLayout.setVisibility(View.VISIBLE);
                callStatusUpdatService(1);//browsed = 1
            }

            //ContentAdapter contentAdapter = new ContentAdapter(context, dataList);
            ContentRecycleAdapter contentAdapter = new ContentRecycleAdapter(context, dataList);
            contentAdapter.setHasStableIds(true);
            //ListView listView = (ListView) view.findViewById(R.id.listView);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.content_recycler_view);
            recyclerView.setAdapter(contentAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            //listView.setAdapter(contentAdapter);
             mark_complete=view.findViewById(R.id.save);
            mark_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callStatusUpdatService(2); //completed = 2
                }
            });
        }
        if (Consts.IS_DEBUG_LOG) {
            Log.d(Consts.LOG_TAG, "Content Fragment: data_item count: " + dataList.size());
        }
        showIntro(mark_complete, INTRO_CARD1, getString(R.string.save), Focus.ALL);
    }
    @Override
    public void onResume() {
        super.onResume();
        LocationUtility.startLocationUpdates(context);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocationUtility.stopLocationUpdates();
    }


    //call Status Updat Service
    private void callStatusUpdatService(final int status) {
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(context, R.mipmap.syc);
        StatusUpdateService ServiceRequest = new StatusUpdateService();
        ServiceRequest.setCompletion_status(status);
        ServiceRequest.setContent_id(parentId);
        if (Utility.isOnline(context)) {
            customProgressDialog.show();
            ServiceCaller sc = new ServiceCaller(context);
            sc.StatusUpdat(ServiceRequest, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {


                    if (isComplete) {
                        if (Consts.IS_DEBUG_LOG) {
                            Log.d(Consts.LOG_TAG, " callProfileUpdateService success result: " + isComplete);
                        }
                        if(status==2) {
                            Toast.makeText(context, getString(R.string.updatestatus), Toast.LENGTH_LONG).show();
                            statusLayout.setVisibility(View.VISIBLE);
                            markLayout.setVisibility(View.GONE);
                        }
                        customProgressDialog.dismiss();
                    } else {
                        Utility.alertForErrorMessage(getString(R.string.updatestatusnot), context);
                        customProgressDialog.dismiss();
                    }

                }
            });
        } else {
            Utility.alertForErrorMessage(context.getString(R.string.online_msg), context);
        }
    }

    public void showIntro(View view, String id, String text, Focus focusType) {
        new MaterialIntroView.Builder(getActivity())
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
        // if (materialIntroViewId == INTRO_CARD1)
        //   showIntro(doneLayout, INTRO_CARD2, "Select Done", FocusGravity.CENTER);
    }

}
