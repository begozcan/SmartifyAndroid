package company.whitespace.smartifyandroid.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import company.whitespace.smartifyandroid.R;
import company.whitespace.smartifyandroid.networking.NetworkingAsyncTask;

public class ConfirmSignupActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmSignupActivity";

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_conf_code) EditText _confCode;
    @Bind(R.id.btn_submit) Button _submitButton;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_signup);
        ButterKnife.bind(this);
        _submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    public void confirm() {
        Log.d(TAG, "Confirm");

        if (!validate()) {
            onSubmitFailed();
            return;
        }

        _submitButton.setEnabled(false);

        progressDialog = new ProgressDialog(ConfirmSignupActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting...");
        progressDialog.show();

        Pair<String, String>[] pairs = new Pair[2];
        pairs[0] = new Pair<>("email", _emailText.getText().toString());
        pairs[1] = new Pair<>("key", _confCode.getText().toString());

        new ConfirmSignupAsyncTask(ConfirmSignupActivity.this, "activate").execute(pairs);
    }

    public void onSubmitSuccess() {
        _submitButton.setEnabled(true);
        startActivity(new Intent(ConfirmSignupActivity.this, LoginActivity.class));
    }

    public void onSubmitFailed() {
        Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();
        if(progressDialog != null)
            progressDialog.dismiss();
        _submitButton.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        if(progressDialog != null)
            progressDialog.dismiss();
        super.onDestroy();
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        return valid;
    }

    private class ConfirmSignupAsyncTask extends NetworkingAsyncTask {

        public ConfirmSignupAsyncTask(Context context, String requestLink) {
            super(context, requestLink);
        }

        @Override
        public void onSessionFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onSubmitFailed();
                }
            });

        }

        @Override
        public void onSuccess() {
            if (result.equals("Activated")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onSubmitSuccess();
                    }
                });
            } else {
                onSessionFail();
            }
        }
    }
}
