package com.example.ssh.kamandconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ssh.kamandconnect.data.DirectoryContract;
import com.example.ssh.kamandconnect.data.DirectoryHelper;
import com.example.ssh.kamandconnect.utilities.NetworkUtils;

import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginEmailEditText;
    private EditText mLoginPasswordEditText;

    private Button mLoginButton;
    private Button mSignupButton;

    private SQLiteOpenHelper mDirectoryHelper;
    private SQLiteDatabase mDb;

    private boolean gotDataFromServer;
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View view = this.findViewById(android.R.id.content);

        mLoginEmailEditText = (EditText) findViewById(R.id.login_email_edit_text);
        mLoginPasswordEditText = (EditText) findViewById(R.id.login_password_edit_text);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        isLoggedIn = sharedPreferences.getBoolean(getString(R.string.logged_in), false);
        if(isLoggedIn) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        gotDataFromServer = sharedPreferences.getBoolean("got_data", false);
        if(gotDataFromServer)
            Log.d("Got it", "got it");
        else
            Log.d("not got it", "not got it");

        mLoginButton = (Button) findViewById(R.id.button_login);
        mSignupButton = (Button) findViewById(R.id.button_signup);

        mDirectoryHelper = new DirectoryHelper(getApplicationContext());
        mDb = mDirectoryHelper.getReadableDatabase();

        if(!gotDataFromServer) {
            URL searchUrl = NetworkUtils.buildUrlForAll();
            new GetAllUsersTask(this, view).execute(searchUrl);
        }

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String webmail = mLoginEmailEditText.getText().toString().trim();
                String password = mLoginPasswordEditText.getText().toString();
                boolean isValid = true;
                if(webmail.equals("") || webmail.equals(null)) {
                    mLoginEmailEditText.setError(getString(R.string.empty_field));
                    isValid = false;
                }
                if(password.equals("") || password.equals(null)) {
                    mLoginPasswordEditText.setError(getString(R.string.empty_field));
                    isValid = false;
                }
                if(isValid) {
                    String[] loginInfo = new String[]{webmail, password};
                    new LoginTask().execute(webmail, password);
                }
            }
        });
    }

    public void invalidCredentials() {
        Toast.makeText(this, getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show();
    }

    public void validCredentials(Cursor cursor) {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putBoolean(getString(R.string.logged_in), true);
        editor.putString(getString(R.string.webmail_of_current_user), cursor.getString(cursor.getColumnIndex(DirectoryContract.UserTable.WEBMAIL)));
        editor.commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void notSignedUp() {
        Toast.makeText(this, getString(R.string.signup_first), Toast.LENGTH_LONG).show();
    }

    public class LoginTask extends AsyncTask<String, Void, Cursor> {

        public String webmail;
        public String password;

        @Override
        protected Cursor doInBackground(String... loginInfo) {
            webmail = loginInfo[0];
            password = loginInfo[1];
            Uri uri = Uri.parse(DirectoryContract.UserTable.CONTENT_URI.toString() + "/id/" + webmail);
            Cursor cursor = getContentResolver().query(uri,
                    null,
                    null,
                    null,
                    null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if(cursor == null || cursor.getCount() == 0) {
                invalidCredentials();
                return;
            }
            cursor.moveToNext();
            String dbWebmail = cursor.getString(cursor.getColumnIndex(DirectoryContract.UserTable.WEBMAIL));
            String dbPassword = cursor.getString(cursor.getColumnIndex(DirectoryContract.UserTable.PASSWORD));
            boolean verified = cursor.getInt(cursor.getColumnIndex(DirectoryContract.UserTable.VERIFIED)) > 0;
            if(!verified) {
                notSignedUp();
                return;
            }
            if(!dbWebmail.equals(webmail) || !dbPassword.equals(password)) {
                invalidCredentials();
                return;
            }
            validCredentials(cursor);
        }
    }
}
