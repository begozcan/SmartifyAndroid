package company.whitespace.smartifyandroid.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.fragment.AddDeviceFragment;
import company.whitespace.smartifyandroid.other.DevicesViewAdapter;
import company.whitespace.smartifyandroid.other.TasksViewAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by begum on 23/02/17.
 */
public class TaskAsyncTask extends NetworkingAsyncTask {
    TasksViewAdapter tasksViewAdapter;

    public TaskAsyncTask(Context context, String requestLink) {
        super(context, requestLink);
        tasksViewAdapter = null;
    }

    public void setTasksViewAdapter(TasksViewAdapter tasksViewAdapter) {
        this.tasksViewAdapter = tasksViewAdapter;
    }

    @Override
    public void onSessionFail() {
        Log.d("TASKS", "errr");

    }

    @Override
    public void onSuccess() {
        Log.d("TASKS", result);

        SharedPreferences taskSharedPref = context.getSharedPreferences(context.getString(R.string.tasks_shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor taskEditor = taskSharedPref.edit();
        taskEditor.clear();

        try {
            JSONArray allDevices = new JSONObject(result).getJSONArray("Data");

            for (int i = 0; i < allDevices.length(); i++) {
                JSONArray array = allDevices.getJSONArray(i);
                taskEditor.putString(String.valueOf(i), array.toString());
            }

            taskEditor.apply();

            if (tasksViewAdapter != null){
                tasksViewAdapter.onSuccess();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
