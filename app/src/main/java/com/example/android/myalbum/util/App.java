package com.example.android.myalbum.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by android on 17-9-4.
 */

public class App extends Application {

    private static  Context sContext;

    public static Context getAppContext() {
        return sContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
}
