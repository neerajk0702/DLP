package dlp.bluelupin.dlp.Fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dlp.bluelupin.dlp.Activities.ReferFriendActivity;
import dlp.bluelupin.dlp.Adapters.ChaptersAdapterNew;
import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.MainActivity;
import dlp.bluelupin.dlp.Models.DashboardData;
import dlp.bluelupin.dlp.Models.Data;
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
 * {@link ChaptersFragmentNew.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChaptersFragmentNew#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChaptersFragmentNew extends Fragment implements View.OnClickListener, MaterialIntroListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String INTRO_CARD = "recyclerView_material_intro";
    // TODO: Rename and change types of parameters
    private int parentId;
    private String type, title;
    ImageView logiiocn;
    TextView titletxt;
    TextView countchapter;
    TextView learnLable;
    TextView topicCount;
    TextView topictext;

    TextView quizCount;
    TextView quiztext;
    TextView Name;
    ImageView moreChapter;
    LinearLayout totalItemView;
    RecyclerView chaptersRecyclerView;
    private static final String INTRO_FOCUS_1 = "intro_focus_1";
    private static final String INTRO_FOCUS_2 = "intro_focus_2";
    private OnFragmentInteractionListener mListener;

    public ChaptersFragmentNew() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ChaptersFragmentNew newInstance(Integer parentId, String type, String title, String imagePath) {
        ChaptersFragmentNew fragment = new ChaptersFragmentNew();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, parentId);
        args.putString(ARG_PARAM2, type);
        args.putString(ARG_PARAM3, title);
        args.putString(ARG_PARAM4, imagePath);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView chapterTitle;
    private Context context;
    private String imagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentId = getArguments().getInt(ARG_PARAM1);
            type = getArguments().getString(ARG_PARAM2);
            title = getArguments().getString(ARG_PARAM3);
            imagePath = getArguments().getString(ARG_PARAM4);
        }
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chapters_new, container, false);
        context = getActivity();
        if (Utility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
        return view;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init() {
        LocationUtility.getmFusedLocationClient(context);
        MainActivity rootActivity = (MainActivity) getActivity();
        rootActivity.setShowQuestionIconOption(false);
        Typeface custom_fontawesome = FontManager.getFontTypeface(context, "fonts/fontawesome-webfont.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        Typeface VodafoneExB = FontManager.getFontTypeface(context, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(context, "fonts/VodafoneRg.ttf");
        //chapterTitle = (TextView) view.findViewById(R.id.chapterTitle);
        //chapterTitle.setTypeface(VodafoneExB);
        DbHelper db = new DbHelper(context);
        logiiocn = view.findViewById(R.id.logiiocn);
        if (imagePath != null && !imagePath.equals("")) {
            Picasso.with(context).load(imagePath).placeholder(R.drawable.imageplaceholder).into(logiiocn);
        }
        titletxt = view.findViewById(R.id.titletxt);
        titletxt.setTypeface(VodafoneExB);
        titletxt.setText(title);
        countchapter = view.findViewById(R.id.countchapter);
        learnLable = view.findViewById(R.id.learnLable);
        topicCount = view.findViewById(R.id.topicCount);
        topictext = view.findViewById(R.id.topictext);
        quizCount = view.findViewById(R.id.quizCount);
        quiztext = view.findViewById(R.id.quiztext);
        Name = view.findViewById(R.id.Name);
        moreChapter = view.findViewById(R.id.moreChapter);
        moreChapter.setOnClickListener(this);
        totalItemView = view.findViewById(R.id.totalItemView);
        countchapter.setTypeface(VodafoneExB);
        topicCount.setTypeface(VodafoneExB);
        quizCount.setTypeface(VodafoneExB);

        if (type.equalsIgnoreCase("Chapter")) {
            rootActivity.setScreenTitle(context.getString(R.string.Chapters));
            totalItemView.setVisibility(View.VISIBLE);
            Name.setText(context.getString(R.string.Chapters));
            titletxt.setMaxLines(1);
            titletxt.setEllipsize(TextUtils.TruncateAt.END);
            // chapterTitle.setText(context.getString(R.string.Chapters));
        } else if (type.equalsIgnoreCase("Topic")) {
            rootActivity.setScreenTitle(context.getString(R.string.Topic));
            totalItemView.setVisibility(View.GONE);
            Name.setText(context.getString(R.string.Topic));
            //chapterTitle.setText(context.getString(R.string.Topic));
        }


        List<Data> dataList = db.getDataEntityByParentIdAndType(parentId, type);
        if (dataList.size() == 0) {
            view = view.inflate(context, R.layout.no_record_found_fragment, null);
            TextView noRecordIcon = (TextView) view.findViewById(R.id.noRecordIcon);
            noRecordIcon.setTypeface(materialdesignicons_font);
            noRecordIcon.setText(Html.fromHtml("&#xf187;"));
        } else {
            TextView quiz = (TextView) view.findViewById(R.id.quiz);
            LinearLayout quizStartLayout = (LinearLayout) view.findViewById(R.id.quizStartLayout);
            quiz.setTypeface(VodafoneExB);
            quizStartLayout.setOnClickListener(this);

            //get quiz exits in this chapter or not
            if (type.equalsIgnoreCase("Topic")) {
                Data contentData = db.getContentQuizEntityByContentId(parentId);
                if (contentData != null) {
                    contentData.setQuizAvailable(true);
                    contentData.setContent_id(parentId);

                    dataList.add(contentData);
                }
            }

            ChaptersAdapterNew chaptersAdapter = new ChaptersAdapterNew(context, dataList, type);
            chaptersRecyclerView = (RecyclerView) view.findViewById(R.id.chaptersRecyclerView);
            // chaptersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            GridLayoutManager gridLayoutManager;
            if (type.equalsIgnoreCase("Topic")) {
                gridLayoutManager = new GridLayoutManager(getContext(), 1);
            } else {
                gridLayoutManager = new GridLayoutManager(getContext(), 2);
            }


            chaptersRecyclerView.setLayoutManager(gridLayoutManager);
            chaptersRecyclerView.setHasFixedSize(true);
            chaptersRecyclerView.setNestedScrollingEnabled(false);
            chaptersRecyclerView.setAdapter(chaptersAdapter);
        }
        if (Consts.IS_DEBUG_LOG) {
            Log.d(Consts.LOG_TAG, "Chapter Fragment: data_item count: " + dataList.size());
        }

        callchapterDetailsService();
        showIntro(moreChapter, INTRO_FOCUS_1, "More", Focus.NORMAL, FocusGravity.CENTER);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            // mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quizStartLayout:
                break;
            case R.id.moreChapter:
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                ChaptersFragment fragment = ChaptersFragment.newInstance(parentId, type);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right);
                transaction.replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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



    //call chapterDetails service
    private void callchapterDetailsService() {
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(context, R.mipmap.syc);
        if (Utility.isOnline(context)) {
            customProgressDialog.show();
            ServiceCaller sc = new ServiceCaller(context);
            sc.dashboarddata(2, parentId, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        setchapterDetails();
                        customProgressDialog.dismiss();
                    } else {
                        Utility.alertForErrorMessage(getString(R.string.profile_not_updated), context);
                        customProgressDialog.dismiss();
                    }

                }
            });
        } else {
            setchapterDetails();
        }
    }

    private void setchapterDetails() {
        DbHelper db = new DbHelper(context);
        DashboardData dadata = db.getDashboarddataEntityById(2);//for save chapter details
        if (dadata != null) {
            countchapter.setText(dadata.getChapters() + "");
            topicCount.setText(dadata.getTopics() + "");
            quizCount.setText(dadata.getQuizzes() + "");
        }
    }


    public void showIntro(View view, String id, String text, Focus focusType, FocusGravity focusGravity) {
        new MaterialIntroView.Builder(getActivity())
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
        if (materialIntroViewId.equals(INTRO_CARD)) {
            //   Toast.makeText(getActivity(), "User Clicked", Toast.LENGTH_SHORT).show();
        }
    }


}
