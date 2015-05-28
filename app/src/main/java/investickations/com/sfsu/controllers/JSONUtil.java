package investickations.com.sfsu.controllers;

import org.json.JSONException;

import java.util.ArrayList;

import investickations.com.sfsu.entities.*;

/**
 * Created by Pavitra on 5/19/2015.
 */

/**
 * JSONUtil class is the Utility class which provides helps to create the JSON Parser
 * for each of the entity in the application. This JSON Parser are used to retrieve the
 * data from remote connection.
 * The data representation of data retrieved are in JSOn format.
 */
public class JSONUtil {
    static public class UserJSONParser {
        static ArrayList<User> parseUser(String in) throws JSONException {
            return null;
        }
    }

    static public class ActivityJSONParser {
        static ArrayList<Activity> parseActivity(String in) throws JSONException {
            return null;
        }
    }

    static public class ObservationJSONParser {
        static ArrayList<Observation> parseObservation(String in) throws JSONException {
            return null;
        }
    }

    static public class TickJSONParser {
        static ArrayList<Tick> parseTick(String in) throws JSONException {
            return null;
        }
    }
}
