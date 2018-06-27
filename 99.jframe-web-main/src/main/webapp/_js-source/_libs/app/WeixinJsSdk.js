(function () {
    if (window.WeixinJsSdk) {
        return;
    }


    var userId = '';
    var isInitialized = false;
    var initializing = false;
    var initialCallbacks = [];
    var urlConfigOptions = "/app/weixin/getConfigOptions";
    var jsApiList = [
        'onMenuShareTimeline',
        'onMenuShareAppMessage',
        'onMenuShareQQ',
        'onMenuShareWeibo',
        'onMenuShareQZone',
        'startRecord',
        'stopRecord',
        'onVoiceRecordEnd',
        'playVoice',
        'pauseVoice',
        'stopVoice',
        'onVoicePlayEnd',
        'uploadVoice',
        'downloadVoice',
        'chooseImage',
        'previewImage',
        'getLocalImgData',
        'uploadImage',
        'downloadImage',
        'translateVoice',
        'getNetworkType',
        'openLocation',
        'getLocation',
        'hideOptionMenu',
        'showOptionMenu',
        'hideMenuItems',
        'showMenuItems',
        'hideAllNonBaseMenuItem',
        'showAllNonBaseMenuItem',
        'closeWindow',
        'scanQRCode',
        'chooseWXPay',
        'openProductSpecificView',
        'addCard',
        'chooseCard',
        'openCard'
    ];

    window.WeixinJsSdk = function () {

    };

    WeixinJsSdk.isInWeixin = function () {
        return mvcApp.isInWeixin();
    };

    WeixinJsSdk.registerJsApiList = function (array) {
        jsApiList = array;
    };


    WeixinJsSdk.onShare = function (title, link, imgUrl, description, successCallback, cancelCallback) {
        if (description == null || description === '') {
            description = title;
        }
        if (description.length > 100) {
            description = description.substring(0, 100) + "...";
        }

        WeixinJsSdk.ready(function () {
            link = addUserIdToLink(link);
            wx.onMenuShareTimeline({
                title: title,
                link: link,
                imgUrl: imgUrl,
                success: function () {
                    successCallback && successCallback();
                },
                cancel: function () {
                    cancelCallback && cancelCallback();
                }
            });

            wx.onMenuShareAppMessage({
                title: title,
                desc: description,
                link: link,
                imgUrl: imgUrl,
                type: 'link',
                dataUrl: '',
                success: function () {
                    successCallback && successCallback();
                },
                cancel: function () {
                    cancelCallback && cancelCallback();
                }
            });

            wx.onMenuShareQQ({
                title: title,
                desc: description,
                link: link,
                imgUrl: imgUrl,
                success: function () {
                    successCallback && successCallback();
                },
                cancel: function () {
                    cancelCallback && cancelCallback();
                }
            });

            wx.onMenuShareWeibo({
                title: title,
                desc: description,
                link: link,
                imgUrl: imgUrl,
                success: function () {
                    successCallback && successCallback();
                },
                cancel: function () {
                    cancelCallback && cancelCallback();
                }
            });

            wx.onMenuShareQZone({
                title: title,
                desc: description,
                link: link,
                imgUrl: imgUrl,
                success: function () {
                    successCallback && successCallback();
                },
                cancel: function () {
                    cancelCallback && cancelCallback();
                }
            });

        });
    };

    WeixinJsSdk.ready = function (func) {
        if (!WeixinJsSdk.isInWeixin()) {
            return;
        }
        if (isInitialized) {
            func && func();
        } else {
            if (func != undefined) {
                initialCallbacks.push(func);
            }
            if (initializing == true) {
                return;
            }
            initializing = true;
            initializeWeixin();
        }
    };

    WeixinJsSdk.closeWindow = function () {
        WeixinJsSdk.ready(function () {
            wx.closeWindow();
        });
    };

    WeixinJsSdk.showScan = function (callback) {
        WeixinJsSdk.ready(function () {
            wx.scanQRCode({
                needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
                scanType: ["qrCode"], // 可以指定扫二维码还是一维码，默认二者都有
                success: function (result) {
                    // 当needResult 为 1 时，扫码返回的结果
                    callback(result.resultStr);
                }
            });
        });
    };

    var initializeWeixin = function () {
        getConfigOptions(function (options) {
            wx.config({
                debug: false,
                appId: options.appId,
                timestamp: options.timestamp,
                nonceStr: options.nonceStr,
                signature: options.signature,
                jsApiList: jsApiList
            });

            wx.ready(function () {
                isInitialized = true;
                initializing = false;
                for (var fi = 0; fi < initialCallbacks.length; fi++) {
                    var func = initialCallbacks[fi];
                    func && func();
                }
            });

            wx.error(function (res) {
                var msg = "微信出错啦：";
                for (var p=0; p<res.length; p++) {
                    msg += '<br/>' + p + ": " + res[p];
                }
                mvcApp.notification.toast(msg);
            });
        });
    };

    function getConfigOptions(callback) {
        mvcApp.ajax.post(urlConfigOptions, null, function (result) {
            if (result.success) {
                var value = result.value;
                userId = value.userId;
                callback({
                    appId: value.appId,
                    timestamp: value.timestamp,
                    nonceStr: value.nonceStr,
                    signature: value.signature
                });
            } else {
                mvcApp.notification.toast(result.message);
            }
        });
    }
    function addUserIdToLink(link) {
        if (userId == '' || userId == null) {
            return link;
        }
        if (-1 === link.indexOf("?")) {
            return link + "?fu=" + userId;
        }
        var query = link.substring(link.lastIndexOf("?") + 1);
        var keyValues = query.split("&");
        var oldKeyValue = '';
        keyValues.forEach(function (p) {
            if (p.startsWith("fu=")) {
                oldKeyValue = p;

            }
        });
        if (oldKeyValue === '') {
            return link + "&fu=" + userId;
        }
        return link.replace(new RegExp(oldKeyValue, "g"), "fu=" + userId);
    }
})();