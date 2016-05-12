package com.sfsu.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

import com.sfsu.db.DatabaseOpenHelper;
import com.sfsu.db.EntityDao;
import com.sfsu.entities.Entity;

import java.util.List;

/**
 * DatabaseDataController class provides Abstraction layer over DAO layer that contains all the methods required
 * to perform database transactions over the DB for that Model.
 * <br/>
 * This class provides abstraction on top of the DAO (Entity DAOs) layer for efficient error handling and modularity
 * of data retrieval.
 * <p>
 * Created by Pavitra on 5/27/2015.
 */
public class DatabaseDataController {
    private final String TAG = "~!@#$DBDataCtrl";
    private Context myContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private EntityDao entityDao;

    /**
     * Initializes the required dependencies and abstracts the underlying wrappers and DAOs
     *
     * @param context
     * @param dao
     */
    public DatabaseDataController(Context context, EntityDao dao) {
        // set the Helpers and Managers in Constructor
        myContext = context;
        dbOpenHelper = new DatabaseOpenHelper(myContext);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        entityDao = dao;
        // IMP: finally initialize the calling class type Entity
        entityDao.setDatabase(sqLiteDatabase);
    }

    // Basic helper methods for DB transaction
    public long save(Entity entity) {
        return this.entityDao.save(entity);
    }

    public boolean update(String id, Entity entity) {
        return this.entityDao.update(id, entity);
    }

    public boolean delete(String id) {
        return this.entityDao.delete(id);
    }

    public Entity get(String id) {
        return this.entityDao.get(id);
    }

    public Entity getByName(String entityName) {
        return this.entityDao.get(entityName);
    }

    public List<? extends Entity> getAll() {
        return this.entityDao.getAll();
    }

    public List<? extends Entity> getAll(String id) {
        return this.entityDao.getAll(id);
    }

    /**
     * Closes the active SQLiteDatabase connection
     */
    public void closeConnection() {
        try {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        } catch (SQLiteException se) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "closeConnection: ", se);
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "closeConnection: ", e);
        }
    }


}
