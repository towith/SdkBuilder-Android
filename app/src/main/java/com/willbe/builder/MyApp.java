package com.willbe.builder;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

public class MyApp extends MultiDexApplication {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApp.context;
    }
}
