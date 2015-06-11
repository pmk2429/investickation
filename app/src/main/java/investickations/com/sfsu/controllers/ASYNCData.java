package investickations.com.sfsu.controllers;

import android.os.AsyncTask;

import org.json.JSONException;

import java.util.ArrayList;

import investickations.com.sfsu.entities.AppConfig;
import investickations.com.sfsu.entities.Entity;
import investickations.com.sfsu.entities.RequestParams;

/**
 * Created by Pavitra on 5/19/2015.
 */
public class ASYNCData {
    /**
     * <p>
     * <tt>GetDataAsync</tt> class provides an AsyncTask implementation of Getting User's data
     * from the remote Url using the REST API callbacks.
     * </p>
     */
    private static class GetDataAsync extends AsyncTask<RequestParams, Void, ArrayList<Entity>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // performs operations after detecting the entity; User, Tick, Observation etc
        @Override
        protected ArrayList<Entity> doInBackground(RequestParams... requestParams) {

            try {
                if (requestParams[0].getResourceIdentifier().equals(AppConfig.USER_RESOURCE)) {
                    return JSONUtil.EntityJSONParser.parseUsers("pmk");
                } else if (requestParams[0].getResourceIdentifier().equals(AppConfig.TICK_RESOURCE)) {
                    return JSONUtil.EntityJSONParser.parseTicks("pmk");
                } else if (requestParams[0].getResourceIdentifier().equals(AppConfig.ACTIVITY_RESOURCE)) {
                    return JSONUtil.EntityJSONParser.parseActivities("pmk");
                } else if (requestParams[0].getResourceIdentifier().equals(AppConfig.OBSERVATION_RESOURCE)) {
                    return JSONUtil.EntityJSONParser.parseObservations("pmk");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Entity> entities) {
            super.onPostExecute(entities);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
