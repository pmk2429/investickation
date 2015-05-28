package investickations.com.sfsu.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pavitra on 5/19/2015.
 */
public class Location {
    static public Location createLocationFactory(JSONObject jsonObject) throws JSONException {
        Location location = new Location();

        return location;
    }
}
