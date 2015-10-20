package com.sfsu.entities;

/**
 * <p>
 * <tt>Entity </tt> interface defines the behavior of all the entities.
 * This interface contains simple method to get the Entity of the class.
 * <p/>
 * </p>
 * Created by Pavitra on 6/3/2015.
 */
public interface Entity {
    public Entity getEntity();

    public String getName();

    public String getJSONResourceIdentifier();

//    public Entity createEntityFactory(JSONObject jsonObject);

    public String getResourceType();

}
