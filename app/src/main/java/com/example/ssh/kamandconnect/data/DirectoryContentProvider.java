package com.example.ssh.kamandconnect.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by hitman on 28/07/17.
 */

public class DirectoryContentProvider extends ContentProvider {

    private static final int USER_DETAILS = 100;
    private static final int USER_DETAILS_WITH_ID = 101;
    private static final int USER_DETAILS_WITH_NAME = 102;
    private static final int USER_DETAILS_WITH_ROLL_NO = 103;
    private static final int USER_DETAILS_WITH_BATCH = 104;
    private static final int USER_DETAILS_WITH_STREAM = 105;
    private static final int USER_DETAILS_WITH_HOSTEL = 106;
    private static final int USER_DETAILS_WITH_ROOM = 107;
    private static final int USER_DETAILS_WITH_HOSTEL_AND_ROOM = 108;

    private static final int USER_PHONE = 200;
    private static final int USER_PHONE_WITH_ID = 201;
    private static final int USER_PHONE_WITH_NUMBER = 202;

    private static final int USER_POSITIONS = 300;
    private static final int USER_POSITIONS_WITH_ID = 301;
    private static final int USER_POSITIONS_WITH_POSITION = 302;

    private static final UriMatcher sUriMatcher = buildMatcher();

    public static UriMatcher buildMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_DETAILS, USER_DETAILS);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_DETAILS + "/id/*", USER_DETAILS_WITH_ID);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_DETAILS + "/name/*", USER_DETAILS_WITH_NAME);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_DETAILS + "/batch/*", USER_DETAILS_WITH_BATCH);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_DETAILS + "/roll/*", USER_DETAILS_WITH_ROLL_NO);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_DETAILS + "/stream/*", USER_DETAILS_WITH_STREAM);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_DETAILS + "/hostel/*", USER_DETAILS_WITH_HOSTEL);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_DETAILS + "/room/*", USER_DETAILS_WITH_ROOM);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_DETAILS + "/hostel/*/room/*", USER_DETAILS_WITH_HOSTEL_AND_ROOM);

        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_PHONES, USER_PHONE);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_PHONES + "/id/*", USER_PHONE_WITH_ID);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_PHONES + "/phone/*", USER_PHONE_WITH_NUMBER);

        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_POSITIONS, USER_POSITIONS);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_POSITIONS + "/id/*", USER_POSITIONS_WITH_ID);
        uriMatcher.addURI(DirectoryContract.AUTHORITY, DirectoryContract.PATH_USER_POSITIONS + "/positions/*", USER_POSITIONS_WITH_POSITION);

        return uriMatcher;
    }

    private DirectoryHelper mDirectoryHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDirectoryHelper = new DirectoryHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
