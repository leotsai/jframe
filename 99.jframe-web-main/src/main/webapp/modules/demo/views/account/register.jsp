<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${model.title}</title>
    <link href="/_storage/css/jqueryui/base/jquery-ui.css" rel="stylesheet"/>
    <link href="/modules/demo/css/core.css" rel="stylesheet"/>
    <link href="/modules/demo/css/pad.css" rel="stylesheet" media="screen and (max-width:1024px)"/>
    <script src="/_storage/js/jquery/jquery-1.7.2.min.js"></script>
    <script src="/_storage/js/jquery/jquery-ui-1.8.20.min.js"></script>
    <script src="/modules/demo/js/core.js"></script>

    <script src="/modules/demo/js/account/register.js"></script>
    <link href="/modules/demo/css/pages/account/register.css" rel="stylesheet"/>
</head>
<body>
<input type="hidden" id="hfReturnUrl" value="${model.value}"/>
<div class="panel">
    <div class="register">
        <div class="register-box">
            <form id="formRegister">
                <div class="form-group">
                    <label for="txtUsername" class=""></label>
                    <input id="txtUsername" type="tel" class="" name="" placeholder="请输入你的手机号"/>
                </div>
                <div id="captcha" class="form-group">
                    <label for="txtCaptcha" class=""></label>
                    <input id="txtCaptcha" type="text" class="wd138" name="" placeholder="请输入验证码"/>
                    <span><img src="/captcha/refresh" class="captcha"/></span>
                </div>
                <div class="form-group">
                    <label for="txtSmsCaptcha" class=""></label>
                    <input id="txtSmsCaptcha" type="tel" class="wd138" name="" placeholder="请输入短信验证码"/>
                    <a href="javascript:;" class="code" id="btnSendSms">获取验证码</a>
                </div>
                <div class="form-group">
                    <label for="txtPassword" class=""></label>
                    <input id="txtPassword" type="password" class="" name="" placeholder="请输入你的密码"/>
                </div>
                <div class="form-group">
                    <label for="txtPasswordSure" class=""></label>
                    <input id="txtPasswordSure" type="password" class="" name="" placeholder="请确认密码"/>
                </div>
                <div class="sub-btn">
                    <a id="btnRegister" href="javascript:;">Register</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>