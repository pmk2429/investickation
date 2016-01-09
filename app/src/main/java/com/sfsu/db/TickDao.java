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
    private final String LOGTAG = "~!@#$TickDao: ";
    private SQLiteDatabase db;
    private String[] tickEntryArray = new String[]{EntityTable.TicksTable.COLUMN_ID, EntityTable.TicksTable.COLUMN_TICK_NAME, EntityTable.TicksTable.COLUMN_TICK_SPECIES, EntityTable.TicksTable.COLUMN_KNOWN_FOR, EntityTable.TicksTable.COLUMN_DESCRIPTION, EntityTable.TicksTable.COLUMN_IMAGE};

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
        Tick tick = (Tick) entity;
        ContentValues contentValues = new ContentValues();
        contentValues.put(EntityTable.TicksTable.COLUMN_ID, tick.getId());
        contentValues.put(EntityTable.TicksTable.COLUMN_TICK_NAME, tick.getTickName());
        contentValues.put(EntityTable.TicksTable.COLUMN_TICK_SPECIES, tick.getSpecies());
        contentValues.put(EntityTable.TicksTable.COLUMN_KNOWN_FOR, tick.getKnown_for());
        contentValues.put(EntityTable.TicksTable.COLUMN_DESCRIPTION, tick.getDescription());
        contentValues.put(EntityTable.TicksTable.COLUMN_IMAGE, tick.getImageUrl());
        Log.d(LOGTAG, "TICK : INSERT reached");
        return db.insert(EntityTable.TicksTable.TABLENAME, null, contentValues);
    }

    /**
     * update(Tick) method to update the entries in Tick Table
     *
     * @param Ticks
     * @return
     */
    public boolean update(Entity entity) {
        Tick tick = (Tick) entity;
        ContentValues contentValues = new ContentValues();
        contentValues.put(EntityTable.TicksTable.COLUMN_ID, tick.getId());
        contentValues.put(EntityTable.TicksTable.COLUMN_TICK_NAME, tick.getTickName());
        contentValues.put(EntityTable.TicksTable.COLUMN_TICK_SPECIES, tick.getSpecies());
        contentValues.put(EntityTable.TicksTable.COLUMN_KNOWN_FOR, tick.getKnown_for());
        contentValues.put(EntityTable.TicksTable.COLUMN_DESCRIPTION, tick.getDescription());
        contentValues.put(EntityTable.TicksTable.COLUMN_IMAGE, tick.getImageUrl());
        Log.d(LOGTAG, "Tick : UPDATE reached");
        // the db.update() method will return INT for number of rows updated. and so return db.update()>0 will check
        // for whether its true or false.
        return db.update(EntityTable.TicksTable.TABLENAME, contentValues, EntityTable.TicksTable.COLUMN_ID + "=?", new String[]{tick.getId() + ""}) > 0;
    }

    // build the Tick Object using Cursor.
    public Tick buildFromCursor(Cursor c) {
        Tick tickItem = null;
        if (c != null) {
            tickItem = new Tick();
            tickItem.setId(c.getString(0));
            tickItem.setTickName(c.getString(1));
            tickItem.setSpecies(c.getString(2));
            tickItem.setKnown_for(c.getString(3));
            tickItem.setDescription(c.getString(4));
            tickItem.setImageUrl(c.getString(5));
        }
        return tickItem;
    }

    /**
     * Delete the Tick entry from the Table.
     *
     * @param Ticks
     * @return
     */
    public boolean delete(Entity entity) {
        Tick ticks = (Tick) entity;
        return db.delete(EntityTable.TicksTable.TABLENAME, EntityTable.TicksTable.COLUMN_ID + "=?", new String[]{ticks.getId() + ""}) > 0;
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
        Cursor c = db.query(true, EntityTable.TicksTable.TABLENAME, tickEntryArray, EntityTable.TicksTable.COLUMN_ID + "=?", new String[]{id + ""}, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            tickItem = buildFromCursor(c);
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
        Cursor c = db.query(true, EntityTable.TicksTable.TABLENAME, tickEntryArray, EntityTable.TicksTable.COLUMN_ID + "=?", new String[]{name + ""},
                null, null, null, null);

        if (c != null && c.moveToFirst()) {
            tickItem = buildFromCursor(c);
            if (!c.isClosed()) {
                c.close();
            }
        }
        return tickItem;
    }


    /**
     * Get list of all the ticks stored in DB
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


}
