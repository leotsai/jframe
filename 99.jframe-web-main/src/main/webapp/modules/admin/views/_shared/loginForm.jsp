<%--
  Created by IntelliJ IDEA.
  User: qq
  Date: 2017/11/10
  Time: 16:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="/modules/admin/css/pages/account/login.css" rel="stylesheet"/>
<form id="formLogin">
    <div class="form-group">
        <label for="txtUsername" class="label1"></label>
        <input id="txtUsername" type="tel" class="" name="username" placeholder="请输入你的账号"/>
    </div>
    <div class="form-group">
        <label for="txtPassword" class="label2"></label>
        <input id="txtPassword" type="password" class="" name="password" placeholder="请输入你的密码"/>
    </div>
    <div id="captcha" class="form-group hide last focus">
        <label for="txtCaptcha" class="label3"></label>
        <input id="txtCaptcha" type="text" class="wd138 " name="captcha" placeholder="请输入验证码"/>
        <span><img src="/captcha/refresh" class="captcha"/></span>
    </div>
    <div class="handle">
        <div id="errmes"></div>
    </div>
</form>
