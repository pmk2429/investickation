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
 * This class provides abstraction on top of the DAO layer for efficient error handling and modularity
 * of the application.
 * <p/>
 * Created by Pavitra on 5/27/2015.
 */
public class DatabaseDataController {
    private Context myContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Entity entity;
    private EntityDao entityDao;


    public DatabaseDataController(Context myContext, Entity entity) {
        // set the Helpers and Managers in Constructor
        this.myContext = myContext;
        dbOpenHelper = new DatabaseOpenHelper(this.myContext);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        this.entity = entity;
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

    public List<Entity> getAll() {
        return this.entityDao.getAll();
    }

}
