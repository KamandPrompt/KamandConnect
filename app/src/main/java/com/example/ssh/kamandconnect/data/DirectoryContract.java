package com.example.ssh.kamandconnect.data;

import android.provider.BaseColumns;

public class DirectoryContract {
    public static final class UserTable implements BaseColumns {
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
        public static final String USER_PHONES_TABLE = "user_phone";
        public static final String ID = "id";
        public static final String WEBMAIL = "webmail";
        public static final String PHONE_NO = "phone";
    }

    public static final class UserPositions implements BaseColumns {
        public static final String USER_POSITIONS_TABLE = "user_positions";
        public static final String ID = "id";
        public static final String WEBMAIL = "webmail";
        public static final String POSITION = "position";
    }
}
