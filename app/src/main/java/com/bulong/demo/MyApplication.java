package com.bulong.demo;

import android.app.Application;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by Caodongyao on 2017/7/28.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        resetDensity();
    }

    @Override
    //横竖屏的切换等Configuration变化会导致更新Resources，需要重新处理一下
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        resetDensity();
    }

    public final static float DESIGN_WIDTH = 750;
    //更改DisplayMetrics为我们想要的与屏幕宽度相关的比例
    public void resetDensity(){
        Point size = new Point();
        ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(size);

        getResources().getDisplayMetrics().xdpi = size.x/DESIGN_WIDTH*72f;
    }

    //将pt转换为px值
    public float pt2px(int value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, getResources().getDisplayMetrics());
    }
}
