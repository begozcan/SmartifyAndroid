package company.whitespace.smartifyandroid.other;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.fragment.DevicesFragment.OnListFragmentInteractionListener;
import company.whitespace.smartifyandroid.model.Device;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DevicesViewAdapter extends RecyclerView.Adapter<DevicesViewAdapter.ViewHolder> {

    private final List<Device> mDevices;
    private final OnListFragmentInteractionListener mListener;

    public DevicesViewAdapter(List<Device> devices, OnListFragmentInteractionListener listener) {
        mDevices = devices;
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
        holder.mItem = mDevices.get(position);
        holder.deviceName.setText(mDevices.get(position).getName());
        holder.deviceRoom.setText(mDevices.get(position).getRoom());

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
        return mDevices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Device mItem;

        TextView deviceName;
        TextView deviceRoom;

        View btnOpen;
        View btnDelete;
        View btnLeft;
        SwipeHorizontalMenuLayout sml;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            deviceName = (TextView) itemView.findViewById(R.id.device_name);
            deviceRoom = (TextView) itemView.findViewById(R.id.device_room);

            btnOpen = itemView.findViewById(R.id.btOpen);
            btnDelete = itemView.findViewById(R.id.btDelete);
            btnLeft = itemView.findViewById(R.id.btLeft);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + deviceName.getText() + "'";
        }

    }
}
