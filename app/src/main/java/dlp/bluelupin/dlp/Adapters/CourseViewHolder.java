package dlp.bluelupin.dlp.Adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dlp.bluelupin.dlp.R;

/**
 * Created by Neeraj on 7/26/2016.
 */
public class CourseViewHolder extends RecyclerView.ViewHolder {
    public TextView courseTitle, courseDescription,startlearningtext;
    public CardView cardView;
    public ImageView courseImage;
   // public LinearLayout mediaLayout;

    ImageView mainImage;
    TextView countchapter, topicCount, topiclabel, quizCount, quizlabel, viewCount, viewlabel, userCount, userlabel,chapterLable;
    LinearLayout startlearning;


    public CourseViewHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.card_view);
        //cardView.setCardElevation(2);
       // cardView.setRadius(10);
        courseTitle = (TextView) itemView.findViewById(R.id.courseTitle);
        courseDescription = (TextView) itemView.findViewById(R.id.courseDescription);
        courseImage =  itemView.findViewById(R.id.courseImage);
        //learnIcon = (TextView) itemView.findViewById(R.id.learnIcon);
       // learnLable = (TextView) itemView.findViewById(R.id.learnLable);
       //media_Icon = (TextView) itemView.findViewById(R.id.media_Icon);
       // media_text = (TextView) itemView.findViewById(R.id.media_text);
       // mediaLayout = (LinearLayout) itemView.findViewById(R.id.mediaLayout);

        chapterLable = (TextView) itemView.findViewById(R.id.chapterLable);
        countchapter = itemView.findViewById(R.id.countchapter);
        topicCount = itemView.findViewById(R.id.topicCount);
        topiclabel = itemView.findViewById(R.id.topiclabel);
        quizCount = itemView.findViewById(R.id.quizCount);
        quizlabel = itemView.findViewById(R.id.quizlabel);
        viewCount = itemView.findViewById(R.id.viewCount);
        viewlabel = itemView.findViewById(R.id.viewlabel);
        userCount = itemView.findViewById(R.id.userCount);
        userlabel = itemView.findViewById(R.id.userlabel);
        startlearning = itemView.findViewById(R.id.startlearning);
        startlearningtext = itemView.findViewById(R.id.startlearningtext);


    }





}
