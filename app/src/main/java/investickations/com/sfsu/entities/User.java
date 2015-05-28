package investickations.com.sfsu.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pavitra on 5/19/2015.
 */
public class User {

    static public User createUserFactory(JSONObject jsonObject) throws JSONException {
        User user = new User();

        return user;
    }

}
