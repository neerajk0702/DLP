package dlp.bluelupin.dlp;

import android.content.res.Resources;
import android.os.Environment;

/**
 * Created by subod on 21-Jul-16.
 */
public class Consts {
    public static final String LOG_TAG = "HIH";
    public static final Boolean IS_DEBUG_LOG = true;
    public final static boolean PROD = true; //false;8J3nj1c5IKEdTltpLDXP1QocdVyXV9LkesED0sr2wozK80Iqqjj8NDkxZuPk
//    public static String API_KEY = "XsMwq2updd3L5MZAtgwx7PAA0wKaylFnCejD0ei9WjSuwQVmXMQxGg3ZiH5X";
    public static String API_KEY ="XsMwq2updd3L5MZAtgwx7PAA0wKaylFnCejD0ei9WjSuwQVmXMQxGg3ZiH5X";
    public static String USER_API_KEY ="";
/*
            1. Production point to : https://cms.samvaad.me
            2. Staging pointing to: https://cms2.samvaad.me
            3. QA pointing to: http://qa.samvaad.me/
            4. Dev pointing to: https://dlpdev.bluelup.in*/
    public static String BASE_URL ="https://cms-2020-dev.samvaad.me/api/v1/";//development
//    public static String BASE_URL = "https://cms.samvaad.me/api/v1/";//"https://cms.samvaad.me/api/v1/";//https://dlpdev.bluelup.in/api/v1/"; // http://dlp-qa.bluelup.in/api/v1/;//https://cms2.samvaad.me/api/v1/;//"http://180.151.10.60:8080/classkonnect/api/";


    public static String getBaseUrl() {
        if (PROD) {
            BASE_URL = "https://cms.samvaad.me/api/v1/";
            return BASE_URL;
        }
        return BASE_URL;
    }

    public static final String URL_CONTENT_LATEST = "content/latest";

    public static final String URL_LANGUAGE_RESOURCE_LATEST = "langresource/latest";

    public static final String URL_MEDIA_LATEST = "media/latest";
    public static final String CREATE_NEW_USER = "user/create";
    public static final String VERIFY_OTP = "user/verifyotp";
    public static final String send_Otp = "user/sendotp";

    public static final String DownloadBroadcast = "DownloadBroadcast";

    public static final String COURSE = "Course";

    public static final String SUBJECT = "Subject";
    public static final String CHAPTER = "Chapter";
    public static final String TOPIC = "Topic";
    public static final String Notifications = "notifications";
    public static final String MediaLanguage_Latest = "medialanguage/latest";
    public static final String Languages = "languages";
    public static final String EXTRA_MEDIA = "extraMedia";
    public static final String EXTRA_DOWNLOAD_DATA = "extraDownloadData";
    public static final String MESSAGE_CANCEL_DOWNLOAD = "CANCEL_DOWNLOAD";
    public static final String EXTRA_URLPropertyForDownload = "urlPropertyForDownload";
    public static final String URL = "URL";
    public static final String THUMBNAIL_URL = "Thumbnail_URL";
    public static final String DOWNLOAD_URL = "Download_URL";
    public static final String Profile_Update = "user/profile/update";
    public static final String Quizzes = "quizzes";
    public static final String QuizzesQuestions = "quizzes/questions";
    public static final String QuizzesOptions = "quizzes/options";
    public static final String ContentQuiz = "content/quiz";
    public static final String PlayStoreVersion = "appstoreversion";
    public static final String SENDER = "MOBKNT";
    public static final String statusUpdate = "user/content/status/update";
    public static final String dashboarddata = "dashboarddata";
    public static final String chapterdata = "chapterdata/";

    public static String inputDirectoryLocation = "/dlp/";


    // 'Course','Subject','Chapter','Topic','Text','Image','Video','Url','Audio','Comment','Home','Other','DownloadFile','Folder'
    //public static final String OFFLINE_MESSAGE = "You are not online!!!!";
    public static final String APP_DIRECTORY = "/DLP" + "Directory/";

    public static final String MESSAGE_PROGRESS = "message_progress";
    public static String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static String outputBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String outputDirectoryLocation = outputBasePath + "/dlpUnzipped/";
    public static final String mBroadcastDeleteAction = "deleteDownload";
    public static final String mBroadcastProgressUpdateAction = "downloadProgress";

    public static final String SERVICE = "gcm";//“apns”, “gcm”, “mpns”
    public static final Boolean IS_DEVELOPMENT = false;
    public static Boolean playYouTubeFlag = true;//for play online you tube video device back press handel
    public static String dataBaseName = "dlp_db.db";
    public static final String logsRequest = "https://api.samvaad.me/api/logs/create";

}
