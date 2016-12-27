package company.whitespace.smartifyandroid.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.activity.SignupActivity;
import company.whitespace.smartifyandroid.networking.GetUpdatesAsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by begum on 22/12/16.
 */
public class Rooms {
    private static List<Room> rooms;

    public static List<Room> getRooms(Context context) {
        rooms = new ArrayList<Room>();
        Pair<String, String>[] pairs = new Pair[1];
        pairs[0] = new Pair<>("message", "[]");
        new GetUpdatesAsyncTask(context, "messages").execute(pairs);

        SharedPreferences sensorSharedPref = context.getSharedPreferences(context.getString(R.string.sensors_shared_preferences), Context.MODE_PRIVATE);

        for (String key :
                sensorSharedPref.getAll().keySet()) {
            try {
                JSONObject jsonObject = new JSONObject(sensorSharedPref.getString(key,""));

                rooms.add(new Room(jsonObject.getString("name"),String.valueOf(jsonObject.getString("light")), jsonObject.getInt("temp"), jsonObject.getInt("hum")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return rooms;
    }
}
