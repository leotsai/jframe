(function () {
    var urls = {
        sendSms: "/captcha/send",
        resendSms: "/captcha/resend",
        refreshCaptcha: "/captcha/refresh",
        validateSmsCaptcha: "/app/findPassword/checkSmsCaptcha",
        resetPassword: "/app/findPassword/resetPassword",
        customerHome: "/app",
        dealerHome: "/dealer",
        peHome:"/pe#home/index"
    };

    $(document).ready(function () {
        var $btnSendSms = $("#btnSendSms");
        $btnSendSms.click(function () {
            if ($btnSendSms.hasClass("has-send")) {
                return;
            }
            var phone = $.trim($("#txtUsername").val());
            var data = "phone=" + phone + "&usage=RESET_PASSWORD";
            mvcApp.ajax.busyPost(urls.resendSms, data, function () {
                smsSendSuccess();
            }, "发送中...", true);
        });
        $('.eye').click(function () {
            var $div = $(this).closest('.login-items');
            if ($(this).hasClass('open-eye') === true) {
                $(this).removeClass('open-eye');
                $div.find('.hide-pwd').show();
                $div.find('.show-pwd').hide();
            } else {
                $(this).addClass('open-eye');
                $div.find('.show-pwd').show();
                $div.find('.hide-pwd').hide();
            }
        });

        $('.pwd').change(function () {
            var $div = $(this).closest('.password-items');
            var val = $(this).val();
            $div.find('input').val(val);
        });

        $("#nextBtn").click(function () {
            if (validateForget() === false) {
                return;
            }
            var username = $.trim($("#txtUsername").val());
            var imgCaptcha = $.trim($("#txtCaptcha").val());
            var data = "phone=" + username + "&captcha=" + imgCaptcha + "&usage=RESET_PASSWORD";
            mvcApp.ajax.busyPost(urls.sendSms, data, function (result) {
                if (result.success === true) {
                    smsSendSuccess();
                    $("#tableForget").hide();
                    $("#tableReset").show();
                    $('.login-tab').find('h2').text('设置新密码');
                } else {
                    mvcApp.notification.alert(null, result.message);
                    refreshCaptcha();
                }
            }, "发送中...", false);
        });

        $("#btnReset").click(function () {
            if (validateReset() === false) {
                return;
            }
            var username = $.trim($("#txtUsername").val());
            var smsCaptcha = $.trim($("#txtSmsCaptcha").val());
            var password = $.trim($("#txtPassword").val());
            var data = "username=" + username + "&password=" + password + "&smsCaptcha=" + smsCaptcha;
            mvcApp.notification.busy("登陆中...");
            mvcApp.ajax.post(urls.resetPassword, data, function (result) {
                var loginResult = result.value;
                if (result.success && loginResult != null) {
                    mvcApp.login.isUsingSms = function () {
                        return true;
                    };
                    mvcApp.login.getSmsUser = function () {
                        return {
                            phone: username,
                            smsCaptcha: smsCaptcha,
                            smsUsage: "RESET_PASSWORD"
                        }
                    };
                    if (loginResult.didBindWeixin) {
                        mvcApp.notification.busy(false);
                        mvcApp.login._showWeixin(loginResult.nickname, loginResult.imageUrl, function () {
                            toIndex();
                        })
                    } else {
                        toIndex();
                    }

                } else {
                    mvcApp.notification.busy(false);
                    mvcApp.notification.alert(null, result.message)
                }
            }, false);

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

    function toIndex() {
        var returnUrl = $("#hfReturnUrl").val();
        if (/^\/dealer.*$/.test(returnUrl)) {
            window.location.replace(urls.dealerHome);
        } else if(/^\/pe.*$/.test(returnUrl)){
            window.location.replace(urls.peHome);

        }else {
            window.location.replace(urls.customerHome);
        }
    }

    function refreshCaptcha() {
        $(".captcha").attr("src", "/captcha/refresh?ts=" + new Date().getTime());
    }

    function validateForget() {
        var username = $.trim($("#txtUsername").val());
        var imgCaptcha = $.trim($("#txtCaptcha").val());
        if (username === '') {
            mvcApp.notification.toastError("手机号不能为空");
            return false;
        }
        if (!mvcApp.isPhoneNumber(username)) {
            mvcApp.notification.toastError("手机号不正确");
            return false;
        }
        if (imgCaptcha === "") {
            mvcApp.notification.toastError("请输入图片验证码");
            return false;
        }
        return true;
    }

    function validateReset() {
        var password = $.trim($("#txtPassword").val());
        var passwordSure = $.trim($("#txtPasswordSure").val());
        var smsCaptcha = $.trim($("#txtSmsCaptcha").val());
        if (smsCaptcha === '') {
            mvcApp.notification.toastError("请输入短信验证码");
            return false;
        }
        if (password === '') {
            mvcApp.notification.toastError("密码不能为空");
            return false;
        }
        if (passwordSure === '') {
            mvcApp.notification.toastError("请再次输入密码");
            return false;
        }
        if (!validatePassword(password) || !validatePassword(passwordSure)) {
            mvcApp.notification.toastError("密码为6-20位字母、数字或下划线");
            return false;
        }
        if (password !== passwordSure) {
            mvcApp.notification.toastError("密码输入不一致");
            return false;
        }
        return true;
    }

    function validatePassword(input) {
        return /^\w{6,20}$/.test(input);
    }

})();