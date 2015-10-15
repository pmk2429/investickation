package com.sfsu.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.sfsu.entities.AppConfig;
import com.sfsu.entities.User;

/**
 * Created by Pavitra on 6/3/2015.
 */
public class UsersDao {
    private SQLiteDatabase db;
    private String[] usersEntryArray = new String[]{UsersTable.COLUMN_ID, UsersTable.COLUMN_USERNAME, UsersTable.COLUMN_EMAIL, UsersTable.COLUMN_PASSWORD, UsersTable.COLUMN_ADDRESS, UsersTable.COLUMN_CITY, UsersTable.COLUMN_STATE, UsersTable.COLUMN_ZIPCODE, UsersTable.COLUMN_CREATEDAT, UsersTable.COLUMN_UPDATEDAT};


    public UsersDao(SQLiteDatabase db) {
        this.db = db;
    }

    public Long save(User user) {
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
        Log.d(AppConfig.LOGSTRING, "User : INSERT reached");
        return db.insert(UsersTable.TABLENAME, null, contentValues);
    }

    public boolean update(User user) {
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
        Log.d(AppConfig.LOGSTRING, "User : INSERT reached");
        // the db.update() method will return INT for number of rows updated. and so return db.update()>0 will check
        // for whether its true or false.
        return db.update(UsersTable.TABLENAME, contentValues, UsersTable.COLUMN_ID + "=?", new String[]{user.getUser_id() + ""}) > 0;
    }

    public boolean delete(User user) {
        return db.delete(UsersTable.TABLENAME, UsersTable.COLUMN_ID + "=?", new String[]{user.getUser_id() + ""}) > 0;
    }

    public User get(long id) {
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


    public List<User> getAll() {
        List<User> usersList = new ArrayList<User>();
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

    // build the Observation Object using Cursor.
    public User buildFromCursor(Cursor c) {
        User userItem = null;
        if (c != null) {
            userItem = new User();
            userItem.setUser_id(c.getLong(0));
            userItem.setUsername(c.getString(1));
            userItem.setEmail(c.getString(1));
            userItem.setPassword(c.getString(1));
            userItem.setAddress(c.getString(1));
            userItem.setCity(c.getString(1));
            userItem.setState(c.getString(1));
            userItem.setZipcode(c.getInt(1));
            userItem.setCreated_at(c.getLong(1));
            userItem.setUpdated_at(c.getLong(1));
        }
        return userItem;
    }
}
