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
    public TextView courseTitle, courseDescription,learnIcon,learnLable,media_Icon,media_text;
    public CardView cardView;
    public ImageView courseImage;
public LinearLayout mediaLayout;
    public CourseViewHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.card_view);
        cardView.setCardElevation(2);
        cardView.setRadius(10);
        courseTitle = (TextView) itemView.findViewById(R.id.courseTitle);
        courseDescription = (TextView) itemView.findViewById(R.id.courseDescription);
        courseImage = (ImageView) itemView.findViewById(R.id.courseImage);
        learnIcon = (TextView) itemView.findViewById(R.id.learnIcon);
        learnLable = (TextView) itemView.findViewById(R.id.learnLable);
        media_Icon = (TextView) itemView.findViewById(R.id.media_Icon);
        media_text = (TextView) itemView.findViewById(R.id.media_text);
        mediaLayout = (LinearLayout) itemView.findViewById(R.id.mediaLayout);

    }


}
