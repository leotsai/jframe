(function () {
    var urls = {
        refreshCaptcha: "/captcha/refresh",
        home: "/demo",
        sendSms: "/captcha/send",
        register: "/demo/register/doRegister"
    };

    $(document).ready(function () {
        var $btnSendSms = $("#btnSendSms");

        $btnSendSms.click(function () {
            if (validateFnone() === false) {
                return;
            }
            if ($btnSendSms.hasClass("has-send")) {
                return;
            }
            var phone = $.trim($("#txtUsername").val());
            var imgCaptcha = $.trim($("#txtCaptcha").val());
            var data = "captcha=" + imgCaptcha + "&phone=" + phone + "&usage=REGISTER";
            mvcApp.ajax.busyPost(urls.sendSms, data, function (result) {
                if (result.success === true) {
                    smsSendSuccess();
                } else {
                    mvcApp.notification.alert(null, result.message);
                    refreshCaptcha();
                }

            }, "发送中...", false);
        });

        $("#btnRegister").click(function () {
            doRegister();
        });

        $(".captcha").click(function () {
            refreshCaptcha();
        });
    });

    function smsSendSuccess() {
        var $btnSendSms = $("#btnSendSms");
        mvcApp.notification.toast("短信验证码发送成功");
        $btnSendSms.html('60s');
        $btnSendSms.addClass("has-send");
        (function countdown(seconds) {
            setTimeout(function () {
                seconds--;
                $btnSendSms.html(seconds + 's');
                if (seconds <= 0) {
                    $btnSendSms.removeClass("has-send");
                    $btnSendSms.html("重新获取验证码")
                } else {
                    countdown(seconds);
                }
            }, 1000)
        })(60);
    }

    function refreshCaptcha() {
        $(".captcha").attr("src", urls.refreshCaptcha + "?ts=" + new Date().getTime());
    }

    function doRegister() {
        if (validateTwo() === false) {
            return;
        }
        var username = $.trim($("#txtUsername").val());
        var password = $.trim($("#txtPassword").val());
        var smsCaptcha = $.trim($("#txtSmsCaptcha").val());
        var data = "username=" + username + "&password=" + password + "&smsCaptcha=" + smsCaptcha;
        mvcApp.ajax.busyPost(urls.register, data, function (result) {
            var loginResult = result.value;
            if (result.success && loginResult != null) {
                window.location.replace(urls.home);
            } else {
                mvcApp.notification.busy(false);
                mvcApp.notification.alert(null, result.message)
            }
        },'注册中...', false);
    }

    function validateFnone() {
        var username = $.trim($("#txtUsername").val());
        var txtCaptcha = $.trim($("#txtCaptcha").val());
        if (username === '') {
            mvcApp.notification.toastError("手机号不能为空");
            return false;
        }
        if (!mvcApp.isPhoneNumber(username)) {
            mvcApp.notification.toastError("手机号格式不正确");
            return false;
        }
        if (txtCaptcha === '') {
            mvcApp.notification.toastError("图形验证码不能为空");
            return false;
        }

        return true;
    }

    function validateTwo() {
        var username = $.trim($("#txtUsername").val());
        if (username === '') {
            mvcApp.notification.toastError("手机号不能为空");
            return false;
        }
        if (!mvcApp.isPhoneNumber(username)) {
            mvcApp.notification.toastError("手机号格式不正确");
            return false;
        }
        var smsCaptcha = $.trim($("#txtSmsCaptcha").val());
        var password = $.trim($("#txtPassword").val());
        var passwordSure = $.trim($("#txtPasswordSure").val());
        if (smsCaptcha === '') {
            mvcApp.notification.toastError("请输入短信验证码");
            return false;
        }
        if (password === '') {
            mvcApp.notification.toastError("登陆密码不能为空");
            return false;
        }
        if (passwordSure === '') {
            mvcApp.notification.toastError("请再次确认密码");
            return false;
        }
        if (!validatePassword(password) || !validatePassword(passwordSure)) {
            mvcApp.notification.toastError("登陆密码为6-20位字母、数字或下划线");
            return false;
        }
        if (password !== passwordSure) {
            mvcApp.notification.toastError("登陆密码输入不一致");
            return false;
        }
        return true;
    }

    function validatePassword(input) {
        return /^\w{6,20}$/.test(input);
    }

})();