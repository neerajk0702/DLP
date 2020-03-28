package dlp.bluelupin.dlp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import dlp.bluelupin.dlp.Activities.AudioActivity;
import dlp.bluelupin.dlp.Activities.VideoPlayerActivity;
import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.Fragments.ChaptersFragment;
import dlp.bluelupin.dlp.Fragments.ContentFragment;
import dlp.bluelupin.dlp.Fragments.WebFragment;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.Models.FavoritesData;
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
public class ChaptersAdapterNew extends RecyclerView.Adapter<ChaptersViewHolderNew> {

    private List<Data> itemList;
    private Context context;
    private String selectType;
    private String contentTitle;
    private CustomProgressDialog customProgressDialog;
    private List<Data> quizList;
    private MediaPlayer mediaPlayer;


    public ChaptersAdapterNew(Context context, List<Data> itemList, String type) {
        this.itemList = itemList;
        this.context = context;
        this.selectType = type;
        customProgressDialog = new CustomProgressDialog(context, R.mipmap.syc);
    }

    @Override
    public ChaptersViewHolderNew onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView;
        if (selectType.equalsIgnoreCase("Topic")) {
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_list_view_item_new, parent, false);

        } else {
            layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapters_list_view_item_new, parent, false);

        }


        return new ChaptersViewHolderNew(layoutView);
    }

    @Override
    public void onBindViewHolder(final ChaptersViewHolderNew holder, final int position) {
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        Typeface VodafoneExB = FontManager.getFontTypeface(context, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(context, "fonts/VodafoneRg.ttf");
        holder.chapterTitle.setTypeface(VodafoneExB);
        //  holder.chapterDescription.setTypeface(VodafoneRg);
        //  holder.favorite.setTypeface(VodafoneRg);
        //  holder.download.setTypeface(VodafoneRg);
       // holder.cardView.setCardBackgroundColor(Color.parseColor("#EEEEEE"));
        // holder.quiz.setTypeface(VodafoneExB);
        // holder.quiz_Icon.setTypeface(materialdesignicons_font);
        // holder.quiz_Icon.setText(Html.fromHtml("&#xf186;"));
        // holder.start_quiz_Icon.setTypeface(materialdesignicons_font);
        // holder.start_quiz_Icon.setText(Html.fromHtml("&#xf186;"));
        // holder.arrowIcon.setTypeface(materialdesignicons_font);
        //// holder.arrowIcon.setText(Html.fromHtml("&#xf054;"));

        // holder.downloadIcon.setImageResource(R.drawable.downloadupdate);

        //holder.starIcon.setTypeface(materialdesignicons_font);
//        holder.starIcon.setText(Html.fromHtml("&#xf4ce;"));
        //   holder.favorite.setTypeface(VodafoneRg);
        //   holder.download.setTypeface(VodafoneRg);
        //   holder.downloadIcon.setTypeface(materialdesignicons_font);

        //show and hide favorite icon layout only in chapter layout
        /*if (type.equalsIgnoreCase(Consts.CHAPTER)) {
            holder.favorite_layout.setVisibility(View.VISIBLE);
        } else {
            holder.favorite_layout.setVisibility(View.GONE);
        }*/
        final DbHelper dbHelper = new DbHelper(context);
        final Data data = itemList.get(position);

        final Data resource = dbHelper.getResourceEntityByName(data.getLang_resource_name(),
                Utility.getLanguageIdFromSharedPreferences(context));
        if (data.getMedia_id() != 0) {

            final Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getMedia_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));
            if (media != null)
                if (media.getType().equalsIgnoreCase("video")) {
                    //   holder.media_Icon.setTypeface(materialdesignicons_font);
                    //   holder.media_Icon.setText(Html.fromHtml("&#xf36b;"));

                }
            if (media.getType().equalsIgnoreCase("Audio")) {
                //holder.media_Icon.setTypeface(materialdesignicons_font);
                // holder.media_Icon.setText(Html.fromHtml("&#xf387;"));
            }
            if (media.getType().equalsIgnoreCase("Youtube")) {
                // holder.media_Icon.setTypeface(materialdesignicons_font);
                // holder.media_Icon.setText(Html.fromHtml("&#xf36b;"));
            }

        }


        if (data.getLang_resource_name() != null) {
            Data titleResource = dbHelper.getResourceEntityByName(data.getLang_resource_name(),
                    Utility.getLanguageIdFromSharedPreferences(context));
            if (titleResource != null) {
                holder.chapterTitle.setVisibility(View.VISIBLE);
                holder.chapterTitle.setText(Html.fromHtml(titleResource.getContent()));
            } else {
                holder.chapterTitle.setVisibility(View.GONE);
            }
        } else {
            holder.chapterTitle.setVisibility(View.GONE);
        }

        if (data.getLang_resource_description() != null) {
            Data descriptionResource = dbHelper.getResourceEntityByName(data.getLang_resource_description(),
                    Utility.getLanguageIdFromSharedPreferences(context));
            if (descriptionResource != null) {
                //   holder.chapterDescription.setVisibility(View.VISIBLE);
                // holder.chapterDescription.setText(Html.fromHtml(descriptionResource.getContent()));
            } else {
                //  holder.chapterDescription.setVisibility(View.GONE);
            }
        } else {
            // holder.chapterDescription.setVisibility(View.GONE);
        }

      /*  if (data.getThumbnail_media_id() != 0) {
            Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getThumbnail_media_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));
            holder.chapterImage.setImageBitmap(null);
            if (media != null && media.getDownload_url() != null) {
                if (media.getLocalFilePath() == null) {
                    if (Utility.isOnline(context)) {
                        DownloadImage(holder, media);
                    }
                } else {
                   *//* holder.chapterImage.setImageBitmap(null);
                    File imgFile = new File(media.getLocalFilePath());
                    Bitmap bitmap = null;
                    if (imgFile.exists()) {
                        bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        holder.chapterImage.setImageBitmap(bitmap);
                    }*//*
                    Uri uri = Uri.fromFile(new File(media.getLocalFilePath()));
                    if (uri != null) {
                        Picasso.with(context).load(uri).placeholder(R.drawable.imageplaceholder).into(holder.chapterImage);
                    }
                    *//*LoadImageFromDataBase imageFromDataBase = new LoadImageFromDataBase(holder.chapterImage, holder.progressBar);
                    imageFromDataBase.execute(media.getLocalFilePath());*//*
                }

            }
        }*/
        //
        if (data.getThumbnail_media_id() != 0) {
            Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getThumbnail_media_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));
