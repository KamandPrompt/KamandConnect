package com.example.ssh.kamandconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ssh.kamandconnect.utilities.NetworkUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;

/**
 * Created by hitman on 28/07/17.
 */

public class GetAllUsersTask extends AsyncTask<URL, Void, String> {

    private ProgressBar mLoadingIndicator;
    private LinearLayout mLoginLinearLayout;
    private TextView mNetworkErrorTextView;

    private Context context;
    private View view;

    public GetAllUsersTask(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        mLoginLinearLayout = (LinearLayout) view.findViewById(R.id.login_layout);
        mNetworkErrorTextView = (TextView) view.findViewById(R.id.network_error_text_view);
        mLoginLinearLayout.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL searchUrl = urls[0];
        String userSearchResults = null;
        Log.d("Url", searchUrl.toString());
        try {
            userSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch(IOException e) {
            Log.d("Error", "fetching data");
            e.printStackTrace();
        }
        return userSearchResults;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s != null) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mLoginLinearLayout.setVisibility(View.VISIBLE);
            new InsertAllUsersInDbTask(context, view).execute(s);
            Log.d("Data from net", s);
        }
        else {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mNetworkErrorTextView.setVisibility(View.VISIBLE);
        }
    }
}
