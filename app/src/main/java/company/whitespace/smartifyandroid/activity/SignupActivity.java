package company.whitespace.smartifyandroid.activity;

        import android.app.Dialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Looper;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.util.Pair;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.util.Pair;

        import butterknife.ButterKnife;
        import butterknife.Bind;
        import company.whitespace.smartifyandroid.R;
        import company.whitespace.smartifyandroid.networking.NetworkingAsyncTask;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_surname) EditText _surnameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_reEnterEmail) EditText _reEnterEmailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null)
            progressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String email = _emailText.getText().toString();
        String reEnterEmail = _reEnterEmailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        Pair<String, String>[] pairs = new Pair[6];
        pairs[0] = new Pair<>("email", email);
        pairs[1] = new Pair<>("email2", reEnterEmail);
        pairs[2] = new Pair<>("password", password);
        pairs[3] = new Pair<>("password2", reEnterPassword);
        pairs[4] = new Pair<>("name", name);
        pairs[5] = new Pair<>("surname", surname);

        new SignupAsyncTask(SignupActivity.this, "signup").execute(pairs);

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
    }

    public void onSignupFailed() {
        progressDialog.dismiss();
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String email = _emailText.getText().toString();
        String reEnterEmail = _reEnterEmailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (surname.isEmpty() || surname.length() < 3) {
            _surnameText.setError("At least 3 characters");
            valid = false;
        } else {
            _surnameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (reEnterEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(reEnterEmail).matches() || !(reEnterEmail.equals(email))) {
            Log.i(TAG, "validate: ");
            _reEnterEmailText.setError("Emails do not match");
            valid = false;
        } else {
            _reEnterEmailText.setError(null);
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

    private class SignupAsyncTask extends NetworkingAsyncTask {

        public SignupAsyncTask(Context context, String requestLink) {
            super(context, requestLink);
        }

        @Override
        public void onSessionFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onSignupFailed();
                }
            });

        }

        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onSignupSuccess();
                }
            });
        }
    }
}
