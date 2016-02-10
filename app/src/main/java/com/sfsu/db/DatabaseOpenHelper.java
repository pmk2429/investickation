package com.sfsu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DatabaseHelper class to create and update the database table.
 * <p/>
 * Created by Pavitra on 5/27/2015.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    // specify the name and version of DB.
    protected static final String DB_NAME = "Investickations.db";
    protected static final String TAG = "~!@#$DBOpenHelper";
    protected static final int DB_VERSION = 4;

    // the default constructor
    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * {@inheritDoc}
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "creating tables");
        boolean ifNotExists = true;
        EntityTable.UsersTable.createTable(sqLiteDatabase, ifNotExists);
        EntityTable.ActivitiesTable.createTable(sqLiteDatabase, ifNotExists);
        EntityTable.LocationsTable.createTable(sqLiteDatabase, ifNotExists);
        EntityTable.TicksTable.createTable(sqLiteDatabase, ifNotExists);
        EntityTable.ObservationsTable.createTable(sqLiteDatabase, ifNotExists);
    }


    /**
     * {@inheritDoc}
     *
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        DaoMaster.upgradeAllTables(sqLiteDatabase, oldVersion, newVersion);
    }
}
