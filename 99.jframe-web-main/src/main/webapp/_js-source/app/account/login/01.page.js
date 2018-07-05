(function () {
    var urls = {
        refreshCaptcha: "/captcha/refresh",
        login: "/app/login",
        home: "/app"
    };

    $(document).ready(function () {
        $("#navLogin").addClass("selected");

        $('.change-btn').click(function () {
            var text = $(this).text();
            if(text === "使用快捷登录"){
                $(this).closest('.login-tab').find('h2').text('快捷登录');
                $(this).text('使用密码登录');
                $('#passwordLogin').hide();
                $('#codeLogin').show();
            } else {
                $(this).closest('.login-tab').find('h2').text('密码登录');
                $(this).text('使用快捷登录');
                $('#passwordLogin').show();
                $('#codeLogin').hide();


            }
        });
        $('.login-switch ').click(function () {
            $(this).addClass('select-font').siblings().removeClass('select-font');
            $(this).find('.select-style').show();
            $(this).siblings().find('.select-style').hide();
            var text =$.trim($(this).text());
            if(text==="密码登录"){
                $('#passwordLogin').show();
                $('#codeLogin').hide();
            }
            else {
                $('#passwordLogin').hide();
                $('#codeLogin').show();
            }
        });

        $(".captcha").click(function () {
            refreshCaptcha();
        });

        $("#btnSendSms").click(function () {
            var captcha = $.trim($("#txtImgCaptcha").val());
            var phone = $.trim($("#txtPhone").val());
            var $btnSendSms = $(this);
            var usage = "SMS_LOGIN";
            if ($btnSendSms.hasClass("btn-send")) {
                return;
            }
            mvcApp.login._sendSms(captcha, phone, usage, $btnSendSms, function () {
                $btnSendSms.html("发送验证码");
                refreshCaptcha();
            })
        });

        $('#txtUsername').focus(function () {
            $(this).next('.close-btn').show();
        });
        $('.close-btn').click(function () {
            $(this).closest('.login-items').find('input').val('');
        });
        $(document).click(function (e) {
            if(e.target.className !== "close-btn" && e.target.className !== 'phone-number'){
                $('#txtUsername').next('.close-btn').hide();
            }
        });
        mvcApp.login.isUsingSms = function () {
            return $("#codeLogin").is(":visible");
        };
        mvcApp.login.loginSuccess = function () {
            var returnUrl = $("#hfReturnUrl").val();
            if (returnUrl === "") {
                returnUrl = urls.home;
            }
            window.location.replace(returnUrl);
        };

        mvcApp.login.getPasswordUser = function () {
            return {
                username: $.trim($("#txtUsername").val()),
                password: $.trim($("#txtPassword").val()),
                captcha: $.trim($("#txtCaptcha").val())
            }
        };

        mvcApp.login.getSmsUser = function () {
            return {
                phone: $.trim($("#txtPhone").val()),
                smsCaptcha: $.trim($("#txtSmsCaptcha").val()),
                smsUsage: "SMS_LOGIN"
            }
        };

        mvcApp.login.get$Captcha = function () {
          return $("#captcha");
        };

        $("#btnLogin").click(function () {
            login();
        });

        $("#btnSmsLogin").click(function () {
            smsLogin();
        });

    });

    function login() {
        if (validate() === false) {
            return;
        }
        mvcApp.login._submit()
    }

    function smsLogin() {
        if (validateSms() === false) {
            return;
        }
        mvcApp.login._submit();
    }

    function refreshCaptcha() {
        $(".captcha").attr("src", urls.refreshCaptcha + "?ts=" + new Date().getTime());
    }

    function validate() {
        var username = $.trim($("#txtUsername").val());
        var password = $.trim($("#txtPassword").val());
        var captcha = $.trim($("#txtCaptcha").val());
        if (username === '' || password === '') {
            mvcApp.notification.toastError("请输入账号密码");
            return false;
        }
        if ($("#captcha").is(":visible") && captcha === '') {
            mvcApp.notification.toastError("请输入验证码");
            return false;
        }
        return true;
    }

    function validateSms() {
        var phone = $.trim($("#txtPhone").val());
        var smsCaptcha = $.trim($("#txtSmsCaptcha").val());
        if (phone === '') {
            mvcApp.notification.toastError("请输入手机号");
            return false;
        }
        if (!mvcApp.isPhoneNumber(phone)) {
            mvcApp.notification.toastError("手机号不正确");
            return false;
        }
        if (smsCaptcha === '') {
            mvcApp.notification.toastError("请输入短信验证码");
            return false;
        }
        return true;
    }
})();