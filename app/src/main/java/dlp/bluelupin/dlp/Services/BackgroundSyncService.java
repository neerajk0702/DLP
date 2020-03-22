package dlp.bluelupin.dlp.Services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.sql.Date;
import java.util.List;

import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.MainActivity;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.CacheServiceCallData;
import dlp.bluelupin.dlp.Models.ContentServiceRequest;
import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.Models.LanguageData;
import dlp.bluelupin.dlp.Models.LogsDataRequest;
import dlp.bluelupin.dlp.Utilities.LocationUtility;
import dlp.bluelupin.dlp.Utilities.LogAnalyticsHelper;
import dlp.bluelupin.dlp.Utilities.Utility;
import okhttp3.internal.Util;

/**
 * Created by subod on 08-Aug-16.
 */
public class BackgroundSyncService extends IntentService {

    private int result = Activity.RESULT_CANCELED;

    public BackgroundSyncService() {
        super("BackgroundSyncService");
    }

    // Will be called asynchronously be Android
    @Override
    protected void onHandleIntent(Intent intent) {
        LocationUtility.getmFusedLocationClient(BackgroundSyncService.this);
        if (Utility.isOnline(BackgroundSyncService.this)) {
            if (Consts.IS_DEBUG_LOG)
                Log.d(Consts.LOG_TAG, "starting BackgroundSyncService onHandleIntent");
            // Uri data_item = intent.getData();
            String someData = intent.getStringExtra("appName");
            if (Consts.IS_DEBUG_LOG)
                Log.d(Consts.LOG_TAG, someData);

            Bundle extras = intent.getExtras();

            ContentServiceRequest request = new ContentServiceRequest();
            callContentAsync(extras);
            callResourceAsync(extras);
            callMediaAsync(extras);
            callMedialanguageLatestAsync(extras);
            callGetAllLanguage(extras);
            callQuizzesAsync();
            callQuizzesQuestionsAsync();
            callQuizzesQuestionsOptionsAsync();
            callContentQuizAsync();
            result = Activity.RESULT_OK; // success

            sendMessageIfAllCallsDone(extras);
        }
    }

    private Boolean contentCallDone = false, resourceCallDone = false, mediaCallDone = false;

