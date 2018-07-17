package kr.co.plani.fitlab.tripko;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;


import io.fabric.sdk.android.Fabric;
import kr.co.plani.fitlab.tripko.Manager.PropertyManager;

/**
 * Created by jihun on 2017-02-16.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        Fabric.with(this,new Crashlytics());
        if(!TextUtils.isEmpty(PropertyManager.getInstance().getUsername())){
            Crashlytics.setUserIdentifier(PropertyManager.getInstance().getUsername());
        }
    }

    public static Context getContext() {
        return context;
    }
}
