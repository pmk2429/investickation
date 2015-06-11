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
 * The data representation of data retrieved are in JSOn format.
 * <p/>
 * Created by Pavitra on 5/19/2015.
 */
public class JSONUtil {
    static public class EntityJSONParser {
        ArrayList<Entity> list;

        // parse users
        static ArrayList<Entity> parseUsers(String in) throws JSONException {

            JSONObject root = new JSONObject(in);
            JSONArray usersArray = root.getJSONArray("persons");

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject personsJSONObject = usersArray.getJSONObject(i);

            }

            return null;
        }

        // parse tick
        static ArrayList<Entity> parseTicks(String in) throws JSONException {

            JSONObject root = new JSONObject(in);
            JSONArray ticksArray = root.getJSONArray("persons");

            for (int i = 0; i < ticksArray.length(); i++) {
                JSONObject personsJSONObject = ticksArray.getJSONObject(i);

            }
            return null;
        }

        // parse Activities
        static ArrayList<Entity> parseActivities(String in) throws JSONException {

            JSONObject root = new JSONObject(in);
            JSONArray activitiesArray = root.getJSONArray("persons");

            for (int i = 0; i < activitiesArray.length(); i++) {
                JSONObject personsJSONObject = activitiesArray.getJSONObject(i);

            }

            return null;
        }

        // parse Observations
        static ArrayList<Entity> parseObservations(String in) throws JSONException {

            JSONObject root = new JSONObject(in);
            JSONArray observationsArray = root.getJSONArray("persons");

            for (int i = 0; i < observationsArray.length(); i++) {
                JSONObject personsJSONObject = observationsArray.getJSONObject(i);


            }

            return null;
        }

    }
}
/*
    static ArrayList<Person> parsePerson(String in) throws JSONException {
        ArrayList<Person> personsList = new ArrayList<>();

        JSONObject root = new JSONObject(in);
        JSONArray personArray = root.getJSONArray("persons");

        for (int i = 0; i < personArray.length(); i++) {
            JSONObject personsJSONObject = personArray.getJSONObject(i);

            Person person = Person.createPersonFactory(personsJSONObject);
            personsList.add(person);
        }

        return personsList;
    }*/