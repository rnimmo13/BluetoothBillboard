package com.seniordesign.bluetoothbillboard;

import android.app.Application;
import android.content.Context;

/**
 * Created by Eric Anderson on 7/5/2015.
 * Adapted from Rohit Ghatol's code published at:
 * http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android
 */
public class Application_Context extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        Application_Context.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Application_Context.context;
    }
}
