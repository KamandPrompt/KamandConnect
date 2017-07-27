package com.example.ssh.kamandconnect.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class DirectoryContract {

    public static final String AUTHORITY = "com.example.ssh.kamandconnect";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_USER_DETAILS = "details";
    public static final String PATH_USER_PHONES = "phones";
    public static final String PATH_USER_POSITIONS = "positions";

    public static final class UserTable implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_DETAILS).build();

        public static final String USER_TABLE = "user";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String ROLL_NO = "roll_no";
        public static final String WEBMAIL = "webmail";
        public static final String BATCH = "batch";
        public static final String STREAM = "stream";
        public static final String DESCRIPTION = "description";
        public static final String PASSWORD = "password";
        public static final String VERIFIED = "verified";
        public static final String HOSTEL = "hostel";
        public static final String ROOM_NO = "room_no";
    }

    public static final class UserPhonesTable implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_PHONES).build();

        public static final String USER_PHONES_TABLE = "user_phone";
        public static final String ID = "id";
        public static final String WEBMAIL = "webmail";
        public static final String PHONE_NO = "phone";
    }

    public static final class UserPositions implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER_POSITIONS).build();
        public static final String USER_POSITIONS_TABLE = "user_positions";
        public static final String ID = "id";
        public static final String WEBMAIL = "webmail";
        public static final String POSITION = "position";
    }
}
