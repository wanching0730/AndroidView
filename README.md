# AndroidView
## 说明
这里存放了一些自定义的AndroidView，可作为学习指导参考，或者可选择性地根据自己需求下载源码进行更改后使用；

<br>

** 使用工具切记配置相关权限 **

</br>
注意：Android >= 5.0，拷贝快链源码即可使用
## 详细介绍
### Module项目列表
* [DiffSizeTextView][1]：DiffSizeTextView定义前后字体不同的TextView控件
* [LocalImage][2]：利用缓存与多线程，加载并展示本地图片（工具类）
* [MyCircleImgView][3]：MyCircleImgView定义圆形图片的ImageView带Padding设置
* [SlideListView_4_28][4]：SlideListView自定义高仿QQ的滑动删除类ListView控件
### Module详情
#### [DiffSizeTextView][1] ：DiffSizeTextView定义前后字体不同的TextView控件
* 无截图记录
* 控件使用说明:
	<i></i><br>1、将自定义控件类文件[DiffSizeTextView.java][7]拷贝进项目中
	<i></i><br>2、在styles.xml文件中添加控件配置信息代码：<br>
	```xml
	<declare-styleable name="DiffSizeTextView">
        <attr name="char_count" format="integer"/>
        <attr name="special_size" format="dimension"/>
        <attr name="normal_size" format="dimension"/>
        <attr name="gravity" format="integer"/>
        <attr name="text" format="string"/>
        <attr name="gap" format="dimension"/>
        <attr name="textColor" format="color"/>
    </declare-styleable>
	```
	**char_count**: 定义前半部分String的length();<br>
	**normal_size**: 定义前半部分String的字体大小;<br>
	**special_size**: 定义后半部分String的字体大小;<br>
	**text**: 等同TextView的text;<br>
	**gravity**: 等同TextView的gravity;<br>
	**gap**: 前后String的间距;<br>
	<i></i><br>3、控件使用<br>
	```xml
	<com.student0.www.diffsizetextview.DiffSizeTextView
    android:layout_width="wrap_content"
    android:layout_height="50dp"
    android:paddingLeft="20dp"
    app:char_count="2"
    app:special_size="32sp"
    app:normal_size="20sp"
    app:gap="5dp"
    app:text="@string/app_name"
    app:textColor="@color/colorAccent"
    />
	```
* 附加说明：
	* 本控件继承原生的View类；
	* 开发者可对控件类中的onDraw(Canvas canvas)的画笔进行修改，丰富控件外观；
	* 开发者可在styles.xml中定义更加具有特性的属性，在控件类的init(Context context, AttributeSet attrs)中读取配置，在onDraw(Canvas canvas)使用配置信息；
	* 如有问题，请留言，或发送邮件至:2946465099@qq.com
#### [LocalImage][2]： 利用缓存与多线程，加载并展示本地图片（工具类）
* 图片演示:
<i></i><br>![本地图片][13]<br>
* 工具使用说明:
	<i></i><br>1、本Module中的工具类已淘汰，请使用新工具类[ImageLoadUtil.java][14];
	<i></i><br>2、具体使用说明在[ImageLoadUtil.java][14]内部有详细说明;
*  附加说明：
	* 如有问题，请留言，或发送邮件至:2946465099@qq.com
#### [MyCircleImgView][3]：MyCircleImgView定义圆形图片的ImageView带Padding设置
* 图片演示([图片来源][5])：
<i></i><br>![演示图][6]<br>
* 控件使用说明:
	<i></i><br>1、将自定义控件类文件[MyCircleImageView.java][8]拷贝进项目中
	<i></i><br>2、控件使用实例<br>
	```xml
	<com.student0.www.mycircleimgview.CircularImageView
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:src="@mipmap/code"
        android:padding="5dp"
        />
	```
