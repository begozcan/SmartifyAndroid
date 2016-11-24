package company.whitespace.smartifyandroid.networking;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.goebl.david.Webb;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.activity.LoginActivity;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by Alchemistake on 24/11/2016.
 */
public class LoginAsyncTask extends AsyncTask<String, Void, Boolean> {
    LoginActivity loginActivity;

    public LoginAsyncTask(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        if (strings.length != 2)
            return false;

        try {
            Webb webb = Webb.create();

            CookieManager manager = new CookieManager(new InMemoryCookieStore(), CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(manager);

            JSONObject result = webb.post(loginActivity.getString(R.string.domain))
                    .param("email", strings[0])
                    .param("password", strings[1])
                    .ensureSuccess().asJsonObject().getBody();

            if (result.length() == 0)
                return false;

            loginActivity.setUserInformation(result);

            Gson gson = new Gson();
            JsonElement jsonElement = gson.toJsonTree(manager.getCookieStore());
            jsonElement.getAsJsonObject().remove("lock");

            loginActivity.getSharedPreferences(loginActivity.getString(R.string.networking_shared_preferences), Context.MODE_PRIVATE)
                    .edit().putString("session", gson.toJson(jsonElement)).apply();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        loginActivity.dismissAuthenticationDialog();

        if (result) {
            loginActivity.onLoginSuccess();
        } else {
            loginActivity.onLoginFailed();
        }
    }
}
