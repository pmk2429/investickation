package investickations.com.sfsu.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pavitra on 5/19/2015.
 */
public class Tick {

    static public Tick createTickFactory(JSONObject jsonObject) throws JSONException {
        Tick tick = new Tick();

        return tick;
    }
}
