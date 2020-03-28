package dlp.bluelupin.dlp.Adapters;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Utilities.ScaleImageView;

/**
 * Created by Neeraj on 7/26/2016.
 */
public class ChaptersViewHolderNew extends RecyclerView.ViewHolder {
    public TextView chapterTitle;
    public CardView cardView;
    public ScaleImageView chapterImage;
    //public TextView starIcon,arrowIcon,start_quiz_Icon;
    //public TextView  media_Icon,media_text;
    //public LinearLayout mediaLayout;
    //public RelativeLayout starIconlayout, downloadIconlayout;
    //public LinearLayout download_layout, favoriteLayout;
    public ProgressBar progressBar;
    // public LinearLayout quizStartLayout,quizLayout,titleLayout,buttonLayout;


    LinearLayout chapterView;


    public ChaptersViewHolderNew(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.card_view);
        cardView.setCardElevation(2);
        cardView.setRadius(10);
        chapterTitle = (TextView) itemView.findViewById(R.id.chapterTitle);
        // chapterDescription = (TextView) itemView.findViewById(R.id.chapterDescription);
        //favorite = (TextView) itemView.findViewById(R.id.favourite);
        //  starIcon = (TextView) itemView.findViewById(R.id.starIcon);
        //  download = (TextView) itemView.findViewById(R.id.download);
        //  downloadIcon = (TextView) itemView.findViewById(R.id.downloadIcon);
        chapterImage = (ScaleImageView) itemView.findViewById(R.id.chapterImage);
        ////favoriteLayout = (LinearLayout) itemView.findViewById(R.id.favoriteLayout);
        // download_layout = (LinearLayout) itemView.findViewById(R.id.download_layout);
        // starIconlayout = (RelativeLayout) itemView.findViewById(R.id.starIconlayout);
        // downloadIconlayout = (RelativeLayout) itemView.findViewById(R.id.downloadIconlayout);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        // quiz = (TextView) itemView.findViewById(R.id.quiz);
        // quiz_Icon = (TextView) itemView.findViewById(R.id.quiz_Icon);
        // start_quiz_Icon = (TextView) itemView.findViewById(R.id.start_quiz_Icon);
        // arrowIcon = (TextView) itemView.findViewById(R.id.arrowIcon);
        //quizStartLayout = (LinearLayout) itemView.findViewById(R.id.quizStartLayout);
        //quizLayout = (LinearLayout) itemView.findViewById(R.id.quizLayout);
        //titleLayout = (LinearLayout) itemView.findViewById(R.id.titleLayout);
        //buttonLayout = (LinearLayout) itemView.findViewById(R.id.buttonLayout);

        //  media_Icon = (TextView) itemView.findViewById(R.id.media_Icon);
        // media_text = (TextView) itemView.findViewById(R.id.media_text);
        // mediaLayout = (LinearLayout) itemView.findViewById(R.id.mediaLayout);


        chapterView = itemView.findViewById(R.id.chapterView);

    }


}