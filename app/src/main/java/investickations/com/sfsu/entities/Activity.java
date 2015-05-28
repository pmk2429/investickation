package investickations.com.sfsu.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pavitra on 5/19/2015.
 */
public class Activity {

    static public Activity createActivityFactory(JSONObject jsonObject) throws JSONException {
        Activity activity = new Activity();

        return activity;
    }

}
