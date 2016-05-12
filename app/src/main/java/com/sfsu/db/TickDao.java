package com.sfsu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

import com.sfsu.entities.Entity;
import com.sfsu.entities.Tick;

import java.util.ArrayList;
import java.util.List;

/**
 * Dao for Ticks related DB operation and data manipulation.
 * Created by Pavitra on 10/8/2015.
 */
public class TickDao implements EntityDao {
    // Singleton pattern
    private static final TickDao mInstance = new TickDao();
    private static final String TAG = "~!@#$TickDao";
    private SQLiteDatabase db;
    private String[] tickEntryArray = new String[]{
            EntityTable.TicksTable.COLUMN_ID,
            EntityTable.TicksTable.COLUMN_TICK_NAME,
            EntityTable.TicksTable.COLUMN_TICK_SCIENTIFIC_NAME,
            EntityTable.TicksTable.COLUMN_TICK_SPECIES,
            EntityTable.TicksTable.COLUMN_KNOWN_FOR,
            EntityTable.TicksTable.COLUMN_DESCRIPTION,
            EntityTable.TicksTable.COLUMN_FOUND_NEAR,
            EntityTable.TicksTable.COLUMN_IMAGE,
            EntityTable.TicksTable.COLUMN_CREATED_AT,
            EntityTable.TicksTable.COLUMN_UPDATED_AT};

    /**
     * Required
     */
    private TickDao() {
    }

    /**
     * Returns static singleton instance of TickDao
     *
     * @return
     */
    public static TickDao getInstance() {
        return mInstance;
    }

