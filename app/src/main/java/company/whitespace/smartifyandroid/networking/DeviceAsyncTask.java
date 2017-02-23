package company.whitespace.smartifyandroid.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.fragment.AddDeviceFragment;
import company.whitespace.smartifyandroid.model.Device;
import company.whitespace.smartifyandroid.other.DevicesViewAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by begum on 23/02/17.
 */
public class DeviceAsyncTask extends NetworkingAsyncTask {
    private AddDeviceFragment addDeviceFragment;
    private DevicesViewAdapter devicesViewAdapter;

    public DeviceAsyncTask(Context context, String requestLink) {
        super(context, requestLink);
        addDeviceFragment = null;
        devicesViewAdapter = null;
    }

    public void setAddDeviceFragment(AddDeviceFragment addDeviceFragment) {
        this.addDeviceFragment = addDeviceFragment;
    }

    public void setDevicesViewAdapter(DevicesViewAdapter devicesViewAdapter) {
        this.devicesViewAdapter = devicesViewAdapter;
    }

    @Override
    public void onSessionFail() {
        Log.d("DEVICES", "errr");

    }

    @Override
    public void onSuccess() {
        Log.d("DEVICES", result);

        SharedPreferences sensorSharedPref = context.getSharedPreferences(context.getString(R.string.devices_shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor deviceEditor = sensorSharedPref.edit();
        deviceEditor.clear();

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

        if(addDeviceFragment != null){
            addDeviceFragment.onSuccess();
        }

        if(devicesViewAdapter != null){
            devicesViewAdapter.onSuccess();
        }
    }
}
