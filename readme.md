
本项目是[一种粗暴快速的Android全屏幕适配方案](http://www.jianshu.com/p/b6b9bd1fba4d)的demo。

-------

- *正常编写的页面* 是按照dp来编写的页面
- *粗暴适配的页面* 是按照本方案编写的页面

在多种不同屏幕大小的真机与虚拟机下运行项目，可见*粗暴适配的页面*表现几乎一致，而*正常编写的页面*在大屏与小屏之间看起来差异较大。


**正常编写的页面** 左图API19 400x800， 右图API24 1440x2560）：  
![图五.png](http://upload-images.jianshu.io/upload_images/3490737-d5add2f4b91cc383.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)   

**粗暴适配的页面**（左图API19 400x800， 右图API24 1440x2560）：
![图六.png](http://upload-images.jianshu.io/upload_images/3490737-775011f0567ceb10.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---------

要点：

- 核心。使用冷门的pt作为长度单位。

- 绘制。编写xml时完全对照设计稿上的尺寸来编写，只不过单位换为pt。  
需要在代码中动态转换成px时使用`TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, metrics)`。

- 预览。实时预览时绘制页面是很重要的一个环节。以1334x750的设计图为例，为了实现于正常绘制时一样的预览功能，创建一个长为1334磅，宽为750磅的设备作为预览，经换算约为21.5英寸(`(sqrt(1334^2+750^2))/72`)。预览时选择这个设备即可。  
![图三.png](http://upload-images.jianshu.io/upload_images/3490737-58833d43921ca88b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
![图四.png](http://upload-images.jianshu.io/upload_images/3490737-0fba2d15eaebfd8a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 代码处理。(已修改，感谢[lingeringsmile](http://www.jianshu.com/users/fcf910b30984/timeline)的建议)。
    ```java
    public class MyApplication extends Application {

        @Override
        public void onCreate() {
            super.onCreate();

            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    //通常情况下application与activity得到的resource虽然不是一个实例，但是displayMetrics是同一个实例，只需调用一次即可
                    //为了面对一些不可预计的情况以及向上兼容，分别调用一次较为保险
                    resetDensity(MyApplication.this, 750);
                    resetDensity(activity, 750);
                }

                @Override
                public void onActivityStarted(Activity activity) {}
                @Override
                public void onActivityResumed(Activity activity) {}
                @Override
                public void onActivityPaused(Activity activity) {}
                @Override
                public void onActivityStopped(Activity activity) {}
                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
                @Override
                public void onActivityDestroyed(Activity activity) {}
            });
        }

        /**
        * 重新计算displayMetrics.xhdpi, 使单位pt重定义为设计稿的相对长度
        *
        * @param context
        * @param designWidth 设计稿的宽度
        */
        private static void resetDensity(Context context, float designWidth) {
            Point size = new Point();
            ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(size);

            context.getResources().getDisplayMetrics().xdpi = size.x / designWidth * 72f;
        }
    }
    ```

- 最佳实践。调用demo中的`DensityHelper.activate()`

这样绘制出来的页面就跟设计图几乎完全一样，无论大小屏上看起来就只是将设计图缩放之后的结果。