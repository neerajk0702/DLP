package dlp.bluelupin.dlp.Services;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.AccountServiceRequest;
import dlp.bluelupin.dlp.Models.ApplicationVersionResponse;
import dlp.bluelupin.dlp.Models.CacheServiceCallData;
import dlp.bluelupin.dlp.Models.ContentData;
import dlp.bluelupin.dlp.Models.ContentServiceRequest;
import dlp.bluelupin.dlp.Models.Content_status;
import dlp.bluelupin.dlp.Models.DashboardData;
import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.Models.LanguageData;
import dlp.bluelupin.dlp.Models.LogsDataRequest;
import dlp.bluelupin.dlp.Models.OtpData;
import dlp.bluelupin.dlp.Models.OtpVerificationServiceRequest;
import dlp.bluelupin.dlp.Models.ProfileUpdateServiceRequest;
import dlp.bluelupin.dlp.Models.StatusUpdateService;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Utilities.LocationUtility;
import dlp.bluelupin.dlp.Utilities.LogAnalyticsHelper;
import dlp.bluelupin.dlp.Utilities.Utility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by subod on 22-Jul-16.
 */
public class ServiceHelper {

    IServiceManager service;
    Context context;

    public ServiceHelper(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(IServiceManager.class);
    }

    public void callContentService(ContentServiceRequest request, final IServiceSuccessCallback<ContentData> callback) {
        request.setApi_token(Consts.API_KEY);

        Call<ContentData> cd = service.latestContent(request);
        final DbHelper dbhelper = new DbHelper(context);
        cd.enqueue(new Callback<ContentData>() {
            @Override
            public void onResponse(Call<ContentData> call, final Response<ContentData> response) {
                final ContentData cd = response.body();
                //Log.d(Consts.LOG_TAG, cd.toString());
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        for (Data d : response.body().getData()) {
                            if (dbhelper.upsertDataEntity(d)) {
                                // publishProgress(cd.getCurrent_page() * cd.getPer_page() / cd.getTotal());
                                // Log.d(Consts.LOG_TAG,"successfully adding Data for page: "+ cd.getCurrent_page());
                            } else {
                                Log.d(Consts.LOG_TAG, "failure adding Data for page: " + cd.getCurrent_page());
                            }
                        }
                        String lastcalled = response.headers().get("last_request_date");
                        Log.d(Consts.LOG_TAG, "response last_request_date: " + lastcalled);
                        if (lastcalled != null) {
                            DbHelper dbhelper = new DbHelper(context);
                            CacheServiceCallData ob = new CacheServiceCallData();
                            ob.setUrl(Consts.URL_CONTENT_LATEST);
                            ob.setLastCalled(lastcalled);

                            dbhelper.upsertCacheServiceCall(ob);
                        }
                        callback.onDone(Consts.URL_CONTENT_LATEST, cd, null);
                        return null;
                    }
                }.execute();

            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service latestContent" + t.toString());
                callback.onDone(Consts.URL_CONTENT_LATEST, null, t.toString());
            }

        });
    }

    public void callResourceService(ContentServiceRequest request, final IServiceSuccessCallback<ContentData> callback) {
        request.setApi_token(Consts.API_KEY);

        final Call<ContentData> cd = service.latestResource(request);
        final DbHelper dbhelper = new DbHelper(context);
        cd.enqueue(new Callback<ContentData>() {
            @Override
            public void onResponse(Call<ContentData> call, final Response<ContentData> response) {
                final ContentData cd = response.body();
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {

                        //Log.d(Consts.LOG_TAG, cd.toString());

                        for (Data d : cd.getData()) {
                            Log.d(Consts.LOG_TAG, "*******: updated " + d.getName() + d.getContent());
                            if (dbhelper.upsertResourceEntity(d)) {
                                // publishProgress(cd.getCurrent_page() * cd.getPer_page() / cd.getTotal());
                                //Log.d(Consts.LOG_TAG,"successfully adding Data for page: "+ cd.getCurrent_page());
                            } else {
                                Log.d(Consts.LOG_TAG, "failure adding resource Data for page: " + cd.getCurrent_page());
                            }
                        }
                        String lastCalled = response.headers().get("last_request_date");
                        Log.d(Consts.LOG_TAG, "******: response last_request_date: " + lastCalled);
                        if (lastCalled != null) {
                            DbHelper dbhelper = new DbHelper(context);
                            CacheServiceCallData ob = new CacheServiceCallData();
                            ob.setUrl(Consts.URL_LANGUAGE_RESOURCE_LATEST);
                            ob.setLastCalled(lastCalled);

                            dbhelper.upsertCacheServiceCall(ob);
                        }
                        callback.onDone(Consts.URL_LANGUAGE_RESOURCE_LATEST, cd, null);
                        return null;
                    }
                }.execute();

            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service latestResource" + t.toString());
                callback.onDone(Consts.URL_LANGUAGE_RESOURCE_LATEST, null, t.toString());
            }

        });
    }

    public void callMediaService(ContentServiceRequest request, final IServiceSuccessCallback<ContentData> callback) {
        request.setApi_token(Consts.API_KEY);

        final Call<ContentData> cd = service.latestMedia(request);
        Log.d(Consts.LOG_TAG, "payload***" + request);
        final DbHelper dbhelper = new DbHelper(context);
        cd.enqueue(new Callback<ContentData>() {
            @Override
            public void onResponse(Call<ContentData> call, final Response<ContentData> response) {
                final ContentData cd = response.body();
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        //Log.d(Consts.LOG_TAG, cd.toString());
                        for (Data d : response.body().getData()) {
                            if (dbhelper.upsertMediaEntity(d)) {
                                // publishProgress(cd.getCurrent_page() * cd.getPer_page() / cd.getTotal());
                                // Log.d(Consts.LOG_TAG,"successfully adding Data for page: "+ cd.getCurrent_page());

                                Data data = dbhelper.getMediaEntityByIdAndLaunguageId(d.getId(),
                                        Utility.getLanguageIdFromSharedPreferences(context));
                                if (data != null) {
                                    if (data.getLocalFilePath() != null && !data.getLocalFilePath().equals("")) {
                                        if (Consts.IS_DEBUG_LOG) {
                                            Log.d(Consts.LOG_TAG, "getLocalFilePath " + null);
                                        }
                                    } else {
                                        //upsert media entity if not downloaded
                                        dbhelper.upsertDownloadMediaEntity(d);
                                        if (Consts.IS_DEBUG_LOG) {
                                            //Log.d(Consts.LOG_TAG, " Data for page: " + d);
                                        }
                                    }
                                }
                            } else {
                                if (Consts.IS_DEBUG_LOG) {
                                    Log.d(Consts.LOG_TAG, "failure adding Media Data for page: " + cd.getCurrent_page());
                                }
                            }
                        }
                        String lastCalled = response.headers().get("last_request_date");
                        Log.d(Consts.LOG_TAG, "response last_request_date: " + lastCalled);
                        if (lastCalled != null) {
                            DbHelper dbhelper = new DbHelper(context);
                            CacheServiceCallData ob = new CacheServiceCallData();
                            ob.setUrl(Consts.URL_MEDIA_LATEST);
                            ob.setLastCalled(lastCalled);

                            dbhelper.upsertCacheServiceCall(ob);
                        }
                        callback.onDone(Consts.URL_MEDIA_LATEST, cd, null);
                        return null;
                    }
                }.execute();

            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service latest Media" + t.toString());
                callback.onDone(Consts.URL_MEDIA_LATEST, null, t.toString());
            }

        });
    }

    //call medialanguage/latest Service
    public void callMedialanguageLatestContentService(ContentServiceRequest request, final IServiceSuccessCallback<ContentData> callback) {
        request.setApi_token(Consts.API_KEY);

        final Call<ContentData> cd = service.MedialanguageLatestContent(request);
        final DbHelper dbhelper = new DbHelper(context);
        cd.enqueue(new Callback<ContentData>() {
            @Override
            public void onResponse(Call<ContentData> call, final Response<ContentData> response) {
                final ContentData cd = response.body();
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        //Log.d(Consts.LOG_TAG, cd.toString());
                        if (response.body() != null) {
                            for (Data d : response.body().getData()) {

                                if (dbhelper.upsertMedialanguageLatestDataEntity(d)) {
                                    // publishProgress(cd.getCurrent_page() * cd.getPer_page() / cd.getTotal());
                                    //  Log.d(Consts.LOG_TAG,"successfully adding Data for page: "+ cd.getCurrent_page());
                                } else {
                                    Log.d(Consts.LOG_TAG, "failure adding Data for page: " + cd.getCurrent_page());
                                }
                            }
                        }
                        String lastcalled = response.headers().get("last_request_date");
                        Log.d(Consts.LOG_TAG, "response last_request_date: " + lastcalled);
                        if (lastcalled != null) {
                            DbHelper dbhelper = new DbHelper(context);
                            CacheServiceCallData ob = new CacheServiceCallData();
                            ob.setUrl(Consts.MediaLanguage_Latest);
                            ob.setLastCalled(lastcalled);

                            dbhelper.upsertCacheServiceCall(ob);
                        }
                        callback.onDone(Consts.MediaLanguage_Latest, cd, null);
                        return null;
                    }
                }.execute();


            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callMedialanguageLatestAsync" + t.toString());
                callback.onDone(Consts.MediaLanguage_Latest, null, t.toString());
            }

        });
    }

    //call Quizzes Service
    public void callQuizzesService(ContentServiceRequest request, final IServiceSuccessCallback<ContentData> callback) {
        request.setApi_token(Consts.API_KEY);

        final Call<ContentData> cd = service.QuizzesContent(request);
        final DbHelper dbhelper = new DbHelper(context);
        cd.enqueue(new Callback<ContentData>() {
            @Override
            public void onResponse(Call<ContentData> call, final Response<ContentData> response) {
                final ContentData cd = response.body();
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        if (response.body() != null) {
                            for (Data d : response.body().getData()) {
                                if (dbhelper.upsertQuizzesDataEntity(d)) {
                                } else {
                                    Log.d(Consts.LOG_TAG, "failure adding Data for page: " + cd.getCurrent_page());
                                }
                            }
                        }
                        String lastcalled = response.headers().get("last_request_date");
                        Log.d(Consts.LOG_TAG, "response last_request_date: " + lastcalled);
                        if (lastcalled != null) {
                            DbHelper dbhelper = new DbHelper(context);
                            CacheServiceCallData ob = new CacheServiceCallData();
                            ob.setUrl(Consts.Quizzes);
                            ob.setLastCalled(lastcalled);

                            dbhelper.upsertCacheServiceCall(ob);
                        }
                        callback.onDone(Consts.Quizzes, cd, null);
                        return null;
                    }
                }.execute();
            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callQuizzesServiceAsync" + t.toString());
                callback.onDone(Consts.Quizzes, null, t.toString());
            }
        });
    }

    //call Quizzes Questions Service
    public void callQuizzesQuestionsService(ContentServiceRequest request, final IServiceSuccessCallback<ContentData> callback) {
        request.setApi_token(Consts.API_KEY);

        final Call<ContentData> cd = service.QuizzesQuestionsContent(request);
        final DbHelper dbhelper = new DbHelper(context);
        cd.enqueue(new Callback<ContentData>() {
            @Override
            public void onResponse(Call<ContentData> call, final Response<ContentData> response) {
                final ContentData cd = response.body();
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        if (response.body() != null) {
                            for (Data d : response.body().getData()) {
                                if (dbhelper.upsertQuizzesQuestionsDataEntity(d)) {
                                } else {
                                    Log.d(Consts.LOG_TAG, "failure adding Data for page: " + cd.getCurrent_page());
                                }
                            }
                        }
                        String lastcalled = response.headers().get("last_request_date");
                        Log.d(Consts.LOG_TAG, "response last_request_date: " + lastcalled);
                        if (lastcalled != null) {
                            DbHelper dbhelper = new DbHelper(context);
                            CacheServiceCallData ob = new CacheServiceCallData();
                            ob.setUrl(Consts.QuizzesQuestions);
                            ob.setLastCalled(lastcalled);

                            dbhelper.upsertCacheServiceCall(ob);
                        }
                        callback.onDone(Consts.QuizzesQuestions, cd, null);
                        return null;
                    }
                }.execute();
            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callQuizzesServiceAsync" + t.toString());
                callback.onDone(Consts.QuizzesQuestions, null, t.toString());
            }
        });
    }

    //call Quizzes Questions Options Service
    public void callQuizzesQuestionsOptionsService(ContentServiceRequest request, final IServiceSuccessCallback<ContentData> callback) {
        request.setApi_token(Consts.API_KEY);

        final Call<ContentData> cd = service.QuizzesOptionsContent(request);
        final DbHelper dbhelper = new DbHelper(context);
        cd.enqueue(new Callback<ContentData>() {
            @Override
            public void onResponse(Call<ContentData> call, final Response<ContentData> response) {
                final ContentData cd = response.body();
                try {
                    if (response.body() != null) {
                        for (Data d : response.body().getData()) {
                            if (dbhelper.upsertQuestionsOptionsDataEntity(d)) {
                            } else {
                                Log.d(Consts.LOG_TAG, "failure adding Data for page: " + cd.getCurrent_page());
                            }
                        }
                    }
                    String lastcalled = response.headers().get("last_request_date");
                    Log.d(Consts.LOG_TAG, "response last_request_date: " + lastcalled);
                    if (lastcalled != null) {
                        DbHelper dbhelper = new DbHelper(context);
                        CacheServiceCallData ob = new CacheServiceCallData();
                        ob.setUrl(Consts.QuizzesOptions);
                        ob.setLastCalled(lastcalled);

                        dbhelper.upsertCacheServiceCall(ob);
                    }
                    callback.onDone(Consts.QuizzesOptions, cd, null);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callQuizzesServiceAsync" + t.toString());
                callback.onDone(Consts.QuizzesOptions, null, t.toString());
            }
        });
    }

    // Content Quiz service
    public void callContentQuizService(ContentServiceRequest request, final IServiceSuccessCallback<ContentData> callback) {
        request.setApi_token(Consts.API_KEY);

        final Call<ContentData> cd = service.ContentQuiz(request);
        Log.d(Consts.LOG_TAG, "payload***" + request);
        final DbHelper dbhelper = new DbHelper(context);
        cd.enqueue(new Callback<ContentData>() {
            @Override
            public void onResponse(Call<ContentData> call, final Response<ContentData> response) {
                final ContentData cd = response.body();
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        if (response.body() != null) {
                            for (Data d : response.body().getData()) {
                                if (dbhelper.upsertContentQuizEntity(d)) {
                                } else {
                                    Log.d(Consts.LOG_TAG, "failure adding Data for page: " + cd.getCurrent_page());
                                }
                            }
                        }
                        String lastcalled = response.headers().get("last_request_date");
                        Log.d(Consts.LOG_TAG, "response last_request_date: " + lastcalled);
                        if (lastcalled != null) {
                            DbHelper dbhelper = new DbHelper(context);
                            CacheServiceCallData ob = new CacheServiceCallData();
                            ob.setUrl(Consts.ContentQuiz);
                            ob.setLastCalled(lastcalled);

                            dbhelper.upsertCacheServiceCall(ob);
                        }
                        callback.onDone(Consts.ContentQuiz, cd, null);
                        return null;
                    }
                }.execute();
            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callContentQuizService" + t.toString());
                callback.onDone(Consts.ContentQuiz, null, t.toString());
            }
        });
    }

    //create account service
    public void callCreateAccountService(AccountServiceRequest request, final IServiceSuccessCallback<AccountData> callback) {
        request.setApi_token(Consts.API_KEY);

        final DbHelper dbhelper = new DbHelper(context);
        String device_token = Utility.getDeviceIDFromSharedPreferences(context);
        if (device_token != null) {
            request.setDevice_token(device_token);
        }
        request.setService(Consts.SERVICE);
        request.setIs_development(Consts.IS_DEVELOPMENT);
        Call<AccountData> ac = service.accountCreate(request);
        Log.d(Consts.LOG_TAG, "payload***" + request);
        ac.enqueue(new Callback<AccountData>() {
            @Override
            public void onResponse(Call<AccountData> call, Response<AccountData> response) {
                AccountData data = response.body();

                if (data != null) {
                    Log.d(Consts.LOG_TAG, "account create data_item:" + data.toString());
                    LogAnalyticsHelper analyticsHelper = new LogAnalyticsHelper(context);
                    Bundle bundle = new Bundle();
                    bundle.putString("Unique_Identifier", data.getId() + "");
                    bundle.putString("ContentId", data.getId() + "");
                    bundle.putString("Name", data.getName());
                    bundle.putString("EmailID", data.getEmail());
                    bundle.putString("Phone", data.getPhone());
                    bundle.putString("Role", data.getRole());
                    bundle.putString("Date_Time", Utility.getCurrentDateTime());
                    bundle.putString("Application_Version", Utility.getAppVersion(context) + "");
                    bundle.putString("Mobile_Make", AppController.getInstance().deviceName());
                    bundle.putString("Mobile_Model", AppController.getInstance().deviceModel());
                    bundle.putString("Status", data.getIsVerified() + "");
                    bundle.putString("Current_Language_Selected", Utility.getLanguageIdFromSharedPreferences(context) + "");
                    bundle.putString("Language_Name_Code", Utility.getLanguageCodeFromSharedPreferences(context));
                    bundle.putString("Initial_Language_Selected", data.getPreferred_language_id() + "");
                    analyticsHelper.logEvent("Registeration", bundle);
                    dbhelper.deleteAccountData();
                    if (dbhelper.upsertAccountData(data)) {
                        Utility.setUserServerIdIntoSharedPreferences(context, data.getId());//for check verification done or not
                        Log.d(Consts.LOG_TAG, "Account successfully add in database ");
                    }
                    LogsDataRequest ServiceRequest = new LogsDataRequest();
                    ServiceRequest.setLogEventName("Registeration");
                    ServiceRequest.setUnique_Identifier(data.getId() + "");
                    ServiceRequest.setContentId(data.getId() + "");
                    ServiceRequest.setName(data.getName());
                    ServiceRequest.setEmailID(data.getEmail());
                    ServiceRequest.setPhone(data.getPhone());
                    ServiceRequest.setRole(data.getRole());
                    ServiceRequest.setDate_Time(Utility.getCurrentDateTime());
                    ServiceRequest.setApplication_Version(Utility.getAppVersion(context) + "");
                    ServiceRequest.setMobile_Make(AppController.getInstance().deviceName());
                    ServiceRequest.setMobile_Model(AppController.getInstance().deviceModel());
                    ServiceRequest.setStatus(data.getIsVerified() + "");
                    ServiceRequest.setCurrent_Language_Selected(Utility.getLanguageIdFromSharedPreferences(context) + "");
                    ServiceRequest.setLanguage_Name_Code(Utility.getLanguageCodeFromSharedPreferences(context));
                    ServiceRequest.setInitial_Language_Selected(data.getPreferred_language_id() + "");
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
                    callback.onDone(Consts.CREATE_NEW_USER, data, null);
                } else {
                    callback.onDone(Consts.CREATE_NEW_USER, null, null);
                }

            }

            @Override
            public void onFailure(Call<AccountData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service account create" + t.toString());
                callback.onDone(Consts.CREATE_NEW_USER, null, t.toString());
            }

        });


    }

    //updated profile service
    public void callProfileUpdateService(ProfileUpdateServiceRequest request, final IServiceSuccessCallback<AccountData> callback) {

        final DbHelper dbhelper = new DbHelper(context);
        AccountData accountDataApToken = dbhelper.getAccountData();
        if (accountDataApToken != null) {
            if (accountDataApToken.getApi_token() != null) {
                request.setApi_token(accountDataApToken.getApi_token());
            }
        }

        Call<AccountData> ac = service.profileUpdated(request);
        Log.d(Consts.LOG_TAG, "payload***" + request);
        ac.enqueue(new Callback<AccountData>() {
            @Override
            public void onResponse(Call<AccountData> call, Response<AccountData> response) {
                AccountData data = response.body();

                if (data != null) {
                    Log.d(Consts.LOG_TAG, "profile updated data_item:" + data.toString());
                    if (dbhelper.updateAccountDataAfter(data)) {
                        Log.d(Consts.LOG_TAG, "Profile updated successfully in database ");
                    }
                    callback.onDone(Consts.Profile_Update, data, null);
                } else {
                    callback.onDone(Consts.Profile_Update, null, null);
                }

            }

            @Override
            public void onFailure(Call<AccountData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service Profile_Update" + t.toString());
                callback.onDone(Consts.Profile_Update, null, t.toString());
            }

        });
    }

    //Otp verification service
    public void callOtpVerificationService(OtpVerificationServiceRequest request, final IServiceSuccessCallback<OtpData> callback) {


        final DbHelper dbhelper = new DbHelper(context);

        final int serverId = Utility.getUserServerIdFromSharedPreferences(context);
        AccountData accountData = dbhelper.getAccountData();
        if (accountData != null) {
            if (accountData.getPhone() != null) {
                request.setPhone(accountData.getPhone());
            }
        }
        Log.d(Consts.LOG_TAG, "payload***" + request);
        Call<OtpData> ac = service.otpVerify(request);
        ac.enqueue(new Callback<OtpData>() {
            @Override
            public void onResponse(Call<OtpData> call, Response<OtpData> response) {
                OtpData data = response.body();
                AccountData accountData = new AccountData();
                if (data != null) {
                    Consts.USER_API_KEY = data.getApi_token();

                    Log.d(Consts.LOG_TAG, "Api_token : " + data.getApi_token());
                    accountData.setId(serverId);
                    accountData.setIsVerified(1);
                    accountData.setApi_token(data.getApi_token());
                    //update account verified for check account verified or not
                    dbhelper.updateAccountDataVerified(accountData);
                    if (Consts.IS_DEBUG_LOG) {
                        Log.d(Consts.LOG_TAG, "response data_item:" + response.toString());
                        Log.d(Consts.LOG_TAG, "Otp verify data_item:" + data.toString());
                    }
                    Toast.makeText(context, context.getString(R.string.registered), Toast.LENGTH_LONG).show();
                    //Log.d(Consts.LOG_TAG, "Otp verify successfully ");
                    callback.onDone(Consts.VERIFY_OTP, data, null);
                } else {
                    accountData.setId(serverId);
                    accountData.setIsVerified(0);
                    accountData.setApi_token("");
                    //update account verified for check account verified or not
                    dbhelper.updateAccountDataVerified(accountData);
                    Toast.makeText(context, context.getString(R.string.enter_valid_otp), Toast.LENGTH_LONG).show();
                    if (Consts.IS_DEBUG_LOG) {
                        Log.d(Consts.LOG_TAG, "response data_item:" + response.toString());
                        Log.d(Consts.LOG_TAG, "Otp not verify");
                    }
                    callback.onDone(Consts.VERIFY_OTP, null, null);
                }

            }

            @Override
            public void onFailure(Call<OtpData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service latestContent" + t.toString());
                callback.onDone(Consts.VERIFY_OTP, null, t.toString());
            }

        });
    }

    //download file request delete
    public void callDownloadDeleteRequest(String fileUrl) {
        Call<ResponseBody> call = service.downloadFileWithDynamicUrlSync(fileUrl);
        call.cancel();
    }

    //download file service
    public void callDownloadFileService(final String fileUrl, final int mediaId, final IServiceSuccessCallback<String> callback) {
        Call<ResponseBody> call = service.downloadFileWithDynamicUrlSync(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (Consts.IS_DEBUG_LOG) {
                        Log.d(Consts.LOG_TAG, "server contacted and has file");
                    }

                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), fileUrl, mediaId);
                    if (writtenToDisk) {
                        DbHelper dbHelper = new DbHelper(context);
                    }
                    callback.onDone(fileUrl, response.toString(), null);
                    if (Consts.IS_DEBUG_LOG) {
                        Log.d(Consts.LOG_TAG, "file download was a success? " + writtenToDisk);
                    }
                } else {
                    callback.onDone(fileUrl, null, null);
                    if (Consts.IS_DEBUG_LOG) {
                        Log.d(Consts.LOG_TAG, "server contact failed");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (Consts.IS_DEBUG_LOG) {
                    Log.e(Consts.LOG_TAG, "error");
                }
            }
        });


    }

    //write download file
    private boolean writeResponseBodyToDisk(ResponseBody body, String fileUrl, int mediaId) {
        try {
            //File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "Future Studio Icon.png");

            File testDirectory = Utility.getFilePath(context);
            String localFilePath = null;

            if (fileUrl.contains(".mp4")) {
                localFilePath = testDirectory + File.separator + mediaId + ".mp4";
            } else {
                if (fileUrl.contains(".jpg")) {
                    localFilePath = testDirectory + File.separator + mediaId + ".jpg";
                } else if (fileUrl.contains(".png")) {
                    localFilePath = testDirectory + File.separator + mediaId + ".png";
                } else if (fileUrl.contains(".jpeg")) {
                    localFilePath = testDirectory + File.separator + mediaId + ".jpeg";
                } else {
                    if (Consts.IS_DEBUG_LOG) {
                        Log.d(Consts.LOG_TAG, "downloading file worng formate");
                    }
                }
            }
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "localFilePath = " + localFilePath);
            }


            InputStream inputStream = null;
            FileOutputStream outputStream = null;

            try {
                testDirectory.mkdirs();
                if (!testDirectory.exists()) {
                    testDirectory.mkdir();
                }

                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                long total = 0;
                int progress = 0;

                inputStream = body.byteStream();
                long lenghtOfFile = body.contentLength();
                outputStream = new FileOutputStream(localFilePath);
                DbHelper dbHelper = new DbHelper(context);
                while (true) {
                    int read = inputStream.read(fileReader);
                    total += read;
                    int progress_temp = (int) ((int) total * 100 / lenghtOfFile);

                    progress = progress_temp;
                    Data downloadingFileData = new Data();
                    downloadingFileData.setMediaId(mediaId);
                    downloadingFileData.setProgress(progress);
                    dbHelper.upsertDownloadingFileEntity(downloadingFileData);

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(Consts.mBroadcastProgressUpdateAction);
                    broadcastIntent.putExtra("progresss", progress);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);

                    if (Consts.IS_DEBUG_LOG) {
                        Log.d(Consts.LOG_TAG, "progress: " + progress + " progress_temp " + progress_temp);
                    }
                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;


                    if (Consts.IS_DEBUG_LOG) {
                        Log.d(Consts.LOG_TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                    }
                }
                //update MediaEntity table for local file path
                Data fileData = new Data();
                fileData.setLocalFilePath(localFilePath);
                fileData.setId(mediaId);
                dbHelper.updateMediaLanguageLocalFilePathEntity(fileData);
                dbHelper.updateDownloadMediaLocalFilePathEntity(fileData);

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    //call languages service
    public void calllanguagesService(final IServiceSuccessCallback<String> callback) {
        Call<List<LanguageData>> call = service.getLanguage(Consts.Languages);
        final DbHelper dbHelper = new DbHelper(context);
        call.enqueue(new Callback<List<LanguageData>>() {
            @Override
            public void onResponse(Call<List<LanguageData>> call, Response<List<LanguageData>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (LanguageData data : response.body()) {
                            if (dbHelper.upsertLanguageDataEntity(data)) {
                                // Log.d(Consts.LOG_TAG,"successfully adding Data: "+ data_item.getName());
                            } else {
                                Log.d(Consts.LOG_TAG, "failure adding Data for page: " + data.getName());
                            }
                        }
                    }
                    callback.onDone(Consts.Languages, "Done", null);
                } else {
                    callback.onDone(Consts.Languages, null, null);
                    if (Consts.IS_DEBUG_LOG) {
                        Log.d(Consts.LOG_TAG, "server response false");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LanguageData>> call, Throwable t) {
                callback.onDone(Consts.Languages, null, null);
                if (Consts.IS_DEBUG_LOG) {
                    Log.e(Consts.LOG_TAG, "error");
                }
            }
        });
    }

    //call notification service to get notification data_item
    public void callNotificationService(ContentServiceRequest request, final IServiceSuccessCallback<ContentData> callback) {
        request.setApi_token(Consts.API_KEY);

        int languageId = Utility.getLanguageIdFromSharedPreferences(context);
        if (languageId != 0) {
            request.setLanguage_id(languageId);
        }
        Call<ContentData> cd = service.NotificationContent(request);
        final DbHelper dbhelper = new DbHelper(context);
        cd.enqueue(new Callback<ContentData>() {
            @Override
            public void onResponse(Call<ContentData> call, Response<ContentData> response) {
                ContentData cd = response.body();
                //Log.d(Consts.LOG_TAG, cd.toString());

                for (Data d : response.body().getData()) {
                    if (dbhelper.upsertNotificationDataEntity(d)) {
                        // Log.d(Consts.LOG_TAG,"successfully adding Data for page: "+ cd.getCurrent_page());
                    } else {
                        Log.d(Consts.LOG_TAG, "failure adding Data for page: " + cd.getCurrent_page());
                    }
                }
                String lastcalled = response.headers().get("last_request_date");
                Log.d(Consts.LOG_TAG, "response last_request_date: " + lastcalled);
                if (lastcalled != null) {
                    DbHelper dbhelper = new DbHelper(context);
                    CacheServiceCallData ob = new CacheServiceCallData();
                    ob.setUrl(Consts.Notifications);
                    ob.setLastCalled(lastcalled);

                    dbhelper.upsertCacheServiceCall(ob);
                }
                callback.onDone(Consts.Notifications, cd, null);
            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service latestContent" + t.toString());
                callback.onDone(Consts.Notifications, null, t.toString());
            }

        });
    }

    //create app version service
    public void callVersionApiService(String version, final IServiceSuccessCallback<ApplicationVersionResponse> callback) {
        Call<ApplicationVersionResponse> cd = service.GetPlayStoreVersion(Consts.PlayStoreVersion);
        cd.enqueue(new Callback<ApplicationVersionResponse>() {

            @Override
            public void onResponse(Call<ApplicationVersionResponse> call, Response<ApplicationVersionResponse> response) {
                if (response != null) {
                    callback.onDone(Consts.PlayStoreVersion, response.body(), null);
                }
            }

            @Override
            public void onFailure(Call<ApplicationVersionResponse> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callVersionApiService" + t.toString());
                callback.onDone(Consts.PlayStoreVersion, null, t.toString());
            }
        });

//
    }

    //create log service
    public void callLogsService(LogsDataRequest request, final IServiceSuccessCallback<LogsDataRequest> callback) {
        Call<LogsDataRequest> ac = service.saveLogs(request);
        Log.d(Consts.LOG_TAG, "payload***" + request);
        ac.enqueue(new Callback<LogsDataRequest>() {
            @Override
            public void onResponse(Call<LogsDataRequest> call, Response<LogsDataRequest> response) {
                LogsDataRequest data = response.body();

                if (data != null) {
                    Log.d(Consts.LOG_TAG, "logs Responce:" + data.toString());
                    callback.onDone(Consts.logsRequest, data, null);
                } else {
                    callback.onDone(Consts.logsRequest, null, null);
                }
            }

            @Override
            public void onFailure(Call<LogsDataRequest> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service logsRequest" + t.toString());
                callback.onDone(Consts.logsRequest, null, t.toString());
            }

        });
    }

    //resend opt service
    public void callResenOtpService(AccountServiceRequest request, final IServiceSuccessCallback<AccountData> callback) {
        // request.setApi_token(Consts.API_KEY);

        Call<AccountData> ac = service.resendOtp(request);
        Log.d(Consts.LOG_TAG, "payload***" + request);
        ac.enqueue(new Callback<AccountData>() {
            @Override
            public void onResponse(Call<AccountData> call, Response<AccountData> response) {
                AccountData data = response.body();
                if (data != null) {
                    if (data.getMessage() != null && !data.getMessage().equals("")) {
                        Log.d(Consts.LOG_TAG, "Otp Resend:" + data.toString());
                        callback.onDone(Consts.CREATE_NEW_USER, data, null);
                    } else {
                        callback.onDone(Consts.CREATE_NEW_USER, null, null);
                    }
                } else {
                    callback.onDone(Consts.CREATE_NEW_USER, null, null);
                }

            }

            @Override
            public void onFailure(Call<AccountData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service account create" + t.toString());
                callback.onDone(Consts.CREATE_NEW_USER, null, t.toString());
            }

        });
    }

    //callStatusUpdatServiceservice
    public void callStatusUpdatService(StatusUpdateService request, final IServiceSuccessCallback<AccountData> callback) {
        final DbHelper dbhelper = new DbHelper(context);
        AccountData accountDataApToken = dbhelper.getAccountData();
        if (accountDataApToken != null) {
            if (accountDataApToken.getApi_token() != null) {
                request.setApi_token(accountDataApToken.getApi_token());
            }
        }

        Call<AccountData> ac = service.statusUpdated(request);
        Log.d(Consts.LOG_TAG, "payload***" + request);
        ac.enqueue(new Callback<AccountData>() {
            @Override
            public void onResponse(Call<AccountData> call, Response<AccountData> response) {
                AccountData data = response.body();

                if (data != null) {
                    Log.d(Consts.LOG_TAG, "callStatusUpdatServices :" + data.toString());
                    callback.onDone(Consts.statusUpdate, data, null);
                } else {
                    callback.onDone(Consts.statusUpdate, null, null);
                }

            }

            @Override
            public void onFailure(Call<AccountData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callStatusUpdatServices" + t.toString());
                callback.onDone(Consts.statusUpdate, null, t.toString());
            }

        });
    }


    //dashboarddata service
    public void calldashboarddataService(int parentId, final IServiceSuccessCallback<DashboardData> callback) {
        Call<DashboardData> cd = null;
        cd = service.Getdashboarddata(Consts.dashboarddata);
        cd.enqueue(new Callback<DashboardData>() {

            @Override
            public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
                if (response != null) {
                    DbHelper dbhelper = new DbHelper(context);
                    DashboardData dashboardData = response.body();
                    if (dashboardData != null && dashboardData.getData().size() > 0) {
                        dbhelper.deleteDashboarddataEntity();//delete old data
                        for (Data data:dashboardData.getData()) {
                            dbhelper.insertDashboarddataEntity(data);
                        }
                        callback.onDone(Consts.dashboarddata, response.body(), null);
                    }else{
                        callback.onDone(Consts.dashboarddata, null, null);
                    }
                }
            }

            @Override
            public void onFailure(Call<DashboardData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service dashboarddata" + t.toString());
                callback.onDone(Consts.dashboarddata, null, t.toString());
            }
        });
    }

    //chapter  service
    public void callChapterdataService(int parentId, final IServiceSuccessCallback<DashboardData> callback) {
        Call<DashboardData> cd = null;
        cd = service.Getchapterdata(Consts.chapterdata + parentId);
        cd.enqueue(new Callback<DashboardData>() {

            @Override
            public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
                if (response != null) {
                    DbHelper dbhelper = new DbHelper(context);
                    DashboardData dashboardData = response.body();
                    dbhelper.deleteChapterdataEntity();
                    dbhelper.insertChapterdataEntity(dashboardData);
                    callback.onDone(Consts.chapterdata, response.body(), null);
                }
            }

            @Override
            public void onFailure(Call<DashboardData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callChapterdataService" + t.toString());
                callback.onDone(Consts.chapterdata, null, t.toString());
            }
        });
    }

    //get contentStatus  service
    public void callContectStatusdataService(final IServiceSuccessCallback<ContentData> callback) {
        final DbHelper dbhelper = new DbHelper(context);
        AccountData accountDataApToken = dbhelper.getAccountData();
        String apiToken = "";
        if (accountDataApToken != null) {
            if (accountDataApToken.getApi_token() != null) {
                apiToken = accountDataApToken.getApi_token();
            }
        }
        Call<ContentData> cd = service.GetContentStatusdata(Consts.GetContentstatus + apiToken);
        cd.enqueue(new Callback<ContentData>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ContentData> call, Response<ContentData> response) {
                if (response != null) {
                    DbHelper dbhelper = new DbHelper(context);
                    ContentData statusData = response.body();
//                    dbhelper.deleteDashboarddataEntity(courseChapterType);//delete old data
                    if (statusData != null && statusData.getContent_status().length >= 0) {
                        for (Content_status contentStatus : statusData.getContent_status()) {
                            dbhelper.upsertcontentStatusEntity(contentStatus);
                        }
                    }
                    callback.onDone(Consts.GetContentstatus, response.body(), null);
                }
            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callContectStatusdataService" + t.toString());
                callback.onDone(Consts.GetContentstatus, null, t.toString());
            }
        });
    }

    public void callInviteFriendService(AccountServiceRequest request, final IServiceSuccessCallback<AccountServiceRequest> callback) {
        final DbHelper dbhelper = new DbHelper(context);
        AccountData accountDataApToken = dbhelper.getAccountData();
        if (accountDataApToken != null) {
            if (accountDataApToken.getApi_token() != null) {
                request.setApi_token(accountDataApToken.getApi_token());
            }
        }

        Call<AccountServiceRequest> ac = service.inviteFriend(request);
        Log.d(Consts.LOG_TAG, "payload***" + request);
        ac.enqueue(new Callback<AccountServiceRequest>() {
            @Override
            public void onResponse(Call<AccountServiceRequest> call, Response<AccountServiceRequest> response) {
                AccountServiceRequest data = response.body();
                if (data != null) {
                    Log.d(Consts.LOG_TAG, "callInviteFriendService :" + data.toString());
                    if (data.isStatus()) {
                        callback.onDone(Consts.inviteFriend, data, null);
                    } else {
                        callback.onDone(Consts.inviteFriend, null, null);
                    }
                } else {
                    callback.onDone(Consts.inviteFriend, null, null);
                }

            }

            @Override
            public void onFailure(Call<AccountServiceRequest> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callInviteFriendService" + t.toString());
                callback.onDone(Consts.inviteFriend, null, t.toString());
            }

        });
    }

    public void callInviteFriendListService(final IServiceSuccessCallback<ContentData> callback) {
        final DbHelper dbhelper = new DbHelper(context);
        AccountData accountDataApToken = dbhelper.getAccountData();
        String apiToken = "";
        if (accountDataApToken != null) {
            if (accountDataApToken.getApi_token() != null) {
                apiToken = accountDataApToken.getApi_token();
            }
        }

        Call<ContentData> ac = service.inviteFriendList(Consts.inviteFriendList + apiToken);
        ac.enqueue(new Callback<ContentData>() {
            @Override
            public void onResponse(Call<ContentData> call, Response<ContentData> response) {
                ContentData data = response.body();
                if (data != null && data.getInvitations().size() > 0) {
                    Log.d(Consts.LOG_TAG, "callInviteFriendListService :" + data.toString());
                    callback.onDone(Consts.inviteFriendList, data, null);
                } else {
                    callback.onDone(Consts.inviteFriendList, null, null);
                }

            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callInviteFriendListService" + t.toString());
                callback.onDone(Consts.inviteFriendList, null, t.toString());
            }

        });
    }

    public void callCertificatesListService(final IServiceSuccessCallback<ContentData> callback) {
        final DbHelper dbhelper = new DbHelper(context);
        AccountData accountDataApToken = dbhelper.getAccountData();
        String apiToken = "";
        if (accountDataApToken != null) {
            if (accountDataApToken.getApi_token() != null) {
                apiToken = accountDataApToken.getApi_token();//"amFA3kOKt5mXWDWVHs8gU5zk5gWe1KS6dV5yJ4pMloyDmJIRqQjI09ohtB9Z";//accountDataApToken.getApi_token();
            }
        }
        Call<ContentData> ac = service.certificateList(Consts.certificatesList + apiToken);
        ac.enqueue(new Callback<ContentData>() {
            @Override
            public void onResponse(Call<ContentData> call, Response<ContentData> response) {
                ContentData data = response.body();
                if (data != null && data.getCertificates().size() > 0) {
                    Log.d(Consts.LOG_TAG, "callCertificatesListService :" + data.toString());
                    callback.onDone(Consts.certificatesList, data, null);
                } else {
                    callback.onDone(Consts.certificatesList, null, null);
                }

            }

            @Override
            public void onFailure(Call<ContentData> call, Throwable t) {
                Log.d(Consts.LOG_TAG, "Failure in service callCertificatesListService" + t.toString());
                callback.onDone(Consts.certificatesList, null, t.toString());
            }

        });
    }

}
