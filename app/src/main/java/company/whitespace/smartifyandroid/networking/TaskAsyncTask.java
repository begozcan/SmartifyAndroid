package company.whitespace.smartifyandroid.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.fragment.AddDeviceFragment;
import company.whitespace.smartifyandroid.other.DevicesViewAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by begum on 23/02/17.
 */
public class TaskAsyncTask extends NetworkingAsyncTask {
    public TaskAsyncTask(Context context, String requestLink) {
        super(context, requestLink);
    }

    @Override
    public void onSessionFail() {
        Log.d("TASKS", "errr");

    }

    @Override
    public void onSuccess() {
        Log.d("TASKS", result);

        SharedPreferences sensorSharedPref = context.getSharedPreferences(context.getString(R.string.tasks_shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor deviceEditor = sensorSharedPref.edit();
        deviceEditor.clear();

        try {
            JSONArray allDevices = new JSONObject(result).getJSONArray("Data");

            for (int i = 0; i < allDevices.length(); i++) {
                JSONArray array = allDevices.getJSONArray(i);
                deviceEditor.putString(String.valueOf(i), array.toString());
            }

            deviceEditor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
