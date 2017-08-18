# MapDirect

## 建立步骤

* 1、建立主Activity(MainActivity.java)
* 2、建立MapsActivity（根据Android Studio推荐选卡建立）
 * 2-1、创建Google API 密钥:https://developers.google.com/places/android-api/signup#find-cert获取
 * 2-2、在项目res/values/google_map_api.xml配置密钥
 * 2-3、将项目信息（包名、SHA-1 证书指纹--在2-2问价的注释中有）加入密钥管理中
* compile 'com.google.android.gms:play-services-places:10.2.0'
* 启用 Google Places API for Android
* 