//            Data media = dbHelper.getMediaEntityByDownloadUrlAndLanguageIdLaunguageId(media1.getDownload_url(),
//                           Utility.getLanguageIdFromSharedPreferences(context));
            holder.chapterImage.setImageBitmap(null);
            if (media != null && media.getDownload_url() != null) {
                if (selectType.equalsIgnoreCase("Topic")) {

                    Picasso.with(context).load(media.getDownload_url()).placeholder(R.drawable.imageplaceholder).into(holder.chapterImage);

                } else {
                    Picasso.with(context).load(media.getDownload_url()).placeholder(R.drawable.imageplaceholder).into(holder.chapterImage);
                }

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

                            Picasso.with(context).load(uri).placeholder(R.drawable.imageplaceholder).into(holder.chapterImage);
                            //holder.chapterImage.setImageURI(uri);
                           // new DownloadImageTask(holder.chapterImage,media).execute("");
                            Log.d(Consts.LOG_TAG, "1. display image by picasso: " +uri);
                        } else {
                            DownloadImage(holder, media);
                        }
                    }
                }else {
                    DownloadImage(holder, media);
                }*/
            }
        }

        final DbHelper dbhelper = new DbHelper(context);
        // REMOVE BELOW IN PROD
        //dbHelper.setLocalPathOfAllMediaToNull();
        // REMOVE ABOVE IN PROD

        new AsyncTask<String, Void, List<Data>>() {
            @Override
            protected List<Data> doInBackground(String... params) {
                final List<Data> resourcesToDownloadList = dbhelper.getResourcesToDownload(data.getId(), Utility.getLanguageIdFromSharedPreferences(context));
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "Number of  downloads for chapter: " + data.getId() + " is: " + resourcesToDownloadList.size());
                }

                return resourcesToDownloadList;
            }

            @Override
            protected void onPostExecute(final List<Data> resourcesToDownloadList) {
                super.onPostExecute(resourcesToDownloadList);
                if (resourcesToDownloadList.size() <= 0) {
                    // holder.downloadIcon.setTextColor(Color.parseColor("#ffffff"));
                    //     holder.downloadIcon.setText(Html.fromHtml("&#xf12c;"));
                    //    holder.download.setText(context.getString(R.string.update));
                } else {
                    //  holder.downloadIcon.setText(Html.fromHtml("&#xf1da;"));
                    // holder.downloadIcon.setTextColor(Color.parseColor("#ffffff"));
                    // holder.download.setText(context.getString(R.string.Take_Offline));
                }
                //if meadia not downloaded then show download_layout
                if (data.getThumbnail_media_id() != 0) {
                    final Data media = dbHelper.getDownloadMediaEntityById(data.getThumbnail_media_id());
                    if (media != null) {
                        //   holder.download_layout.setVisibility(View.VISIBLE);

                    /*    holder.download_layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (resourcesToDownloadList.size() > 0) {
//                        {
//                            List<Data> resourceListToDownload = new ArrayList<Data>();
//                            List<Data> resourceToDownload = dbhelper.getThumbnailsToDownload(data_item.getId(),resourceListToDownload);
//                            if (Consts.IS_DEBUG_LOG) {
//                                Log.d(Consts.LOG_TAG, "Number of thumbnailsToDownload to download for chapter: " + data_item.getId() + " is: " + resourceToDownload.size());
//                                for (Data resource:resourceToDownload) {
//                                    Log.d(Consts.LOG_TAG, "Resource to be DL: " + resource.getId() + " url: " + resource.getUrl());
//                                }
//                            }
//                        }
                                    if (Utility.isOnline(context)) {
                                        DownloadService1.shouldContinue = true;
                                        Gson gson = new Gson();
                                        String strJsonResourcesToDownloadList = gson.toJson(resourcesToDownloadList);

                               *//* DownloadBasedOnParentId downloadBasedOnParentId = new DownloadBasedOnParentId();
                                downloadBasedOnParentId.setStrJsonResourcesToDownloadList(strJsonResourcesToDownloadList);
                                downloadBasedOnParentId.setParentId(media.getParent_id());
                                Data media = dbHelper.getMediaEntityByIdAndLaunguageId(media.getParent_id(), Utility.getLanguageIdFromSharedPreferences(context));
                               Log.d(Consts.LOG_TAG, "media.getName************** " + media.getName());*//*
                                        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                                        DownloadingFragment fragment = DownloadingFragment.newInstance(strJsonResourcesToDownloadList, data.getId());
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right);
                                        transaction.replace(R.id.container, fragment)
                                                .addToBackStack(null)
                                                .commit();
                                    } else {
                                        Utility.alertForErrorMessage(context.getString(R.string.online_msg), context);
                                    }

                                }
                            }
                        });
*/
                    } else {

                        // holder.download_layout.setVisibility(View.GONE);
                    }
                }
            }
        }.execute();

        if (selectType.equalsIgnoreCase("Topic")) {//Quiz show only in Topic
            quizList = dbhelper.getDataEntityByParentIdAndType(data.getId(), "Quiz");
            if (data.getQuizAvailable()) {


                //  holder.titleLayout.setVisibility(View.GONE);
                // holder.buttonLayout.setVisibility(View.GONE);
                // holder.quizStartLayout.setVisibility(View.VISIBLE);
            } else {
                // holder.quizStartLayout.setVisibility(View.GONE);
                holder.chapterImage.setVisibility(View.VISIBLE);
                // holder.titleLayout.setVisibility(View.VISIBLE);
                // holder.buttonLayout.setVisibility(View.VISIBLE);


            }
           /* int contentId=itemList.get(position).getId();
            List<Data> contentQuiz=dbHelper.getAllContentQuizEntity();
            for(int i=0;i<contentQuiz.size();i++){
                Pivot pivot=contentQuiz.get(i).getPivot();
                if(pivot.getContent_id()==contentId){
                    int QuizId=pivot.getQuiz_id();
                    holder.quizStartLayout.setVisibility(View.VISIBLE);

                }else{
                    holder.quizStartLayout.setVisibility(View.GONE);
                }

            }*/
        }
        //for chapter show Quiz icon
        if (selectType.equalsIgnoreCase("Chapter")) {
            final Data contentData = dbHelper.getContentQuizEntityByContentId(data.getId());
            if (contentData != null) {
                holder.chapterView.setVisibility(View.VISIBLE);
          /*  \    holder.quizLayout.setVisibility(View.VISIBLE);
                holder.quizLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (contentData.getQuiz_id() != 0) {
                            DbHelper dbhelper = new DbHelper(context);
                            dbhelper.deleteQuizAnswerEntityById(contentData.getQuiz_id(), data.getId());//delete old data
                            FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                            QuizQuestionFragment fragment = QuizQuestionFragment.newInstance(contentData.getQuiz_id(), data.getId());
                            fragmentManager.beginTransaction().setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right)
                                    .replace(R.id.container, fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });*/

            } else {
                // holder.quizLayout.setVisibility(View.GONE);
            }
        }


        if (data.getMedia_id() != 0) {

            final Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getMedia_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));
            if (media != null) {

                //   holder.mediaLayout.setVisibility(View.VISIBLE);
          /*      holder.mediaLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (media.getType().equalsIgnoreCase("video")) {
                            playVideoOnSelect(data, holder, "video");
                        } else if (media.getType().equalsIgnoreCase("Audio")) {
                            playAudioOnSelect(data, holder, "Audio");
                        } else if (media.getType().equalsIgnoreCase("Url")) {
                            openUrlOnSelect(data, holder, "Url");
                        } else if (media.getType().equalsIgnoreCase("Youtube")) {
                            playVideoOnSelect(data, holder, "Youtube");
                        }
                    }
                });*/

            }
            if (media == null) {
                // holder.mediaLayout.setVisibility(View.INVISIBLE);
            }
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDo code review
                String type = Consts.CHAPTER;
                if (data.getType().equalsIgnoreCase(Consts.CHAPTER)) {
                    type = Consts.TOPIC;
                } else {
                    type = dbHelper.getTypeOfChildren(data.getId());
                }
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "Navigating to  data_item id: " + data.getId() + " type: " + type);
                }
                if (selectType.equalsIgnoreCase(Consts.CHAPTER)) {
                    logChapterDetails(data, "", "", "Chapters Details");
                } else if (selectType.equalsIgnoreCase(Consts.TOPIC)) {
                    logChapterDetails(data, "", "", "Topic Details");
                }
                if (type.equalsIgnoreCase(Consts.COURSE) || type.equalsIgnoreCase(Consts.CHAPTER) || type.equalsIgnoreCase(Consts.TOPIC)) {

                    stopAudio();
                    FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                    ChaptersFragment fragment = ChaptersFragment.newInstance(data.getId(), type);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right);
                    transaction.replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    if (data.getLang_resource_name() != null) {
                        Data titleResource = dbHelper.getResourceEntityByName(data.getLang_resource_name(),
                                Utility.getLanguageIdFromSharedPreferences(context));
                        if (titleResource != null) {
                            contentTitle = titleResource.getContent();
                        }
                    }
                    stopAudio();
                    FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                    ContentFragment fragment = ContentFragment.newInstance(data.getId(), contentTitle);

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right);
                    transaction.replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        // isFavorites(data, holder);//set favorites icon
  /*      holder.favoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoritesAfterClick(data);
                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.click_animation));//onclick animation

            }
        });*/

   /*     holder.quizStartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudio();
                //Data contentData = dbHelper.getContentQuizEntityByContentId(data.getId());
                if (data.getQuiz_id() != 0) {
                    dbhelper.deleteQuizAnswerEntityById(data.getQuiz_id(), data.getId());//delete old data
                    FragmentManager fragmentManager = ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                    QuizQuestionFragment fragment = QuizQuestionFragment.newInstance(data.getQuiz_id(), data.getContent_id());
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right)
                            .replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });*/
    }

    private void playVideoOnSelect(Data data, ChaptersViewHolderNew holder, String actionType) {
        if (data.getMedia_id() != 0) {
            final DbHelper dbHelper = new DbHelper(context);
            Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getMedia_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));

            if (media != null) {
                if (selectType.equalsIgnoreCase(Consts.CHAPTER)) {
                    logChapterDetails(data, "", "", "Chapters Details");
                } else if (selectType.equalsIgnoreCase(Consts.TOPIC)) {
                    logChapterDetails(data, "", "", "Topic Details");
                }
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "Media id" + media.getId() + " video Url: " + media.getUrl());
                }
                navigateBasedOnMediaType(media, holder);
            }
        }
    }

    private void playAudioOnSelect(Data data, ChaptersViewHolderNew holder, String actionType) {
        if (data.getMedia_id() != 0) {
            final DbHelper dbHelper = new DbHelper(context);
            Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getMedia_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));

            if (media != null) {
                if (selectType.equalsIgnoreCase(Consts.CHAPTER)) {
                    logChapterDetails(data, "", "", "Chapters Details");
                } else if (selectType.equalsIgnoreCase(Consts.TOPIC)) {
                    logChapterDetails(data, "", "", "Topic Details");
                }
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "Media id" + media.getId() + " audio Url: " + media.getUrl());
                }
                navigateBasedOnMediaType(media, holder);
            }
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

                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {//listen_text.getText() == "PAUSE") {
                            mediaPlayer.pause();
                        } else {

                            mediaPlayer = MediaPlayer.create(context, myUri);
                            mediaPlayer.start();
                        }
                    } catch (Exception e) {
                        Log.d(Consts.LOG_TAG, "**************: play audio " + e.getMessage());
                    }
                }
            }
        }.execute(url);
    }

    private void navigateBasedOnMediaType(Data media, ChaptersViewHolderNew holder) {
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
                        //  showOnlineAudio(media);
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
                        try {
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
                        } catch (Exception e) {

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

    private void openUrlOnSelect(Data data, ChaptersViewHolderNew holder, String actionType) {
        if (data.getUrl() != null) {


            final DbHelper dbHelper = new DbHelper(context);
            if (data.getLang_resource_name() != null) {
                Data titleResource = dbHelper.getResourceEntityByName(data.getLang_resource_name(),
                        Utility.getLanguageIdFromSharedPreferences(context));
                if (titleResource != null) {
                    contentTitle = titleResource.getContent();
                    if (selectType.equalsIgnoreCase(Consts.CHAPTER)) {
                        logChapterDetails(data, "", "", "Chapters Details");
                    } else if (selectType.equalsIgnoreCase(Consts.TOPIC)) {
                        logChapterDetails(data, "", "", "Topic Details");
                    }
                }
            }
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            WebFragment fragment = WebFragment.newInstance(data.getUrl(), contentTitle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_right);
            transaction.replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void DownloadImage(ChaptersViewHolderNew holder, Data media) {
        Gson gson = new Gson();
        Intent intent = new Intent(context, DownloadService1.class);
        String strJsonmedia = gson.toJson(media);
        intent.putExtra(Consts.EXTRA_MEDIA, strJsonmedia);
        intent.putExtra(Consts.EXTRA_URLPropertyForDownload, Consts.DOWNLOAD_URL);
        context.startService(intent);
        new DownloadImageTask(holder.chapterImage)
                .execute(media.getDownload_url());
        /*  Picasso.with(context).load(media.getDownload_url()).placeholder(R.drawable.imageplaceholder).into(holder.chapterImage);*/
        Log.d(Consts.LOG_TAG, "2.  display image by picasso: " + media.getDownload_url());
    }

    private void logChapterDetails(Data data, String actionType, String mediaPath, String eventName) {
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
        ServiceRequest.setLogEventName(eventName);
        ServiceRequest.setContentId(data.getId() + "");
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


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    //set favorites
    private void setFavoritesAfterClick(Data data) {
        DbHelper dbHelper = new DbHelper(context);
        FavoritesData favoritesData = dbHelper.getFavoritesData(data.getId());
        FavoritesData favoData = new FavoritesData();
        if (favoritesData != null) {
            if (favoritesData.getFavoritesFlag().equals("1")) {
                favoData.setId(data.getId());
                favoData.setFavoritesFlag("0");
            } else {
                favoData.setId(data.getId());
                favoData.setFavoritesFlag("1");
            }
        } else {
            favoData.setId(data.getId());
            favoData.setFavoritesFlag("1");
        }
        dbHelper.upsertFavoritesData(favoData);
        notifyDataSetChanged();
    }

   /* //set favorites icon
    private void isFavorites(Data data, ChaptersViewHolderNew holder) {
        DbHelper dbHelper = new DbHelper(context);
        FavoritesData favoritesData = dbHelper.getFavoritesData(data.getId());
        if (favoritesData != null) {
            if (favoritesData.getFavoritesFlag().equals("1")) {
                holder.starIcon.setText(Html.fromHtml("&#xf4ce;"));
                holder.starIcon.setTextColor(Color.parseColor("#ffffff"));
                holder.favorite.setText(context.getString(R.string.favourite));
            } else {
                holder.starIcon.setText(Html.fromHtml("&#xf4d2;"));
                holder.starIcon.setTextColor(Color.parseColor("#ffffff"));
                holder.favorite.setText(context.getString(R.string.mark_as_favourite));
            }
        } else {
            holder.starIcon.setText(Html.fromHtml("&#xf4d2;"));
            holder.starIcon.setTextColor(Color.parseColor("#ffffff"));
            holder.favorite.setText(context.getString(R.string.mark_as_favourite));
        }
    }*/

    /*private class LoadImageFromDataBase extends AsyncTask<String, Void, Uri> {
        ScaleImageView bmImage;
        ProgressBar progressBar;

        public LoadImageFromDataBase(ScaleImageView bmImage, ProgressBar bar) {
            this.bmImage = bmImage;
            this.progressBar = bar;
        }

        @Override
        protected Uri doInBackground(String... params) {

            String localFilePath = params[0];
            Uri uri = null;
            if (localFilePath != null) {
                uri = Uri.fromFile(new File(localFilePath));
            }
            return uri;
        }

        @Override
        protected void onPostExecute(Uri result) {
            if (result != null) {
                Picasso.with(context).load(result).placeholder(R.drawable.imageplaceholder).into(bmImage);
            }
            progressBar.setVisibility(View.GONE);
           *//* if (customProgressDialog != null) {
                if (customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
            }*//*

        }

        @Override
        protected void onPreExecute() {
            //customProgressDialog.show();
            progressBar.setVisibility(View.VISIBLE);
        }
    }`
*/
    private void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {//listen_text.getText() == "PAUSE") {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

}