    @Override
    public void setDatabase(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * save(Tick) method is used to save the entries (field values) in to Tick Database table
     *
     * @param ticks
     * @return
     */
    public long save(Entity entity) {
        long isSaved = 0;
        try {
            Tick tick = (Tick) entity;
            ContentValues contentValues = getContentValues(tick);
            // save the entity
            isSaved = db.insert(EntityTable.TicksTable.TABLENAME, null, contentValues);
        } catch (SQLiteException se) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "save: ", se);
            isSaved = -1;
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "save: ", e);
            isSaved = -1;
        }
        return isSaved;
    }

    /**
     * save(Tick) method is used to save the entries (field values) in to Tick Database table
     *
     * @param ticks
     * @return
     */
    public long save(List<Tick> tickList) {
        long isSaved = 0;
        try {
            for (int i = 0; i < tickList.size(); i++) {
                Tick tick = tickList.get(i);
                ContentValues contentValues = getContentValues(tick);
                db.insert(EntityTable.TicksTable.TABLENAME, null, contentValues);
            }
            isSaved = tickList.size();
        } catch (SQLiteException se) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "saveList: ", se);
            isSaved = -1;
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "saveList: ", e);
            isSaved = -1;
        }
        return isSaved;
    }

    /**
     * Returns ContentValue object for the current TickDao
     *
     * @param tick
     * @return
     */
    private ContentValues getContentValues(Tick tick) {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(EntityTable.TicksTable.COLUMN_ID, tick.getId());
            contentValues.put(EntityTable.TicksTable.COLUMN_TICK_NAME, tick.getTickName());
            contentValues.put(EntityTable.TicksTable.COLUMN_TICK_SCIENTIFIC_NAME, tick.getScientific_name());
            contentValues.put(EntityTable.TicksTable.COLUMN_TICK_SPECIES, tick.getSpecies());
            contentValues.put(EntityTable.TicksTable.COLUMN_KNOWN_FOR, tick.getKnown_for());
            contentValues.put(EntityTable.TicksTable.COLUMN_DESCRIPTION, tick.getDescription());
            contentValues.put(EntityTable.TicksTable.COLUMN_FOUND_NEAR, tick.getFound_near_habitat());
            contentValues.put(EntityTable.TicksTable.COLUMN_IMAGE, tick.getImageUrl());
            contentValues.put(EntityTable.TicksTable.COLUMN_CREATED_AT, tick.getCreated_at());
            contentValues.put(EntityTable.TicksTable.COLUMN_UPDATED_AT, tick.getUpdated_at());

            return contentValues;
        } catch (SQLiteException se) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "getContentValues: ", se);
            return null;
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "getContentValues: ", e);
            return null;
        }
    }

    /**
     * update(Tick) method to update the entries in Tick Table
     *
     * @param Ticks
     * @return
     */
    @Override
    public boolean update(String id, Entity entity) {
        boolean isUpdated = false;
        try {
            Tick tick = (Tick) entity;
            ContentValues contentValues = getContentValues(tick);
            // update the record.
            isUpdated = db.update(
                    EntityTable.TicksTable.TABLENAME,
                    contentValues,
                    EntityTable.TicksTable.COLUMN_ID + "=?",
                    new String[]{tick.getId() + ""})
                    > 0;
        } catch (SQLiteException se) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "update: ", se);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "update: ", e);
        }
        return isUpdated;
    }

    /**
     * Build the {@link Tick} object from Cursor.
     *
     * @param c
     * @return
     */
    public Tick buildFromCursor(Cursor c) {
        Tick tickItem = null;
        try {
            if (c != null) {
                tickItem = new Tick();
                tickItem.setId(c.getString(0));
                tickItem.setTickName(c.getString(1));
                tickItem.setScientific_name(c.getString(2));
                tickItem.setSpecies(c.getString(3));
                tickItem.setKnown_for(c.getString(4));
                tickItem.setDescription(c.getString(5));
                tickItem.setFound_near_habitat(c.getString(6));
                tickItem.setImageUrl(c.getString(7));
                tickItem.setCreated_at(c.getLong(8));
                tickItem.setUpdated_at(c.getLong(9));
            }
            return tickItem;
        } catch (SQLiteException se) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "buildFromCursor: ", se);
            return null;
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "buildFromCursor: ", e);
            return null;
        }
    }

    /**
     * Delete the Tick entry from the Table.
     *
     * @param Ticks
     * @return
     */
    @Override
    public boolean delete(String id) {
        boolean isDeleted = false;
        try {
            isDeleted = db.delete(
                    EntityTable.TicksTable.TABLENAME,
                    EntityTable.TicksTable.COLUMN_ID + "=?",
                    new String[]{id + ""})
                    > 0;
        } catch (SQLiteException se) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "buildFromCursor: ", se);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "buildFromCursor: ", e);
        }
        return isDeleted;
    }

    /**
     * Returns Tick for corresponding Id.
     *
     * @param id
     * @return
     */
    @Override
    public Tick get(String id) {
        Tick tickItem = null;
        Cursor c = null;
        try {
            c = db.query(true, EntityTable.TicksTable.TABLENAME, tickEntryArray, EntityTable.TicksTable.COLUMN_ID + "=?",
                    new String[]{id + ""}, null, null, null, null);

            if (c != null && c.moveToFirst()) {
                tickItem = buildFromCursor(c);
            }
        } catch (SQLiteException se) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "get: ", se);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "get: ", e);
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
        return tickItem;
    }

    /**
     * Returns Tick for corresponding Name.
     *
     * @param id
     * @return
     */
    @Override
    public Tick getByName(String name) {
        Tick tickItem = null;
        Cursor c = null;
        try {
            c = db.query(true,
                    EntityTable.TicksTable.TABLENAME,
                    tickEntryArray,
                    EntityTable.TicksTable.COLUMN_TICK_NAME + "=?", new String[]{name + ""},
                    null, null, null, null);

            if (c != null && c.moveToFirst()) {
                tickItem = buildFromCursor(c);
            }
        } catch (SQLiteException se) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "getByName: ", se);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "getByName: ", e);
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
        return tickItem;
    }


    /**
     * Returns list of {@link Tick} stored in database.
     *
     * @return
     */
    public List<Tick> getAll() {
        List<Tick> ticksList = new ArrayList<Tick>();
        Cursor c = null;
        try {
            // Query the Database to get all the records.
            c = db.query(
                    EntityTable.TicksTable.TABLENAME,
                    tickEntryArray,
                    null, null, null, null, null);
            // get all Ticks from Cursor
            if (c != null && c.moveToFirst()) {
                // loop until the end of Cursor and add each entry to Ticks ArrayList.
                do {
                    Tick tickItem = buildFromCursor(c);
                    if (tickItem != null) {
                        ticksList.add(tickItem);
                    }
                } while (c.moveToNext());
            }
        } catch (SQLiteException se) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "getAll: ", se);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "getAll: ", e);
        } finally {
            if (!c.isClosed()) {
                c.close();
            }
        }
        return ticksList;
    }

    @Override
    public List<? extends Entity> getAll(String id) {
        return null;
    }

}
