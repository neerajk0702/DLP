package dlp.bluelupin.dlp.Adapters;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.Models.Invitations;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.Utility;

import java.util.Date;
import java.util.List;

public class MyInviteFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MyInviteFragmentRecyclerViewAdapter.ViewHolder>{

    private final List<Invitations> mValues;
    Context context;
    public MyInviteFragmentRecyclerViewAdapter(Context context,List<Invitations> items) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_invitefragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Typeface VodafoneExB = FontManager.getFontTypeface(context, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(context, "fonts/VodafoneRg.ttf");
        holder.mobileNo.setTypeface(VodafoneExB);
        holder.dateTime.setTypeface(VodafoneRg);
        holder.invitetext.setTypeface(VodafoneRg);
        holder.mobileNo.setText(mValues.get(position).getPhone());
        if(mValues.get(position).getSignedup_date()!=null){
            holder.inviteView.setBackground(ContextCompat.getDrawable(context, R.drawable.green_bg));
            holder.invitetext.setText(context.getString(R.string.Joined));
        }else{
            holder.inviteView.setBackground(ContextCompat.getDrawable(context, R.drawable.light_gray_bg));
            holder.invitetext.setText(context.getString(R.string.Invited));//android:background="@drawable/white_bg"
        }
        if(mValues.get(position).getCreated_at()!=null){
            Date date=Utility.parseDateFromString(mValues.get(position).getCreated_at());
            String stringMonth = (String) android.text.format.DateFormat.format("MMM", date); //Jun
            String day = (String) android.text.format.DateFormat.format("dd", date); //20
            String year = (String) android.text.format.DateFormat.format("yyyy", date); //2013
            holder.dateTime.setText(day+" "+stringMonth+" "+year);
        }


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         View mView;
         TextView mobileNo,invitetext,dateTime;
         LinearLayout inviteView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mobileNo = (TextView) view.findViewById(R.id.mobileNo);
            invitetext = (TextView) view.findViewById(R.id.invitetext);
            inviteView =  view.findViewById(R.id.inviteView);
            dateTime=view.findViewById(R.id.dateTime);
        }

    }
}
