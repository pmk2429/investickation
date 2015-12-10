package com.sfsu.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Pavitra on 6/3/2015.
 */
public class UsersTable {
    static final String TABLENAME = "Users";
    static final String COLUMN_ID = "id";
    static final String COLUMN_FULLNAME = "full_name";
    static final String COLUMN_EMAIL = "email";
    static final String COLUMN_PASSWORD = "password";
    static final String COLUMN_ADDRESS = "address";
    static final String COLUMN_CITY = "city";
    static final String COLUMN_STATE = "state";
    static final String COLUMN_ZIPCODE = "zipcode";
    static final String COLUMN_CREATEDAT = "created_at";
    static final String COLUMN_UPDATEDAT = "updated_at";

    static public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + UsersTable.TABLENAME + " (");
        sb.append(COLUMN_ID + " text unique primary key, ");
        sb.append(COLUMN_FULLNAME + " text not null, ");
        sb.append(COLUMN_EMAIL + " text not null, ");
        sb.append(COLUMN_PASSWORD + " text not null, ");
        sb.append(COLUMN_ADDRESS + " text not null, ");
        sb.append(COLUMN_CITY + " text not null, ");
        sb.append(COLUMN_STATE + " text not null, ");
        sb.append(COLUMN_ZIPCODE + " int not null, ");
        sb.append(COLUMN_CREATEDAT + " long not null, ");
        sb.append(COLUMN_UPDATEDAT + " long not null); ");

        try {
            db.execSQL(sb.toString());
        } catch (android.database.SQLException se) {
            se.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UsersTable.TABLENAME);
        UsersTable.onCreate(db);
    }
}
