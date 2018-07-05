<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src="/_storage/js/jquery/jquery.md5.js"></script>
<script src="/modules/app/js/account/login.js"></script>
<link rel="stylesheet" type="text/css" href="/modules/app/css/pages/account/login.css">
<input type="hidden" id="hfReturnUrl" value="${model.value}"/>
<div class="page ${model.origin}">
    <div class="header">
        ${model.title}
    </div>
    <div class="panel-content ${model.origin}">
        <div class="logo ${model.origin}"><a href="javascript:;"></a></div>
        <div class="login-tab ${model.origin}">
            <h2 class="login-switch login-tab-psw select-font  ${model.origin}">
                密码登录
                <label class="select-style" style="display: block;"></label>
            </h2>
            <h2 class="login-txt  ${model.origin}">
                登录
            </h2>
            <span class="split-line"></span>
            <h2 class=" login-switch  login-tab-captcha ${model.origin}">验证码登录
                <label class="select-style"></label>
            </h2>
            <a class="change-btn ${model.origin}" href="javascript:;">使用快捷登录</a>
        </div>
        <ul class="login-form">
            <li id="passwordLogin">
                <form id="formLogin">
                    <div class="login-items">
                        <input id="txtUsername" class="phone-number" type="tel" name="username" placeholder="请输入您手机号"
                               autocomplete="off">
                        <a href="javascript:;" class="close-btn"></a>
                    </div>
                    <div class="login-items">
                        <input id="txtPassword" type="password" name="password" placeholder="请输入登录密码"
                               autocomplete="off">
                    </div>
                    <div id="captcha" class="login-items captcha-items hide">
                        <input id="txtCaptcha" type="text" name="captcha" placeholder="请输入验证码"
                               autocomplete="off">
                        <img src="/captcha/refresh" class="captcha"/>
                        <%--<span id="" class="sent-captcha">发送验证码</span>--%>
                    </div>
                    <div class="submit-btn ">
                        <button type="button" id="btnLogin">登录</button>
                    </div>
                </form>
                <div class="link">
                    <a href="/app/register?returnUrl=${model.value}" class="btn-register">新用户注册</a>
                    <a href="/app/findPassword?returnUrl=${model.value}" class="btn-forget">忘记密码？</a>
                </div>
            </li>
            <li id="codeLogin" class="hide">
                <form id="formSmsLogin">
                    <div id="captchaInput" class="login-items captcha-items">
                        <input id="txtImgCaptcha" type="text" name="captcha" placeholder="请输入验证码"
                               autocomplete="off"/>
                        <img src="/captcha/refresh" class="captcha"/>
                    </div>
                    <div class="login-items">
                        <input id="txtPhone" class="phone-number" type="tel" name="phone" placeholder="请输入手机号"
                               autocomplete="off">
                        <a href="javascript:;" class="close-btn"></a>
                    </div>
                    <div class="login-items query-code">
                        <input id="txtSmsCaptcha" type="text" name="captcha" placeholder="请输入短信验证码" autocomplete="off">
                        <a href="javascript:;" class="code sent-captcha" id="btnSendSms">获取验证码</a>
                        <%--<span id="sentCaptcha" class="sent-captcha">发送验证码</span>--%>
                    </div>
                    <div class="submit-btn">
                        <button type="button " id="btnSmsLogin">登录</button>
                    </div>
                </form>
                <div class="link">
                    <a href="/app/register?returnUrl=${model.value}" class="btn-register">新用户注册</a>
                    <a href="/app/findPassword?returnUrl=${model.value}" class="btn-forget">忘记密码？</a>
                </div>
            </li>
        </ul>
    </div>
    <div class="footer-line ${model.origin}">
        <div><span class="line-customer">LOGIN</span><span class="line-dealer">LOGIN</span></div>
    </div>
</div>
