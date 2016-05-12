package com.sfsu.db;

import android.database.sqlite.SQLiteDatabase;

import com.sfsu.entities.Entity;

import java.util.List;

/**
 * EntityDao is the interface to provide the implementing classes get access to the DAO layer. Every Entity has a default behavior
 * which can be implemented using EntityDao interface;
 * <p>
 * Created by Pavitra on 10/19/2015.
 */
public interface EntityDao {

    /**
     * Sets a strong reference for the SQLiteDatabase for the current DAO in order to perform querying or CRUD operations
     *
     * @param db
     */
    void setDatabase(SQLiteDatabase db);

    /**
     * Insert operation for the Entity. Saves the Entity type instance in the SQLite data store
     *
     * @param entityItem
     * @return
     */
    long save(Entity entityItem);

    /**
     * Updates the entity type instance for the matching ID.
     *
     * @param id
     * @param entityItem
     * @return
     */
    boolean update(String id, Entity entityItem);

    /**
     * Deletes the Entity type instance for the matching ID.
     *
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * Returns the specific Entity type instance
     *
     * @param id
     * @param <T>
     * @return
     */
    <T extends Entity> T get(String id);

    /**
     * Returns a specific Entity type instance matching the <tt>entityName</tt> param
     *
     * @param entityName
     * @param <T>
     * @return
     */
    <T extends Entity> T getByName(String entityName);

    /**
     * Returns a list of Entity type instances stored in the database
     *
     * @return
     */
    List<? extends Entity> getAll();

    
    List<? extends Entity> getAll(String id);

}
