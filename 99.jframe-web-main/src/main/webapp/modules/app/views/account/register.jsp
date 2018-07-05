<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="/modules/app/js/account/register.js"></script>
<link rel="stylesheet" href="/modules/app/css/pages/account/login.css">
<input type="hidden" id="hfReturnUrl" value="${model.value}"/>
<div class="page">
    <div class="header">
        <a class="btn-back" href="javascript:void(0);"></a>
        ${model.title}
    </div>
    <div class="content">
        <div class="panel-content customer">
            <div class="logo"><a href="javascript:;"></a></div>
            <div class="login-tab">
                <span class="reg-tips">短信验证码已发送至手机号</span>
                <h2>新用户注册</h2>
            </div>
            <ul class="login-form">
                <li id="firstStep" class="register">
                    <div class="login-items">
                        <input id="txtUsername" type="tel" class="phone-number" value="${model.phone}" name="username" placeholder="请输入您手机号" autocomplete="off">
                        <a href="javascript:;" class="close-btn"></a>
                    </div>
                    <div class="login-items captcha-items">
                        <input id="txtCaptcha" type="text" name="captcha" placeholder="请输入图片验证码"
                               autocomplete="off">
                        <img src="/captcha/refresh" class="captcha"/>
                    </div>
                    <div class="check checked"><span></span>我已阅读并同意<a href="#">《五品库会员服务协议》</a></div>
                    <div class="submit-btn"><button type="button" id="nextBtn">下一步</button></div>
                    <div class="link">
                        <a href="/app/login?returnUrl=${model.value}" class="btn-register">已有账号，登录点这里</a>
                    </div>
                </li>
                <li id="nextStep" class="register hide">
                    <div class="login-items query-code">
                        <input id="txtSmsCaptcha" type="text" placeholder="请输入短信验证码" autocomplete="off">
                        <a href="javascript:;" class="code" id="btnSendSms">获取验证码</a>
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
                    <div class="submit-btn"><button type="button" id="btnRegister">确定</button></div>
                </li>
            </ul>
        </div>
        <div class="footer-line customer"><div><span class="line-customer">REGISTER</span></div>
    </div>
</div>
</div>