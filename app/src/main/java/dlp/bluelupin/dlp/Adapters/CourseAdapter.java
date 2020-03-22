package dlp.bluelupin.dlp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import dlp.bluelupin.dlp.Activities.AudioActivity;
import dlp.bluelupin.dlp.Activities.VideoPlayerActivity;
import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.Fragments.ChaptersFragment;
import dlp.bluelupin.dlp.Fragments.WebFragment;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.Models.LogsDataRequest;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Services.AppController;
import dlp.bluelupin.dlp.Services.DownloadService1;
import dlp.bluelupin.dlp.Services.IAsyncWorkCompletedCallback;
import dlp.bluelupin.dlp.Services.ServiceCaller;
import dlp.bluelupin.dlp.Utilities.CustomProgressDialog;
import dlp.bluelupin.dlp.Utilities.DownloadImageTask;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.LocationUtility;
import dlp.bluelupin.dlp.Utilities.LogAnalyticsHelper;
import dlp.bluelupin.dlp.Utilities.Utility;

/**
 * Created by Neeraj on 7/26/2016.
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseViewHolder> {
    public LinearLayout mediaLayout;
    private List<Data> itemList;
    private Context context;
    private CustomProgressDialog customProgressDialog;
    private String contentTitle;
    private MediaPlayer mediaPlayer;

    public CourseAdapter(Context context, List<Data> itemList) {
        this.itemList = itemList;
        this.context = context;
        customProgressDialog = new CustomProgressDialog(context, R.mipmap.syc);
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_view_item, parent, false);
        return new CourseViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(final CourseViewHolder holder, int position) {

        Typeface VodafoneExB = FontManager.getFontTypeface(context, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(context, "fonts/VodafoneRg.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        holder.courseTitle.setTypeface(VodafoneExB);
        holder.courseDescription.setTypeface(VodafoneRg);
        holder.cardView.setCardBackgroundColor(Color.parseColor("#00000000"));
        holder.learnIcon.setTypeface(materialdesignicons_font);
        holder.learnIcon.setText(Html.fromHtml("&#xf5da;"));
        holder.learnLable.setTypeface(VodafoneExB);

        final DbHelper dbHelper = new DbHelper(context);
        final Data data = itemList.get(position);
        if (data.getMedia_id() != 0) {

            final Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getMedia_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));
            if (media != null) {


                if (media.getType().equalsIgnoreCase("video")) {
                    holder.media_Icon.setTypeface(materialdesignicons_font);
                    holder.media_Icon.setText(Html.fromHtml("&#xf36b;"));
                } else if (media.getType().equalsIgnoreCase("Audio")) {
                    holder.media_Icon.setTypeface(materialdesignicons_font);
                    holder.media_Icon.setText(Html.fromHtml("&#xf387;"));
                } else if (media.getType().equalsIgnoreCase("Url")) {
                    holder.media_Icon.setTypeface(materialdesignicons_font);
                    holder.media_Icon.setText(Html.fromHtml("&#xf57e;"));
                } else if (media.getType().equalsIgnoreCase("Youtube")) {
                    holder.media_Icon.setTypeface(materialdesignicons_font);
                    holder.media_Icon.setText(Html.fromHtml("&#xf36b;"));
                }


            }

        }
        if (data.getLang_resource_name() != null) {
            Data titleResource = dbHelper.getResourceEntityByName(data.getLang_resource_name(),
                    Utility.getLanguageIdFromSharedPreferences(context));
            if (titleResource != null) {
                holder.courseTitle.setText(Html.fromHtml(titleResource.getContent()));
            }
        }

        if (data.getLang_resource_description() != null) {
            Data descriptionResource = dbHelper.getResourceEntityByName(data.getLang_resource_description(),
                    Utility.getLanguageIdFromSharedPreferences(context));
            if (descriptionResource != null) {
                holder.courseDescription.setText(Html.fromHtml(descriptionResource.getContent()));
            }
        }

        if (data.getThumbnail_media_id() != 0) {
            Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getThumbnail_media_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));
//            Data media = dbHelper.getMediaEntityByDownloadUrlAndLanguageIdLaunguageId(media1.getDownload_url(),
//                           Utility.getLanguageIdFromSharedPreferences(context));
            if (media != null && media.getUrl() != null) {
                Picasso.with(context).load(media.getDownload_url()).placeholder(R.drawable.imageplaceholder).into(holder.courseImage);
               /* if(media.getLocalFilePath()!=null) {
                    String localFileName = Utility.getFileName(media.getLocalFilePath());
                    // if (media.getLocalFilePath() == null) {
                  String  localFileNameWithoutExtension=Utility.getFileNameWithoutExtension(localFileName);
                    if (!localFileNameWithoutExtension.equalsIgnoreCase(media.getLocalFileName())) {
                        DownloadImage(holder, media);
                    }
                    else {
                        Uri uri = Uri.fromFile(new File(media.getLocalFilePath()));
                        if (uri != null) {
                            Picasso.with(context).load(media.getDownload_url()).placeholder(R.drawable.imageplaceholder).into(holder.courseImage);
                          //  new DownloadImageTask(holder.courseImage,media).execute("");
                        } else {
                            DownloadImage(holder, media);
                        }
                    }
                }else {
                    DownloadImage(holder, media);
                }*/
            }
        }
        if (data.getMedia_id() != 0) {

            final Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getMedia_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));
            if (media != null) {

                holder.mediaLayout.setVisibility(View.VISIBLE);
                holder.mediaLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (media.getType().equalsIgnoreCase("video")) {
                            playVideoOnSelect(data, holder, "Video");
                        } else if (media.getType().equalsIgnoreCase("Audio")) {
                            playAudioOnSelect(data, holder, "Audio");
                        } else if (media.getType().equalsIgnoreCase("Url")) {
                            openUrlOnSelect(data, holder, "Url");
                        } else if (media.getType().equalsIgnoreCase("Youtube")) {
                            playVideoOnSelect(data, holder, "Youtube");
                        }
                    }
                });

            }
            if (media == null) {
                holder.mediaLayout.setVisibility(View.INVISIBLE);
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                String type = dbHelper.getTypeOfChildren(data.getId());
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "Navigating to  data_item id: " + data.getId() + " type: " + type);
                }
                logCourseDetails(data, "", "", "CourseDetails");
                stopAudio();
                ChaptersFragment fragment = ChaptersFragment.newInstance(data.getId(), type);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right);
                transaction.replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void logCourseDetails(Data data, String actionType, String mediaPath, String eventName) {
        String title = "";
        DbHelper db = new DbHelper(context);
        Data titleResource = db.getResourceEntityByName(data.getLang_resource_name(),
                Utility.getLanguageIdFromSharedPreferences(context));
        if (titleResource != null) {
            title = titleResource.getContent();
        }
        AccountData acdata = db.getAccountData();
        LogAnalyticsHelper analyticsHelper = new LogAnalyticsHelper(context);
        Bundle bundle = new Bundle();
        bundle.putString("Unique_Identifier", data.getId() + "");
        bundle.putString("ContentId", data.getId() + "");
        bundle.putString("Name", data.getName());
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
        bundle.putString("Course_Selected", title);
        bundle.putString("Action_Type", "Text");
        if (actionType != null && !actionType.equals("")) {
            bundle.putString("Action_Type", actionType);
        }
        if (mediaPath != null && !mediaPath.equals("")) {
            bundle.putString("MediaUrl", mediaPath);
        }
        analyticsHelper.logEvent(eventName, bundle);

        LogsDataRequest ServiceRequest = new LogsDataRequest();
        ServiceRequest.setContentId(data.getId() + "");
        ServiceRequest.setLogEventName(eventName);
        ServiceRequest.setUnique_Identifier(data.getId() + "");
        ServiceRequest.setName(data.getName());
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
        ServiceRequest.setCourse_Selected(title);
        ServiceRequest.setAction_Type(actionType);
        ServiceRequest.setMediaUrl(mediaPath);
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

    private void DownloadImage(CourseViewHolder holder, Data media) {
        if (Utility.isOnline(context)) {
            Gson gson = new Gson();
            Intent intent = new Intent(context, DownloadService1.class);
            String strJsonmedia = gson.toJson(media);
            intent.putExtra(Consts.EXTRA_MEDIA, strJsonmedia);
            intent.putExtra(Consts.EXTRA_URLPropertyForDownload, Consts.URL);
            context.startService(intent);
            new DownloadImageTask(holder.courseImage, customProgressDialog)
                    .execute(media.getUrl());
            customProgressDialog.dismiss();
        }
    }

    private void playAudioAsync(final String url) {

        new AsyncTask<String, Void, MediaPlayer>() {

            @Override
            protected MediaPlayer doInBackground(String... params) {
                if (mediaPlayer == null) {
                    Uri myUri = Uri.parse(url);
                    mediaPlayer = MediaPlayer.create(context, myUri);
                }
                return mediaPlayer;
            }

            @Override
            protected void onPostExecute(MediaPlayer mediaplayer) {
                super.onPostExecute(mediaplayer);
                if (url != null && !url.equals("")) {

                    try {
                        Uri myUri = Uri.parse(url);

                        if (mediaPlayer == null) {

                            mediaPlayer = MediaPlayer.create(context, myUri);

                        }

                        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {//listen_text.getText() == "PAUSE") {

                            mediaPlayer.pause();

                        } else {


                            mediaPlayer.start();


                        }
                    } catch (Exception e) {
                        Log.d(Consts.LOG_TAG, "**************: play audio " + e.getMessage());
                    }
                }
            }
        }.execute(url);
    }

    private void playVideoOnSelect(Data data, CourseViewHolder holder, String actionType) {
        if (data.getMedia_id() != 0) {
            final DbHelper dbHelper = new DbHelper(context);
            Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getMedia_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));

            if (media != null) {
                logCourseDetails(data, actionType, media.getFile_path() + " Seen", "CourseDetails");
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "Media id" + media.getId() + " video Url: " + media.getUrl());
                }
                navigateBasedOnMediaType(media, holder);
            }
        }
    }

    private void playAudioOnSelect(Data data, CourseViewHolder holder, String actionType) {
        if (data.getMedia_id() != 0) {
            final DbHelper dbHelper = new DbHelper(context);
            Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getMedia_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));

            if (media != null) {
                logCourseDetails(data, actionType, media.getFile_path() + " Seen", "CourseDetails");
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "Media id" + media.getId() + " audio Url: " + media.getUrl());
                }
                navigateBasedOnMediaType(media, holder);
            }
        }
    }

    private void openUrlOnSelect(Data data, CourseViewHolder holder, String actionType) {
        if (data.getUrl() != null) {
            final DbHelper dbHelper = new DbHelper(context);
            if (data.getLang_resource_name() != null) {
                Data titleResource = dbHelper.getResourceEntityByName(data.getLang_resource_name(),
                        Utility.getLanguageIdFromSharedPreferences(context));
                if (titleResource != null) {
                    contentTitle = titleResource.getContent();
                }
            }
            logCourseDetails(data, actionType, contentTitle + "  " + data.getUrl() + " Seen", "CourseDetails");
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            WebFragment fragment = WebFragment.newInstance(data.getUrl(), contentTitle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right);
            transaction.replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void navigateBasedOnMediaType(Data media, CourseViewHolder holder) {
        String url;
        if (media.getType() != null) {
            switch (media.getType()) {
                case "Video":
                    url = media.getDownload_url();
                    if (url != null && !url.equals("")) {
                        showOnlineVideo(media);
                    } else {
                        showOfflineVideo(media);
                    }
                    break;
                case "Audio":
                    url = media.getDownload_url();
                    if (url != null && !url.equals("")) {
                        //showOnlineAudio(media);
                        playAudioAsync(url);
                    } else {
                        //showOfflineAudio(media);
                        playAudioAsync(url);
                    }
                    break;
                case "Youtube":
                    url = media.getLocalFilePath();
                    if (url != null && !url.equals("")) {
                        showOfflineVideo(media);
                    } else {
                        if (Utility.isOnline(context)) {
                            url = media.getUrl();
                            if (url != null && !url.equals("")) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                intent.putExtra("force_fullscreen", true);
                                context.startActivity(intent);
                                Consts.playYouTubeFlag = false;//after device back press MainAtivity don't reload
                            }
                        } else {
                            Utility.alertForErrorMessage(context.getString(R.string.online_msg), context);
                        }
                    }
                    break;
            }
        }
    }

    private void showOfflineVideo(Data media) {
        String url;
        url = media.getLocalFilePath();
        if (url != null) {
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("videoUrl", url);
            context.startActivity(intent);
        } else {
            Utility.alertForErrorMessage(context.getString(R.string.online_msg), context);
        }
    }

    private void showOnlineVideo(Data media) {
        String url;
        if (Utility.isOnline(context)) {
            url = media.getUrl();
            if (url != null && !url.equals("")) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("videoUrl", url);
                context.startActivity(intent);
            } else {
                Utility.alertForErrorMessage(context.getString(R.string.online_msg), context);
            }
        }
    }

    private void showOfflineAudio(Data media) {
        String url;
        url = media.getLocalFilePath();
        Intent intent = new Intent(context, AudioActivity.class);
        intent.putExtra("audioUrl", url);
        context.startActivity(intent);
    }

    private void showOnlineAudio(Data media) {
        String url;
        if (Utility.isOnline(context)) {
            url = media.getUrl();
            if (url != null && !url.equals("")) {
                Intent intent = new Intent(context, AudioActivity.class);
                intent.putExtra("audioUrl", url);
                context.startActivity(intent);
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    private void stopAudio() {
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {//listen_text.getText() == "PAUSE") {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
