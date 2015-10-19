package com.sfsu.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sfsu.entities.AppConfig;
import com.sfsu.entities.Entity;
import com.sfsu.entities.Observation;

import java.util.ArrayList;
import java.util.List;

/**
 * ObservationsDao for providing abstraction layer over DB.
 * Created by Pavitra on 6/3/2015.
 */
public class ObservationsDao implements EntityDao {
    private SQLiteDatabase db;
    // Observations's entry array for storing all the column names
    private String[] observationEntryArray = new String[]{ObservationsTable.COLUMN_ID, ObservationsTable.COLUMN_NUMOFTICKS, ObservationsTable.COLUMN_TICK_IMAGE, ObservationsTable.COLUMN_LAT, ObservationsTable.COLUMN_LONG, ObservationsTable.COLUMN_TIMESTAMP, ObservationsTable.COLUMN_CREATEDAT, ObservationsTable.COLUMN_UPDATEDAT};


    @Override
    public void setDatabase(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Delete the Observation entry from the Table.
     *
     * @param observations
     * @return
     */
    public boolean delete(Entity entity) {
        Observation observations = (Observation) entity;

        return db.delete(ObservationsTable.TABLENAME, ObservationsTable.COLUMN_ID + "=?", new String[]{observations.getObservation_id() + ""}) > 0;
    }

    /**
     * get specific observation using ID.
     *
     * @param id
     * @return
     */
    public Entity get(long id) {

        Observation observationItem = null;
        Cursor c = db.query(true, ObservationsTable.TABLENAME, observationEntryArray, ObservationsTable.COLUMN_ID + "=?", new String[]{id + ""}, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            observationItem = buildFromCursor(c);
            if (!c.isClosed()) {
                c.close();
            }
        }
        return observationItem;
    }

    /**
     * Method to get all the Observations from the DB
     *
     * @return
     */
    public List<Entity> getAll() {
        List<Entity> observationsList = new ArrayList<Entity>();

        // Query the Database to get all the records.
        Cursor c = db.query(ObservationsTable.TABLENAME, observationEntryArray, null, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            // loop until the end of Cursor and add each entry to Observations ArrayList.
            do {
                Observation observationItem = buildFromCursor(c);
                if (observationItem != null) {
                    observationsList.add(observationItem);
                }
            } while (c.moveToNext());
        }
        return observationsList;
    }

    /**
     * This method is used to save the entries (field values) in to Observation Database table
     *
     * @param observations
     * @return
     */
    public long save(Entity entity) {
        Observation observations = (Observation) entity;
        ContentValues contentValues = new ContentValues();
        contentValues.put(ObservationsTable.COLUMN_ID, observations.getObservation_id());
        contentValues.put(ObservationsTable.COLUMN_NUMOFTICKS, observations.getNum_ticks());
        contentValues.put(ObservationsTable.COLUMN_TICK_IMAGE, observations.getTickImageUrl());
        contentValues.put(ObservationsTable.COLUMN_LAT, observations.getLatitude());
        contentValues.put(ObservationsTable.COLUMN_LONG, observations.getLongitude());
        contentValues.put(ObservationsTable.COLUMN_TIMESTAMP, observations.getTimestamp());
        contentValues.put(ObservationsTable.COLUMN_CREATEDAT, observations.getCreated_at());
        contentValues.put(ObservationsTable.COLUMN_UPDATEDAT, observations.getUpdated_at());
        Log.d(AppConfig.LOGSTRING, "Observation : INSERT reached");
        return db.insert(ObservationsTable.TABLENAME, null, contentValues);
    }

    /**
     * This method is used to update the entries in Observation Table
     *
     * @param observations
     * @return
     */
    public boolean update(Entity entity) {
        Observation observations = (Observation) entity;
        ContentValues contentValues = new ContentValues();
        contentValues.put(ObservationsTable.COLUMN_ID, observations.getObservation_id());
        contentValues.put(ObservationsTable.COLUMN_NUMOFTICKS, observations.getNum_ticks());
        contentValues.put(ObservationsTable.COLUMN_TICK_IMAGE, observations.getTickImageUrl());
        contentValues.put(ObservationsTable.COLUMN_LAT, observations.getLatitude());
        contentValues.put(ObservationsTable.COLUMN_LONG, observations.getLongitude());
        contentValues.put(ObservationsTable.COLUMN_TIMESTAMP, observations.getTimestamp());
        contentValues.put(ObservationsTable.COLUMN_CREATEDAT, observations.getCreated_at());
        contentValues.put(ObservationsTable.COLUMN_UPDATEDAT, observations.getUpdated_at());
        Log.d(AppConfig.LOGSTRING, "Observation : UPDATE reached");
        // the db.update() method will return INT for number of rows updated. and so return db.update()>0 will check
        // for whether its true or false.
        return db.update(ObservationsTable.TABLENAME, contentValues, ObservationsTable.COLUMN_ID + "=?", new String[]{observations.getObservation_id() + ""}) > 0;
    }

    // build the Observation Object using Cursor.
    public Observation buildFromCursor(Cursor c) {
        Observation observationItem = null;
        if (c != null) {
            observationItem = new Observation();
            observationItem.setObservation_id(c.getLong(0));
            observationItem.setNum_ticks(c.getInt(1));
            observationItem.setTickImageUrl(c.getString(2));
            observationItem.setLatitude(c.getDouble(3));
            observationItem.setLongitude(c.getDouble(4));
            observationItem.setTimestamp(c.getLong(5));
            observationItem.setCreated_at(c.getLong(6));
            observationItem.setUpdated_at(c.getLong(7));
        }
        return observationItem;
    }
}
