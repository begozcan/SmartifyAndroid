package company.whitespace.smartifyandroid.networking;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import com.google.gson.Gson;
import company.whitespace.smartifyandroid.R;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by Alchemistake on 24/11/2016.
 */
public class NetworkingAsyncTask extends AsyncTask<Pair<String, String>, Void, Void> {
    NetworkRelated networkRelated;
    Context context;

    public NetworkingAsyncTask(NetworkRelated networkRelated, Context context) {
        this.networkRelated = networkRelated;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Pair<String, String>... pairs) {
        try {
            Gson gson = new Gson();
            String session =
                    context.getSharedPreferences(
                            context.getString(R.string.networking_shared_preferences), Context.MODE_PRIVATE)
                            .getString("session", "");
            if (session.equals("")) {
                // TODO login!
                return null;
            }
            InMemoryCookieStore inMemoryCookieStore = gson.fromJson(session, InMemoryCookieStore.class);
            CookieManager manager = new CookieManager(inMemoryCookieStore, CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(manager);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        networkRelated.doOnPostExecute();
    }
}
