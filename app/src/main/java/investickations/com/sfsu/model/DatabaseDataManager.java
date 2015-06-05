package investickations.com.sfsu.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * DatabaseDataManager class provides the Data Access Objects for
 * Created by Pavitra on 5/27/2015.
 */
public class DatabaseDataManager {
    private Context myContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private UsersDao usersDao;
    private ObservationsDao observationsDao;

    public DatabaseDataManager(Context myContext) {

        // set the Helpers and Managers in Constructor
        this.myContext = myContext;
        dbOpenHelper = new DatabaseOpenHelper(this.myContext);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();

        // initialize the Database Tables.
        usersDao = new UsersDao(sqLiteDatabase);
        observationsDao = new ObservationsDao(sqLiteDatabase);

    }

    public void close() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }


    // implement all the methods to manipulate the Database table.
    // get, set, save, update, delete, getDAO, getAll
}
