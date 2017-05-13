# AndroidView



闲来无事做，不如自定义。
</br>
注意：Android >= 5.0，拷贝快链源码即可使用
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