# MapDirect

## 建立步骤

* 1、建立主Activity(MainActivity.java)
* 2、建立MapsActivity（根据Android Studio推荐选卡建立）
 * 2-1、创建Google API 密钥:https://developers.google.com/places/android-api/signup#find-cert获取
 * 2-2、在项目res/values/google_map_api.xml配置密钥（https://console.developers.google.com/apis/credentials?project=milka-176507）
 * 2-3、将项目信息（包名、SHA-1 证书指纹--在2-2问价的注释中有）加入密钥管理中
* 3、compile 'com.google.android.gms:play-services-places:10.2.0'
* 4、启用 Google Places API for Android
* 5、导入导航工具包util（./googlemapdirectdemo项目中）
* 6、compile 'com.google.code.gson:gson:2.7'
* 7、在map所在的activity中，导入异步请求类函数GetJsonByUrl与方法reqJSONByUrlAndPoint()、pointMap()方法
* 8、导入./googlemapdirectdemo中MapsActivity中的必须常量
* compile "com.google.android.gms:play-services-location:10.2.0"
* 在主build.gradle中，添加maven
```java
allprojects {
    repositories {
        jcenter()
        maven {
            url "https://maven.google.com"
        }
    }
}
```
* 将gradle的依赖包升级至`11.2.0`

