package dlp.bluelupin.dlp.Services;

import android.content.Context;
import android.util.Log;

import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.AccountServiceRequest;
import dlp.bluelupin.dlp.Models.CacheServiceCallData;
import dlp.bluelupin.dlp.Models.ContentData;
import dlp.bluelupin.dlp.Models.ContentServiceRequest;
import dlp.bluelupin.dlp.Models.DashboardData;
import dlp.bluelupin.dlp.Models.LogsDataRequest;
import dlp.bluelupin.dlp.Models.OtpData;
import dlp.bluelupin.dlp.Models.OtpVerificationServiceRequest;
import dlp.bluelupin.dlp.Models.ProfileUpdateServiceRequest;
import dlp.bluelupin.dlp.Models.StatusUpdateService;

/**
 * Created by subod on 25-Jul-16.
 */
public class ServiceCaller {
    Context context;

    public ServiceCaller(Context context) {
        this.context = context;
    }

    public void getAllContent(final ContentServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callContentService(request, new IServiceSuccessCallback<ContentData>() {
            @Override
            public void onDone(final String callerUrl, final ContentData result, String error) {
                Boolean success = false;
                if (result != null) {
                    if (request.getPage() <= result.getLast_page()) {
                        Log.d(Consts.LOG_TAG, "Recursively calling next content page: " + result.getCurrent_page());
                        final ContentServiceRequest nextRequest = new ContentServiceRequest();
                        nextRequest.setPage(result.getCurrent_page() + 1);
                        nextRequest.setStart_date(request.getStart_date());
                        getAllContent(nextRequest, new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {
                                // Log.d(Consts.LOG_TAG, "Success: Service caller getAllContent done at page: " + nextRequest.getPage());
                                if (nextRequest.getPage() > result.getLast_page()) {
                                    Log.d(Consts.LOG_TAG, "Content Parsed successfully till page: " + result.getCurrent_page());
                                    workCompletedCallback.onDone("getAllContent", true);

                                } else {
                                    // all parsed successfully; recursion complete
                                    workCompletedCallback.onDone("getAllContent", true);
                                }
                            }
                        });

                    } else {
                        // all parsed successfully; recursion complete
                        // Log.d(Consts.LOG_TAG, "Content Parsed successfully till page: " + result.getCurrent_page());
                        success = true;
                        workCompletedCallback.onDone("getAllContent", success);
                    }

                } else {
                    success = false;
                }
            }
        });
    }

    public void getAllResource(final ContentServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callResourceService(request, new IServiceSuccessCallback<ContentData>() {
            @Override
            public void onDone(final String callerUrl, final ContentData result, String error) {
                Boolean success = false;
                if (result != null) {
                    if (request.getPage() <= result.getLast_page()) {
                        Log.d(Consts.LOG_TAG, "Recursively calling next resource page: " + result.getCurrent_page());
                        final ContentServiceRequest nextRequest = new ContentServiceRequest();
                        nextRequest.setPage(result.getCurrent_page() + 1);
                        nextRequest.setStart_date(request.getStart_date());
                        getAllResource(nextRequest, new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {

                                if (nextRequest.getPage() > result.getLast_page()) {

                                    Log.d(Consts.LOG_TAG, "resource Parsed successfully till page: " + result.getCurrent_page());
                                    workCompletedCallback.onDone("getAllResource", true);

                                } else {
                                    // all parsed successfully; recursion complete
                                    workCompletedCallback.onDone("getAllResource", true);
                                }
                            }
                        });

                    } else {
                        // all parsed successfully; recursion complete
                        // Log.d(Consts.LOG_TAG, "Content Parsed successfully till page: " + result.getCurrent_page());
                        success = true;
                        workCompletedCallback.onDone("getAllContent", success);
                    }

                } else {
                    success = false;
                }
            }
        });
    }

    public void getAllMedia(final ContentServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callMediaService(request, new IServiceSuccessCallback<ContentData>() {
            @Override
            public void onDone(final String callerUrl, final ContentData result, String error) {
                Boolean success = false;
                if (result != null) {
                    if (request.getPage() <= result.getLast_page()) {
                        Log.d(Consts.LOG_TAG, "Recursively calling next Media page: " + result.getCurrent_page());
                        final ContentServiceRequest nextRequest = new ContentServiceRequest();
                        nextRequest.setPage(result.getCurrent_page() + 1);
                        nextRequest.setStart_date(request.getStart_date());
                        getAllMedia(nextRequest, new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {

                                if (nextRequest.getPage() > result.getLast_page()) {
                                    Log.d(Consts.LOG_TAG, "Media Parsed successfully till page: " + result.getCurrent_page());
                                    workCompletedCallback.onDone("getAllMedia", true);

                                } else {
                                    // all parsed successfully; recursion complete
                                    workCompletedCallback.onDone("getAllMedia", true);
                                }
                            }
                        });

                    } else {
                        // all parsed successfully; recursion complete
                        // Log.d(Consts.LOG_TAG, "Content Parsed successfully till page: " + result.getCurrent_page());
                        success = true;
                        workCompletedCallback.onDone("getAllMedia", success);
                    }

                } else {
                    success = false;
                }
            }
        });
    }

    //get all getAllMedialanguageLatestContent
    public void getAllMedialanguageLatestContent(final ContentServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callMedialanguageLatestContentService(request, new IServiceSuccessCallback<ContentData>() {
            @Override
            public void onDone(final String callerUrl, final ContentData result, String error) {
                Boolean success = false;
                if (result != null) {
                    if (request.getPage() <= result.getLast_page()) {
                        Log.d(Consts.LOG_TAG, "Recursively calling next content page: " + result.getCurrent_page());
                        final ContentServiceRequest nextRequest = new ContentServiceRequest();
                        nextRequest.setPage(result.getCurrent_page() + 1);
                        nextRequest.setStart_date(request.getStart_date());
                        getAllMedialanguageLatestContent(nextRequest, new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {
                                // Log.d(Consts.LOG_TAG, "Success: Service caller getAllContent done at page: " + nextRequest.getPage());
                                if (nextRequest.getPage() > result.getLast_page()) {
                                    Log.d(Consts.LOG_TAG, "Content Parsed successfully till page: " + result.getCurrent_page());
                                    workCompletedCallback.onDone("getAllMedialanguageLatestContent", true);

                                } else {
                                    // all parsed successfully; recursion complete
                                    workCompletedCallback.onDone("getAllMedialanguageLatestContent", true);
                                }
                            }
                        });

                    } else {
                        // all parsed successfully; recursion complete
                        // Log.d(Consts.LOG_TAG, "Content Parsed successfully till page: " + result.getCurrent_page());
                        success = true;
                        workCompletedCallback.onDone("getAllMedialanguageLatestContent", success);
                    }

                } else {
                    success = false;
                }
            }
        });
    }

    //get all Quizzes Content
    public void getAllQuizzes(final ContentServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callQuizzesService(request, new IServiceSuccessCallback<ContentData>() {
            @Override
            public void onDone(final String callerUrl, final ContentData result, String error) {
                Boolean success = false;
                if (result != null) {
                    if (request.getPage() <= result.getLast_page()) {
                        Log.d(Consts.LOG_TAG, "Recursively calling next Quizzes content page: " + result.getCurrent_page());
                        final ContentServiceRequest nextRequest = new ContentServiceRequest();
                        nextRequest.setPage(result.getCurrent_page() + 1);
                        nextRequest.setStart_date(request.getStart_date());
                        getAllQuizzes(nextRequest, new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {
                                if (nextRequest.getPage() > result.getLast_page()) {
                                    Log.d(Consts.LOG_TAG, "Quizzes Content Parsed successfully till page: " + result.getCurrent_page());
                                    workCompletedCallback.onDone("getAllQuizzes", true);
                                } else {
                                    // all parsed successfully; recursion complete
                                    workCompletedCallback.onDone("getAllQuizzes", true);
                                }
                            }
                        });

                    } else {
                        // all parsed successfully; recursion complete
                        // Log.d(Consts.LOG_TAG, "Content Parsed successfully till page: " + result.getCurrent_page());
                        success = true;
                        workCompletedCallback.onDone("getAllQuizzes", success);
                    }

                } else {
                    success = false;
                }
            }
        });
    }

    //get all Quizzes Questions Content
    public void getAllQuizzesQuestions(final ContentServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callQuizzesQuestionsService(request, new IServiceSuccessCallback<ContentData>() {
            @Override
            public void onDone(final String callerUrl, final ContentData result, String error) {
                Boolean success = false;
                if (result != null) {
                    if (request.getPage() <= result.getLast_page()) {
                        Log.d(Consts.LOG_TAG, "Recursively calling next Quizzes Questions content page: " + result.getCurrent_page());
                        final ContentServiceRequest nextRequest = new ContentServiceRequest();
                        nextRequest.setPage(result.getCurrent_page() + 1);
                        nextRequest.setStart_date(request.getStart_date());
                        getAllQuizzesQuestions(nextRequest, new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {
                                if (nextRequest.getPage() > result.getLast_page()) {
                                    Log.d(Consts.LOG_TAG, "Quizzes Questions Content Parsed successfully till page: " + result.getCurrent_page());
                                    workCompletedCallback.onDone("getAllQuizzesQuestions", true);
                                } else {
                                    // all parsed successfully; recursion complete
                                    workCompletedCallback.onDone("getAllQuizzesQuestions", true);
                                }
                            }
                        });

                    } else {
                        // all parsed successfully; recursion complete
                        // Log.d(Consts.LOG_TAG, "Content Parsed successfully till page: " + result.getCurrent_page());
                        success = true;
                        workCompletedCallback.onDone("getAllQuizzesQuestions", success);
                    }

                } else {
                    success = false;
                }
            }
        });
    }

    //get all Quizzes Questions Options Content
    public void getAllQuizzesQuestionsOptions(final ContentServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callQuizzesQuestionsOptionsService(request, new IServiceSuccessCallback<ContentData>() {
            @Override
            public void onDone(final String callerUrl, final ContentData result, String error) {
                Boolean success = false;
                if (result != null) {
                    if (request.getPage() <= result.getLast_page()) {
                        Log.d(Consts.LOG_TAG, "Recursively calling next Quizzes Options content page: " + result.getCurrent_page());
                        final ContentServiceRequest nextRequest = new ContentServiceRequest();
                        nextRequest.setPage(result.getCurrent_page() + 1);
                        nextRequest.setStart_date(request.getStart_date());
                        getAllQuizzesQuestionsOptions(nextRequest, new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {
                                if (nextRequest.getPage() > result.getLast_page()) {
                                    Log.d(Consts.LOG_TAG, "Quizzes Options Content Parsed successfully till page: " + result.getCurrent_page());
                                    workCompletedCallback.onDone("getAllQuizzesQuestionsOptions", true);
                                } else {
                                    // all parsed successfully; recursion complete
                                    workCompletedCallback.onDone("getAllQuizzesQuestionsOptions", true);
                                }
                            }
                        });

                    } else {
                        // all parsed successfully; recursion complete
                        // Log.d(Consts.LOG_TAG, "Content Parsed successfully till page: " + result.getCurrent_page());
                        success = true;
                        workCompletedCallback.onDone("getAllQuizzesQuestionsOptions", success);
                    }

                } else {
                    success = false;
                }
            }
        });
    }

    //call Content Quiz service
    public void ContentQuiz(final ContentServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callContentQuizService(request, new IServiceSuccessCallback<ContentData>() {
            @Override
            public void onDone(final String callerUrl, final ContentData result, String error) {
                Boolean success = false;
                if (result != null) {
                    if (request.getPage() <= result.getLast_page()) {
                        Log.d(Consts.LOG_TAG, "Recursively calling next Content Quiz content page: " + result.getCurrent_page());
                        final ContentServiceRequest nextRequest = new ContentServiceRequest();
                        nextRequest.setPage(result.getCurrent_page() + 1);
                        nextRequest.setStart_date(request.getStart_date());
                        ContentQuiz(nextRequest, new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {
                                if (nextRequest.getPage() > result.getLast_page()) {
                                    Log.d(Consts.LOG_TAG, "ContentQuiz Parsed successfully till page: " + result.getCurrent_page());
                                    workCompletedCallback.onDone("ContentQuiz", true);
                                } else {
                                    // all parsed successfully; recursion complete
                                    workCompletedCallback.onDone("ContentQuiz", true);
                                }
                            }
                        });

                    } else {
                        success = true;
                        workCompletedCallback.onDone("ContentQuiz", success);
                    }

                } else {
                    success = false;
                }
            }
        });
    }

    //call create account service
    public void CreateAccount(final AccountServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callCreateAccountService(request, new IServiceSuccessCallback<AccountData>() {
            @Override
            public void onDone(final String callerUrl, final AccountData result, String error) {
                Boolean success = false;
                if (result != null) {
                    success = true;
                    workCompletedCallback.onDone("AccountCreated", success);

                } else {
                    success = false;
                    workCompletedCallback.onDone("Account not created", success);
                }
            }
        });
    }

    //call profile update service
    public void updateProfile(final ProfileUpdateServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callProfileUpdateService(request, new IServiceSuccessCallback<AccountData>() {
            @Override
            public void onDone(final String callerUrl, final AccountData result, String error) {
                Boolean success = false;
                if (result != null) {
                    success = true;
                    workCompletedCallback.onDone("Profile updated", success);

                } else {
                    success = false;
                    workCompletedCallback.onDone("Profile not updated", success);
                }
            }
        });
    }

    //call OTP verification service
    public void OtpVerification(OtpVerificationServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callOtpVerificationService(request, new IServiceSuccessCallback<OtpData>() {
            @Override
            public void onDone(final String callerUrl, final OtpData result, String error) {
                Boolean success = false;
                if (result != null) {
                    success = true;
                    workCompletedCallback.onDone(result.getMessage(), success);

                } else {
                    success = false;
                    workCompletedCallback.onDone("OTP not virify", success);
                }
            }
        });
    }

    //call notification service
    public void getAllNotificationContent(final ContentServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callNotificationService(request, new IServiceSuccessCallback<ContentData>() {
            @Override
            public void onDone(final String callerUrl, final ContentData result, String error) {
                Boolean success = false;
                if (result != null) {
                    if (request.getPage() <= result.getLast_page()) {
                        Log.d(Consts.LOG_TAG, "Recursively calling next content page: " + result.getCurrent_page());
                        final ContentServiceRequest nextRequest = new ContentServiceRequest();
                        nextRequest.setPage(result.getCurrent_page() + 1);
                        getAllNotificationContent(nextRequest, new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {
                                // Log.d(Consts.LOG_TAG, "Success: Service caller getAllNotification done at page: " + nextRequest.getPage());
                                if (nextRequest.getPage() > result.getLast_page()) {
                                    Log.d(Consts.LOG_TAG, "Content Parsed successfully till page: " + result.getCurrent_page());
                                    workCompletedCallback.onDone("getAllNotification", true);

                                } else {
                                    // all parsed successfully; recursion complete
                                    workCompletedCallback.onDone("getAllNotification", true);
                                }
                            }
                        });

                    } else {
                        // all parsed successfully; recursion complete
                        // Log.d(Consts.LOG_TAG, "Content Parsed successfully till page: " + result.getCurrent_page());
                        success = true;
                        workCompletedCallback.onDone("getAllNotification", success);
                    }

                } else {
                    success = false;
                }
            }
        });
    }

    //call log account service
    public void logsRequest(final LogsDataRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callLogsService(request, new IServiceSuccessCallback<LogsDataRequest>() {
            @Override
            public void onDone(final String callerUrl, final LogsDataRequest result, String error) {
                Boolean success = false;
                if (result != null) {
                    success = true;
                    workCompletedCallback.onDone("logsRequest", success);

                } else {
                    success = false;
                    workCompletedCallback.onDone("logsRequest not Save", success);
                }
            }
        });
    }

    //call resen otp service
    public void resendOTP(final AccountServiceRequest request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callResenOtpService(request, new IServiceSuccessCallback<AccountData>() {
            @Override
            public void onDone(final String callerUrl, final AccountData result, String error) {
                Boolean success = false;
                if (result != null) {
                    success = true;
                    workCompletedCallback.onDone("AccountCreated", success);

                } else {
                    success = false;
                    workCompletedCallback.onDone("Account not created", success);
                }
            }
        });
    }


    //call Status Updat service
    public void StatusUpdat(final StatusUpdateService request, final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.callStatusUpdatService(request, new IServiceSuccessCallback<AccountData>() {
            @Override
            public void onDone(final String callerUrl, final AccountData result, String error) {
                Boolean success = false;
                if (result != null) {
                    success = true;
                    workCompletedCallback.onDone("Profile updated", success);

                } else {
                    success = false;
                    workCompletedCallback.onDone("Profile not updated", success);
                }
            }
        });
    }


    //call dashboarddata service
    public void dashboarddata(int courseChapterType,int parentId,final IAsyncWorkCompletedCallback workCompletedCallback) {
        final ServiceHelper sh = new ServiceHelper(context);
        sh.calldashboarddataService(courseChapterType,parentId,new IServiceSuccessCallback<DashboardData>() {
            @Override
            public void onDone(final String callerUrl, final DashboardData result, String error) {
                Boolean success = false;
                if (result != null) {
                    success = true;
                    workCompletedCallback.onDone("dashboarddata", success);

                } else {
                    success = false;
                    workCompletedCallback.onDone("dashboarddata", success);
                }
            }
        });
    }
}
