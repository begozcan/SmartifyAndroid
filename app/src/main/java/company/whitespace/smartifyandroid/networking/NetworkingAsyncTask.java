package company.whitespace.smartifyandroid.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import com.goebl.david.Request;
import com.goebl.david.Webb;
import com.google.gson.Gson;
import company.whitespace.smartifyandroid.R;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by Alchemistake on 24/11/2016.
 */
public abstract class NetworkingAsyncTask extends AsyncTask<Pair<String, String>, Void, Void> {
    protected Context context;
    protected Webb webb;
    protected String requestLink;
    protected String result;
    private boolean error;

    public NetworkingAsyncTask(Context context, String requestLink) {
        this.context = context;
        this.requestLink = requestLink;

        webb = Webb.create();
    }

    @Override
    protected Void doInBackground(Pair<String, String>... pairs) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.networking_shared_preferences), Context.MODE_PRIVATE);
            String session = sharedPreferences.getString("session", "");
            if (session.equals(""))
                onSessionFail();

            Request request = webb.post(context.getString(R.string.domain) + requestLink);
            for (Pair<String, String> pair : pairs) {
                request.param(pair.first, pair.second);
                Log.i("Pair", pair.first + pair.second);
            }
            result = request.asString().getBody();
            if (result == null || result.contentEquals("Session Expired")) {
                onSessionFail();
            } else {
                onSuccess();
            }
        } catch (Exception e) {
            //e.printStackTrace();

            error = true;
        } finally {
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(error){
            Toast.makeText(context, "Please check your Internet Connection.", Toast.LENGTH_LONG).show();
        }

        super.onPostExecute(aVoid);
    }

    public abstract void onSessionFail();

    public abstract void onSuccess();
}
