package com.sfsu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private final String LOGTAG = "~!@#$TickDao: ";
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

    private TickDao() {
    }

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

        try {
            Log.d(LOGTAG, "TICK : INSERT reached");
            Tick tick = (Tick) entity;
            ContentValues contentValues = getContentValues(tick);
            // save the entity
            return db.insert(EntityTable.TicksTable.TABLENAME, null, contentValues);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * save(Tick) method is used to save the entries (field values) in to Tick Database table
     *
     * @param ticks
     * @return
     */
    public long save(List<Tick> tickList) {
        Log.d(LOGTAG, "TICK : insert List");
        try {
            for (int i = 0; i < tickList.size(); i++) {
                Tick tick = tickList.get(i);
                ContentValues contentValues = getContentValues(tick);
                db.insert(EntityTable.TicksTable.TABLENAME, null, contentValues);
            }
            return tickList.size();
        } catch (Exception e) {
            // save the entity
            return -1;
        }

    }

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
        } catch (Exception e) {
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

        try {
            Log.d(LOGTAG, "Tick : UPDATE reached");
            Tick tick = (Tick) entity;
            ContentValues contentValues = getContentValues(tick);
            // update the record.
            return db.update(EntityTable.TicksTable.TABLENAME, contentValues, EntityTable.TicksTable.COLUMN_ID + "=?",
                    new String[]{tick.getId() + ""}) > 0;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Build the {@link Tick} object from Cursor.
     *
     * @param c
     * @return
     */
    public Tick buildFromCursor(Cursor c) {
        Tick tickItem = null;
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
    }

    /**
     * Delete the Tick entry from the Table.
     *
     * @param Ticks
     * @return
     */
    @Override
    public boolean delete(String id) {
        return db.delete(EntityTable.TicksTable.TABLENAME, EntityTable.TicksTable.COLUMN_ID + "=?", new String[]{id + ""}) > 0;
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
        try {
            Cursor c = db.query(true, EntityTable.TicksTable.TABLENAME, tickEntryArray, EntityTable.TicksTable.COLUMN_ID + "=?",
                    new String[]{id + ""}, null, null, null, null);

            if (c != null && c.moveToFirst()) {
                tickItem = buildFromCursor(c);
                if (!c.isClosed()) {
                    c.close();
                }
            }
        } catch (Exception e) {

        } finally {
            db.close();
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
        try {
            Cursor c = db.query(true,
                    EntityTable.TicksTable.TABLENAME,
                    tickEntryArray,
                    EntityTable.TicksTable.COLUMN_TICK_NAME + "=?", new String[]{name + ""},
                    null, null, null, null);

            if (c != null && c.moveToFirst()) {
                tickItem = buildFromCursor(c);
                if (!c.isClosed()) {
                    c.close();
                }
            }
        } catch (Exception e) {

        } finally {
            db.close();
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

        // Query the Database to get all the records.
        Cursor c = db.query(EntityTable.TicksTable.TABLENAME, tickEntryArray, null, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            // loop until the end of Cursor and add each entry to Ticks ArrayList.
            do {
                Tick tickItem = buildFromCursor(c);
                if (tickItem != null) {
                    ticksList.add(tickItem);
                }
            } while (c.moveToNext());
        }
        return ticksList;
    }

    @Override
    public List<? extends Entity> getAll(String id) {
        return null;
    }


}
