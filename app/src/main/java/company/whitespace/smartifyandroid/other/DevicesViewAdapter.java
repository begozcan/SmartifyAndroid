package company.whitespace.smartifyandroid.other;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.activity.MainActivity;
import company.whitespace.smartifyandroid.fragment.AddConditionFragment;
import company.whitespace.smartifyandroid.fragment.AddScheduleFragment;
import company.whitespace.smartifyandroid.fragment.ControlDeviceFragment;
import company.whitespace.smartifyandroid.fragment.DevicesFragment;
import company.whitespace.smartifyandroid.fragment.DevicesFragment.OnListFragmentInteractionListener;
import company.whitespace.smartifyandroid.model.Device;
import company.whitespace.smartifyandroid.networking.DeviceAsyncTask;

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
    private Context context;

    public DevicesViewAdapter(List<Device> devices, OnListFragmentInteractionListener listener, FragmentManager fragmentManager) {
        mDevices = devices;
        mListener = listener;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.swipe_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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

                Bundle bundle = new Bundle();
                bundle.putString("device_id", String.valueOf(position));


                MainActivity.CURRENT_TAG = MainActivity.TAG_CONTROL_DEVICE;

                // update the main content by replacing fragments
                Fragment fragment = new ControlDeviceFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "control device");
                fragmentTransaction.commit();

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
                Pair<String, String>[] pairs = new Pair[3];
                pairs[0] = new Pair<>("name", holder.mItem.getName());
                pairs[1] = new Pair<>("room", holder.mItem.getRoom());
                pairs[2] = new Pair<>("type", holder.mItem.getType());
                DeviceAsyncTask deviceAsyncTask = new DeviceAsyncTask(context, "devices_remove");
                deviceAsyncTask.setDevicesViewAdapter(DevicesViewAdapter.this);
                deviceAsyncTask.execute(pairs);
            }
        });

        holder.btnAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SWIPE MENU", "Add Schedule: " + holder.mItem.toString());

                Bundle bundle = new Bundle();
                bundle.putString("device_id", String.valueOf(position));

                // update the main content by replacing fragments
                MainActivity.CURRENT_TAG = MainActivity.TAG_ADD_SCHEDULE;

                Fragment fragment = new AddScheduleFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "add schedule");
                fragmentTransaction.commit();
            }
        });

        holder.btnAddCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SWIPE MENU", "Add Condition: " + holder.mItem.toString());

                Bundle bundle = new Bundle();
                bundle.putString("device_id", String.valueOf(position));

                // update the main content by replacing fragments
                MainActivity.CURRENT_TAG = MainActivity.TAG_ADD_CONDITION;

                Fragment fragment = new AddConditionFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "add condition");
                fragmentTransaction.commit();
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
        View btnAddSchedule;
        View btnAddCond;
        SwipeHorizontalMenuLayout sml;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            deviceName = (TextView) itemView.findViewById(R.id.device_name);
            deviceRoom = (TextView) itemView.findViewById(R.id.device_room);

            btnOpen = itemView.findViewById(R.id.btOpen);
            btnDelete = itemView.findViewById(R.id.btDelete);
            btnAddSchedule = itemView.findViewById(R.id.bt_AddSchedule);
            btnAddCond = itemView.findViewById(R.id.bt_AddCond);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + deviceName.getText() + "'";
        }

    }

    public void onSuccess(){
        MainActivity.CURRENT_TAG = MainActivity.TAG_DEVICES;
        // update the main content by replacing fragments
        Fragment fragment = new DevicesFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, "devices");
        fragmentTransaction.commit();
    }
}
