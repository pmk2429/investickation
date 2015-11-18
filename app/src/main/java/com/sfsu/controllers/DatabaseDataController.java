package com.sfsu.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sfsu.entities.Entity;
import com.sfsu.model.DatabaseOpenHelper;
import com.sfsu.model.EntityDao;

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
    private Context myContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private EntityDao entityDao;


    public DatabaseDataController(Context myContext, EntityDao entityDao) {
        // set the Helpers and Managers in Constructor
        this.myContext = myContext;
        dbOpenHelper = new DatabaseOpenHelper(this.myContext);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        this.entityDao = entityDao;
        // IMP: finally initialize the calling class type Entity
        entityDao.setDatabase(sqLiteDatabase);
    }

    public void close() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }

    // Basic helper methods for DB transaction
    public long save(Entity entity) {
        return this.entityDao.save(entity);
    }

    public boolean update(Entity entity) {
        return this.entityDao.update(entity);
    }

    public boolean delete(Entity entity) {
        return this.entityDao.delete(entity);
    }

    public Entity get(long id) {
        return this.entityDao.get(id);
    }

    public Entity get(String entityName) {
        return this.entityDao.get(entityName);
    }

    public List<? extends Entity> getAll() {
        return this.entityDao.getAll();
    }

}
