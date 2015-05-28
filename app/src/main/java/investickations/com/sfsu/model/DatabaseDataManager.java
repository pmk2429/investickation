package investickations.com.sfsu.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Pavitra on 5/27/2015.
 */

/**
 * DatabaseDataManager class provides the
 */
public class DatabaseDataManager {
    private Context myContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseDataManager(Context myContext) {

        // set the Helpers and Managers in Constructor
        this.myContext = myContext;
        dbOpenHelper = new DatabaseOpenHelper(this.myContext);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();

        // initialize the Database Tables.

    }

    public void close() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }


    // implement all the methods to manipulate the Database table.
    // get, set, save, update, delete, getDAO, getAll
}
