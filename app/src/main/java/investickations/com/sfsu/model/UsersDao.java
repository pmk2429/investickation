package investickations.com.sfsu.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import investickations.com.sfsu.entities.User;

/**
 * Created by Pavitra on 6/3/2015.
 */
public class UsersDao {
    private SQLiteDatabase db;

    public UsersDao(SQLiteDatabase db) {
        this.db = db;
    }

    public Long save(User user) {
        return null;
    }

    public boolean update(User user) {
        return false;
    }

    public boolean delete(User user) {
        return false;
    }

    public User get(long id) {
        return null;
    }

    public User get(String username) {
        return null;
    }

    public List<User> getAll() {
        List<User> usersList = new ArrayList<User>();
        return usersList;
    }

    public User buildFromCursor(Cursor c) {
        User user = null;
        return user;
    }
}
