package hu.ait.android.weatherinfoapplication;

/**
 * Created by johnc on 11/20/2017.
 */

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication  extends Application {

    private Realm realmCities;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmCities = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmCities.close();
    }

    public Realm getRealmCities() {
        return realmCities;
    }
}
