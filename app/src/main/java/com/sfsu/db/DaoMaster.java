package com.sfsu.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Manager for all DAOs in the application.
 * <p>
 * Created by Pavitra on 1/9/2016.
 */
public class DaoMaster {

    public static final int SCHEMA_VERSION = 1;


    /**
     * Creates underlying database table using DAOs.
     */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        Log.i("~!@#$", "creating tables");
        EntityTable.UsersTable.createTable(db, ifNotExists);
        EntityTable.ActivitiesTable.createTable(db, ifNotExists);
        EntityTable.LocationsTable.createTable(db, ifNotExists);
        EntityTable.TicksTable.createTable(db, ifNotExists);
        EntityTable.ObservationsTable.createTable(db, ifNotExists);
    }


    /**
     * Upgrades all Tables from oldVersion to newVersion
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public static void upgradeAllTables(SQLiteDatabase db, int oldVersion, int newVersion) {
        EntityTable.UsersTable.onUpgrade(db, oldVersion, newVersion);
        EntityTable.LocationsTable.onUpgrade(db, oldVersion, newVersion);
        EntityTable.TicksTable.onUpgrade(db, oldVersion, newVersion);
        EntityTable.ObservationsTable.onUpgrade(db, oldVersion, newVersion);
        EntityTable.ActivitiesTable.onUpgrade(db, oldVersion, newVersion);
    }

}
