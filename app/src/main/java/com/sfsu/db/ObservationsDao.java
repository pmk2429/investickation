package com.sfsu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sfsu.entities.Entity;
import com.sfsu.entities.Observation;

import java.util.ArrayList;
import java.util.List;

/**
 * ObservationsDao for providing abstraction layer over DB. Since the Observation entity is a composite object the entire logic
 * for building up the composite object is handled by the DAO layer. The DAO layer is used for getting the data from the
 * Database and then build up the Observation object.
 * <p>
 * Created by Pavitra on 6/3/2015.
 */
public class ObservationsDao implements EntityDao {
    private static ObservationsDao mInstance = new ObservationsDao();
    private final String TAG = "~!@#$ObsDao";
    private SQLiteDatabase db;
    // Observation's entry array for storing all the column names
    private String[] observationEntryArray = new String[]{
            EntityTable.ObservationsTable.COLUMN_ID,
            EntityTable.ObservationsTable.COLUMN_NAME,
            EntityTable.ObservationsTable.COLUMN_SPECIES,
            EntityTable.ObservationsTable.COLUMN_NUMOFTICKS,
            EntityTable.ObservationsTable.COLUMN_TIMESTAMP,
            EntityTable.ObservationsTable.COLUMN_IMAGE_URL,
            EntityTable.ObservationsTable.COLUMN_DESCRIPTION,
            EntityTable.ObservationsTable.COLUMN_LATITUDE,
            EntityTable.ObservationsTable.COLUMN_LONGITUDE,
            EntityTable.ObservationsTable.COLUMN_FK_TICK_ID,
            EntityTable.ObservationsTable.COLUMN_FK_ACTIVITY_ID,
            EntityTable.ObservationsTable.COLUMN_FK_USER_ID
    };


    private ObservationsDao() {
    }

    public static ObservationsDao getInstance() {
        return mInstance;
    }

