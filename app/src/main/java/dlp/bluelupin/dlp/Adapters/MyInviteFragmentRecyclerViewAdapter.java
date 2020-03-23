package dlp.bluelupin.dlp.Adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dlp.bluelupin.dlp.Fragments.InviteFragmentFragment.OnListFragmentInteractionListener;
import dlp.bluelupin.dlp.Activities.dummy.DummyContent.DummyItem;
import dlp.bluelupin.dlp.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyInviteFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MyInviteFragmentRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyInviteFragmentRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_invitefragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         View mView;
         TextView mobileNo,invitetext;
         LinearLayout inviteView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mobileNo = (TextView) view.findViewById(R.id.mobileNo);
            invitetext = (TextView) view.findViewById(R.id.invitetext);
            inviteView =  view.findViewById(R.id.inviteView);
        }

    }
}
