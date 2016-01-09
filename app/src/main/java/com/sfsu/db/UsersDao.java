package com.sfsu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sfsu.entities.Account;
import com.sfsu.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link Account} .
 * <p>
 * Created by Pavitra on 6/3/2015.
 */
public class UsersDao implements EntityDao {
    private final String TAG = "~!@#$UsersDao";
    private SQLiteDatabase db;
    // Account's string array.
    private String[] usersEntryArray = new String[]{
            EntityTable.UsersTable.COLUMN_ID,
            EntityTable.UsersTable.COLUMN_FULLNAME,
            EntityTable.UsersTable.COLUMN_EMAIL,
            EntityTable.UsersTable.COLUMN_PASSWORD,
            EntityTable.UsersTable.COLUMN_ADDRESS,
            EntityTable.UsersTable.COLUMN_CITY,
            EntityTable.UsersTable.COLUMN_STATE,
            EntityTable.UsersTable.COLUMN_ZIPCODE,
            EntityTable.UsersTable.COLUMN_CREATEDAT
    };

    /**
     * IMP : Required
     */
    public UsersDao() {
    }

    @Override
    public void setDatabase(SQLiteDatabase db) {
        Log.i(TAG, "setting db");
        this.db = db;
    }

    /**
     * Saves {@link Account} to SQLite DB.
     *
     * @param user
     * @return
     */
    public long save(Entity entity) {
        ContentValues contentValues = new ContentValues();
        try {
            Log.d(TAG, "INSERT reached");
            Account user = (Account) entity;
            contentValues.put(EntityTable.UsersTable.COLUMN_ID, user.getId());
            contentValues.put(EntityTable.UsersTable.COLUMN_FULLNAME, user.getFull_name());
            contentValues.put(EntityTable.UsersTable.COLUMN_EMAIL, user.getEmail());
            contentValues.put(EntityTable.UsersTable.COLUMN_PASSWORD, user.getPassword());
            contentValues.put(EntityTable.UsersTable.COLUMN_ADDRESS, user.getAddress());
            contentValues.put(EntityTable.UsersTable.COLUMN_CITY, user.getCity());
            contentValues.put(EntityTable.UsersTable.COLUMN_STATE, user.getState());
            contentValues.put(EntityTable.UsersTable.COLUMN_ZIPCODE, user.getZipCode());
            contentValues.put(EntityTable.UsersTable.COLUMN_CREATEDAT, user.getCreated_at());
            Log.i(TAG, "saving users");
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
        return db.insert(EntityTable.UsersTable.TABLENAME, null, contentValues);
    }

    /**
     * Method to update the {@link Account}.
     *
     * @param user
     * @return
     */
    public boolean update(Entity entity) {
        Account user = (Account) entity;
        ContentValues contentValues = new ContentValues();
        contentValues.put(EntityTable.UsersTable.COLUMN_ID, user.getId());
        contentValues.put(EntityTable.UsersTable.COLUMN_FULLNAME, user.getFull_name());
        contentValues.put(EntityTable.UsersTable.COLUMN_EMAIL, user.getEmail());
        contentValues.put(EntityTable.UsersTable.COLUMN_PASSWORD, user.getPassword());
        contentValues.put(EntityTable.UsersTable.COLUMN_ADDRESS, user.getAddress());
        contentValues.put(EntityTable.UsersTable.COLUMN_CITY, user.getCity());
        contentValues.put(EntityTable.UsersTable.COLUMN_STATE, user.getState());
        contentValues.put(EntityTable.UsersTable.COLUMN_ZIPCODE, user.getZipCode());
        contentValues.put(EntityTable.UsersTable.COLUMN_CREATEDAT, user.getCreated_at());
        Log.d(TAG, "UPDATE reached");
        // the db.update() method will return INT for number of rows updated. and so return db.update()>0 will check
        // for whether its true or false.
        return db.update(EntityTable.UsersTable.TABLENAME, contentValues, EntityTable.UsersTable.COLUMN_ID + "=?", new String[]{user.getId() + ""}) > 0;
    }

    /**
     * Method to delete the user from DB
     *
     * @param user
     * @return
     */
    public boolean delete(Entity entity) {
        Account user = (Account) entity;
        return db.delete(EntityTable.UsersTable.TABLENAME, EntityTable.UsersTable.COLUMN_ID + "=?", new String[]{user.getId() + ""}) > 0;
    }

    /**
     * Method to get Account using Userid
     *
     * @param id
     * @return
     */
    public Entity get(String id) {
        Account userItem = null;
        Cursor c = db.query(true, EntityTable.UsersTable.TABLENAME, usersEntryArray, EntityTable.UsersTable.COLUMN_ID + "=?", new String[]{id + ""}, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            userItem = buildFromCursor(c);
            if (!c.isClosed()) {
                c.close();
            }
        }
        return userItem;
    }

    /**
     * Method to get Account from username
     *
     * @param username
     * @return
     */
    public Account getByName(String username) {
        Account userItem = null;
        Cursor c = db.query(true, EntityTable.UsersTable.TABLENAME, usersEntryArray, EntityTable.UsersTable.COLUMN_FULLNAME + "=?", new String[]{username + ""}, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            userItem = buildFromCursor(c);
            if (!c.isClosed()) {
                c.close();
            }
        }
        return userItem;
    }


    /**
     * Method to get list of all Users from DB.
     *
     * @return
     */
    public List<Entity> getAll() {
        List<Entity> usersList = new ArrayList<Entity>();
        // Query the Database to get all the records.
        Cursor c = db.query(EntityTable.UsersTable.TABLENAME, usersEntryArray, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            // loop until the end of Cursor and add each entry to Observations ArrayList.
            do {
                Account userItem = buildFromCursor(c);
                if (userItem != null) {
                    usersList.add(userItem);
                }
            } while (c.moveToNext());
        }

        return usersList;
    }

    // build the Account Object using Cursor.
    public Account buildFromCursor(Cursor c) {
        Account userItem = null;
        if (c != null) {
            userItem = new Account();
            userItem.setId(c.getString(0));
            userItem.setFull_name(c.getString(1));
            userItem.setEmail(c.getString(2));
            userItem.setPassword(c.getString(3));
            userItem.setAddress(c.getString(4));
            userItem.setCity(c.getString(5));
            userItem.setState(c.getString(6));
            userItem.setZipCode(c.getInt(7));
            userItem.setCreated_at(c.getLong(8));
        }
        return userItem;
    }


}
