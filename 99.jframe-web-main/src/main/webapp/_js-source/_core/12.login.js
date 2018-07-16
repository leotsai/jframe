/**
 * Created by Leo on 2017/11/10.
 */
(function () {
    var loggedInEvent = "ON_USER_LOGGED_IN";

    var urls = {
        refreshCaptcha: "/captcha/refresh",
        sendCaptcha: "/captcha/send",
        appLoginForm: "/app/login/form",
        pcLoginForm: "/admin/login/form",
        appLogin: "/app/login/doLogin",
        smsLogin: "/app/smsLogin/doLogin",
        adminLogin: "/admin/login/doLogin",
        registerUrl: "/app/register",
        appFindPassword: "/app/findPassword",
        weixinDialog: "/app/weixinDialog",
        loginUrl: "/app/login"
    };
    var _successCallback = null;
    var _cancelCallback = null;

    var _passwordUser = null;
    var _smsUser = null;

    mvcApp.login = {
        onLoggedIn: function (handler) {
            $(document).unbind(loggedInEvent).bind(loggedInEvent, function () {
                handler && handler();
            });
        },
        _showLogin: function (successCallback, cancelCallback) {
            _successCallback = successCallback;
            _cancelCallback = cancelCallback;
            if (mvcApp.isPc === true) {
                showPcDialog();
            } else {
                showAppDialog();
            }
        },
        _submit: function () {
            if (mvcApp.login.isUsingSms() === true) {
                loginBySms();
            } else {
                loginByPassword();
            }
        },
        _showWeixin: function (nickname, imageUrl, callback) {
            showWeixinDialog(nickname, imageUrl, callback)
        },
        _sendSms: function (captcha, phone, usage, $btnSendSms, resetCallback) {
            sendSmsCaptcha(captcha, phone, usage, $btnSendSms, resetCallback)
        },
        loginSuccess: function () {

        },
        errorNotification: function (message) {
            mvcApp.notification.toast(message);
        },
        isUsingSms: function () {

        },
        getPasswordUser: function () {

        },
        getSmsUser: function () {

        },
        get$Captcha: function () {

        }
    };

    function showPcDialog() {
        mvcApp.notification.dialog("dialogLogin", "请登陆", {
            buttons: {
                "登录": function () {
                    mvcApp.login._submit();
                }
            },
            open: function () {
                mvcApp.ajax.load("#dialogLogin", urls.pcLoginForm, function () {
                    bindFormEvents();
                })
            },
            close: function () {
                $("#dialogLogin").remove();
                _cancelCallback && _cancelCallback();
            }
        })
    }

    function showAppDialog() {
        var html = '<div id="loginPanel" class="loading">加载中...</div>';
        mvcApp.notification.dialog("dialogLogin", "请登录", html, {
            "取消": function () {
                $("#dialogLogin").remove();
                _cancelCallback && _cancelCallback();
            },
            "登录": function () {
                mvcApp.login._submit();
            }
        });
        mvcApp.ajax.load("#loginPanel", urls.appLoginForm, function () {
            $("#loginPanel").removeClass("loading");
            if (/^\/dealer.*$/.test(getReturnUrl()) || /^\/pe.*$/.test(getReturnUrl())) {
                $("#btnRegister").hide();
            }
            bindFormEvents();
            var $wrap = $("#dialogLogin .dialog-wrap");
            var top = $wrap.outerHeight() / 2;
            $wrap.css("margin-top", -top);
        });
    }

    function bindFormEvents() {
        $(".captcha").click(function () {
            refreshCaptcha();
        });
        mvcApp.login.loginSuccess = function () {
            mvcApp.notification.busy(false);
            $("#dialogLogin").remove();
            $(document).trigger(loggedInEvent);
            _successCallback && _successCallback();
        };
        if (mvcApp.isPc === true) {
            bindPcFormEvent();
        } else {
            bindAppFormEvent();
        }

    }

    function bindPcFormEvent() {
        mvcApp.login.errorNotification = function (message) {
            $('#errmes').text(message).css({"padding": "10px 0 0 0", "color": "#C20000"});
        };
        mvcApp.login.get$Captcha = function () {
            return $("#captcha");
        };
        mvcApp.login.getPasswordUser = function () {
            return {
                username: $.trim($("#txtUsername").val()),
                password: $.trim($("#txtPassword").val()),
                captcha: $.trim($("#txtCaptcha").val())
            }
        };
        pcAccountInit();
        mvcApp.utils.onEnterKeydown("#formLogin input", function () {
            mvcApp.login._submit();
        });
        $('.form-group input').focus(function () {
            $(this).closest('.form-group').addClass('focus');
        }).blur(function () {
            if ($(this).val()) {
                $(this).closest('.form-group').addClass('focus');
            } else {
                $(this).closest('.form-group').removeClass('focus');
            }
        });
        $('.remember-btn').click(function () {
            var $label = $(this).closest('label');
            if ($label.hasClass('check')) {
                $label.removeClass('check');
            } else {
                $label.addClass('check');
            }
        });
    }

    function bindAppFormEvent() {
        mvcApp.login.isUsingSms = function () {
            return $(".tab-headers li.mes").hasClass("active");
        };
        mvcApp.login.get$Captcha = function () {
            return $("#captcha");
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
        $("#btnSendSms").click(function () {
            var captcha = $.trim($("#txtImgCaptcha").val());
            var phone = $.trim($("#txtPhone").val());
            var $btnSendSms = $(this);
            var usage = "SMS_LOGIN";
            if ($btnSendSms.hasClass("btn-send")) {
                return;
            }
            sendSmsCaptcha(captcha, phone, usage, $btnSendSms, function () {
                $btnSendSms.html("发送验证码");
                refreshCaptcha();
            })
        });

        $('.tab-headers li').click(function () {
            var index = $(this).index();
            $(this).addClass('active').siblings().removeClass("active");
            var $liContent = $('.tab-contents li').eq(index);
            $liContent.siblings().hide();
            $liContent.show();
        });

        $("#btnFindPassword").click(function () {
            window.location.href = urls.appFindPassword + "?returnUrl=" + getReturnUrl();
        });

        $("#btnRegister").click(function () {
            window.location.href = urls.registerUrl + "?returnUrl=" + getReturnUrl();
        });
    }

    function loginByPassword() {
        _passwordUser = mvcApp.login.getPasswordUser();
        if (validateByPassword() === false) {
            return;
        }
        var loginUrl = urls.appLogin;
        if (mvcApp.isPc) {
            loginUrl = urls.adminLogin;
            doRememberMe();
        }
        var data = "username=" + _passwordUser.username
            + "&password=" + _passwordUser.password
            + "&captcha=" + _passwordUser.captcha
            + "&password2=" + ($.md5 == undefined ? "" : $.md5(_passwordUser.password));
        mvcApp.notification.busy("登陆中...");
        mvcApp.ajax.post(loginUrl, data, function (result) {
            if (result.success === true) {
                handleLoginSuccess(result.value);
            } else {
                mvcApp.notification.busy(false);
                if (result.code == mvcApp.resultCode.WRONG_PASSWORD) {
                    mvcApp.login.get$Captcha().show();
                }
                refreshCaptcha();
                mvcApp.login.errorNotification(result.message);
            }
        }, false);
    }

    function loginBySms() {
        _smsUser = mvcApp.login.getSmsUser();
        if (validateBySms() === false) {
            return;
        }
        var data = "username=" + _smsUser.phone + "&smsCaptcha=" + _smsUser.smsCaptcha;
        mvcApp.notification.busy("登陆中...");
        mvcApp.ajax.post(urls.smsLogin, data, function (result) {
            if (result.success === true) {
                handleLoginSuccess(result.value)
            } else {
                mvcApp.notification.busy(false);
                mvcApp.login.errorNotification(result.message);
            }
        }, false);
    }

    function handleLoginSuccess(loginResult) {
        if (loginResult != null && loginResult.didBindWeixin === true) {
            mvcApp.notification.busy(false);
            showWeixinDialog(loginResult.nickname, loginResult.imageUrl, function () {
                mvcApp.login.loginSuccess && mvcApp.login.loginSuccess();
            })
        } else {
            mvcApp.login.loginSuccess && mvcApp.login.loginSuccess();
        }
    }

    function showWeixinDialog(nickname, imageUrl, callback) {
        if (mvcApp.isPc === true) {
            return;
        }
        $("#dialogLogin").hide();
        var html = '<div id="weixinPanel" class="loading">加载中...</div>';
        mvcApp.notification.dialog("weixinDialog", " ", html, {});
        mvcApp.ajax.load("#weixinPanel", urls.weixinDialog, function () {
            $("#weixinPanel").removeClass("loading");
            $("#weixinPanel .weixin-head-img").attr("src", imageUrl);
            $("#weixinPanel .wexin-name").text(nickname);
            $("#weixinPanel .btnSure").click(function () {
                $("#weixinDialog").remove();
                callback && callback();
            });
            var $wrap = $("#weixinDialog .dialog-wrap");
            var top = $wrap.outerHeight() / 2;
            $wrap.css("margin-top", -top);
        });
    }

    function doRememberMe() {
        var rememberCookieName = mvcApp.cookieNames.REMEMBERME;
        if ($("#lbRemember").hasClass("check")) {
            var toDay = new Date();
            toDay.setFullYear(toDay.getFullYear() + 1);
            mvcApp.utils.setCookie(rememberCookieName, _passwordUser.username, toDay);
        } else {
            mvcApp.utils.removeCookie(rememberCookieName);
        }
    }

    function pcAccountInit() {
        var lastAccount = mvcApp.utils.getCookieValue(mvcApp.cookieNames.REMEMBERME);
        if (lastAccount == null || lastAccount == '') {
            return;
        }
        $("#lbRemember").addClass("check");
        $("#txtUsername").val(lastAccount);
        $("#txtUsername").closest('.form-group').addClass('focus');
    }

    function sendSmsCaptcha(captcha, phone, usage, $btnSendSms, resetCallback) {
        if ($btnSendSms.hasClass("has-send")) {
            return;
        }
        if (captcha === "") {
            mvcApp.login.errorNotification("请输入图形验证码");
            return;
        }
        if (mvcApp.isPhoneNumber(phone) === false) {
            mvcApp.login.errorNotification("请输入正确的手机号码");
            return;
        }
        var data = "captcha=" + captcha + "&phone=" + phone + "&usage=" + usage;
        mvcApp.ajax.post(urls.sendCaptcha, data, function (result) {
            if (result.success === true) {
                mvcApp.notification.toast('短信验证码发送成功');
                $btnSendSms.html('60s');
                $btnSendSms.addClass("has-send");
                (function countdown(seconds) {
                    setTimeout(function () {
                        seconds--;
                        $btnSendSms.html(seconds + 's');
                        if (seconds <= 0) {
                            $btnSendSms.removeClass("has-send");
                            resetCallback && resetCallback();
                        } else {
                            countdown(seconds);
                        }
                    }, 1000)
                })(60)
            } else {
                mvcApp.login.errorNotification(result.message);
                resetCallback && resetCallback();
            }
        }, false)
    }

    function refreshCaptcha() {
        $(".captcha").attr("src", urls.refreshCaptcha + "?ts=" + new Date().getTime());
    }

    function getReturnUrl() {
        return location.pathname + location.search;
    }

    function validateByPassword() {
        var username = _passwordUser.username;
        var password = _passwordUser.password;
        var captcha = _passwordUser.captcha;
        if (username === '' || password === '') {
            mvcApp.login.errorNotification("请输入账号密码");
            return false;
        }
        if (mvcApp.login.get$Captcha().is(":visible") && captcha === '') {
            mvcApp.login.errorNotification("请输入验证码");
            return false;
        }
        return true;
    }

    function validateBySms() {
        var phone = _smsUser.phone;
        var smsCaptcha = _smsUser.smsCaptcha;
        if (mvcApp.isPhoneNumber(phone) === false) {
            mvcApp.login.errorNotification("请输入正确的手机号码");
            return false;
        }
        if (smsCaptcha === '') {
            mvcApp.login.errorNotification("请输入短信验证码");
            return false;
        }
        return true;
    }

})();