package dlp.bluelupin.dlp.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import dlp.bluelupin.dlp.R;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by neeraj on 20-Apr-2020.
 */
public class DownloadCertificateService extends IntentService {

    public static volatile boolean shouldContinue = true;

    public DownloadCertificateService() {
        super("Download Certificate Service");
    }

    private int totalFileSize;
    private String downloadUrl;
    private String SerialNo;
    private int CourseID;

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {
            SerialNo = extras.getString("SerialNo");
            downloadUrl = extras.getString("DownloadUrl");
            CourseID = extras.getInt("CourseID");
        }
        initDownload(downloadUrl);
    }

    OkHttpClient okHttpClient;

    private void initDownload(String downloadUrl) {
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
        if (downloadUrl != null) {
            Call<ResponseBody> request = retrofitInterface.downloadFile(downloadUrl);
            try {
                downloadFile(request.execute().body(), downloadUrl);
            } catch (IOException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "started downloading certificate: " + downloadUrl);
            }
        }
    }

    public void cancel() {//OkHttpClient client, Object tag) {
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

    private void downloadFile(ResponseBody body, String downloadUrl) throws IOException {
        if (shouldContinue == false) {
            if (Consts.IS_DEBUG_LOG) {
                Log.d(Consts.LOG_TAG, "Cancelling download service in onHandleIntent " + downloadUrl);
            }
            return;
        }

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = 0;
        if (body != null) {
            fileSize = body.contentLength();
//            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "dlpCertificates");
//            boolean success = true;
//            if (!folder.exists()) {
//                success = folder.mkdirs();
//            }
            String localFilePath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + SerialNo + ".png";
//            String localFilePath = getExternalFilesDir(null) + File.separator + SerialNo + ".png";
//            String localFilePath = DownloadCertificateService.this.getFilesDir() + "/" + Consts.outputDirectoryLocation + SerialNo + ".png";
            InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
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


                downloadData.setId(CourseID);
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
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "certificate successfully downloaded: " + localFilePath);
                }

            }

            output.flush();
            output.close();
            bis.close();
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
        LocalBroadcastManager.getInstance(DownloadCertificateService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(DownloadData downlaodData) {
        downlaodData.setProgress(100);
        //Toast.makeText(DownloadCertificateService.this, DownloadCertificateService.this.getString(R.string.certificatesdownloadsuccessfully), Toast.LENGTH_LONG).show();
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
        LocalBroadcastManager.getInstance(DownloadCertificateService.this).sendBroadcast(intent);
        //notificationManager.cancel(0);

    }

    private boolean DownloadImage(ResponseBody body) {

        try {
            Log.d("DownloadImage", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = body.byteStream();
                out = new FileOutputStream(getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            } catch (IOException e) {
                Log.d("DownloadImage", e.toString());
                return false;
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
            return true;
        } catch (IOException e) {
            Log.d("DownloadImage", e.toString());
            return false;
        }
    }
}
