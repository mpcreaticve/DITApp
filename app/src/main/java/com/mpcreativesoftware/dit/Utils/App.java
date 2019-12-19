package com.mpcreativesoftware.dit.Utils;

import android.app.Application;

import com.appizona.yehiahd.fastsave.FastSave;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;

import io.fabric.sdk.android.Fabric;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FastSave.init(getApplicationContext());
    }
}


