package company.whitespace.smartifyandroid.networking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
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
    Context context;
    Webb webb;
    String requestLink;
    String result;

    public NetworkingAsyncTask(Context context, String requestLink) {
        this.context = context;
        this.requestLink = requestLink;

        webb = Webb.create();
    }

    @Override
    protected Void doInBackground(Pair<String, String>... pairs) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.networking_shared_preferences), Context.MODE_PRIVATE);
        String session = sharedPreferences.getString("session", "");
        if (session.equals(""))
            onSessionFail();

        Request request = webb.post(context.getString(R.string.domain) + '/' + requestLink);
        for (Pair<String, String> pair : pairs
                ) {
            request = request.param(pair.first, pair.second);
        }
        result = request.ensureSuccess().asString().getBody();
        if (result.contentEquals("Session Expired")) {
            onSessionFail();
        } else {
            onSuccess();
        }

        return null;
    }

    public abstract void onSessionFail();

    public abstract void onSuccess();
}
