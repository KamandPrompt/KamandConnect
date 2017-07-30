package com.example.ssh.kamandconnect.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by hitman on 28/07/17.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String USER_DETAILS_URL = "http://192.168.1.101:3000/apikey/users";

    public static URL buildUrlForAll() {
        Uri allUsersUri = Uri.parse(USER_DETAILS_URL);
        try {
            URL allUsersUrl = new URL(allUsersUri.toString());
            Log.v(TAG, "URL: " + allUsersUrl);
            return allUsersUrl;
        } catch(MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}

