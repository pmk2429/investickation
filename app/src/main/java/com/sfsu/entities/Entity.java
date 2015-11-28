package com.sfsu.entities;

/**
 * <p>
 * <tt>Entity</tt> represents the type of real world entities in the InvesTICKation project. This interface defines the
 * generic behavior exhibited by of all the entities present in the application. The interface contains util methods common to
 * each Entity.
 * <p/>
 * In addition, Entity interface exemplifies the idea of polymorphism and provides bridge methods to preserce the
 * polymorphism in InvesTICKations.
 * </p>
 * <p>
 * Created by Pavitra on 6/3/2015.
 */
public interface Entity {

    /**
     * Method to get the Entity Type.
     *
     * @return
     */
    public Entity getEntity();

    /**
     * Method to get the Name of the calling Entity
     *
     * @return
     */
    public String getName();

    /**
     * Returns the Resource type of each Entity used to make REST calls to the API.
     *
     * @return
     */
    public String getResourceType();
}