* 附加说明：
	* 本控件继承原生的ImageView类；
	* 为了美观在控件内已做了正方形设置，即CircularImageView会以Math.min(宽度，高度)作为CircularImageView的宽和高；
	* 实现的具体说明，参照本人的[CSDN][9]；
	* 如有问题，请留言，或发送邮件至:2946465099@qq.com；
#### [SlideListView_4_28][4]：SlideListView自定义高仿QQ的滑动删除类ListView控件
* 演示地址: 个人的[CSDN][10]
* 控件使用说明:
<i></i><br>
1、将控件的类文件[SlideListView.java][111]、分辨率计算工具类[DisplayUtil.java][112]、SlideListView适配器[SlideAdapter.java][113]、适配器Item布局文件[slide_item.xml][114]拷贝进项目中的布局文件Layout文件夹下
<i></i><br>2、控件使用实例<br>
```xml
<com.student0.www.slidelistview_4_28.SlideListView
        android:id="@+id/slideView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"></com.student0.www.slidelistview_4_28.SlideListView>
```
在代码中添加适配器，适配器初始化等<br>
```java
slideListView = (SlideListView) findViewById(R.id.slideView);
slideAdapter = new SlideAdapter(this, data, slideListView);
slideListView.setAdapter(slideAdapter);
```
* 附加说明：
	* 控件类中核心在[事件传递机][12]制的实现与控件属性的掌控；
	* 如果想在item中展示更多的信息，则修改item的布局文件[slide_item.xml][114]，并在适配器[SlideAdapter.java][113]中做简单的修改即可；
	* 如果想在item中开发更多的类似“删除”的类按钮控件，则可能需要在[SlideListView.java][111]、[SlideAdapter.java][113]、[slide_item.xml][114]做更多较复杂的更改，具体方法可参考“删除”控件的实现方式；
	* 如有问题，请留言，或发送邮件至:2946465099@qq.com

[1]:https://github.com/jiarWang/AndroidView/tree/master/DiffSizeTextView
[2]:https://github.com/jiarWang/AndroidView/tree/master/LocalImage
[3]:https://github.com/jiarWang/AndroidView/tree/master/MyCircleImgView
[4]:https://github.com/jiarWang/AndroidView/tree/master/SlideListView_4_28
[5]:http://www.jianshu.com/p/4f55200cea14
[6]:http://upload-images.jianshu.io/upload_images/1094967-8fe878e55b39af75.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240
[7]:https://github.com/jiarWang/AndroidView/blob/master/DiffSizeTextView/app/src/main/java/com/student0/www/diffsizetextview/DiffSizeTextView.java
[8]:https://github.com/jiarWang/AndroidView/blob/master/MyCircleImgView/app/src/main/java/com/student0/www/mycircleimgview/CircularImageView.java
[9]:http://blog.csdn.net/weixin_36570478/article/details/70865625
[10]:http://blog.csdn.net/weixin_36570478/article/details/70908015
[12]:http://blog.csdn.net/weixin_36570478/article/details/70470422
[13]:https://github.com/jiarWang/AndroidView/blob/master/LocalImage/20170809122613.jpg
[14]:https://github.com/jiarWang/Android-RN/blob/master/M0802/app/src/main/java/com/example/milka/m0802/Camera/Util/ImageLoadUtil.java
[111]:https://github.com/jiarWang/AndroidView/blob/master/SlideListView_4_28/app/src/main/java/com/student0/www/slidelistview_4_28/SlideListView.java
[112]:https://github.com/jiarWang/AndroidView/blob/master/SlideListView_4_28/app/src/main/java/com/student0/www/slidelistview_4_28/DisplayUtil.java
[113]:
https://github.com/jiarWang/AndroidView/blob/master/SlideListView_4_28/app/src/main/java/com/student0/www/slidelistview_4_28/SlideAdapter.java
[114]:
https://github.com/jiarWang/AndroidView/blob/master/SlideListView_4_28/app/src/main/res/layout/slide_item.xml