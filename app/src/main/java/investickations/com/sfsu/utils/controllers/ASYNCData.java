package investickations.com.sfsu.utils.controllers;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import java.util.ArrayList;

import investickations.com.sfsu.entities.AppConfig;
import investickations.com.sfsu.entities.Entity;
import investickations.com.sfsu.entities.RequestParams;

/**
 * TODO: Remove this class once the RetroFit is setup completely.
 *
 * Created by Pavitra on 5/19/2015.
 */
public class ASYNCData {
    public static interface IEntityData {
        public void setProgressDialog();

        public void setEntitiesData(ArrayList<Entity> entityList);

        public Context myContext();
    }

    /**
     * <p>
     * <tt>GetDataAsync</tt> class provides an AsyncTask implementation of Getting User's data
     * from the remote Url using the REST API callbacks.
     * </p>
     */
    private static class GetDataAsync extends AsyncTask<RequestParams, Void, ArrayList<Entity>> {


        IEntityData activity;
        Entity entity;

        public GetDataAsync(IEntityData activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // performs operations after detecting the entity; User, Tick, Observation etc
        @Override
        protected ArrayList<Entity> doInBackground(RequestParams... requestParams) {

            try {
                if (requestParams[0].getResourceIdentifier().equals(AppConfig.USER_RESOURCE)) {
                    return JSONUtil.EntityJSONParser.parseEntities("users", requestParams[0].getEntityType());
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
