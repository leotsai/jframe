/**
 * Created by huxiaoli on 2018/1/9.
 */


(function () {
    var urls = {
        refreshCaptcha: "/captcha/refresh",
        sendCaptcha: "/captcha/send",
        smsLogin: "/app/smsLogin/doLogin",
        loginUrl: "/app/login",
        resend: "/captcha/resend"
    };
    mvcApp.quickLogin = {};
    mvcApp.quickLogin.show = function (title, btnText, callback) {
        var html = '<div id="loginDialog" class="login-dialog-box">\
            <div class="login-dialog-content">\
                <a href="javascript:;" id="closeBtn" class="login-dialog-close-btn"></a>\
                     <div class="login-dialog-inner">\
                        <div class="login-dialog-title">\
                            <span>' + title + '</span>\
                            <a href="' + urls.loginUrl + '?returnUrl=' + location.pathname + location.search + '">老用户登录</a>\
                        </div>\
                        <div class="login-dialog-form">\
                            <div class="login-dialog-form-cell"><input type="text" id="txtUsername" name="username" class="phone" placeholder="请输入手机号"/></div>\
                            <div class="login-dialog-form-cell"><input type="text" id="txtCaptcha" name="captcha" class="code" placeholder="图片验证码"/><img src="' + urls.refreshCaptcha + '" class="captcha"/></div>\
                        </div>\
                        <div class="login-dialog-btn">\
                            <a href="javascript:;" id="btnSure">确定</a>\
                        </div>\
                    </div>\
                </div>\
            </div>';
        $("#body").append(html);
        var $wrap = $('.login-dialog-content');
        var $btnSure = $('#btnSure');
        $wrap.css("margin-top", -$wrap.outerHeight() / 2).css("left", ($("#loginDialog").innerWidth() - $wrap.outerWidth()) / 2);
        $('.login-dialog-inner input').keyup(function () {
            $('.error-msg').remove();
        });
        $btnSure.click(function () {
            $('.error-msg').remove();
            var phoneNumber = $('#txtUsername').val();
            var txtCaptcha = $('#txtCaptcha').val();
            if (mvcApp.isPhoneNumber(phoneNumber) === false || phoneNumber == '') {
                showError('请输入正确的手机号码！');
                refreshCaptcha();
                return;
            }
            if (txtCaptcha == '') {
                showError('请输入图形验证码！');
                refreshCaptcha();
                return;
            }
            var data = "captcha=" + txtCaptcha + "&phone=" + phoneNumber + '&usage=SMS_LOGIN';
            mvcApp.ajax.post(urls.sendCaptcha, data, function (result) {
                if (result.success === true) {
                    nextStep(phoneNumber, btnText, callback);
                } else {
                    showError(result.message);
                    refreshCaptcha();
                }
            });
        });
        $('.captcha').click(function () {
            refreshCaptcha();
        });
        $('#closeBtn').click(function () {
            $('#loginDialog').remove();
        });
    };

    function refreshCaptcha() {
        $(".captcha").attr("src", urls.refreshCaptcha + "?ts=" + new Date().getTime());
    }

    function showError(msg) {
        if ($('.login-dialog-form>.error-msg').length > 0) {
            $('.login-dialog-form>.error-msg').html(msg);
        } else {
            $('.login-dialog-form').append('<div class="error-msg">' + msg + '</div>');
        }
    }

    function nextStep(phoneNumber, btnText, callback) {
        var html = '<div class="login-dialog-send-code">\
                        <div class="tip-text">短信验证码已发送到<span>' + phoneNumber + '</span></div>\
                        <div class="login-dialog-form">\
                            <div class="login-dialog-form-cell"><input type="text" id="smsCaptcha" name="captcha" placeholder="短信验证码" /><a href="javascript:;" id="btnSendSms" class="btn-send-sms">60s</a>\</div>\
                        </div>\
                        <div class="login-dialog-btn">\
                            <a href="javascript:;" id="btnNext">' + btnText + '</a>\
                        </div>\
                    </div>';
        $('.login-dialog-inner').html(html);
        var $btnSendSms = $('#btnSendSms');
        var $btnNext = $('#btnNext');

        countDown($btnSendSms, 60);

        $('.login-dialog-form input').keyup(function () {
            $('.error-msg').remove();
        });
        $btnSendSms.click(function () {
            if ($(this).hasClass('has-send')) {
                return;
            }
            var timer = countDown($(this), 60);
            var data = 'phone=' + phoneNumber + '&usage=SMS_LOGIN';
            mvcApp.ajax.post(urls.resend, data, function (result) {
                if (result.success === false) {
                    showError(result.message);
                    $btnSendSms.removeClass("has-send");
                    $btnSendSms.html('重新获取');
                    clearTimeout(timer);
                }
            });
        });
        $btnNext.click(function () {
            var smsCaptcha = $('#smsCaptcha').val();
            if (smsCaptcha == '') {
                showError('请输入短信验证码！');
                return;
            }
            var data = "username=" + phoneNumber + "&smsCaptcha=" + smsCaptcha;
            mvcApp.ajax.post(urls.smsLogin, data, function (result) {
                if (result.success) {
                    $('#loginDialog').remove();
                    callback && callback();
                } else {
                    showError(result.message);
                }
            })
        });
    }

    function countDown($btnSendSms, seconds) {
        $btnSendSms.addClass("has-send");
        $btnSendSms.html(seconds + 's');
        var timer = setTimeout(function () {
            seconds--;
            $btnSendSms.html(seconds + 's');
            if (seconds <= 0) {
                $btnSendSms.removeClass("has-send");
                $btnSendSms.html('重新获取');
            } else {
                countDown($btnSendSms, seconds);
            }
        }, 1000);
        return timer;
    }
})();