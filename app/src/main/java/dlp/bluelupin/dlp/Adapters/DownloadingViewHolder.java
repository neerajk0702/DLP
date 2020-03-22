package dlp.bluelupin.dlp.Adapters;

import android.os.AsyncTask;
import android.os.SystemClock;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import dlp.bluelupin.dlp.R;

/**
 * Created by Neeraj on 8/5/2016.
 */
public class DownloadingViewHolder extends RecyclerView.ViewHolder {
    public TextView mediaTitle, mediaDescription, download, downloadProgress;
    public CardView cardView;
    public ImageView mediaImage;
    public TextView cancelIcon;
    public ProgressBar mProgress;
    public LinearLayout container;
    public ListView progressList;

    public DownloadingViewHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.card_view);
        cardView.setCardElevation(2);
        cardView.setRadius(10);
        mediaTitle = (TextView) itemView.findViewById(R.id.mediaTitle);
        mediaDescription = (TextView) itemView.findViewById(R.id.mediaDescription);
        download = (TextView) itemView.findViewById(R.id.download);
        downloadProgress = (TextView) itemView.findViewById(R.id.downloadProgress);
        mediaImage = (ImageView) itemView.findViewById(R.id.mediaImage);
        cancelIcon = (TextView) itemView.findViewById(R.id.cancelIcon);
        mProgress = (ProgressBar) itemView.findViewById(R.id.progressBar);
        container = (LinearLayout) itemView.findViewById(R.id.container);
        progressList = (ListView) itemView.findViewById(R.id.progressList);
         /* holder.mProgress.setProgress(25);   // Main Progress
        holder.mProgress.setSecondaryProgress(50); // Secondary Progress
        holder.mProgress.setMax(100); // Maximum Progress*/
       //new ShowCustomProgressBarAsyncTask().execute();

    }

    public class ShowCustomProgressBarAsyncTask extends AsyncTask<Void, Integer, Void> {
        int myProgress;

        @Override
        protected void onPostExecute(Void result) {
           /* textview.setText("Finish work with custom ProgressBar");
            button1.setEnabled(true);
            progressBar2.setVisibility(View.INVISIBLE);*/
        }

        @Override
        protected void onPreExecute() {
            myProgress = 0;
            //progressBar.setSecondaryProgress(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (myProgress < 100) {
                myProgress++;
                publishProgress(myProgress);
                SystemClock.sleep(100);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgress.setProgress(values[0]);
            mProgress.setSecondaryProgress(values[0] + 5);
        }
    }



}