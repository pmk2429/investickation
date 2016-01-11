package com.sfsu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sfsu.entities.Entity;
import com.sfsu.entities.EntityLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavitra on 1/9/2016.
 */
public class LocationsDao implements EntityDao {

    private String TAG = "~!@#$LocationsDao";
    private SQLiteDatabase db;
    private String[] locationEntryArray = new String[]{
            EntityTable.LocationsTable.COLUMN_ID,
            EntityTable.LocationsTable.COLUMN_LATITUDE,
            EntityTable.LocationsTable.COLUMN_LONGITUDE,
            EntityTable.LocationsTable.COLUMN_TIMESTAMP,
            EntityTable.LocationsTable.COLUMN_FK_OBSERVATION_ID,
            EntityTable.LocationsTable.COLUMN_FK_ACTIVITY_ID,
            EntityTable.LocationsTable.COLUMN_FK_USER_ID};

    @Override
    public void setDatabase(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * {@inheritDoc}
     *
     * @param entityItem
     * @return
     */
    @Override
    public long save(Entity entityItem) {
        try {
            Log.d(TAG, "INSERT reached");
            EntityLocation location = (EntityLocation) entityItem;
            ContentValues contentValues = getContentValues(location);
            // save the entity
            return db.insert(EntityTable.LocationsTable.TABLENAME, null, contentValues);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Helper method to build the ContentValues for the EntityLocation object.
     *
     * @param location
     * @return
     */
    private ContentValues getContentValues(EntityLocation location) {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(EntityTable.LocationsTable.COLUMN_ID, location.getId());
            contentValues.put(EntityTable.LocationsTable.COLUMN_LATITUDE, location.getLatitude());
            contentValues.put(EntityTable.LocationsTable.COLUMN_LONGITUDE, location.getLongitude());
            contentValues.put(EntityTable.LocationsTable.COLUMN_TIMESTAMP, location.getTimestamp());
            contentValues.put(EntityTable.LocationsTable.COLUMN_FK_OBSERVATION_ID, location.getObservation_id());
            contentValues.put(EntityTable.LocationsTable.COLUMN_FK_ACTIVITY_ID, location.getActivity_id());
            contentValues.put(EntityTable.LocationsTable.COLUMN_FK_USER_ID, location.getUser_id());

            return contentValues;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean update(Entity entityItem) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return db.delete(EntityTable.LocationsTable.TABLENAME, EntityTable.LocationsTable.COLUMN_ID + "=?", new String[]{id + ""}) > 0;
    }

    @Override
    public EntityLocation get(String id) {
        try {
            EntityLocation location = null;
            Cursor c = db.query(true,
                    EntityTable.LocationsTable.TABLENAME,
                    locationEntryArray,
                    EntityTable.LocationsTable.COLUMN_ID + "=?",
                    new String[]{id + ""},
                    null, null, null, null);

            if (c != null && c.moveToFirst()) {
                location = buildFromCursor(c);
                if (!c.isClosed()) {
                    c.close();
                }
            }
            return location;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T extends Entity> T getByName(String entityName) {
        return null;
    }

    @Override
    public List<? extends Entity> getAll() {
        return null;
    }

    /**
     * Returns list of {@link EntityLocation} for a specific {@link com.sfsu.entities.Activities}
     *
     * @param activityId
     * @return
     */
    public List<? extends Entity> getAll(String activityId) {
        List<EntityLocation> locationList = new ArrayList<EntityLocation>();

        // Query the Database to get all the records.
        Cursor c = db.query(true,
                EntityTable.LocationsTable.TABLENAME, locationEntryArray,
                EntityTable.LocationsTable.COLUMN_FK_ACTIVITY_ID + "=?",
                new String[]{activityId + ""}, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            // loop until the end of Cursor and add each entry to EntityLocation ArrayList.
            do {
                EntityLocation location = buildFromCursor(c);
                if (location != null) {
                    locationList.add(location);
                }
            } while (c.moveToNext());
        }
        return locationList;
    }

    /**
     * Build the {@link EntityLocation} object from Cursor.
     *
     * @param c
     * @return
     */
    public EntityLocation buildFromCursor(Cursor c) {
        EntityLocation location = null;
        if (c != null) {
            location = new EntityLocation();
            location.setId(c.getString(0));
            location.setLatitude(c.getDouble(1));
            location.setLongitude(c.getDouble(2));
            location.setTimestamp(c.getLong(3));
            location.setObservation_id(c.getString(4));
            location.setActivity_id(c.getString(5));
            location.setUser_id(c.getString(6));
        }
        return location;
    }
}
