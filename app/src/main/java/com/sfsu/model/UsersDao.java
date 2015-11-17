package com.sfsu.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sfsu.entities.Entity;
import com.sfsu.entities.User;
import com.sfsu.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pavitra on 6/3/2015.
 */
public class UsersDao implements EntityDao {

    private SQLiteDatabase db;

    // User's string array.
    private String[] usersEntryArray = new String[]{UsersTable.COLUMN_ID, UsersTable.COLUMN_USERNAME, UsersTable.COLUMN_EMAIL, UsersTable.COLUMN_PASSWORD, UsersTable.COLUMN_ADDRESS, UsersTable.COLUMN_CITY, UsersTable.COLUMN_STATE, UsersTable.COLUMN_ZIPCODE, UsersTable.COLUMN_CREATEDAT, UsersTable.COLUMN_UPDATEDAT};


    @Override
    public void setDatabase(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Method to save the User to DB.
     *
     * @param user
     * @return
     */
    public long save(Entity entity) {
        User user = (User) entity;
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsersTable.COLUMN_ID, user.getUser_id());
        contentValues.put(UsersTable.COLUMN_USERNAME, user.getUsername());
        contentValues.put(UsersTable.COLUMN_EMAIL, user.getEmail());
        contentValues.put(UsersTable.COLUMN_PASSWORD, user.getPassword());
        contentValues.put(UsersTable.COLUMN_ADDRESS, user.getAddress());
        contentValues.put(UsersTable.COLUMN_CITY, user.getCity());
        contentValues.put(UsersTable.COLUMN_STATE, user.getState());
        contentValues.put(UsersTable.COLUMN_ZIPCODE, user.getZipcode());
        contentValues.put(UsersTable.COLUMN_CREATEDAT, user.getCreated_at());
        contentValues.put(UsersTable.COLUMN_UPDATEDAT, user.getUpdated_at());
        Log.d(AppUtils.LOGTAG, "User : INSERT reached");
        return db.insert(UsersTable.TABLENAME, null, contentValues);
    }

    /**
     * Method to update the User.
     *
     * @param user
     * @return
     */
    public boolean update(Entity entity) {
        User user = (User) entity;
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsersTable.COLUMN_ID, user.getUser_id());
        contentValues.put(UsersTable.COLUMN_USERNAME, user.getUsername());
        contentValues.put(UsersTable.COLUMN_EMAIL, user.getEmail());
        contentValues.put(UsersTable.COLUMN_PASSWORD, user.getPassword());
        contentValues.put(UsersTable.COLUMN_ADDRESS, user.getAddress());
        contentValues.put(UsersTable.COLUMN_CITY, user.getCity());
        contentValues.put(UsersTable.COLUMN_STATE, user.getState());
        contentValues.put(UsersTable.COLUMN_ZIPCODE, user.getZipcode());
        contentValues.put(UsersTable.COLUMN_CREATEDAT, user.getCreated_at());
        contentValues.put(UsersTable.COLUMN_UPDATEDAT, user.getUpdated_at());
        Log.d(AppUtils.LOGTAG, "User : INSERT reached");
        // the db.update() method will return INT for number of rows updated. and so return db.update()>0 will check
        // for whether its true or false.
        return db.update(UsersTable.TABLENAME, contentValues, UsersTable.COLUMN_ID + "=?", new String[]{user.getUser_id() + ""}) > 0;
    }

    /**
     * Method to delete the user from DB
     *
     * @param user
     * @return
     */
    public boolean delete(Entity entity) {
        User user = (User) entity;
        return db.delete(UsersTable.TABLENAME, UsersTable.COLUMN_ID + "=?", new String[]{user.getUser_id() + ""}) > 0;
    }

    /**
     * Method to get User using Userid
     *
     * @param id
     * @return
     */
    public Entity get(long id) {
        User userItem = null;
        Cursor c = db.query(true, UsersTable.TABLENAME, usersEntryArray, UsersTable.COLUMN_ID + "=?", new String[]{id + ""}, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            userItem = buildFromCursor(c);
            if (!c.isClosed()) {
                c.close();
            }
        }
        return userItem;
    }

    /**
     * Method to get User from username
     *
     * @param username
     * @return
     */
    public User get(String username) {
        User userItem = null;
        Cursor c = db.query(true, UsersTable.TABLENAME, usersEntryArray, UsersTable.COLUMN_USERNAME + "=?", new String[]{username + ""}, null, null, null, null);

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
        Cursor c = db.query(UsersTable.TABLENAME, usersEntryArray, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            // loop until the end of Cursor and add each entry to Observations ArrayList.
            do {
                User userItem = buildFromCursor(c);
                if (userItem != null) {
                    usersList.add(userItem);
                }
            } while (c.moveToNext());
        }

        return usersList;
    }

    // build the User Object using Cursor.
    public User buildFromCursor(Cursor c) {
        User userItem = null;
        if (c != null) {
            userItem = new User();
            userItem.setUser_id(c.getLong(0));
            userItem.setUsername(c.getString(1));
            userItem.setEmail(c.getString(2));
            userItem.setPassword(c.getString(3));
            userItem.setAddress(c.getString(4));
            userItem.setCity(c.getString(5));
            userItem.setState(c.getString(6));
            userItem.setZipcode(c.getString(7));
            userItem.setCreated_at(c.getLong(8));
            userItem.setUpdated_at(c.getLong(9));
        }
        return userItem;
    }


}
