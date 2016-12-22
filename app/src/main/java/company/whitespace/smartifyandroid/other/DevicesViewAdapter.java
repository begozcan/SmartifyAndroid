package company.whitespace.smartifyandroid.other;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.fragment.AddCommandFragment;
import company.whitespace.smartifyandroid.fragment.ControlDeviceFragment;
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
    private final FragmentManager fragmentManager;

    public DevicesViewAdapter(List<Device> devices, OnListFragmentInteractionListener listener, FragmentManager fragmentManager) {
        mDevices = devices;
        mListener = listener;
        this.fragmentManager = fragmentManager;
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
                Log.d("SWIPE MENU", "Control: " + holder.mItem.toString());

                // update the main content by replacing fragments
                Fragment fragment = new ControlDeviceFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "CONTROL_DEVICE");
                fragmentTransaction.commitAllowingStateLoss();

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // must close normal
//                myViewHolder.sml.smoothCloseMenu();
//                users.remove(vh.getAdapterPosition());
//                mAdapter.notifyItemRemoved(vh.getAdapterPosition());
                Log.d("SWIPE MENU", "Delete: " + holder.mItem.toString());
            }
        });

        holder.btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SWIPE MENU", "Add Command: " + holder.mItem.toString());

                Fragment fragment = new AddCommandFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "ADD_COMMAND");
                fragmentTransaction.commitAllowingStateLoss();
            }
        });


        holder.sml.setSwipeEnable(true);
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
