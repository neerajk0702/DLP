package dlp.bluelupin.dlp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;
import java.util.List;

import dlp.bluelupin.dlp.Activities.CertificateListActivity;
import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.Models.AccountData;
import dlp.bluelupin.dlp.Models.Certificates;
import dlp.bluelupin.dlp.Models.ContentData;
import dlp.bluelupin.dlp.Models.Invitations;
import dlp.bluelupin.dlp.Models.Names;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Services.DownloadCertificateService;
import dlp.bluelupin.dlp.Services.DownloadService1;
import dlp.bluelupin.dlp.Services.IAsyncWorkCompletedCallbackWithContentData;
import dlp.bluelupin.dlp.Services.ServiceCaller;
import dlp.bluelupin.dlp.Utilities.CustomProgressDialog;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.Utility;

public class CertificatesAdapter extends RecyclerView.Adapter<CertificatesAdapter.ViewHolder>{

    private final List<Certificates> mValues;
    Context context;
    int languageId;
    DbHelper dbhelper;
    public CertificatesAdapter(Context context,List<Certificates> items) {
        mValues = items;
        this.context = context;
        languageId = Utility.getLanguageIdFromSharedPreferences(this.context);
        dbhelper = new DbHelper(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.certificate_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Typeface VodafoneExB = FontManager.getFontTypeface(context, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(context, "fonts/VodafoneRg.ttf");
        holder.title.setTypeface(VodafoneExB);
        holder.datetext.setTypeface(VodafoneRg);
        holder.certificateno.setTypeface(VodafoneRg);
        holder.certificateno.setText(mValues.get(position).getSerial_no());
        //show content based on language selected
        List<Names> names=mValues.get(position).getCoursename().getNames();
        for (Names namesobj:names) {
            if(languageId==namesobj.getLanguage_id()){
                holder.title.setText(namesobj.getContent());
            }
        }
        Date date= Utility.parseDateFromString(mValues.get(position).getCreated_at());
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
        String day = (String) android.text.format.DateFormat.format("dd", date); //20
        String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
        holder.datetext.setText(stringMonth+" "+day+", "+year);

        AccountData accountDataApToken = dbhelper.getAccountData();
        String apiToken = "";
        if (accountDataApToken != null) {
            if (accountDataApToken.getApi_token() != null) {
                apiToken=accountDataApToken.getApi_token();//"amFA3kOKt5mXWDWVHs8gU5zk5gWe1KS6dV5yJ4pMloyDmJIRqQjI09ohtB9Z";//accountDataApToken.getApi_token();
            }
        }
                //base url+course id+api token
        final String imageUrl=Consts.BASE_URL+"certificates/"+mValues.get(position).getCoursename().getId()+"?api_token="+apiToken;
//        Picasso.with(context).load(imageUrl).placeholder(R.drawable.imageplaceholder).into(holder.certificateimg);
        Picasso.with(context)
                .load(imageUrl).placeholder(R.drawable.imageplaceholder)
                .into(holder.certificateimg, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.imageprogress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.imageprogress.setVisibility(View.GONE);
                    }
                });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator  + mValues.get(position).getSerial_no() + ".png";
                File imagefile= new File(path);
                if(imagefile.exists()){
                    shareCertificate(path,holder.title.getText().toString());
                }else{
                    Toast.makeText(context, context.getString(R.string.certificatesdownload), Toast.LENGTH_LONG).show();
                }

            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DownloadCertificateService.class);
                intent.putExtra("SerialNo", mValues.get(position).getSerial_no());
                intent.putExtra("DownloadUrl", imageUrl);
                intent.putExtra("CourseID", mValues.get(position).getCoursename().getId());
                context.startService(intent);
            }
        });

    }
    private void shareCertificate(String imagePath,String titel){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, titel);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imagePath));
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView title,datetext,certificateno;
        ImageView certificateimg,share,download;
        ProgressBar imageprogress;
        LinearLayout inviteView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.title);
            certificateimg = view.findViewById(R.id.certificateimg);
            share =  view.findViewById(R.id.share);
            download=view.findViewById(R.id.download);
            datetext=view.findViewById(R.id.datetext);
            certificateno=view.findViewById(R.id.certificateno);
            imageprogress=view.findViewById(R.id.imageprogress);
        }

    }
}

