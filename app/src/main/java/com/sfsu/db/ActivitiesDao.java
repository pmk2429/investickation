package com.sfsu.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sfsu.entities.Activities;
import com.sfsu.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
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


    private ActivitiesDao() {
    }

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
            Log.d(TAG, "INSERT reached");
            Activities mActivity = (Activities) entityItem;
            ContentValues contentValues = getContentValues(mActivity);
            // save the entity
            return db.insert(EntityTable.ActivitiesTable.TABLENAME, null, contentValues);
        } catch (Exception e) {
            return -1;
        } finally {
            db.close();
        }
    }

    @Override
    public boolean update(Entity entityItem) {
        return false;
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
        String table = EntityTable.ActivitiesTable.TABLENAME;
        String whereClause = EntityTable.ActivitiesTable.COLUMN_ID + "=?";
        return db.delete(table, whereClause, new String[]{id}) > 0;
    }

    @Override
    public Activities get(String id) {
        Activities mActivity = null;
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
            db.close();
        }
        return mActivity;
    }

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

        } finally {
            db.close();
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

        }
        return activitiesList;
    }
}
