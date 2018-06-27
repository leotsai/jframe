(function () {
    if (window.SmsCaptchaSender) {
        return;
    }
    window.SmsCaptchaSender = function ($tipNode) {
        this.$tipNode = $($tipNode);
        this.url = "/captcha/send";
        this.countdownSeconds = 60;
    };

    SmsCaptchaSender.prototype = {
        reset: function () {

        },
        _validate: function (captcha, phone) {
            var me = this;
            if ($.trim(captcha) === '') {
                me._onSendError("图形验证码不能为空");
                return false;
            }
            if ($.trim(phone) === '') {
                me._onSendError("手机号不能为空");
                return false;
            }
            if (!mvcApp.isPhoneNumber(phone)) {
                me._onSendError("手机号不正确");
                return false;
            }
            return true;
        },
        send: function (captcha, phone, usage) {
            var me = this;
            if (!me._validate(captcha, phone)) {
                return;
            }
            var data = "captcha=" + $.trim(captcha) + "&phone=" + $.trim(phone) + "&usage=" + usage;
            mvcApp.ajax.post(me.url, data, function (result) {
                if (result.success) {
                    me._onSendSuccess();
                } else {
                    me._onSendError(result.message);
                }
            }, false)
        },
        _onSendSuccess: function () {
            var me = this;
            mvcApp.notification.toast('短信验证码发送成功');
            me.$tipNode.html(this.countdownSeconds + 's');
            (function countdown(seconds) {
                setTimeout(function () {
                    seconds--;
                    me.$tipNode.html(seconds + 's');
                    if (seconds <= 0) {
                        me.reset();
                    } else {
                        countdown(seconds);
                    }
                }, 1000)
            })(this.countdownSeconds)
        },
        _onSendError: function (errorMsg) {
            mvcApp.notification.alertInfo(errorMsg);
            this.reset();
        }
    };

})();