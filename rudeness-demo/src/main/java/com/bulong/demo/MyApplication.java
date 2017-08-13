package com.bulong.demo;

import android.app.Application;

import com.bulong.rudeness.RudenessScreenHelper;

/**
 * Created by Caodongyao on 2017/8/4.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        new RudenessScreenHelper(this, 750).activate();
    }
}
