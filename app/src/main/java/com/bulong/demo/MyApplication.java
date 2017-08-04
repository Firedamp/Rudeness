package com.bulong.demo;

import android.app.Application;

/**
 * Created by Caodongyao on 2017/8/4.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        new DensityHelper(this, 750).activate();
    }
}
