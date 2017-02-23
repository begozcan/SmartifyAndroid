package company.whitespace.smartifyandroid.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.model.Device;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by begum on 23/02/17.
 */
public class DeviceAsyncTask extends NetworkingAsyncTask {
    public DeviceAsyncTask(Context context, String requestLink) {
        super(context, requestLink);
    }

    @Override
    public void onSessionFail() {
        Log.d("DEVICES", result);

    }

    @Override
    public void onSuccess() {
        Log.d("DEVICES", result);

        SharedPreferences sensorSharedPref = context.getSharedPreferences(context.getString(R.string.devices_shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor deviceEditor = sensorSharedPref.edit();

        try {
            JSONArray allDevices = new JSONObject(result).getJSONArray("Data");

            for (int i = 0; i < allDevices.length(); i++) {
                JSONArray array = allDevices.getJSONArray(i);
                deviceEditor.putString(array.getString(1), array.toString());
            }

            deviceEditor.apply();

        }  catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
