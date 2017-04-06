package company.whitespace.smartifyandroid.other;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tubb.smrv.SwipeHorizontalMenuLayout;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.activity.MainActivity;
import company.whitespace.smartifyandroid.fragment.AddConditionFragment;
import company.whitespace.smartifyandroid.fragment.AddScheduleFragment;
import company.whitespace.smartifyandroid.model.Task;

import java.util.List;
import company.whitespace.smartifyandroid.fragment.TasksFragment.OnListFragmentInteractionListener;
/**
 * {@link RecyclerView.Adapter} makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TasksViewAdapter extends RecyclerView.Adapter<TasksViewAdapter.ViewHolder> {

    private final List<Task> mTasks;
    private final OnListFragmentInteractionListener mListener;
    private final FragmentManager fragmentManager;
    private Context context;

    public TasksViewAdapter(List<Task> tasks, OnListFragmentInteractionListener listener, FragmentManager fragmentManager) {
        mTasks = tasks;
        mListener = listener;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.swipe_menu_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mTasks.get(position);
        holder.type.setText(mTasks.get(position).getType());
        holder.explanation.setText(mTasks.get(position).getExplanation());

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

//        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                // must close normal
////                myViewHolder.sml.smoothCloseMenu();
////                users.remove(vh.getAdapterPosition());
////                mAdapter.notifyItemRemoved(vh.getAdapterPosition());
////                Log.d("SWIPE MENU", "Delete: " + holder.mItem.toString());
////                Pair<String, String>[] pairs = new Pair[3];
////                pairs[0] = new Pair<>("name", holder.mItem.getName());
////                pairs[1] = new Pair<>("room", holder.mItem.getRoom());
////                pairs[2] = new Pair<>("type", holder.mItem.getType());
////                DeviceAsyncTask deviceAsyncTask = new DeviceAsyncTask(context, "devices_remove");
////                deviceAsyncTask.setDevicesViewAdapter((ü) TasksViewAdapter.this);
////                deviceAsyncTask.execute(pairs);
//            }
//        });

        holder.sml.setSwipeEnable(true);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Task mItem;

        TextView type;
        TextView explanation;

        View btnEdit;
        View btnDelete;
        SwipeHorizontalMenuLayout sml;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            type = (TextView) itemView.findViewById(R.id.device_name);
            explanation = (TextView) itemView.findViewById(R.id.device_room);

            btnEdit = itemView.findViewById(R.id.bt_Edit);
            btnDelete = itemView.findViewById(R.id.btDelete);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + type.getText() + "'";
        }

    }
}
