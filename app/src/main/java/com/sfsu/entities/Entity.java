package com.sfsu.entities;

/**
 * <p>
 * <tt>Entity</tt> represents the type of real world entities in the InvesTICKation project. This interface defines the
 * generic behavior exhibited by of all the entities present in the application. This interface contains util methods for each
 * Entity. In addition, Entity interface exemplifies the idea of polymorphism and provides bridge methods to presever the
 * polymorphism.
 * <p/>
 * </p>
 * Created by Pavitra on 6/3/2015.
 */
public interface Entity {
    public Entity getEntity();

    public String getName();

    public String getJSONResourceIdentifier();

    public String getResourceType();


}
