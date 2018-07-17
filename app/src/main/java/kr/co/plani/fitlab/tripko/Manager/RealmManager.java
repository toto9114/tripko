package kr.co.plani.fitlab.tripko.Manager;

import io.realm.Realm;
import kr.co.plani.fitlab.tripko.MyApplication;

/**
 * Created by jihun on 2017-02-16.
 */

public class RealmManager {
    private static RealmManager instance;

    public static RealmManager getInstance() {
        if (instance == null) {
            instance = new RealmManager();
        }
        return instance;
    }

    Realm realm;

    private RealmManager() {
        realm.init(MyApplication.getContext());
        realm = Realm.getDefaultInstance();
    }

    public Realm getRealm() {
        return realm;
    }
}
