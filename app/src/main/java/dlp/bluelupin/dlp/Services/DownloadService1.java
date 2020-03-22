package dlp.bluelupin.dlp.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.Models.DownloadData;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by subod on 05-Sep-16.
 */
public class DownloadService1 extends IntentService {

    public static volatile boolean shouldContinue = true;
    public DownloadService1() {
        super("Download Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    //    private NotificationManager notificationManager;
    private int totalFileSize;
    private String urlPropertyForDownload = Consts.DOWNLOAD_URL;
    private Data media;
    private String strJsonMedia;

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {
            strJsonMedia = extras.getString(Consts.EXTRA_MEDIA);

            urlPropertyForDownload = extras.getString(Consts.EXTRA_URLPropertyForDownload);
            if (urlPropertyForDownload == null) {
                urlPropertyForDownload = Consts.DOWNLOAD_URL;
            }
            Gson gson = new Gson();
            media = gson.fromJson(strJsonMedia, Data.class);
        }

//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//        notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.hihlogo)
//                .setContentTitle("Download")
//                .setContentText("Downloading File" + media.getName())
//                .setAutoCancel(true);
//        notificationManager.notify(0, notificationBuilder.build());

        initDownload(media);

    }
    OkHttpClient okHttpClient;
    private void initDownload(Data media) {
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL)
                .client(okHttpClient)
                .build();


        IServiceManager retrofitInterface = retrofit.create(IServiceManager.class);

        String downloadUrl = getDownloadFromMedia(media);
        if (downloadUrl != null) {
            Call<ResponseBody> request = retrofitInterface.downloadFile(downloadUrl);
            try {

                downloadFile(request.execute().body(), media);

            } catch (IOException e) {

                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        } else {
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "started downloading: media Id:" + media.getId() + " downloading Url: (" + urlPropertyForDownload + ") " + downloadUrl);
            }
        }
    }

    public void cancel(){//OkHttpClient client, Object tag) {
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        for (okhttp3.Call call : okHttpClient.dispatcher().queuedCalls()) {
            //if (tag.equals(call.request().tag())) call.cancel();
            call.cancel();
        }
        for (okhttp3.Call call : okHttpClient.dispatcher().runningCalls()) {
            //if (tag.equals(call.request().tag())) call.cancel();
            call.cancel();
        }
        if (Consts.IS_DEBUG_LOG) {
            Log.d(Consts.LOG_TAG, "all call cancelled!!");
        }
    }

    private String getDownloadFromMedia(Data media) {
        String downloadUrl = null;
        switch (urlPropertyForDownload) {
            case Consts.URL:
                downloadUrl = media.getUrl();
                break;
            case Consts.DOWNLOAD_URL:
                downloadUrl = media.getDownload_url();
                break;
            case Consts.THUMBNAIL_URL:
                downloadUrl = media.getThumbnail_url();
                break;
            default:
                downloadUrl = media.getDownload_url();
                break;
        }
        return downloadUrl;
    }

    private void downloadFile(ResponseBody body, Data media) throws IOException {
        if (shouldContinue == false) {
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "Cancelling download service in onHandleIntent " + media.getDownload_url());
            }
            return;
        }

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = 0;
        if (body != null) {
            fileSize = body.contentLength();

            String localFilePath = DownloadService1.this.getFilesDir() + "/" + media.getFile_path();// Consts.outputDirectoryLocation + media.getFile_path();
            InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
            if (urlPropertyForDownload.equalsIgnoreCase(Consts.THUMBNAIL_URL)) {
                if (media.getThumbnail_file_path() != null)
                    localFilePath = DownloadService1.this.getFilesDir() + "/" + media.getThumbnail_file_path(); // Consts.outputDirectoryLocation + media.getThumbnail_file_path();
            }

            File outputFile = new File(localFilePath);

            OutputStream output = new FileOutputStream(outputFile);
            long total = 0;
            long startTime = System.currentTimeMillis();
            int timeCount = 1;
            DownloadData downloadData = new DownloadData();
            while ((count = bis.read(data)) != -1) {

                total += count;
                totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
                double current = Math.round(total / (Math.pow(1024, 2)));

                int progress = (int) ((total * 100) / fileSize);

                long currentTime = System.currentTimeMillis() - startTime;


                downloadData.setId(media.getId());
                downloadData.setTotalFileSize(totalFileSize);

                if (currentTime > 1000 * timeCount) {

                    downloadData.setCurrentFileSize((int) current);
                    downloadData.setProgress(progress);
                    if (Consts.IS_DEBUG_LOG) {
                        //Log.d(Consts.LOG_TAG, "DownlaodService1: downlaodData Id:" + downloadData.getId() + " progress:" + downloadData.getProgress());
                    }
                    if (shouldContinue != false) {
                        sendNotification(downloadData);
                    }

                    timeCount++;
                }

                output.write(data, 0, count);
            }
            if (shouldContinue != false) {//Consts.IS_DEBUG_LOG) {
                onDownloadComplete(downloadData);
                UpdateMediaInDB(localFilePath);
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "successfully downloaded: media Id:" + media.getId() + " downloading Url: " + media.getDownload_url() + " at " + localFilePath);
                }

            }

            output.flush();
            output.close();
            bis.close();
        }

    }

    private void UpdateMediaInDB(String localPath) {
        DbHelper dbHelper = new DbHelper(DownloadService1.this);
        if (media != null && media.getType() == "Youtube") {
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "successfully downloaded and THUMBNAIL local file updated: media Id:" + media.getId() + " getThumbnail_url_Local_file_path Url: " + media.getThumbnail_url_Local_file_path() + " at " + localPath);
            }
        }
        if (urlPropertyForDownload.equalsIgnoreCase(Consts.THUMBNAIL_URL)) {
            media.setThumbnail_url_Local_file_path(localPath);
            if (dbHelper.updateMediaThumbnailLocalFilePathEntity(media)) {
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "successfully downloaded and THUMBNAIL local file updated: media Id:" + media.getId() + " downloading Url: " + media.getDownload_url() + " at " + localPath);
                }
            }
        } else {
            media.setLocalFilePath(localPath);
            if (dbHelper.updateMediaLanguageLocalFilePathEntity(media)) {
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "successfully downloaded and local file updated: media Id:" + media.getId() + " downloading Url: " + media.getDownload_url() + " at " + localPath);
                }
            }
        }
    }

    private void sendNotification(DownloadData downloadData) {

        sendIntent(downloadData);
//        notificationBuilder.setProgress(100,downloadData.getProgress(),false);
//        notificationBuilder.setContentText(String.format("Downloaded (%d/%d) MB",downloadData.getCurrentFileSize(),downloadData.getTotalFileSize()));
//        notificationManager.notify(0, notificationBuilder.build());
    }


    private void sendIntent(DownloadData downloadData) {

        Intent intent = new Intent(Consts.MESSAGE_PROGRESS);
        intent.putExtra(Consts.EXTRA_DOWNLOAD_DATA, downloadData);
        LocalBroadcastManager.getInstance(DownloadService1.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(DownloadData downlaodData) {

        //DownloadData download = new DownloadData();
        DbHelper dbHelper = new DbHelper(DownloadService1.this);
        dbHelper.updateDownloadedFileFlag(downlaodData.getId(), 1);
        downlaodData.setProgress(100);
        sendIntent(downlaodData);

//        notificationManager.cancel(0);
//        notificationBuilder.setProgress(0,0,false);
//        notificationBuilder.setContentText("File Downloaded: " + media.getId());
//        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //cancel(okHttpClient,"all");
        super.onTaskRemoved(rootIntent);
        Intent intent = new Intent(Consts.MESSAGE_CANCEL_DOWNLOAD);
        intent.putExtra(Consts.EXTRA_MEDIA, strJsonMedia);
        LocalBroadcastManager.getInstance(DownloadService1.this).sendBroadcast(intent);
        //notificationManager.cancel(0);

    }

}
