package dlp.bluelupin.dlp.Adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import dlp.bluelupin.dlp.R;

/**
 * Created by Neeraj on 7/28/2016.
 */
public class NotificationViewHolder extends RecyclerView.ViewHolder {
    public TextView notificationDescription, date, dateIcon, timeIcon, time;
    public LinearLayout cardView;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        cardView = (LinearLayout) itemView.findViewById(R.id.card_view);
        //cardView.setCardElevation(2);
        //cardView.setRadius(10);
        notificationDescription = (TextView) itemView.findViewById(R.id.notificationDescription);
        date = (TextView) itemView.findViewById(R.id.date);
        dateIcon = (TextView) itemView.findViewById(R.id.dateIcon);
        timeIcon = (TextView) itemView.findViewById(R.id.timeIcon);
        time = (TextView) itemView.findViewById(R.id.time);
    }

}
