package com.example.ssh.kamandconnect;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.example.ssh.kamandconnect.data.DirectoryContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitman on 29/07/17.
 */

public class InsertAllUsersInDbTask extends AsyncTask<String, Void, Boolean> {

    private Context context;
    private View view;

    public InsertAllUsersInDbTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String usersData = strings[0];
        ContentValues[] bulkToInsert;
        List<ContentValues> mUserList = new ArrayList<ContentValues>();
        JSONArray usersJsonData = null;
        try {
            usersJsonData = new JSONArray(usersData);
        } catch(Throwable t) {
            Log.e("Could not parse", usersData);
        }
        if(usersJsonData == null)
            return false;

        //Insert user details into database
        for(int i = 0; i < usersJsonData.length(); ++i) {
            try {
                ContentValues contentValues = new ContentValues();
                JSONObject user = usersJsonData.getJSONObject(i);
                contentValues.put(DirectoryContract.UserTable.FIRST_NAME, user.get("first_name").toString());
                contentValues.put(DirectoryContract.UserTable.LAST_NAME, user.get("last_name").toString());
                contentValues.put(DirectoryContract.UserTable.BATCH, user.get("batch").toString());
                contentValues.put(DirectoryContract.UserTable.WEBMAIL, user.get("webmail").toString());
                contentValues.put(DirectoryContract.UserTable.HOSTEL, user.get("hostel").toString());
                contentValues.put(DirectoryContract.UserTable.ROOM_NO, user.get("room_no").toString());
                contentValues.put(DirectoryContract.UserTable.STREAM, user.get("stream").toString());
                contentValues.put(DirectoryContract.UserTable.ROLL_NO, user.get("roll_no").toString());
                contentValues.put(DirectoryContract.UserTable.PASSWORD, user.get("password").toString());
                mUserList.add(contentValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        bulkToInsert = new ContentValues[mUserList.size()];
        mUserList.toArray(bulkToInsert);
        int rowsInserted = context.getContentResolver().bulkInsert(DirectoryContract.UserTable.CONTENT_URI, bulkToInsert);

        mUserList.clear();

        //Insert phone numbers into database
        for(int i = 0; i < usersJsonData.length(); ++i) {
            try {
                JSONObject user = usersJsonData.getJSONObject(i);
                JSONArray user_phone = user.getJSONArray("phone");
                if(user_phone.length() == 0)
                    continue;
                for(int j = 0; j < user_phone.length(); j++) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DirectoryContract.UserPhonesTable.WEBMAIL, user.get("webmail").toString());
                    contentValues.put(DirectoryContract.UserPhonesTable.PHONE_NO, user_phone.getString(j));
                    mUserList.add(contentValues);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(mUserList.size() > 0) {
            bulkToInsert = new ContentValues[mUserList.size()];
            mUserList.toArray(bulkToInsert);
            int phonesInserted = context.getContentResolver().bulkInsert(DirectoryContract.UserPhonesTable.CONTENT_URI, bulkToInsert);
        }
        mUserList.clear();

        //Insert posisions into database
        for(int i = 0; i < usersJsonData.length(); ++i) {
            try {
                JSONObject user = usersJsonData.getJSONObject(i);
                JSONArray user_positions = user.getJSONArray("position");
                if(user_positions.length() == 0)
                    continue;
                for(int j = 0; j < user_positions.length(); ++j) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DirectoryContract.UserPositions.WEBMAIL, user.get("webmail").toString());
                    contentValues.put(DirectoryContract.UserPositions.POSITION, user_positions.getString(j));
                    mUserList.add(contentValues);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(mUserList.size() > 0) {
            bulkToInsert = new ContentValues[mUserList.size()];
            mUserList.toArray(bulkToInsert);
            context.getContentResolver().bulkInsert(DirectoryContract.UserPositions.CONTENT_URI, bulkToInsert);
        }
        if(rowsInserted > 0)
            return true;
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean) {
            view.findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.pb_loading_indicator).setVisibility(View.INVISIBLE);
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("got_data", true);
            editor.apply();
        }
        else {
            view.findViewById(R.id.pb_loading_indicator).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.network_error_text_view).setVisibility(View.VISIBLE);
            Log.d("not done", "not done");
        }
    }
}
