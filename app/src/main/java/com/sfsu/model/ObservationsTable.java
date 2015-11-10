package com.sfsu.model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Pavitra on 6/3/2015.
 */
public class ObservationsTable {
    static final String TABLENAME = "Observations";
    static final String COLUMN_ID = "observation_id";
    static final String COLUMN_NUMOFTICKS = "no_of_ticks";
    static final String COLUMN_TIMESTAMP = "timestamp";
    static final String COLUMN_CREATEDAT = "created_at";
    static final String COLUMN_UPDATEDAT = "updated_at";
    static final String COLUMN_FK_TICK_ID = "fk_tick_id";
    static final String COLUMN_FK_LOCATION_ID = "fk_location_id";

    static public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + ObservationsTable.TABLENAME + " (");
        sb.append(COLUMN_ID + " integer primary key autoincrement, ");
        sb.append(COLUMN_FK_TICK_ID + " integer, ");
        sb.append(COLUMN_FK_LOCATION_ID + " integer, ");
        sb.append(COLUMN_NUMOFTICKS + " integer not null, ");
        sb.append(COLUMN_TIMESTAMP + " long not null, ");
        sb.append(COLUMN_CREATEDAT + " long not null, ");
        sb.append(COLUMN_UPDATEDAT + " long not null, ");
        sb.append("FOREIGN KEY (" + COLUMN_FK_TICK_ID + ") REFERENCES " + TicksTable.TABLENAME + " (" + TicksTable.COLUMN_ID +
                "),");
        sb.append("FOREIGN KEY (" + COLUMN_FK_LOCATION_ID + ") REFERENCES " + LocationsTable.TABLENAME + " (" + LocationsTable
                .COLUMN_ID + ")");
        sb.append(" );");

        try {
            db.execSQL(sb.toString());
        } catch (android.database.SQLException se) {
            se.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ObservationsTable.TABLENAME);
        ObservationsTable.onCreate(db);
    }
}
