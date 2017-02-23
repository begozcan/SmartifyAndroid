package company.whitespace.smartifyandroid.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.networking.DeviceAsyncTask;
import company.whitespace.smartifyandroid.networking.GetUpdatesAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by begum on 22/12/16.
 */
public class Devices {
    private static List<Device> devices;

    public static List<Device> getDevices(Context context) {
        devices = new ArrayList<Device>();
        Pair<String, String>[] pairs = new Pair[3];
        pairs[0] = new Pair<>("name", "----------");
        pairs[1] = new Pair<>("room", "----------");
        pairs[2] = new Pair<>("type", "----------");
        new DeviceAsyncTask(context, "devices_remove").execute(pairs);

        SharedPreferences deviceSharedPref = context.getSharedPreferences(context.getString(R.string.devices_shared_preferences), Context.MODE_PRIVATE);

        for (String key : deviceSharedPref.getAll().keySet()) {
            try {
                JSONArray arr = new JSONArray(deviceSharedPref.getString(key, ""));
                devices.add(new Device(key, arr.getString(2), arr.getString(0)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return devices;
    }
}
