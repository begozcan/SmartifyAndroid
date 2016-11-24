package company.whitespace.smartifyandroid.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import company.whitespace.smartifyandroid.R;

import butterknife.ButterKnife;
import butterknife.Bind;


public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_surname) EditText _surnameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.btn_submit) Button _submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        _submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    public void submit() {
        Log.d(TAG, "Submit");

        if (!validate()) {
            onSubmitFailed();
            return;
        }

        _submitButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ForgotPasswordActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onSubmitSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSubmitSuccess() {
        _submitButton.setEnabled(true);
        startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
    }

    public void onSubmitFailed() {
        Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();

        _submitButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String email = _emailText.getText().toString();


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
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        return valid;
    }

 }
