package com.sfsu.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Pavitra on 1/9/2016.
 */
public class EntityTable {
    /**
     * Schema definition for {@link com.sfsu.entities.Account}
     */
    static class UsersTable {
        // columns
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
        // TAG
        private static final String TAG = "~!@#$UsersTable";

        static public void createTable(SQLiteDatabase db, boolean ifNotExists) {
            try {
                String constraint = ifNotExists ? "IF NOT EXISTS " : "";
                Log.i(TAG, "onCreate of UsersTable");
                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE " + constraint + TABLENAME + " (");
                sb.append(COLUMN_ID + " text unique primary key, ");
                sb.append(COLUMN_FULLNAME + " text not null, ");
                sb.append(COLUMN_EMAIL + " text not null, ");
                sb.append(COLUMN_PASSWORD + " text not null, ");
                sb.append(COLUMN_ADDRESS + " text not null, ");
                sb.append(COLUMN_CITY + " text not null, ");
                sb.append(COLUMN_STATE + " text not null, ");
                sb.append(COLUMN_ZIPCODE + " int not null, ");
                sb.append(COLUMN_CREATEDAT + " long not null); ");
                // execute the statement
                db.execSQL(sb.toString());
            } catch (android.database.SQLException se) {
                Log.i(TAG, se.getMessage());
            }
        }

        static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
            UsersTable.createTable(db, true);
        }
    }


    /**
     * Schema definition for {@link com.sfsu.entities.Tick}
     */
    static class TicksTable {
        static final String TABLENAME = "Ticks";
        static final String COLUMN_ID = "id";
        static final String COLUMN_TICK_NAME = "name";
        static final String COLUMN_TICK_SPECIES = "species";
        static final String COLUMN_KNOWN_FOR = "known_for";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_IMAGE = "image";

