<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${model.title}</title>
    <link href="/_storage/css/jqueryui/base/jquery-ui.css" rel="stylesheet"/>
    <link href="/modules/demo/css/core.css" rel="stylesheet"/>
    <link href="/modules/demo/css/pad.css" rel="stylesheet" media="screen and (max-width:1024px)"/>
    <link href="/modules/demo/css/pages/account/login.css" rel="stylesheet"/>
    <script src="/_storage/js/jquery/jquery-1.7.2.min.js"></script>
    <script src="/_storage/js/jquery/jquery-ui-1.8.20.min.js"></script>
    <script src="/modules/demo/js/core.js"></script>

    <script src="/modules/demo/js/account/login.js"></script>
</head>
<body>
<input type="hidden" id="hfReturnUrl" value="${model.value}"/>
<input type="hidden" id="hfErrorCode" value="${model.error}"/>
<div class="panel">
    <div class="login">
        <div class="login-box">
            <form id="formLogin">
                <div class="form-group">
                    <label for="txtUsername" class="label1"></label>
                    <input id="txtUsername" type="tel" class="" name="username" placeholder="请输入你的账号"/>
                </div>
                <div class="form-group">
                    <label for="txtPassword" class="label2"></label>
                    <input id="txtPassword" type="password" class="" name="password" placeholder="请输入你的密码"/>
                </div>
                <div id="captcha" class="form-group hide last">
                    <label for="txtCaptcha" class="label3"></label>
                    <input id="txtCaptcha" type="text" class="wd138" name="captcha" placeholder="请输入验证码"/>
                    <span><img src="/captcha/refresh" class="captcha"/></span>
                </div>
                <div class="sub-btn">
                    <div id="errmes"></div>
                    <a id="btnLogin" href="javascript:;">Login</a>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>