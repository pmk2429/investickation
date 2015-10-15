package com.sfsu.model;

import android.database.sqlite.SQLiteDatabase;

/**
 * SQLite Ticks Table Schema definition.
 * Created by Pavitra on 10/9/2015.
 */
public class TicksTable {
    static final String TABLENAME = "Ticks";
    static final String COLUMN_ID = "tick_id";
    static final String COLUMN_TICK_NAME = "name";
    static final String COLUMN_TICK_SPECIES = "species";
    static final String COLUMN_KNOWN_FOR = "known_for";
    static final String COLUMN_DESCRIPTION = "description";
    static final String COLUMN_IMAGE = "image";
    static final String COLUMN_CREATEDAT = "created_at";
    static final String COLUMN_UPDATEDAT = "updated_at";

    static public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TicksTable.TABLENAME + " (");
        sb.append(COLUMN_ID + " integer primary key autoincrement, ");
        sb.append(COLUMN_TICK_NAME + " text not null, ");
        sb.append(COLUMN_TICK_SPECIES + " text not null, ");
        sb.append(COLUMN_KNOWN_FOR + " text not null, ");
        sb.append(COLUMN_DESCRIPTION + " text not null, ");
        sb.append(COLUMN_IMAGE + " text not null, ");
        sb.append(COLUMN_CREATEDAT + " long not null, ");
        sb.append(COLUMN_UPDATEDAT + " long not null); ");

        try {
            db.execSQL(sb.toString());
        } catch (android.database.SQLException se) {
            se.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TicksTable.TABLENAME);
        TicksTable.onCreate(db);
    }
}
