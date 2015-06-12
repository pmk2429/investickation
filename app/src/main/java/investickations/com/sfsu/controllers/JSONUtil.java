package investickations.com.sfsu.controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import investickations.com.sfsu.entities.Entity;

/**
 * JSONUtil class is the Utility class which provides helps to create the JSON Parser
 * for each of the entity in the application. This JSON Parser are used to retrieve the
 * data from remote connection.
 * The data representation of data retrieved are in JSON format.
 * <p/>
 * <p>
 * Each method in the EntityJSONParser class represents a construct for parsing corresponding entity.
 * The methods provides a basic mechanism to parse each entity depending on the type of Data (in JSON Format)
 * retrieved.
 * </p>
 * Created by Pavitra on 5/19/2015.
 */
public class JSONUtil {
    static public class EntityJSONParser {
        static ArrayList<Entity> entitiesList;

        // parse entities
        static ArrayList<Entity> parseEntities(String in, Entity entity) throws JSONException {
            entitiesList = new ArrayList<>();
            JSONObject root = new JSONObject(in);
            // pass the name of JSON array to the getJSONArray() method.
            JSONArray entityArray = root.getJSONArray(entity.getJSONResourceIdentifier());

            for (int i = 0; i < entityArray.length(); i++) {
                JSONObject entityJSONObject = entityArray.getJSONObject(i);
                Entity parsedEntityObj = entity.createEntityFactory(entityJSONObject);
                entitiesList.add(parsedEntityObj);
            }
            return entitiesList;
        }
    }
}