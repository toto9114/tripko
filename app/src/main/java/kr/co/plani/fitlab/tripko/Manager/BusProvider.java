package kr.co.plani.fitlab.tripko.Manager;

import com.squareup.otto.Bus;

/**
 * Created by jihun on 2017-02-28.
 */

public class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
