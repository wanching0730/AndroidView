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
#### [DiffSizeTextView][1]
* 图片演示([图片来源][5])：
<br/>
![http://upload-images.jianshu.io/upload_images/1094967-8fe878e55b39af75.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240][6]
* 使用说明:
	<i>1、将自定义控件类文件[DiffSizeTextView.java][7]拷贝进项目中</i>
	<i>2、在styles.xml文件中添加控件配置信息代码<br/>
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
	</i>
	<i>3、</i>
	<i>4、</i>
#### [LocalImage][2]
#### [MyCircleImgView][3]
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