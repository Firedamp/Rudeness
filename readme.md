
方案分析见[一种粗暴快速的Android全屏幕适配方案](http://www.jianshu.com/p/b6b9bd1fba4d)。

---------
本项目包括：
- rudeness-sdk。根据上述方案实现的一个非常简单的库。
- rudeness-demo。这是demo。

---------
使用姿势：

- 核心。使用冷门的pt作为长度单位，按照上述想法将其重定义为与屏幕大小相关的相对单位，不会对dp等常用单位的使用造成影响。

- 绘制。编写xml时完全对照设计稿上的尺寸来编写，只不过单位换为pt。假如设计图宽度为200，一个控件在设计图上标注的长度为3，只需要在初始化时定义宽度为200，绘制该控件时长度写为3pt，那么在任何大小的屏幕上该控件所表现的长度都为屏幕宽度的3/200。如果需要在代码中动态转换成px的话，使用`TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, metrics)`。

- 预览。实时预览时绘制页面是很重要的一个环节。以1334x750的设计图为例，为了实现于正常绘制时一样的预览功能，创建一个长为1334磅，宽为750磅的设备作为预览，经换算约为21.5英寸(`(sqrt(1334^2+750^2))/72`)。预览时选择这个设备即可。
![图三.png](http://upload-images.jianshu.io/upload_images/3490737-58833d43921ca88b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![图四.png](http://upload-images.jianshu.io/upload_images/3490737-0fba2d15eaebfd8a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

- 代码处理。(代码比较简单，所以你也可以不使用本依赖自己处理)
    在build.gradle中加入依赖：
    ```groovy
    compile 'com.bulong.rudeness:rudeness:latest.release@aar'
    ```
    在Application的onCreate中引用：
    ```java
    //设计图标注的宽度
    int designWidth = 750;
    new RudenessScreenHelper(this, designWidth).activate();
    ```

这样绘制出来的页面就跟设计图几乎完全一样，无论大小屏上看起来就只是将设计图缩放之后的结果。

*若存在webview导致适配失效的问题*
可以先继承WebView并重写`setOverScrollMode(int mode)`方法，在方法中调用super之后调用一遍`RudenessScreenHelper.resetDensity(getContext(), designWidth)`规避
*若存在dialog中适配失效的问题*
可以在dialog的oncreate中调用一遍`RudenessScreenHelper.resetDensity(getContext(), designWidth)`规避

-------
SDK API Refrences：
```java
public class RudenessScreenHelper {

    /**
     * 转换dp为px
     * @param context context
     * @param value 需要转换的dp值
     * @return px值
     */
    public static float dp2px(Context context, float value);

    /**
     * 转换pt为px
     * @param context context
     * @param value 需要转换的pt值，若context.resources.displayMetrics经过resetDensity()的修改则得到修正的相对长度，否则得到原生的磅
     * @return px值
     */
    public static float pt2px(Context context, float value);

    /**
     * 构造方法
     * @param application application
     * @param width 设计稿宽度
     */
    public RudenessScreenHelper(Application application, float width);

    /**
     * 激活本方案
     */
    public void activate();

    /**
     * 恢复系统原生方案
     */
    public void inactivate();
}
```

-------
关于demo：

- *正常编写的页面* 是按照dp来编写的页面
- *粗暴适配的页面* 是按照本方案编写的页面

在多种不同屏幕大小的真机与虚拟机下运行项目，可见*粗暴适配的页面*表现几乎一致，而*正常编写的页面*在大屏与小屏之间看起来差异较大。


**正常编写的页面** 左图API19 400x800， 右图API24 1440x2560）：
![图五.png](http://upload-images.jianshu.io/upload_images/3490737-d5add2f4b91cc383.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**粗暴适配的页面**（左图API19 400x800， 右图API24 1440x2560）：
![图六.png](http://upload-images.jianshu.io/upload_images/3490737-775011f0567ceb10.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)