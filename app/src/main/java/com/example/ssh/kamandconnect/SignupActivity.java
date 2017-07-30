package com.example.ssh.kamandconnect;

/**
 * Created by hitman on 21/07/17.
 */

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ssh.kamandconnect.data.DirectoryContract;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mOTP;

    private Button verifyButton;
    private Button signupButton;

    private Spinner streamSpinner;
    private Spinner batchSpinner;

    private String verificationCode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirstName = (EditText) findViewById(R.id.first_name_signup);
        mLastName = (EditText) findViewById(R.id.last_name_signup);
        mEmail = (EditText) findViewById(R.id.signup_email_edit_text);
        mPassword = (EditText) findViewById(R.id.signup_password_edit_text);
        mOTP = (EditText) findViewById(R.id.otp_signup);

        verifyButton = (Button) findViewById(R.id.button_verify);
        signupButton = (Button) findViewById(R.id.button_final_signup);

        streamSpinner = (Spinner) findViewById(R.id.stream_spinner);
        batchSpinner = (Spinner) findViewById(R.id.batch_spinner);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = checkSignUp();
                if(!isValid)
                    return;
                String webmail = mEmail.getText().toString();
                new queryWithWebmailTask().execute(webmail);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = mOTP.getText().toString();
                Log.d("verification code", verificationCode);
                if(otp.equals(verificationCode)) {
                    Toast.makeText(view.getContext(), "Verified", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(view.getContext(), "Wrong verification code", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean checkSignUp() {
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String stream = streamSpinner.getSelectedItem().toString();
        String batch = batchSpinner.getSelectedItem().toString();

        boolean isValid = true;

        if(mFirstName.length() == 0) {
            mFirstName.setError(getString(R.string.empty_field));
            isValid = false;
        }
        if(mEmail.length() == 0) {
            mEmail.setError(getString(R.string.empty_field));
            isValid = false;
        }
        if(mLastName.length() == 0) {
            mLastName.setError(getString(R.string.empty_field));
            isValid = false;
        }
        if(mPassword.length() == 0) {
            mPassword.setError(getString(R.string.empty_field));
            isValid = false;
        }
        if(!isValidEmail(email)) {
            mEmail.setError(getString(R.string.email_error));
            isValid = false;
        }
        return isValid;
    }

    public boolean isValidEmail(String email) {
        String pattern = "[a-zA-Z0-9._-]+@students.iitmandi.ac.in";
        return Pattern.matches(pattern, email);
    }

    public String getVerificationCode() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new Random();
        StringBuilder sb = new StringBuilder((1000 + random.nextInt(9000)) + "");
        for (int i = 0; i < 5; i++)
            sb.append(chars[random.nextInt(chars.length)]);
        Log.d("random", sb.toString());
        return sb.toString();
    }

    public void sendMail() {

        String toEmail = mEmail.getText().toString();
        List toEmailList = Arrays.asList(toEmail.split("\\s*,\\s*"));
        verificationCode = getVerificationCode();
        new SendMailTask().execute(getString(R.string.kc_email),
                getString(R.string.kc_password),toEmailList, "Kamand Connect Verification code",
                "Thanks for signing up " + mFirstName.getText().toString() + ". \nYour verification code is - " + verificationCode);
        mEmail.setEnabled(false);
        mOTP.setVisibility(View.VISIBLE);
        signupButton.setVisibility(View.VISIBLE);
        verifyButton.setEnabled(false);
        String verficiation_code_sent_message = getString(R.string.email_sent_message);
        Toast.makeText(getApplicationContext(), verficiation_code_sent_message, Toast.LENGTH_LONG).show();
        mOTP.setFocusable(true);
    }

    public void alreadyPresentEmail() {
        mEmail.setError(getString(R.string.email_present));
        Toast.makeText(this, "Email Already present", Toast.LENGTH_LONG).show();
    }

    public void invalidMail() {
        mEmail.setError(getString(R.string.email_error));
    }

    public class queryWithWebmailTask extends AsyncTask<String, Void, Cursor> {

        @Override
        protected Cursor doInBackground(String... strings) {
            String webmail = strings[0];
            Cursor cursor = null;
            Uri uri = Uri.parse(DirectoryContract.UserTable.CONTENT_URI.buildUpon()
                    .appendPath("id").build().toString() + "/" + webmail);
            try {
                cursor = getContentResolver().query(uri,
                        null,
                        null,
                        null,
                        null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if(cursor != null && cursor.getCount() > 0) {
                cursor.moveToNext();
                if(cursor.getInt(cursor.getColumnIndex(DirectoryContract.UserTable.VERIFIED)) > 0) {
                    alreadyPresentEmail();
                }
                else {
                    sendMail();
                }
            }
            else
                invalidMail();
        }
    }
}
