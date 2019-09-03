package com.wlison.view.nice;

import android.app.Application;

public class App extends Application {

    private static App sContext;

    public static App getAppContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

}
