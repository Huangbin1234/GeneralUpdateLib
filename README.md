# GeneralUpdateLib
android 通用Android应用程序自动更新库
The Common application update library
 * 主工程集成使用示例，参看TestActivity，正式发布时删除此TestTwoActivity并只拷贝权限到主工程里面。
 集成步骤：
 * 1、实现接口InitUpdateInterface，实现 checkUpdate()方法
 * 2、onCreate中调用 checkUpdate(),参考TestActivity写法;
 * 3、将JSON_URL地址修改为你的地址(json文件见asset中)
 * 4、集成时将build.gradle中apply plugin: 'com.android.application'改为apply plugin: 'com.android.library'
 
 优点：
 1、以library的形式导入项目
 2、自动判断下载APK存储位置，支持sd卡及应用内进行存储,解决没有sd卡导致无法更新应用的问题。
 3、集成简单，只需实现一个接口并调用一个方法即可完成自动更新功能，完全可参照TestActivity进行使用。
 4、同时支持2种更新方式:应用内下载安装和浏览器下载安装。
 
 交流邮箱qianchengluntan@163.com