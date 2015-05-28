package investickations.com.sfsu.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pavitra on 5/27/2015.
 */

/**
 * DatabaseHelper class to create and update the database table.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    // specify the name and version of DB.
    protected static final String DB_NAME = "Investickation.db";
    protected static final int DB_VERSION = 1;

    // the default constructor
    public DatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // onCreate method to create the Table in Database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    // to update the database table after making changes to it.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
