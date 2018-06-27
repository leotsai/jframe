/**
 * Created by Leo on 2017/9/20.
 */

(function () {

    if (window.LoginManager) {
        return;
    }
    window.LoginManager = function () {
        this.loginUrl = "/app/login/doLogin";
        this.smsLogin = "/app/smsLogin/doLogin";
        this.registerUrl = "/app/register/doRegister";
        this.checkSmsCaptchaUrl = "/app/findPassword/checkSmsCaptcha";
        this.resetPasswordUrl = "/app/findPassword/resetPassword";
    };

    LoginManager.prototype = {
        login: function (username, password, captcha) {
            var me = this;
            var data = "username=" + username + "&password=" + password + "&captcha=" + captcha;
            mvcApp.notification.busy("登陆中...");
            mvcApp.ajax.post(me.loginUrl, data, function (result) {
                if (result.success && result.value != null) {
                    me.handleLoginCallback(result.value, false);
                } else {
                    mvcApp.notification.busy(false);
                    if (result.code == 1) {
                        me.showCaptcha();
                    }
                    me.onLoginError(result.message);
                }
            }, false);
        },
        smsLogin: function (username, smsCaptcha) {
            var me = this;
            var data = "username=" + username + "&smsCaptcha=" + smsCaptcha;
            mvcApp.notification.busy("登陆中...");
            mvcApp.ajax.post(me.smsLogin, data, function (result) {
                if (result.success && result.value != null) {
                    me.handleLoginCallback(result.value, true);
                } else {
                    mvcApp.notification.busy(false);
                    me.onLoginError(result.message);
                }
            }, false);

        },
        register: function (username, password, smsCaptcha) {
            var me = this;
            var data = "username=" + username + "&password=" + password + "&smsCaptcha=" + smsCaptcha;
            mvcApp.notification.busy("注册中...");
            mvcApp.ajax.post(me.registerUrl, data, function (result) {
                if (result.success && result.value != null) {
                    me.handleLoginCallback(result.value, true);
                } else {
                    mvcApp.notification.busy(false);
                    me.onLoginError(result.message);
                }
            }, false);
        },
        findPassword_checkCaptcha: function (username, smsCaptcha) {
            var me = this;
            var data = "username=" + username + "&smsCaptcha=" + smsCaptcha;
            mvcApp.ajax.busyPost(me.checkSmsCaptchaUrl, data, function (result) {
                if (result.success) {
                    me.onCheckCaptchaSuccess(username,smsCaptcha);
                } else {
                    me.onLoginError(result.message);
                }
            }, "验证中...", false);
        },
        findPassword_resetPassword: function (username, password, smsCaptcha) {
            var me = this;
            var data = "username=" + username+"&password=" + password + "&smsCaptcha=" + smsCaptcha;
            mvcApp.notification.busy("登陆中...");
            mvcApp.ajax.post(me.resetPasswordUrl, data, function (result) {
                if (result.success&& result.value != null) {
                    me.handleLoginCallback(result.value, true);
                } else {
                    mvcApp.notification.busy(false);
                    me.onLoginError(result.message);
                }
            }, false);
        },
        handleLoginCallback: function (loginResult, isSms) {
            var me = this;
            if (loginResult.needsBindWeixin) {
                mvcApp.notification.busy(false);
                this.showBindWeixin(loginResult.nickname, loginResult.imageUrl, isSms);
            } else {
                me.onLoginSuccess();
            }
        },
        showBindWeixin: function (nickname, imageUrl, isSms) {

        },
        onLoginSuccess: function () {
            window.location.replace(this.getReturnUrl());
        },
        onLoginError: function (message) {

        },
        showCaptcha: function () {

        },
        onCheckCaptchaSuccess: function (username,smsCaptcha) {

        },
        getReturnUrl: function () {
            try {
                var pairs = location.search.split('&');
                for (var i = 0; i < pairs.length; i++) {
                    var pair = pairs[i];
                    if (pair.indexOf("?") === 0) {
                        pair = pair.substring(1);
                    }
                    if (pair.indexOf("returnUrl=") === 0) {
                        var url = pair.substring("returnUrl=".length);
                        if (url.indexOf("%2F") === 0) {
                            url = decodeURIComponent(url);
                        }
                        return url;
                    }
                }
            }
            catch (e) {

            }
            return "/";
        }
    };


})();