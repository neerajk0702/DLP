package dlp.bluelupin.dlp.Adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dlp.bluelupin.dlp.R;

/**
 * Created by Neeraj on 8/10/2016.
 */
public class FavoritesViewHolder extends RecyclerView.ViewHolder  {
    public TextView chapterTitle, chapterDescription,starIcon;
    public CardView cardView;
    public ImageView chapterImage;
    public LinearLayout favorite_layout,download_layout;
    public ImageView downloadIcon;
    public FavoritesViewHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.card_view);
        cardView.setCardElevation(2);
        cardView.setRadius(10);
        chapterTitle = (TextView) itemView.findViewById(R.id.chapterTitle);
        chapterDescription = (TextView) itemView.findViewById(R.id.chapterDescription);
        starIcon = (TextView) itemView.findViewById(R.id.starIcon);
        downloadIcon = (ImageView) itemView.findViewById(R.id.downloadIcon);
        chapterImage = (ImageView) itemView.findViewById(R.id.chapterImage);
        favorite_layout= (LinearLayout) itemView.findViewById(R.id.favorite_layout);
        download_layout= (LinearLayout) itemView.findViewById(R.id.download_layout);
    }



}