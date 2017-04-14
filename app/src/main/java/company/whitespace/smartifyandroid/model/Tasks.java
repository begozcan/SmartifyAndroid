package company.whitespace.smartifyandroid.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.networking.DeviceAsyncTask;
import company.whitespace.smartifyandroid.networking.TaskAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by begum on 22/12/16.
 */
public class Tasks {
    private static List<Task> tasks;

    public static List<Task> getTasks(Context context) {
        tasks = new ArrayList<Task>();
        Pair<String, String>[] pairs = new Pair[5];
        pairs[0] = new Pair<>("type", "Conditional Task");
        pairs[1] = new Pair<>("device_name", "----------");
        pairs[2] = new Pair<>("action_name", "----------");
        pairs[3] = new Pair<>("sensor_type", "----------");
        pairs[4] = new Pair<>("threshold", "----------");
        new TaskAsyncTask(context, "tasks_remove").execute(pairs);

        String name = context.getString(R.string.tasks_shared_preferences);
        SharedPreferences deviceSharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE);

        for (String key : deviceSharedPref.getAll().keySet()) {
            try {
                JSONArray arr = new JSONArray(deviceSharedPref.getString(key, ""));
                if (arr.getString(1).equals("Conditional Task"))
                    tasks.add(new ConditionalTask(arr.getString(2), arr.getString(3), arr.getString(4), arr.getString(5), arr.getString(6)));
                else
                    tasks.add(new ScheduledTask(arr.getString(2), arr.getString(3), arr.getString(4), arr.getString(5), arr.getString(6), arr.getString(7)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return tasks;
    }
}
