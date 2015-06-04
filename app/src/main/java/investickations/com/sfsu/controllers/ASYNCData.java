package investickations.com.sfsu.controllers;

import android.os.AsyncTask;

import java.util.ArrayList;

import investickations.com.sfsu.entities.Entity;
import investickations.com.sfsu.entities.RequestParams;

/**
 * A
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
        protected ArrayList<Entity> doInBackground(RequestParams... requestParamses) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
