# AndroidView
## 说明
这里存放了一些自定义的AndroidView，可作为学习指导参考，或者可选择性地根据自己需求下载源码进行更改后使用；
</br>
注意：Android >= 5.0，拷贝快链源码即可使用
## 详细介绍
### Module项目列表
* [DiffSizeTextView][1]
* [LocalImage][2]
* [MyCircleImgView][3]
* [SlideListView_4_28][4]
### Module详情
#### [DiffSizeTextView][1] ：定义前后字体不同的TextView控件
* 使用说明:
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
	char_count: 定义前半部分String的length();
	normal_size: 定义前半部分String的字体大小;
	special_size: 定义后半部分String的字体大小;
	text: 等同TextView的text;
	gravity: 等同TextView的gravity;
	gap: 前后String的间距;
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
#### [LocalImage][2]
#### [MyCircleImgView][3]
* 图片演示([图片来源][5])：
<i></i><br>![演示图][6]<br>
* 使用说明:
	<i></i><br>1、将自定义控件类文件[DiffSizeTextView.java][7]拷贝进项目中
	<i></i><br>2、在styles.xml文件中添加控件配置信息代码<br>
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
	<i></i><br>3、控件使用<br>
	```xml
	<com.student0.www.mycircleimgview.CircularImageView
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:src="@mipmap/code"
        android:padding="5dp"
        />
	```
* 设计说明：
	* 本控件继承原生的ImageView类
	* 
#### [SlideListView_4_28][4]
<ol>
	<li>
		<a href="https://github.com/jiarWang/AndroidView/blob/master/MyCircleImgView/app/src/main/java/com/student0/www/mycircleimgview/CircularImageView.java">自定义原图带Padding的ImageView</a>
		<ul style="list-style-type:none">
		使用说明：
		<li>将CircularImageView拷贝进项目即可</li>
	</ul>
</li>
	<li><a href="https://github.com/jiarWang/AndroidView/blob/master/SlideListView_4_28/app/src/main/java/com/student0/www/slidelistview_4_28/SlideListView.java">高仿QQ滑动删除item功能效果</a>
		<ul style="list-style-type:none">
			使用说明：
			<li>1、按照上述链接将SlideListView源码拷贝进项目</li>
			<li>2、SlideAdapter与正常自定义的adapter有一点要修改的地方：
				<br/><blockquote>2-1需要将slideListView实例传进来， 
				<br/>2-2在数据删除后执行listView.turnToNormal();notifyDataSetChanged();以达到删除效果</li>
</ul></li>
	<li><a href="https://github.com/jiarWang/AndroidView/tree/master/DiffSizeTextView">前后字体样式不同的自定义TextView</a></li>
	<li><a href="https://github.com/jiarWang/AndroidView/tree/master/LocalImage">利用多线程与缓存加载显示本地图片，解决RecycleView卡顿问题</a></li>
	</ol>

[1]:https://github.com/jiarWang/AndroidView/tree/master/DiffSizeTextView
[2]:https://github.com/jiarWang/AndroidView/tree/master/LocalImage
[3]:https://github.com/jiarWang/AndroidView/tree/master/MyCircleImgView
[4]:https://github.com/jiarWang/AndroidView/tree/master/SlideListView_4_28
[5]:http://www.jianshu.com/p/4f55200cea14
[6]:http://upload-images.jianshu.io/upload_images/1094967-8fe878e55b39af75.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240
[7]:https://github.com/jiarWang/AndroidView/blob/master/DiffSizeTextView/app/src/main/java/com/student0/www/diffsizetextview/DiffSizeTextView.java