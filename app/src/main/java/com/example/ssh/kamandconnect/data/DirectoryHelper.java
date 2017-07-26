package com.example.ssh.kamandconnect.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DirectoryHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "details.db";
    public static final int DATABASE_VERSION = 1;

    public DirectoryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_USER_TABLE = "CREATE TABLE " + DirectoryContract.UserTable.USER_TABLE + " ( " +
                DirectoryContract.UserTable.FIRST_NAME + " TEXT NOT NULL, " +
                DirectoryContract.UserTable.LAST_NAME + " TEXT NOT NULL, " +
                DirectoryContract.UserTable.ROLL_NO + " TEXT NOT NULL, " +
                DirectoryContract.UserTable.WEBMAIL + " TEXT NOT NULL PRIMARY KEY, " +
                DirectoryContract.UserTable.BATCH + " TEXT NOT NULL, " +
                DirectoryContract.UserTable.STREAM + " TEXT NOT NULL, " +
                DirectoryContract.UserTable.HOSTEL + " TEXT, " +
                DirectoryContract.UserTable.ROOM_NO + " TEXT, " +
                DirectoryContract.UserTable.DESCRIPTION + " TEXT, " +
                DirectoryContract.UserTable.PASSWORD + " TEXT NOT NULL, " +
                DirectoryContract.UserTable.VERIFIED + " flag INTEGER DEFAULT 0" +
                " );";

        final String CREATE_USER_PHONES_TABLE = "CREATE TABLE " + DirectoryContract.UserPhonesTable.USER_PHONES_TABLE + " ( " +
                DirectoryContract.UserPhonesTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DirectoryContract.UserPhonesTable.WEBMAIL + " TEXT NOT NULL, " +
                DirectoryContract.UserPhonesTable.PHONE_NO + " TEXT NOT NULL" +
                " );";

        final String CREATE_USER_POSITIONS_TABLE = "CREATE TABLE " + DirectoryContract.UserPositions.USER_POSITIONS_TABLE + " ( " +
                DirectoryContract.UserPositions.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DirectoryContract.UserPositions.WEBMAIL + " TEXT NOT NULL, " +
                DirectoryContract.UserPositions.POSITION + " TEXT NOT NULL" +
                " );";

        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_PHONES_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_POSITIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE If EXISTS " + DirectoryContract.UserTable.USER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE If EXISTS " + DirectoryContract.UserPositions.USER_POSITIONS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE If EXISTS " + DirectoryContract.UserPhonesTable.USER_PHONES_TABLE);
        onCreate(sqLiteDatabase);
    }
}