    @Override
    public void setDatabase(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Deletes the {@link Observation} entry from the Table.
     *
     * @param observations
     * @return
     */
    @Override
    public boolean delete(String id) {
        try {
            return db.delete(EntityTable.ObservationsTable.TABLENAME,
                    EntityTable.ObservationsTable.COLUMN_ID + "=?",
                    new String[]{id + ""}) > 0;
        } catch (Exception e) {
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * get specific observation using ID.
     *
     * @param id
     * @return
     */
    public Entity get(String id) {

        Observation observationItem = null;
        try {
            Cursor c = db.query(true, EntityTable.ObservationsTable.TABLENAME,
                    observationEntryArray,
                    EntityTable.ObservationsTable.COLUMN_ID + "=?",
                    new String[]{id},
                    null, null, null, null);

            if (c != null && c.moveToFirst()) {
                // once the Observation Item is build from cursor, create Tick object and Location object.
                observationItem = buildFromCursor(c);
                if (!c.isClosed()) {
                    c.close();
                }
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }
        // the final ObservationItem will contain the Composite Object composed of Location and Tick objects.
        return observationItem;
    }

    @Override
    public Entity getByName(String entityName) {
        return null;
    }

    /**
     * Returns all the {@link Observation} from the DB
     *
     * @return
     */
    public List<Observation> getAll() {
        List<Observation> observationsList = new ArrayList<Observation>();
        try {
            // Query the Database to get all the records.
            Cursor c = db.query(EntityTable.ObservationsTable.TABLENAME, observationEntryArray, null, null, null, null, null);

            if (c != null && c.moveToFirst()) {
                // loop until the end of Cursor and add each entry to Observations ArrayList.
                do {
                    Observation observationItem = buildFromCursor(c);
                    if (observationItem != null) {
                        observationsList.add(observationItem);
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
        } finally {
            db.close();
        }
        return observationsList;
    }


    /**
     * Returns all the {@link Observation} for the ActivityId passed
     *
     * @return
     */
    public List<Observation> getAll(String activityId) {
        List<Observation> observationsList = new ArrayList<Observation>();
        try {
            // Query the Database to get all the records.
            Cursor c = db.query(
                    EntityTable.ObservationsTable.TABLENAME,
                    observationEntryArray,
                    EntityTable.ObservationsTable.COLUMN_FK_ACTIVITY_ID + "=?",
                    new String[]{activityId},
                    null, null, null, null);

            if (c != null && c.moveToFirst()) {
                // loop until the end of Cursor and add each entry to Observations ArrayList.
                do {
                    Observation observationItem = buildFromCursor(c);
                    if (observationItem != null) {
                        observationsList.add(observationItem);
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
        } finally {
            db.close();
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
        try {
            Observation observations = (Observation) entity;
            ContentValues contentValues = new ContentValues();
            contentValues.put(EntityTable.ObservationsTable.COLUMN_ID, observations.getId());
            contentValues.put(EntityTable.ObservationsTable.COLUMN_NAME, observations.getTickName());
            contentValues.put(EntityTable.ObservationsTable.COLUMN_SPECIES, observations.getSpecies());
            contentValues.put(EntityTable.ObservationsTable.COLUMN_NUMOFTICKS, observations.getNum_ticks());
            contentValues.put(EntityTable.ObservationsTable.COLUMN_TIMESTAMP, observations.getTimestamp());
            contentValues.put(EntityTable.ObservationsTable.COLUMN_LATITUDE, observations.getLatitude());
            contentValues.put(EntityTable.ObservationsTable.COLUMN_LONGITUDE, observations.getLongitude());
            contentValues.put(EntityTable.ObservationsTable.COLUMN_IMAGE_URL, observations.getImageUrl());
            contentValues.put(EntityTable.ObservationsTable.COLUMN_DESCRIPTION, observations.getDescription());
            contentValues.put(EntityTable.ObservationsTable.COLUMN_FK_TICK_ID, observations.getTick_id());
            contentValues.put(EntityTable.ObservationsTable.COLUMN_FK_USER_ID, observations.getUser_id());
            contentValues.put(EntityTable.ObservationsTable.COLUMN_FK_ACTIVITY_ID, observations.getActivity_id());
            // insert entry into Table.
            return db.insert(EntityTable.ObservationsTable.TABLENAME, null, contentValues);
        } catch (Exception e) {
            return -1;
        } finally {
            db.close();
        }
    }

    /**
     * This method is used to update the entries in Observation Table
     *
     * @param observations
     * @return
     */
    @Override
    public boolean update(String id, Entity entity) {
        Observation observations = (Observation) entity;
        ContentValues contentValues = new ContentValues();
        contentValues.put(EntityTable.ObservationsTable.COLUMN_ID, observations.getId());
        contentValues.put(EntityTable.ObservationsTable.COLUMN_NAME, observations.getTickName());
        contentValues.put(EntityTable.ObservationsTable.COLUMN_SPECIES, observations.getSpecies());
        contentValues.put(EntityTable.ObservationsTable.COLUMN_NUMOFTICKS, observations.getNum_ticks());
        contentValues.put(EntityTable.ObservationsTable.COLUMN_IMAGE_URL, observations.getImageUrl());
        contentValues.put(EntityTable.ObservationsTable.COLUMN_DESCRIPTION, observations.getDescription());
        contentValues.put(EntityTable.ObservationsTable.COLUMN_TIMESTAMP, observations.getTimestamp());
        contentValues.put(EntityTable.ObservationsTable.COLUMN_LATITUDE, observations.getLatitude());
        contentValues.put(EntityTable.ObservationsTable.COLUMN_LONGITUDE, observations.getLongitude());
        contentValues.put(EntityTable.ObservationsTable.COLUMN_FK_TICK_ID, observations.getTick_id());
        contentValues.put(EntityTable.ObservationsTable.COLUMN_FK_USER_ID, observations.getUser_id());
        contentValues.put(EntityTable.ObservationsTable.COLUMN_FK_ACTIVITY_ID, observations.getActivity_id());
        // update the entry.
        return db.update(EntityTable.ObservationsTable.TABLENAME, contentValues, EntityTable.ObservationsTable.COLUMN_ID + "=?", new
                String[]{observations.getId() + ""}) > 0;
    }

    // build the Observation Object using Cursor.
    public Observation buildFromCursor(Cursor c) {
        Observation observationItem = null;
        if (c != null) {
            observationItem = new Observation();
            observationItem.setId(c.getString(0));
            observationItem.setTickName(c.getString(1));
            observationItem.setSpecies(c.getString(2));
            observationItem.setNum_ticks(c.getInt(3));
            observationItem.setTimestamp(c.getLong(4));
            observationItem.setImageUrl(c.getString(5));
            observationItem.setDescription(c.getString(6));
            observationItem.setLatitude(c.getDouble(7));
            observationItem.setLongitude(c.getDouble(8));
            observationItem.setTick_id(c.getString(9));
            observationItem.setActivity_id(c.getString(10));
            observationItem.setUser_id(c.getString(11));
        }

        return observationItem;
    }

    /**
     * Method to build the Tick Object and Location Object.
     */
}
