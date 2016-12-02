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

public class NewPasswordActivity extends AppCompatActivity {

    private static final String TAG = "NewPasswordActivity";

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_conf_code)
    EditText _confCode;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @Bind(R.id.btn_submit)
    Button _submitButton;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        ButterKnife.bind(this);
        _submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(progressDialog != null)
            progressDialog.dismiss();
        super.onDestroy();
    }

    public void submit() {
        Log.d(TAG, "Confirm");

        if (!validate()) {
            onSubmitFailed();
            return;
        }

        _submitButton.setEnabled(false);

        progressDialog = new ProgressDialog(NewPasswordActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting...");
        progressDialog.show();

        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        Pair<String, String>[] pairs = new Pair[4];
        pairs[0] = new Pair<>("email", _emailText.getText().toString());
        pairs[1] = new Pair<>("key", _confCode.getText().toString());
        pairs[2] = new Pair<>("password", password);
        pairs[3] = new Pair<>("password2", reEnterPassword);

        new NewPasswordActivity.NewPasswordAsyncTask(NewPasswordActivity.this, "reactivate").execute(pairs);
    }

    public void onSubmitSuccess() {
        _submitButton.setEnabled(true);
        startActivity(new Intent(NewPasswordActivity.this, LoginActivity.class));
    }

    public void onSubmitFailed() {
        Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();
        if(progressDialog != null)
            progressDialog.dismiss();
        _submitButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Passwords do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }


    private class NewPasswordAsyncTask extends NetworkingAsyncTask {

        public NewPasswordAsyncTask(Context context, String requestLink) {
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
