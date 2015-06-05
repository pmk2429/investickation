package investickations.com.sfsu.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import investickations.com.sfsu.entities.Observation;

/**
 * ObservationsDao is a DataAccess Object.
 * Created by Pavitra on 6/3/2015.
 */
public class ObservationsDao {
    private String LOGSTRING = "ObservationsDAO =====> ";
    private SQLiteDatabase db;
    private String[] observationEntryArray = new String[]{ObservationsTable.COLUMN_ID, ObservationsTable.COLUMN_NUMOFTICKS, ObservationsTable.COLUMN_TICK_IMAGE, ObservationsTable.COLUMN_LAT, ObservationsTable.COLUMN_LONG, ObservationsTable.COLUMN_TIMESTAMP, ObservationsTable.COLUMN_CREATEDAT, ObservationsTable.COLUMN_UPDATEDAT};


    public ObservationsDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * save(Observation) method is used to save the entries (field values) in to Observation Database table
     *
     * @param observations
     * @return
     */
    public Long save(Observation observations) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ObservationsTable.COLUMN_ID, observations.getObservation_id());
        contentValues.put(ObservationsTable.COLUMN_NUMOFTICKS, observations.getNum_ticks());
        contentValues.put(ObservationsTable.COLUMN_TICK_IMAGE, observations.getTickImageUrl());
        contentValues.put(ObservationsTable.COLUMN_LAT, observations.getLatitude());
        contentValues.put(ObservationsTable.COLUMN_LONG, observations.getLongitude());
        contentValues.put(ObservationsTable.COLUMN_TIMESTAMP, observations.getTimestamp());
        contentValues.put(ObservationsTable.COLUMN_CREATEDAT, observations.getCreated_at());
        contentValues.put(ObservationsTable.COLUMN_UPDATEDAT, observations.getUpdated_at());
        Log.d(LOGSTRING, "INSERT reached");
        return db.insert(ObservationsTable.TABLENAME, null, contentValues);
    }

    /**
     * update(Observation) method to update the entries in Observation Table
     *
     * @param observations
     * @return
     */
    public boolean update(Observation observations) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ObservationsTable.COLUMN_ID, observations.getObservation_id());
        contentValues.put(ObservationsTable.COLUMN_NUMOFTICKS, observations.getNum_ticks());
        contentValues.put(ObservationsTable.COLUMN_TICK_IMAGE, observations.getTickImageUrl());
        contentValues.put(ObservationsTable.COLUMN_LAT, observations.getLatitude());
        contentValues.put(ObservationsTable.COLUMN_LONG, observations.getLongitude());
        contentValues.put(ObservationsTable.COLUMN_TIMESTAMP, observations.getTimestamp());
        contentValues.put(ObservationsTable.COLUMN_CREATEDAT, observations.getCreated_at());
        contentValues.put(ObservationsTable.COLUMN_UPDATEDAT, observations.getUpdated_at());
        Log.d(LOGSTRING, "UPDATE reached");
        // the db.update() method will return INT for number of rows updated. and so return db.update()>0 will check
        // for whether its true or false.
        return db.update(ObservationsTable.TABLENAME, contentValues, ObservationsTable.COLUMN_ID + "=?", new String[]{observations.getObservation_id() + ""}) > 0;
    }

    /**
     * Delete the Observation entry from the Table.
     *
     * @param observations
     * @return
     */
    public boolean delete(Observation observations) {

        return db.delete(ObservationsTable.TABLENAME, ObservationsTable.COLUMN_ID + "=?", new String[]{observations.getObservation_id() + ""}) > 0;
    }

    /**
     * get specific observation using ID.
     *
     * @param id
     * @return
     */
    public Observation get(long id) {

        Observation observationItem = null;
        Cursor c = db.query(true, ObservationsTable.TABLENAME, observationEntryArray, ObservationsTable.COLUMN_ID + "=?", new String[]{id + ""}, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            observationItem = buildFromCursor(c);

        }

        return null;
    }

    public Observation get(String observationName) {
        return null;
    }

    public List<Observation> getAll() {
        List<Observation> observationsList = new ArrayList<Observation>();
        return observationsList;
    }

    public Observation buildFromCursor(Cursor c) {
        Observation observation = null;
        return observation;
    }
}
