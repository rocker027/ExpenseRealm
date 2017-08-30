package com.coors.expenserealm.app;

import android.app.Application;

import com.coors.expenserealm.RealmHelper;

import io.realm.Realm;

/**
 * Created by rocker on 2017/8/30.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

    public RealmHelper getRealmHelper() {
        return RealmHelper.getInstance(this);
    }
}
