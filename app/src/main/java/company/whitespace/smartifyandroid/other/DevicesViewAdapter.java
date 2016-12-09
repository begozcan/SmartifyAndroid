package company.whitespace.smartifyandroid.other;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.fragment.DeviceFragment.OnListFragmentInteractionListener;
import company.whitespace.smartifyandroid.fragment.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DevicesViewAdapter extends RecyclerView.Adapter<DevicesViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public DevicesViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.swipe_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.itemID.setText(mValues.get(position).id);
        holder.itemName.setText(mValues.get(position).content);

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

        holder.btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SWIPE MENU", "Open");
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // must close normal
//                myViewHolder.sml.smoothCloseMenu();
//                users.remove(vh.getAdapterPosition());
//                mAdapter.notifyItemRemoved(vh.getAdapterPosition());
                Log.d("SWIPE MENU", "Delete");
            }
        });

        holder.btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SWIPE MENU", "Left");
            }
        });


        holder.sml.setSwipeEnable(true);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public DummyItem mItem;

        TextView itemID;
        TextView itemName;

        View btnOpen;
        View btnDelete;
        View btnLeft;
        SwipeHorizontalMenuLayout sml;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemID = (TextView) itemView.findViewById(R.id.id);
            itemName = (TextView) itemView.findViewById(R.id.content);

            btnOpen = itemView.findViewById(R.id.btOpen);
            btnDelete = itemView.findViewById(R.id.btDelete);
            btnLeft = itemView.findViewById(R.id.btLeft);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + itemName.getText() + "'";
        }

    }
}
