(function () {
    var urls = {
        refreshCaptcha: "/captcha/refresh",
        customerHome: "/app",
        dealerHome: "/dealer",
        sendSms: "/captcha/send",
        resendSms: "/captcha/resend",
        register: "/app/register/doRegister"
    };

    $(document).ready(function () {
        var $btnSendSms = $("#btnSendSms");

        $('#txtUsername').focus(function () {
            $(this).next('.close-btn').show();
        });
        $('.close-btn').click(function () {
            $(this).closest('.login-items').find('input').val('');
        });
        $(document).click(function (e) {
            if (e.target.className !== "close-btn" && e.target.className !== 'phone-number') {
                $('#txtUsername').next('.close-btn').hide();
            }
        });
        $('.check').click(function () {
            if ($(this).hasClass('checked') === true) {
                $(this).removeClass('checked');
            } else {
                $(this).addClass('checked');
            }
        });

        $('#nextBtn').click(function () {
            if (validateFnone() === false) {
                return;
            }
            var imgCaptcha = $.trim($("#txtCaptcha").val());
            var phone = $.trim($("#txtUsername").val());
            var data = "captcha=" + imgCaptcha + "&phone=" + phone + "&usage=REGISTER";
            mvcApp.ajax.busyPost(urls.sendSms, data, function (result) {
                if (result.success === true) {
                    smsSendSuccess();
                    $('#firstStep').hide();
                    $('#nextStep').show();
                    var hidePhone = phone.substr(0, 3) + '****' + phone.substr(7);
                    $('.login-tab').addClass('reg');
                    $('.login-tab').find('h2').text(hidePhone);
                } else {
                    mvcApp.notification.alert(null, result.message);
                    refreshCaptcha();
                }
            }, "发送中...", false);
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

        $btnSendSms.click(function () {
            if ($btnSendSms.hasClass("has-send")) {
                return;
            }
            var phone = $.trim($("#txtUsername").val());
            var data = "phone=" + phone + "&usage=REGISTER";
            mvcApp.ajax.busyPost(urls.resendSms, data, function () {
                smsSendSuccess();
            }, "发送中...", true);
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
        mvcApp.notification.busy("注册中...");
        mvcApp.ajax.post(urls.register, data, function (result) {
            var loginResult = result.value;
            if (result.success && loginResult != null) {
                mvcApp.login.isUsingSms = function () {
                    return true;
                };
                mvcApp.login.getSmsUser = function () {
                    return {
                        phone: username,
                        smsCaptcha: smsCaptcha,
                        smsUsage: "REGISTER"
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
    }

    function toIndex() {
        if (/^\/dealer.*$/.test($("#hfReturnUrl").val())) {
            window.location.replace(urls.dealerHome);
        } else {
            window.location.replace(urls.customerHome);
        }
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
        if (!$('.check').hasClass('checked')) {
            mvcApp.notification.toastError("请先阅读同意《五品库会员服务协议》");
            return false;
        }
        return true;
    }

    function validateTwo() {
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