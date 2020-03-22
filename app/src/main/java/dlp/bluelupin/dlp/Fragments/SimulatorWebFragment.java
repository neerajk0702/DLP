package dlp.bluelupin.dlp.Fragments;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.MainActivity;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.LogsDataRequest;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Services.AppController;
import dlp.bluelupin.dlp.Services.IAsyncWorkCompletedCallback;
import dlp.bluelupin.dlp.Services.ServiceCaller;
import dlp.bluelupin.dlp.Utilities.CustomProgressDialog;
import dlp.bluelupin.dlp.Utilities.LocationUtility;
import dlp.bluelupin.dlp.Utilities.LogAnalyticsHelper;
import dlp.bluelupin.dlp.Utilities.Utility;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimulatorWebFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String downloadUrl="";



    public SimulatorWebFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WebFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SimulatorWebFragment newInstance(String param1, String param2,String downloadUrl) {
        SimulatorWebFragment fragment = new SimulatorWebFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString("downloadUrl", downloadUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            downloadUrl = getArguments().getString("downloadUrl");
        }
    }

    private Context context;
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_web, container, false);

        context = getActivity();
        if (Utility.isTablet(context)) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init(view);
        return view;
    }

    private void init(View view) {
        LocationUtility.getmFusedLocationClient(context);
        MainActivity rootActivity = (MainActivity) getActivity();
        if (mParam2 != null && !mParam2.equals("")) {
            rootActivity.setScreenTitle(mParam2);
        }
        rootActivity.setShowQuestionIconOption(false);
        webView = (WebView) view.findViewById(R.id.webView);

        // webView.setInitialScale(50);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.addJavascriptInterface(myJavaScriptInterface, "INTERFACE");
        // webView.getSettings().setBuiltInZoomControls(true);
        // webView.getSettings().setSupportZoom(true);
        // webView.getSettings().setLoadWithOverviewMode(true);
        //webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        //webView.setBackgroundColor(Color.parseColor("#000000"));
        //webView.getSettings().setDomStorageEnabled(true);
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(context, R.mipmap.syc);
        webView.setHorizontalScrollBarEnabled(false);
       // if (Utility.isOnline(context)) {
            if (webView != null) {
                /*customProgressDialog.show();
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        customProgressDialog.dismiss();
                    }
                });*/

                //webView.loadUrl(context.getAssets().open("/NeerajDemo-1-1495000689/Calc.html"));
                // webView.loadUrl("file:///sdcard/dlpUnzipped/samvaadSimulator/NeerajDemo-1-1495000689/Calc.html");
                //webView.loadUrl("file:///storage/emulated/0/dlpUnzipped/samvaadSimulator/NeerajDemo-1-1495000689/Calc.html");
                webView.loadUrl("file:///" + mParam1 + "index.html");
                logSimulatorDetails(mParam1);
                //webView.loadDataWithBaseURL("", mParam1, "text/html", "UTF-8", "");
                // webView.loadData(mParam1, "text/html", "utf8");
            }
        /*} else {
            if (customProgressDialog.isShowing()) {
                // customProgressDialog.dismiss();
            }
            Utility.alertForErrorMessage(getString(R.string.online_msg), context);
        }*/
    }
    private void logSimulatorDetails(String simulatorUrl) {
        DbHelper db = new DbHelper(context);
        AccountData acdata = db.getAccountData();
        LogAnalyticsHelper analyticsHelper = new LogAnalyticsHelper(context);
        Bundle bundle = new Bundle();
        bundle.putString("Unique_Identifier", acdata.getId() + "");
        bundle.putString("ContentId", acdata.getId() + "");
        bundle.putString("Name", acdata.getName());
        bundle.putString("EmailID", acdata.getEmail());
        bundle.putString("Phone", acdata.getPhone());
        bundle.putString("Role", acdata.getRole());
        bundle.putString("Date_Time", Utility.getCurrentDateTime());
        bundle.putString("Application_Version", Utility.getAppVersion(context) + "");
        bundle.putString("Mobile_Make", AppController.getInstance().deviceName());
        bundle.putString("Mobile_Model", AppController.getInstance().deviceModel());
        bundle.putString("Status", acdata.getIsVerified() + "");
        bundle.putString("Current_Language_Selected", Utility.getLanguageIdFromSharedPreferences(context) + "");
        bundle.putString("Language_Name_Code", Utility.getLanguageCodeFromSharedPreferences(context));
        bundle.putString("Initial_Language_Selected", acdata.getPreferred_language_id() + "");
        bundle.putString("Simulator_Url", simulatorUrl);
        bundle.putString("Action_Type", "Text");
        bundle.putString("Media_Url", downloadUrl);
        analyticsHelper .logEvent("Simulator", bundle);

        LogsDataRequest ServiceRequest = new LogsDataRequest();
        ServiceRequest.setLogEventName("Simulator");
        ServiceRequest.setUnique_Identifier(acdata.getId() + "");
        ServiceRequest.setContentId(acdata.getId() + "");
        ServiceRequest.setName(acdata.getName());
        ServiceRequest.setEmailID(acdata.getEmail());
        ServiceRequest.setPhone(acdata.getPhone());
        ServiceRequest.setRole(acdata.getRole());
        ServiceRequest.setDate_Time(Utility.getCurrentDateTime());
        ServiceRequest.setApplication_Version(Utility.getAppVersion(context) + "");
        ServiceRequest.setMobile_Make(AppController.getInstance().deviceName());
        ServiceRequest.setMobile_Model(AppController.getInstance().deviceModel());
        ServiceRequest.setStatus(acdata.getIsVerified() + "");
        ServiceRequest.setCurrent_Language_Selected(Utility.getLanguageIdFromSharedPreferences(context) + "");
        ServiceRequest.setLanguage_Name_Code(Utility.getLanguageCodeFromSharedPreferences(context));
        ServiceRequest.setInitial_Language_Selected(acdata.getPreferred_language_id() + "");
        ServiceRequest.setCourse_Selected(simulatorUrl);
        ServiceRequest.setAction_Type("Simulator");
        ServiceRequest.setMediaUrl(downloadUrl);
        ServiceRequest.setLatitude(String.valueOf(LocationUtility.currentLatitude));
        ServiceRequest.setLongitude(String.valueOf(LocationUtility.currentLongitude));
        ServiceRequest.setOS_Version(AppController.getInstance().OSInfo());

        if (Utility.isOnline(context)) {
            ServiceCaller sc = new ServiceCaller(context);
            sc.logsRequest(ServiceRequest, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {

                    if (isComplete) {
                        if (Consts.IS_DEBUG_LOG) {
                            Log.d(Consts.LOG_TAG, " LogsDataRequest success result: " + isComplete);
                        }
                    }
                }
            });
        }
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
}