        static public void createTable(SQLiteDatabase db, boolean ifNotExists) {
            try {
                String constraint = ifNotExists ? "IF NOT EXISTS " : "";
                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE " + constraint + TicksTable.TABLENAME + " (");
                sb.append(COLUMN_ID + " long primary key autoincrement, ");
                sb.append(COLUMN_TICK_NAME + " text not null, ");
                sb.append(COLUMN_TICK_SPECIES + " text not null, ");
                sb.append(COLUMN_KNOWN_FOR + " text not null, ");
                sb.append(COLUMN_DESCRIPTION + " text not null, ");
                sb.append(COLUMN_IMAGE + " text not null); ");

                db.execSQL(sb.toString());
            } catch (android.database.SQLException se) {
                se.printStackTrace();
            }
        }

        static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TicksTable.TABLENAME);
            TicksTable.createTable(db, false);
        }
    }

    /**
     * Schema definition for {@link com.sfsu.entities.Observation}
     */
    static class ObservationsTable {
        // TAG
        static final String TAG = "~!@#ObservationsTable";
        // columns
        static final String TABLENAME = "Observations";
        static final String COLUMN_ID = "id";
        static final String COLUMN_NUMOFTICKS = "num_of_ticks";
        static final String COLUMN_TIMESTAMP = "timestamp";
        static final String COLUMN_CREATEDAT = "created_at";
        static final String COLUMN_FK_TICK_ID = "tick_id";
        static final String COLUMN_FK_LOCATION_ID = "location_id";
        static final String COLUMN_FK_ACTIVITY_ID = "activity_id";
        static final String COLUMN_FK_USER_ID = "user_id";

        static public void createTable(SQLiteDatabase db, boolean ifNotExists) {
            try {
                String constraint = ifNotExists ? "IF NOT EXISTS " : "";
                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE " + constraint + TABLENAME + " (");
                sb.append(COLUMN_ID + " text primary key, ");
                sb.append(COLUMN_FK_TICK_ID + " text not null, ");
                sb.append(COLUMN_FK_LOCATION_ID + " text not null, ");
                sb.append(COLUMN_NUMOFTICKS + " integer not null, ");
                sb.append(COLUMN_TIMESTAMP + " long not null, ");
                sb.append(COLUMN_CREATEDAT + " long not null); ");

                sb.append("FOREIGN KEY (" + COLUMN_FK_TICK_ID + ") REFERENCES " + TicksTable.TABLENAME +
                        " (" + TicksTable.COLUMN_ID + "), ");

                sb.append("FOREIGN KEY (" + COLUMN_FK_LOCATION_ID + ") REFERENCES " + LocationsTable.TABLENAME +
                        " (" + LocationsTable.COLUMN_ID + "), ");

                sb.append("FOREIGN KEY (" + COLUMN_FK_ACTIVITY_ID + ") REFERENCES " + ActivitiesTable.TABLENAME +
                        " (" + ActivitiesTable.COLUMN_ID + "),");

                sb.append("FOREIGN KEY (" + COLUMN_FK_USER_ID + ") REFERENCES " + UsersTable.TABLENAME +
                        " (" + UsersTable.COLUMN_ID + ") ");

                sb.append(" );");


                db.execSQL(sb.toString());
            } catch (android.database.SQLException se) {
                se.printStackTrace();
            }
        }

        static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ObservationsTable.TABLENAME);
            ObservationsTable.createTable(db, false);
        }
    }

    /**
     * Schema definition for {@link com.sfsu.entities.EntityLocation}
     */
    static class LocationsTable {
        // columns
        static final String TABLENAME = "Locations";
        static final String COLUMN_ID = "id";
        static final String COLUMN_LATITUDE = "latitude";
        static final String COLUMN_LONGITUDE = "longitude";
        static final String COLUMN_TIMESTAMP = "timestamp";
        static final String COLUMN_FK_USER_ID = "user_id";
        //TAG
        static final String TAG = "~!@#$LocationsTable";

        static public void createTable(SQLiteDatabase db, boolean ifNotExists) {
            try {
                String constraint = ifNotExists ? "IF NOT EXISTS " : "";
                StringBuilder sb = new StringBuilder();
                sb.append("CREATE TABLE " + constraint + TABLENAME + " (");
                sb.append(COLUMN_ID + " text unique primary key, ");
                sb.append(COLUMN_LATITUDE + " real not null, ");
                sb.append(COLUMN_LONGITUDE + " real not null, ");
                sb.append(COLUMN_TIMESTAMP + " long not null, ");
                sb.append(COLUMN_FK_USER_ID + " text not null); ");

                db.execSQL(sb.toString());
            } catch (android.database.SQLException se) {
                Log.i(TAG, se.getMessage());
            }
        }

        static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
            TicksTable.createTable(db, false);
        }
    }


    /**
     * Schema definition for {@link com.sfsu.entities.Activities}
     */
    static class ActivitiesTable {
        //TAG
        static final String TAG = "~!@#ActivitiesTable";
        // columns
        static final String TABLENAME = "Activities";
        static final String COLUMN_ID = "id";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_NUM_OF_PEOPLE = "num_of_people";
        static final String COLUMN_NUM_OF_TICKS = "num_of_pets";
        static final String COLUMN_NUM_OF_PETS = "num_of_ticks";
        static final String COLUMN_TIMESTAMP = "timestamp";
        static final String COLUMN_CREATEDAT = "created_at";
        static final String COLUMN_FK_USER_ID = "user_id";
        static final String COLUMN_FK_LOCATION_ID = "location_id";

        static public void createTable(SQLiteDatabase db, boolean ifNotExists) {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE " + TABLENAME + " (");
            sb.append(COLUMN_ID + " text unique primary key, ");
            sb.append(COLUMN_NAME + " text not null, ");
            sb.append(COLUMN_NUM_OF_PEOPLE + " integer not null, ");
            sb.append(COLUMN_NUM_OF_PETS + " integer not null, ");
            sb.append(COLUMN_NUM_OF_TICKS + " integer not null, ");
            sb.append(COLUMN_TIMESTAMP + " long not null, ");
            sb.append(COLUMN_CREATEDAT + " long not null, ");
            sb.append("FOREIGN KEY (" + COLUMN_FK_USER_ID + ") REFERENCES " + UsersTable.TABLENAME +
                    " (" + UsersTable.COLUMN_ID + "),");
            sb.append("FOREIGN KEY (" + COLUMN_FK_LOCATION_ID + ") REFERENCES " + LocationsTable.TABLENAME +
                    " (" + LocationsTable.COLUMN_ID + ") ");
            sb.append(" );");

            try {
                db.execSQL(sb.toString());
            } catch (android.database.SQLException se) {
                se.printStackTrace();
            }
        }

        static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ObservationsTable.TABLENAME);
            ActivitiesTable.createTable(db, false);
        }
    }


}
