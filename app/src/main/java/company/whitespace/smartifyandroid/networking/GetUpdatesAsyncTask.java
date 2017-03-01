package company.whitespace.smartifyandroid.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import company.whitespace.smartifyandroid.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by begum on 26/12/16.
 */
public class GetUpdatesAsyncTask extends NetworkingAsyncTask {

    public GetUpdatesAsyncTask(Context context, String requestLink) {
        super(context, requestLink);
    }

    @Override
    public void onSessionFail() {
        Log.d("UPDATE", "Failed");
    }

    @Override
    public void onSuccess() {
        Log.d("UPDATE", "Success");

        SharedPreferences sensorSharedPref = context.getSharedPreferences(context.getString(R.string.sensors_shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor sensorEditor = sensorSharedPref.edit();

        try {
            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject message = jsonArray.getJSONArray(i).getJSONObject(1);

                switch (message.getString("Header")) {
                    case "sensor_values":
                        sensorEditor.putString(message.getJSONObject("Data").getString("name"), message.getString("Data"));
                        break;
                    //TODO: Add device things
                }

            }

            sensorEditor.apply();


        } catch (JSONException e) {
            e.printStackTrace();
        }

//        if (result.length !)
//        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.devices_shared_preferences), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        //editor.putString();
//        editor.commit();


    }
}
