package com.sfsu.helper;

import com.sfsu.application.InvestickationApp;
import com.sfsu.controllers.DatabaseDataController;
import com.sfsu.db.TickDao;
import com.sfsu.entities.Tick;

import java.util.List;

/**
 * <p>Helper class for {@link Tick} related operations. This class handles all the Tick related DB operations such as getting list
 * of Ticks from the Server, storing the list in DB, updating the list in DB and providing Tick List etc
 * </p>
 * <p/>
 * Created by Pavitra on 12/14/2015.
 */
public class TickHelper {

    private static List<Tick> mAllTicks;

    public static List<Tick> getAllTicks() {
        DatabaseDataController dbController = new DatabaseDataController(InvestickationApp.getInstance(),
                TickDao.getInstance());

        mAllTicks = (List<Tick>) dbController.getAll();
        return mAllTicks;
    }
}
