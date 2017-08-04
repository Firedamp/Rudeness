package com.bulong.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by Caodongyao on 2017/8/4.
 */

public class DensityHelper {

    /**
     * 重新计算displayMetrics.xhdpi, 使单位pt重定义为设计稿的相对长度
     * @see #activate()
     *
     * @param context
     * @param designWidth 设计稿的宽度
     */
    private static void resetDensity(Context context, float designWidth){
        Point size = new Point();
        ((WindowManager)context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(size);

        context.getResources().getDisplayMetrics().xdpi = size.x/designWidth*72f;
    }

    /**
     * 恢复displayMetrics为系统原生状态，单位pt恢复为长度单位磅
     * @see #inactivate()
     *
     * @param context
     */
    private static void restoreDensity(Context context){
        context.getResources().getDisplayMetrics().setToDefaults();
    }

    /**
     * 转换dp为px
     * @param context
     * @param value 需要转换的dp值
     * @return
     */
    public static float dp2px(Context context, float value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    /**
     * 转换pt为px
     * @param context
     * @param value 需要转换的pt值，若context.resources.displayMetrics经过resetDensity()的修改则得到修正的相对长度，否则得到原生的磅
     * @return
     */
    public static float pt2px(Context context, float value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, context.getResources().getDisplayMetrics());
    }



    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;
    private Application mApplication;
    private float designWidth = 720;

    /**
     *
     * @param application
     * @param width 设计稿宽度
     */
    public DensityHelper(Application application, float width){
        mApplication = application;
        designWidth = width;

        activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //通常情况下application与activity得到的resource虽然不是一个实例，但是displayMetrics是同一个实例，只需调用一次即可
                //为了面对一些不可预计的情况以及向上兼容，分别调用一次较为保险
                resetDensity(mApplication, designWidth);
                resetDensity(activity, designWidth);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        };
    }

    /**
     * 激活本方案
     */
    public void activate(){
        resetDensity(mApplication, designWidth);
        mApplication.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    /**
     * 恢复系统原生方案
     */
    public void inactivate(){
        restoreDensity(mApplication);
        mApplication.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }




}
