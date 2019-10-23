#荔枝框架

##功能划分

* 验证码	验证码级别，生成，校验
* web工具	controller约定，result封装
* 权限		自定义权限
* 工具      枚举，安全，短信，excel导入解析
* 全局异常  信息提醒

##参数配置

```shell
	#是否启用权限校验
	lychee.usePermission=false 
	#权限拦截
	lychee.permissionPath=“/”
	#权限忽略拦截
	lychee.permissionExcludePath=
	#权限缓存key
	lychee.permissionKey=“permission:”
	#启用验证码级别安全验证
	lychee.captchaSafe = false
	#验证码级别步长
	lychee.captchaStep = 3
	#验证码有效期（秒）
	lychee.captchaAge = 300;
	#captchaCookieName
	lychee.captchaCookieName = "captcha"
	#每个IP用户每分钟短信最大发送数量
	lychee.smsMaxSendQty = 60
	#用户短信发送数量时间限制（秒）
	lychee.usePermission=false 
	#用户短信有效期
	lychee.smsCodeExpiration = 3600
	#短信key
	lychee.smsCodeKey = "smsCode:" 
	#发送间隔key
	lychee.smsIntervalKey = "smsInterval:" 
```

