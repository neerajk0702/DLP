package dlp.bluelupin.dlp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.Models.DownloadMediaWithParent;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Services.DownloadService1;
import dlp.bluelupin.dlp.Utilities.CustomProgressDialog;
import dlp.bluelupin.dlp.Utilities.DownloadImageTask;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.Utility;

/**
 * Created by Neeraj on 8/5/2016.
 */
public class DownloadingAdapter extends RecyclerView.Adapter<DownloadingViewHolder> {

    private List<DownloadMediaWithParent> itemList;
    private Context context;
    private Boolean favFlage = false;
    private String type;
    private CustomProgressDialog customProgressDialog;

    public DownloadingAdapter(Context context, List<DownloadMediaWithParent> list) {
        this.itemList = list;
        this.context = context;
        this.type = type;
        customProgressDialog = new CustomProgressDialog(context, R.mipmap.syc);
    }


    @Override
    public DownloadingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloading_list_view_item, parent, false);
        return new DownloadingViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final DownloadingViewHolder holder, final int position) {
        Typeface VodafoneExB = FontManager.getFontTypeface(context, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(context, "fonts/VodafoneRg.ttf");
        Typeface materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        holder.mediaTitle.setTypeface(VodafoneExB);
        holder.mediaDescription.setTypeface(VodafoneRg);
        holder.downloadProgress.setTypeface(VodafoneExB);
        holder.download.setTypeface(VodafoneRg);
        holder.cancelIcon.setTypeface(materialdesignicons_font);
        holder.cancelIcon.setText(Html.fromHtml("&#xf156"));
        // starting the download service
        final DbHelper dbHelper = new DbHelper(context);
        final DownloadMediaWithParent dataWithParent = itemList.get(position);
        final Data data = dbHelper.getDataEntityById(dataWithParent.getParentId());
        if (data != null) {
            final Data resource = dbHelper.getResourceEntityByName(data.getLang_resource_name(),
                    Utility.getLanguageIdFromSharedPreferences(context));
            if (data.getLang_resource_name() != null) {
                Data titleResource = dbHelper.getResourceEntityByName(data.getLang_resource_name(),
                        Utility.getLanguageIdFromSharedPreferences(context));
                if (titleResource != null) {
                    holder.mediaTitle.setText(Html.fromHtml(titleResource.getContent()));
                }
            }
        }

        if (data.getThumbnail_media_id() != 0) {
            Data media = dbHelper.getMediaEntityByIdAndLaunguageId(data.getThumbnail_media_id(),
                    Utility.getLanguageIdFromSharedPreferences(context));
            if (media != null && media.getDownload_url() != null) {
                if (media.getLocalFilePath() == null) {
                    if (Utility.isOnline(context)) {
                        Gson gson = new Gson();

                        Intent intent = new Intent(context, DownloadService1.class);
                        String strJsonmedia = gson.toJson(media);
                        intent.putExtra(Consts.EXTRA_MEDIA, strJsonmedia);
                        intent.putExtra(Consts.EXTRA_URLPropertyForDownload, Consts.DOWNLOAD_URL);

                        context.startService(intent);
                        new DownloadImageTask(null)
                                .execute(media.getDownload_url());
                    }
                } else {
                  /*  File imgFile = new File(media.getLocalFilePath());
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        holder.mediaImage.setImageBitmap(bitmap);
                    }*/
                    Uri uri = Uri.fromFile(new File(media.getLocalFilePath()));
                    if (uri != null) {
                        Picasso.with(context).load(uri).placeholder(R.drawable.imageplaceholder).into(holder.mediaImage);
                    }
                }

            }
        }
//        final Data data_item = dataWithParent.getStrJsonResourcesToDownloadList();
//        holder.mediaTitle.setText(data_item.getFile_path());


        /*DataAdapter dataAdapter = new DataAdapter(context, dataWithParent.getStrJsonResourcesToDownloadList());
        holder.progressList.setAdapter(dataAdapter);
        Utility.justifyListViewHeightBasedOnChildrenForDisableScrool(holder.progressList);
        dataAdapter.notifyDataSetChanged();*/

//        if (Consts.IS_DEBUG_LOG) {
//            Log.d(Consts.LOG_TAG, "**** setting progress for media Id: " + data_item.getId() + " progress:" + data_item.getProgress() + "%");
//        }
//        if (data_item.getProgress() != 0) {
//            holder.downloadProgress.setText(data_item.getProgress() + "% Completed");
//            holder.mProgress.setProgress(data_item.getProgress());
//        }
//        if (data_item.getProgress() >= 100) {
//            holder.cardView.setVisibility(View.INVISIBLE);
//        }
        holder.cancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dbHelper.deleteFileDownloadedByParentId(dataWithParent.getParentId());//delete media by parent id
                /*Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(Consts.mBroadcastDeleteAction);
                broadcastIntent.putExtra("mediaId", dataWithParent.getParentId());
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);*/
                DownloadService1.shouldContinue = false;
                if (Consts.IS_DEBUG_LOG) {
                    Log.d(Consts.LOG_TAG, "**** setting DownloadService1.shouldContinue: " + DownloadService1.shouldContinue);
                }

                List<Data> dlResourceToBeCancelled = dataWithParent.getStrJsonResourcesToDownloadList();
                for (Data data : dlResourceToBeCancelled) {
                    dbHelper.deleteFileDownloadedByMediaId(data.getMediaId());
                }
                holder.cardView.setVisibility(View.INVISIBLE);
                itemList.remove(position);
                notifyDataSetChanged();
//                Gson gson = new Gson();
//                String strJsonmedia = gson.toJson(data);
//                Intent intent = new Intent(Consts.MESSAGE_CANCEL_DOWNLOAD);
//                intent.putExtra(Consts.EXTRA_MEDIA, strJsonmedia); // strJsonmedia
//                intent.putExtra("parentId", dataWithParent.getParentId());
//                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//
//                if (Consts.IS_DEBUG_LOG) {
//                    Log.d(Consts.LOG_TAG, "**** sending cancel message in DownloadingAdapter: " + intent.getAction());
//                }
            }
        });

    }


   /* private FrameLayout makeProgressBar(int progress) {

        FrameLayout frameLayout = new FrameLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        layoutParams.setMargins(0, 0, 0, 0);
        frameLayout.setLayoutParams(layoutParams);
        ProgressBar dynamicProgress = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        dynamicProgress.setLayoutParams(new RecyclerView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        dynamicProgress.setProgress(progress);
        dynamicProgress.setIndeterminate(false);
        dynamicProgress.setVisibility(View.VISIBLE);
        dynamicProgress.setProgressDrawable(context.getResources().getDrawable(R.drawable.custom_progressbar));

        frameLayout.addView(dynamicProgress);
        return frameLayout;
    }*/

    @Override
    public int getItemCount() {
        return itemList.size();
    }


}

