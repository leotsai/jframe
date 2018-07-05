<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="/modules/app/js/account/findPassword.js?v=1"></script>
<link rel="stylesheet" href="/modules/app/css/pages/account/findPassword.css">
<input type="hidden" id="hfReturnUrl" value="${model.value}"/>
<%--<input type="hidden" id="differentPort" value="${model.origin}"/>--%>
<div class="page ${model.origin}">
    <div class="header">
        <a class="btn-back" href="javascript:void(0);"></a>
        ${model.title}
    </div>
    <div class="content">
        <div class="panel-content ${model.origin}">
            <div class="logo"><a href="javascript:;"></a></div>
            <div class="login-tab findPassword ">
                <h2 class="find-psw-title">忘记密码
                    <label class="select"></label>
                </h2>
            </div>
            <form id="formForgetPwd" method="POST">
                <ul class="login-form">
                    <li id="tableForget" class="">
                        <div class="login-items">
                            <input id="txtUsername" type="tel" class="phone-number" name="username"
                                   placeholder="请输入您手机号" autocomplete="off">
                            <a href="javascript:;" class="close-btn"></a>
                        </div>
                        <div id="captcha-item-center" class="login-items captcha-items">
                            <input id="txtCaptcha" type="text" name="captcha" placeholder="请输入图片验证码"
                                   autocomplete="off">
                            <img src="/captcha/refresh" class="captcha"/>
                        </div>
                        <div class="submit-btn">
                            <button type="button" id="nextBtn">下一步</button>
                        </div>
                    </li>
                    <li id="tableReset" class="hide ${model.origin}">
                        <div class="login-items query-code captcha-items">
                            <input id="txtSmsCaptcha" type="text" placeholder="请输入短信验证码" autocomplete="off">
                            <a href="javascript:;" class="code sent-captcha" id="btnSendSms">获取验证码</a>
                        </div>
                        <div class="login-items password-items">
                            <input class="use-password" type="hidden" id="txtPassword"/>
                            <input class="pwd hide-pwd" type="password" placeholder="请设置6-20位登陆密码" autocomplete="off"/>
                            <input class="pwd show-pwd hide" type="text" placeholder="请设置6-20位登陆密码" autocomplete="off"/>
                            <a href="javascript:;" class="eye"></a>
                        </div>
                        <div class="login-items password-items">
                            <input class="use-password" type="hidden" id="txtPasswordSure"/>
                            <input class="pwd hide-pwd" type="password" placeholder="请确认密码" autocomplete="off"/>
                            <input class="pwd show-pwd hide" type="text" placeholder="请确认密码" autocomplete="off"/>
                            <a href="javascript:;" class="eye"></a>
                        </div>
                        <div class="tips">密码由6-20位字母、数字或半角符号组成，不能是8位以下纯数字/ 字母/半角符号，字母需区分大小写</div>
                        <div class="submit-btn">
                            <button type="button" id="btnReset">确定</button>
                        </div>
                    </li>
                </ul>
            </form>
        </div>
        <div class="footer-line ${model.origin}">
            <div><span class="line-customer">FIND PASSWORD</span><span class="line-dealer">FIND PASSWORD</span></div>
        </div>
    </div>
</div>