    private void sendMessageIfAllCallsDone(Bundle extras) {
        if (contentCallDone && resourceCallDone && mediaCallDone) {
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "BackgroundSyncServicemessage: ALL DONE");
            }
//            if (extras != null) {
//                Messenger messenger = (Messenger) extras.get("MESSENGER");
//                Message msg = Message.obtain();
//                msg.arg1 = result;
//                //msg.obj = output.getAbsolutePath();
//                try {
//                    messenger.send(msg);
//                    Log.d(Consts.LOG_TAG, "BackgroundSyncServicemessage: send");
//                } catch (android.os.RemoteException e1) {
//                    Log.d(Consts.LOG_TAG, "BackgroundSyncService: Exception sending message", e1);
//                }
//
//            }
        }
    }

    private void callContentAsync(final Bundle extras) {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(BackgroundSyncService.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.URL_CONTENT_LATEST);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "BackgroundSyncService: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(BackgroundSyncService.this);
        sc.getAllContent(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
               /* Log.d(Consts.LOG_TAG, "MainActivity: callContentAsync success result: " + isComplete);
                DbHelper db = new DbHelper(BackgroundSyncService.this);
                List<Data> data = db.getDataEntityByParentId(null);
                Log.d(Consts.LOG_TAG, "MainActivity: data_item count: " + data.size());*/
                contentCallDone = true;
                sendMessageIfAllCallsDone(extras);
            }
        });
    }

    private void callResourceAsync(final Bundle extras) {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(BackgroundSyncService.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.URL_LANGUAGE_RESOURCE_LATEST);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "BackgroundSyncService: cacheSeviceCallData for URL_LANGUAGE_RESOURCE_LATEST: " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(BackgroundSyncService.this);
        sc.getAllResource(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                Log.d(Consts.LOG_TAG, "MainActivity: callResourceAsync success result: " + isComplete);
                DbHelper db = new DbHelper(BackgroundSyncService.this);
                List<Data> data = db.getResources();
                Log.d(Consts.LOG_TAG, "MainActivity: callResourceAsync data_item count: " + data.size());
                resourceCallDone = true;
                sendMessageIfAllCallsDone(extras);
            }
        });
    }

    private void callMediaAsync(final Bundle extras) {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(BackgroundSyncService.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.URL_MEDIA_LATEST);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "BackgroundSyncService: cacheSeviceCallData for URL_MEDIA_LATEST: " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(BackgroundSyncService.this);
        sc.getAllMedia(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
               /* Log.d(Consts.LOG_TAG, "MainActivity: callMediaAsync success result: " + isComplete);
                DbHelper db = new DbHelper(BackgroundSyncService.this);
                List<Data> data = db.getAllMedia();
                Log.d(Consts.LOG_TAG, "MainActivity: callMediaAsync data_item count: " + data.size());*/
                mediaCallDone = true;
                sendMessageIfAllCallsDone(extras);
            }
        });
    }

    private void callMedialanguageLatestAsync(final Bundle extras) {
        if (Utility.isOnline(BackgroundSyncService.this)) {
            ContentServiceRequest request = new ContentServiceRequest();
            request.setPage(1);
            DbHelper db = new DbHelper(BackgroundSyncService.this);
            CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.MediaLanguage_Latest);
            if (cacheSeviceCallData != null) {
                request.setStart_date(cacheSeviceCallData.getLastCalled());
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "BackgroundSyncService: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
                }
            }
            ServiceCaller sc = new ServiceCaller(BackgroundSyncService.this);
            sc.getAllMedialanguageLatestContent(request, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (Consts.IS_DEBUG_LOG) {
                        Log.d(Consts.LOG_TAG, "BackgroundSyncService: callMedialanguageLatestAsync success result: " + isComplete);
                    }
                    //logBackgroundSyncServiceDetails("BackgroundSyncService");
//                DbHelper db = new DbHelper(BackgroundSyncService.this);
//                List<Data> data_item = db.getAllMedialanguageLatestDataEntity();
//                Log.d(Consts.LOG_TAG, "BackgroundSyncService: data_item count: " + data_item.size());
                }
            });
        }
    }

    //Language service call
    public void callGetAllLanguage(final Bundle extras) {
        if (Utility.isOnline(BackgroundSyncService.this)) {
            final ServiceHelper sh = new ServiceHelper(BackgroundSyncService.this);
            sh.calllanguagesService(new IServiceSuccessCallback<String>() {
                @Override
                public void onDone(final String callerUrl, String d, String error) {
                    if (Consts.IS_DEBUG_LOG) {
                        DbHelper db = new DbHelper(BackgroundSyncService.this);
                        List<LanguageData> data = db.getAllLanguageDataEntity();
                        Log.d(Consts.LOG_TAG, "BackgroundSyncService: callGetAllLanguage data_item count: " + data.size() + "  " + data);
                    }
                }
            });
        }
    }

    //get all Quizzes
    private void callQuizzesAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(BackgroundSyncService.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.Quizzes);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "BackgroundSyncService: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(BackgroundSyncService.this);
        sc.getAllQuizzes(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
              /*  if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "BackgroundSyncService: callMedialanguageLatestAsync success result: " + isComplete);
                }
                DbHelper db = new DbHelper(MainActivity.this);
                List<Data> data = db.getAllMedialanguageLatestDataEntity();
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "BackgroundSyncService: callMedialanguageLatestAsync data_item count: " + data.size());
                }*/
            }
        });
    }

    //get all Quizzes Questions
    private void callQuizzesQuestionsAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(BackgroundSyncService.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.QuizzesQuestions);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "BackgroundSyncService: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(BackgroundSyncService.this);
        sc.getAllQuizzesQuestions(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {

            }
        });
    }

    //get all Quizzes Options
    private void callQuizzesQuestionsOptionsAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        request.setPage(1);
        DbHelper db = new DbHelper(BackgroundSyncService.this);
        CacheServiceCallData cacheSeviceCallData = db.getCacheServiceCallByUrl(Consts.QuizzesOptions);
        if (cacheSeviceCallData != null) {
            request.setStart_date(cacheSeviceCallData.getLastCalled());
            Log.d(Consts.LOG_TAG, "BackgroundSyncService: cacheSeviceCallData : " + cacheSeviceCallData.getLastCalled());
        }
        ServiceCaller sc = new ServiceCaller(BackgroundSyncService.this);
        sc.getAllQuizzesQuestionsOptions(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {

            }
        });
    }

    //get all Content Quiz
    private void callContentQuizAsync() {
        ContentServiceRequest request = new ContentServiceRequest();
        ServiceCaller sc = new ServiceCaller(BackgroundSyncService.this);
        sc.ContentQuiz(request, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {

            }
        });
    }

    private void logBackgroundSyncServiceDetails(String eventName) {
        DbHelper db = new DbHelper(BackgroundSyncService.this);
        AccountData acdata = db.getAccountData();
        LogAnalyticsHelper analyticsHelper = new LogAnalyticsHelper(BackgroundSyncService.this);
        Bundle bundle = new Bundle();
        if (acdata != null && !acdata.equals("")) {
            bundle.putString("Unique_Identifier", acdata.getId() + "");
            bundle.putString("Name", acdata.getName());
            bundle.putString("EmailID", acdata.getEmail());
            bundle.putString("Phone", acdata.getPhone());
            bundle.putString("Role", acdata.getRole());
            bundle.putString("Status", acdata.getIsVerified() + "");
            bundle.putString("Initial_Language_Selected", acdata.getPreferred_language_id() + "");
        }
        bundle.putString("Date_Time", Utility.getCurrentDateTime());
        bundle.putString("Application_Version", Utility.getAppVersion(BackgroundSyncService.this) + "");
        bundle.putString("Mobile_Make", AppController.getInstance().deviceName());
        bundle.putString("Mobile_Model", AppController.getInstance().deviceModel());
        bundle.putString("Current_Language_Selected", Utility.getLanguageIdFromSharedPreferences(BackgroundSyncService.this) + "");
        bundle.putString("Language_Name_Code", Utility.getLanguageCodeFromSharedPreferences(BackgroundSyncService.this));
        analyticsHelper.logEvent(eventName, bundle);


        /*LogsDataRequest ServiceRequest = new LogsDataRequest();
        ServiceRequest.setLogEventName(eventName);
        ServiceRequest.setUnique_Identifier(acdata.getId() + "");
        ServiceRequest.setName(acdata.getName());
        ServiceRequest.setEmailID(acdata.getEmail());
        ServiceRequest.setPhone(acdata.getPhone());
        ServiceRequest.setRole(acdata.getRole());
        ServiceRequest.setDate_Time(Utility.getCurrentDateTime());
        ServiceRequest.setApplication_Version(Utility.getAppVersion(BackgroundSyncService.this) + "");
        ServiceRequest.setMobile_Make(AppController.getInstance().deviceName());
        ServiceRequest.setMobile_Model(AppController.getInstance().deviceModel());
        ServiceRequest.setStatus(acdata.getIsVerified() + "");
        ServiceRequest.setCurrent_Language_Selected(Utility.getLanguageIdFromSharedPreferences(BackgroundSyncService.this) + "");
        ServiceRequest.setLanguage_Name_Code(Utility.getLanguageCodeFromSharedPreferences(BackgroundSyncService.this));
        ServiceRequest.setInitial_Language_Selected(acdata.getPreferred_language_id() + "");
        ServiceRequest.setLatitude(String.valueOf(LocationUtility.currentLatitude));
        ServiceRequest.setLongitude(String.valueOf(LocationUtility.currentLongitude));
        ServiceRequest.setOS_Version(AppController.getInstance().OSInfo());

        if (Utility.isOnline(BackgroundSyncService.this)) {
            ServiceCaller sc = new ServiceCaller(BackgroundSyncService.this);
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
        }*/

    }
}
