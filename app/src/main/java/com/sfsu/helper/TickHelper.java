package com.sfsu.helper;

import com.sfsu.entities.Tick;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Helper class for {@link Tick} related operations. This class handles all the Tick related DB operations such as getting list
 * of Ticks from the Server, storing the list in DB, updating the list in DB and providing Tick List etc
 * </p>
 * <p/>
 * Created by Pavitra on 12/14/2015.
 */
public class TickHelper {

    /**
     * Returns List of Tick objects containing Tick Name and Species.
     *
     * @return
     */
    public static List<Tick> getAllTicks() {
        List<Tick> ticks = new ArrayList<>();
        ticks.add(new Tick("American Dog tick", "Species 1"));
        ticks.add(new Tick("Spotted Tick", "Species 2"));
        ticks.add(new Tick("Deer Tick", "Species 3"));
        ticks.add(new Tick("Rocky Mountain Wood Tick", "Species 4"));
        ticks.add(new Tick("Lone Star Tick", "Species 5"));
        ticks.add(new Tick("Gulf Coast Tick", "Species 6"));
        ticks.add(new Tick("Ixodes Ticks", "Species 7"));
        ticks.add(new Tick("Amblyomma americanum", "Species 8"));
        return ticks;
    }
}
