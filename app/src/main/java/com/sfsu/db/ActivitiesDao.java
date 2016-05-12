package com.sfsu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

import com.sfsu.entities.Activities;
import com.sfsu.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO layer for Activities. Contains all the CRUD operations for Activity.
 * Created by Pavitra on 1/9/2016.
 */
public class ActivitiesDao implements EntityDao {

    private static ActivitiesDao mInstance = new ActivitiesDao();
    private SQLiteDatabase db;
    private String TAG = "~!@#$ActivitiesDao";
    private String[] activityEntryArray = new String[]{
            EntityTable.ActivitiesTable.COLUMN_ID,
            EntityTable.ActivitiesTable.COLUMN_NAME,
            EntityTable.ActivitiesTable.COLUMN_NUM_OF_PEOPLE,
            EntityTable.ActivitiesTable.COLUMN_NUM_OF_PETS,
            EntityTable.ActivitiesTable.COLUMN_NUM_OF_TICKS,
            EntityTable.ActivitiesTable.COLUMN_TIMESTAMP,
            EntityTable.ActivitiesTable.COLUMN_IMAGE_URL,
            EntityTable.ActivitiesTable.COLUMN_LOCATION_AREA,
            EntityTable.ActivitiesTable.COLUMN_UPDATED_AT,
            EntityTable.ActivitiesTable.COLUMN_FK_USER_ID};


    /**
     * REQUIRED
     */
    private ActivitiesDao() {
    }

    /**
     * Static instance creation for singleton design pattern
     *
     * @return
     */
    public static ActivitiesDao getInstance() {
        return mInstance;
    }


    @Override
    public void setDatabase(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public long save(Entity entityItem) {
        try {
            Activities mActivity = (Activities) entityItem;
            ContentValues contentValues = getContentValues(mActivity);
            // save the entity
            return db.insert(EntityTable.ActivitiesTable.TABLENAME, null, contentValues);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "ActivitiesDAO save: ", e);
            return -1;
        }
    }

    @Override
    public boolean update(String id, Entity entityItem) {
        boolean update = false;
        try {
            Activities mActivity = (Activities) entityItem;
            ContentValues contentValues = getContentValues(mActivity);
            // update the record.
            update = db.update(
                    EntityTable.ActivitiesTable.TABLENAME,
                    contentValues,
                    EntityTable.ActivitiesTable.COLUMN_ID + "=?",
                    new String[]{id}) > 0;

        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "ActivitiesDAO update: ", e);
        }
        return update;
    }

    public boolean update(Entity entity, String imageUrl) {
        return false;
    }

    /**
     * Helper method to get ContentValues of {@link Activities}.
     *
     * @param mActivity
     * @return
     */
    private ContentValues getContentValues(Activities mActivity) {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(EntityTable.ActivitiesTable.COLUMN_ID, mActivity.getId());
            contentValues.put(EntityTable.ActivitiesTable.COLUMN_NAME, mActivity.getActivityName());
            contentValues.put(EntityTable.ActivitiesTable.COLUMN_NUM_OF_PEOPLE, mActivity.getNum_of_people());
            contentValues.put(EntityTable.ActivitiesTable.COLUMN_NUM_OF_PETS, mActivity.getNum_of_pets());
            contentValues.put(EntityTable.ActivitiesTable.COLUMN_NUM_OF_TICKS, mActivity.getNum_of_ticks());
            contentValues.put(EntityTable.ActivitiesTable.COLUMN_TIMESTAMP, mActivity.getTimestamp());
            contentValues.put(EntityTable.ActivitiesTable.COLUMN_IMAGE_URL, mActivity.getImage_url());
            contentValues.put(EntityTable.ActivitiesTable.COLUMN_LOCATION_AREA, mActivity.getLocation_area());
            contentValues.put(EntityTable.ActivitiesTable.COLUMN_UPDATED_AT, mActivity.getUpdated_at());
            contentValues.put(EntityTable.ActivitiesTable.COLUMN_FK_USER_ID, mActivity.getUser_id());

            return contentValues;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean delete(String id) {
        int deleted = 0;
        try {
            deleted = db.delete(EntityTable.ActivitiesTable.TABLENAME,
                    EntityTable.ActivitiesTable.COLUMN_ID + " = ?",
                    new String[]{id});
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "ActivitiesDAO delete: ", e);
        }
        return deleted > 0;
    }

    @Override
    public Activities get(String id) {
        Activities mActivity = null;
        try {
            Cursor c = db.query(true,
                    EntityTable.ActivitiesTable.TABLENAME,
                    activityEntryArray,
                    EntityTable.ActivitiesTable.COLUMN_ID + "=?",
                    new String[]{id + ""}, null, null, null, null);

            if (c != null && c.moveToFirst()) {
                mActivity = buildFromCursor(c);
                if (!c.isClosed()) {
                    c.close();
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "ActivitiesDAO get: ", e);
        }
        return mActivity;
    }

    /**
     * Builds Activity from the cursor
     *
     * @param c
     * @return
     */
    private Activities buildFromCursor(Cursor c) {
        Activities mActivity = null;
        try {
            if (c != null) {
                mActivity = new Activities();
                mActivity.setId(c.getString(0));
                mActivity.setActivityName(c.getString(1));
                mActivity.setNum_of_people(c.getInt(2));
                mActivity.setNum_of_pets(c.getInt(3));
                mActivity.setNum_of_ticks(c.getInt(4));
                mActivity.setTimestamp(c.getLong(5));
                mActivity.setImage_url(c.getString(6));
                mActivity.setLocation_area(c.getString(7));
                mActivity.setUpdated_at(c.getLong(8));
                mActivity.setUser_id(c.getString(9));
            }
        } catch (Exception e) {
        }
        return mActivity;
    }

    @Override
    public Activities getByName(String entityName) {
        Activities mActivity = null;
        try {
            Cursor c = db.query(true,
                    EntityTable.ActivitiesTable.TABLENAME,
                    activityEntryArray,
                    EntityTable.ActivitiesTable.COLUMN_NAME + "=?",
                    new String[]{entityName + ""}, null, null, null, null);

            if (c != null && c.moveToFirst()) {
                mActivity = buildFromCursor(c);
                if (!c.isClosed()) {
                    c.close();
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "ActivitiesDAO getByName: ", e);
        }
        return mActivity;
    }

    @Override
    public List<? extends Entity> getAll() {
        List<Activities> activitiesList = new ArrayList<Activities>();
        try {
            // Query the Database to get all the records.
            Cursor c = db.query(EntityTable.ActivitiesTable.TABLENAME,
                    activityEntryArray, null, null, null, null, null);

            if (c != null && c.moveToFirst()) {
                // loop until the end of Cursor and add each entry to Ticks ArrayList.
                do {
                    Activities mActivity = buildFromCursor(c);
                    if (mActivity != null) {
                        activitiesList.add(mActivity);
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "ActivitiesDAO getAll: ", e);
        }
        return activitiesList;
    }

    @Override
    public List<? extends Entity> getAll(String id) {
        return null;
    }
}