package investickations.com.sfsu.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pavitra on 5/19/2015.
 */
public class Observation {

    static public Observation createObservationFactory(JSONObject jsonObject) throws JSONException {
        Observation observation = new Observation();

        return observation;
    }
}
