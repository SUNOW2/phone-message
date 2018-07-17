# 短信验证登录的后端实现
## 实现流程
- 用户界面输入手机号(未添加该页面)，点击获取验证码的时候，用户以POST方式请求"/user/login"接口，参数是phone
- "/user/login"接口生成六位随机验证码，并将手机号和验证码存入redis，有效时间是60s，然后调用叮咚云短信平台的接口发送验证码短信
- 用户接收到短信，将验证码填入验证框，点击登录按钮，以POST方式请求"/phone/oauth"接口，参数是phone、code
- "/user/oauth"接口将用户提交的手机号、验证码和redis中存储的手机号、验证号进行比对，从而达到验证的目的
- "/user/tokenLogin"接口用于网页端的登录，提供了token登录验证功能。

## 联系方式
QQ邮箱: 1605611836@qq.com(常用)<br />
google邮箱: sunow521310@gmail.com<br />

## 介绍
 本工程准备分为以下几部分：<br />
### 接口部分
controller包，service包，impl包，还有form包，该文件夹主要存放增删查改的Java类，<br />
但是，本项目的form包没有分的这么明显，可看批量删除接口
### 数据库
dao包，domain包，mapper包，值得注意的是，本项目其他项目不同，dao内存放的是映射的<br />
端口，domain存放的是实体类，mapper存放映射的xml文件
### 其他
conf存放的是配置文件、基础类等，utils存放的是各种工具类，filter存放过滤器<br />
