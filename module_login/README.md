## 欢迎页  登录页

##定制化欢迎页  登录页

# 定制化欢迎页   AndroidManifest.xml中设置欢迎页主题为透明   代码中设置欢迎页主题替换AndroidManifest 的欢迎页主题，所以点app图标会有延迟
  定制欢迎页 在外层框架中 assets 文件存放定制欢迎页图片  命名为 bg_welcome.png ,必须按照这个命名才会有效果

#定制登录页 可修改下面图标:

 页面背景  loginBg
 页面logo  loginLogo
 页面logo下文字  tvLoginLogoTitle
 页面账号前按钮  icLoginUser
 页面密码前按钮  icLoginPassword
 登陆按钮      loginBtn


 #定制某个页面布局 因为assets文件存放的文件都不会被编译，所以讲文件打包编译后再放入assets文件夹下

 如何得到编译后的xml布局文件:
 1.把写好的布局文件放到一个app工程的res/layout里
 2.运行这个app,android studio会生成一个debug.apk
 3.把这个apk改成zip格式解压,然后找到res/layout文件夹找到我们编译后的布局文件拷到assets目录下

 首先开发该xml，将页面打包进apk中，将apk后缀改为zip，取出编译后的该xml放入框架外层assets文件夹下,未编译过的xml放入外层layout中，以便之后修改。
 代码替换原本页面的xml(类似参考LoginActivity)