package dlp.bluelupin.dlp.Adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dlp.bluelupin.dlp.Models.Data;
import dlp.bluelupin.dlp.R;

import java.util.List;

public class MyInviteFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MyInviteFragmentRecyclerViewAdapter.ViewHolder>{

    private final List<Data> mValues;
    Context context;
    public MyInviteFragmentRecyclerViewAdapter(Context context,List<Data> items) {
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
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         View mView;
         TextView mobileNo,invitetext;
         LinearLayout inviteView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mobileNo = (TextView) view.findViewById(R.id.mobileNo);
            invitetext = (TextView) view.findViewById(R.id.invitetext);
            inviteView =  view.findViewById(R.id.inviteView);
        }

    }
}
