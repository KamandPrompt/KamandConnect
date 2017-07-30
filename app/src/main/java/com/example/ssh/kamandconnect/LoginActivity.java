package com.example.ssh.kamandconnect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View view = this.findViewById(android.R.id.content);

        mLoginEmailEditText = (EditText) findViewById(R.id.login_email_edit_text);
        mLoginPasswordEditText = (EditText) findViewById(R.id.login_password_edit_text);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

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
        } else {
            Cursor cursor = getContentResolver().query(DirectoryContract.UserTable.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);
            while(cursor.moveToNext()) {
                Log.d("cursor", cursor.getString(cursor.getColumnIndex(DirectoryContract.UserTable.WEBMAIL)));
            }
        }

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
