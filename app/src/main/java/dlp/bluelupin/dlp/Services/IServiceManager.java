package dlp.bluelupin.dlp.Services;

import java.util.List;

import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.AccountServiceRequest;
import dlp.bluelupin.dlp.Models.ApplicationVersionResponse;
import dlp.bluelupin.dlp.Models.ContentData;
import dlp.bluelupin.dlp.Models.ContentServiceRequest;
import dlp.bluelupin.dlp.Models.LanguageData;
import dlp.bluelupin.dlp.Models.LogsDataRequest;
import dlp.bluelupin.dlp.Models.OtpData;
import dlp.bluelupin.dlp.Models.OtpVerificationServiceRequest;
import dlp.bluelupin.dlp.Models.ProfileUpdateServiceRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by subod on 19-Jul-16.
 */
public interface IServiceManager {
    @POST(Consts.URL_CONTENT_LATEST)
    Call<ContentData> latestContent(@Body ContentServiceRequest request);

    @POST(Consts.URL_LANGUAGE_RESOURCE_LATEST)
    Call<ContentData> latestResource(@Body ContentServiceRequest request);

    @POST(Consts.URL_MEDIA_LATEST)
    Call<ContentData> latestMedia(@Body ContentServiceRequest request);

    @POST(Consts.CREATE_NEW_USER)
    Call<AccountData> accountCreate(@Body AccountServiceRequest request);

    @POST(Consts.VERIFY_OTP)
    Call<OtpData> otpVerify(@Query("api_token") String api_token, @Body OtpVerificationServiceRequest request);

    // @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

    @POST(Consts.MediaLanguage_Latest)
    Call<ContentData> MedialanguageLatestContent(@Body ContentServiceRequest request);

    @GET
    Call<List<LanguageData>> getLanguage(@Url String languageUrl);

    @POST(Consts.Notifications)
    Call<ContentData> NotificationContent(@Body ContentServiceRequest request);

    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @POST(Consts.Profile_Update)
    Call<AccountData> profileUpdated(@Query("api_token") String api_token, @Body ProfileUpdateServiceRequest request);

    @POST(Consts.Quizzes)
    Call<ContentData> QuizzesContent(@Body ContentServiceRequest request);

    @POST(Consts.QuizzesQuestions)
    Call<ContentData> QuizzesQuestionsContent(@Body ContentServiceRequest request);

    @POST(Consts.QuizzesOptions)
    Call<ContentData> QuizzesOptionsContent(@Body ContentServiceRequest request);

    @POST(Consts.ContentQuiz)
    Call<ContentData> ContentQuiz(@Body ContentServiceRequest request);

    @GET
    Call<ApplicationVersionResponse> GetPlayStoreVersion(@Url String version);

    @POST(Consts.logsRequest)
    Call<LogsDataRequest> saveLogs(@Body LogsDataRequest request);

}
