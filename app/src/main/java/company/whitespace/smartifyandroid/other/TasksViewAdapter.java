package company.whitespace.smartifyandroid.other;

import android.content.Context;
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
import company.whitespace.smartifyandroid.fragment.DevicesFragment;
import company.whitespace.smartifyandroid.fragment.TasksFragment;
import company.whitespace.smartifyandroid.model.ConditionalTask;
import company.whitespace.smartifyandroid.model.ScheduledTask;
import company.whitespace.smartifyandroid.model.Task;

import java.util.List;

import company.whitespace.smartifyandroid.fragment.TasksFragment.OnListFragmentInteractionListener;
import company.whitespace.smartifyandroid.networking.DeviceAsyncTask;
import company.whitespace.smartifyandroid.networking.TaskAsyncTask;

/**
 * {@link RecyclerView.Adapter} makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TasksViewAdapter extends RecyclerView.Adapter<TasksViewAdapter.ViewHolder> {

    private final List<Task> mTasks;
    private final OnListFragmentInteractionListener mListener;
    private TasksFragment tasksFragment;
    private Context context;

    public TasksViewAdapter(List<Task> tasks, OnListFragmentInteractionListener listener, TasksFragment tasksFragment) {
        mTasks = tasks;
        mListener = listener;
        this.tasksFragment = tasksFragment;
    }
    public void onSuccess(){
        tasksFragment.onSuccess();
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
        holder.explanation.setText(mTasks.get(position).toString());

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

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // must close normal
//                myViewHolder.sml.smoothCloseMenu();
//                users.remove(vh.getAdapterPosition());
//                mAdapter.notifyItemRemoved(vh.getAdapterPosition());
                if (holder.mItem.getType().equals("Conditional Task")) {
                    Pair<String, String>[] pairs = new Pair[5];
                    ConditionalTask task = (ConditionalTask) holder.mItem;
                    pairs[0] = new Pair<>("type", task.getType());
                    pairs[1] = new Pair<>("device_name", task.getDeviceName());
                    pairs[2] = new Pair<>("action_name", task.getActionName());
                    pairs[3] = new Pair<>("sensor_type", task.getSensorType());
                    pairs[4] = new Pair<>("threshold", task.getThreshold());

                    TaskAsyncTask taskAsyncTask = new TaskAsyncTask(context, "tasks_remove");
                    taskAsyncTask.setTasksViewAdapter(TasksViewAdapter.this);
                    taskAsyncTask.execute(pairs);
                }else{
                    Pair<String, String>[] pairs = new Pair[6];
                    ScheduledTask task = (ScheduledTask) holder.mItem;
                    pairs[0] = new Pair<>("type", task.getType());
                    pairs[1] = new Pair<>("device_name", task.getDeviceName());
                    pairs[2] = new Pair<>("action_name", task.getActionName());
                    pairs[3] = new Pair<>("hour", task.getHour());
                    pairs[4] = new Pair<>("minute", task.getMinute());
                    pairs[5] = new Pair<>("repeatdays", task.getRepeatdays());

                    TaskAsyncTask taskAsyncTask = new TaskAsyncTask(context, "tasks_remove");
                    taskAsyncTask.setTasksViewAdapter(TasksViewAdapter.this);
                    taskAsyncTask.execute(pairs);
                }
            }
        });


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
