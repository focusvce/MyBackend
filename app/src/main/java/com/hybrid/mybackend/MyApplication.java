package com.hybrid.mybackend;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    // Instance of full application
    private static MyApplication sInstance = null;

    // First thing to be done
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    // Get Instance
    public static MyApplication getInstance(){
        return sInstance;
    }
    // Get APPLICATION context rather activity context
    public static Context getAppContext(){
        return sInstance.getApplicationContext();
    }
}
