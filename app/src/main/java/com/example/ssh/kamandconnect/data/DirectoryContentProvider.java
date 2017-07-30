package com.example.ssh.kamandconnect.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

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
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDirectoryHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch (match) {
            case USER_DETAILS:
                returnCursor = db.query(DirectoryContract.UserTable.USER_TABLE,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
                break;
            case USER_DETAILS_WITH_ID:
                String webmail = uri.getPathSegments().get(2);
                String mSelection = DirectoryContract.UserTable.WEBMAIL + "=?";
                String[] mSelectionArgs = new String[]{webmail};
                Log.d("uri", uri.toString());
                Log.d("webmail", webmail);
                returnCursor = db.query(DirectoryContract.UserTable.USER_TABLE,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                Log.d("count", String.valueOf(returnCursor.getCount()));
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDirectoryHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch(match) {
            case USER_DETAILS:
                long id = db.insert(DirectoryContract.UserTable.USER_TABLE, null, contentValues);
                if(id > 0) {
                    //success
                    returnUri = ContentUris.withAppendedId(DirectoryContract.UserTable.CONTENT_URI, id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDirectoryHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsInserted = 0;
        switch (match) {
            case USER_DETAILS:
                db.beginTransaction();
                try {
                    for(ContentValues value : values) {
                        long _id = db.insert(DirectoryContract.UserTable.USER_TABLE, null, value);
                        if(_id != -1)
                            rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }

                if(rowsInserted > 0)
                    getContext().getContentResolver().notifyChange(uri, null);
                return rowsInserted;

            case USER_PHONE:
                db.beginTransaction();
                try {
                    for(ContentValues value : values) {
                        long _id = db.insert(DirectoryContract.UserPhonesTable.USER_PHONES_TABLE, null, value);
                        if(_id != -1)
                            rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                if(rowsInserted > 0)
                    getContext().getContentResolver().notifyChange(uri, null);
                return rowsInserted;

            case USER_POSITIONS:
                db.beginTransaction();
                try {
                    for(ContentValues value : values) {
                        long _id = db.insert(DirectoryContract.UserPositions.USER_POSITIONS_TABLE, null, value);
                        if(_id != -1)
                            rowsInserted++;
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                if(rowsInserted > 0)
                    getContext().getContentResolver().notifyChange(uri, null);
                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
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
