package com.sfsu.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
 * <p/>
 * Created by Pavitra on 5/27/2015.
 */
public class DatabaseDataController {
    private final String TAG = "~!@#$DBDataCtrl";
    private Context myContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private EntityDao entityDao;


    public DatabaseDataController(Context context, EntityDao dao) {
        // set the Helpers and Managers in Constructor
        myContext = context;
        dbOpenHelper = new DatabaseOpenHelper(this.myContext);
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

    /**
     * Method to close the database connection
     */
    public void closeConnection() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }


}
