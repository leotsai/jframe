(function () {
    if (window.mvcApp) {
        return;
    }
    if (window.appConfig == undefined) {
        window.appConfig = {
            cdn: "http://cdn.daben100.com",
            mock: false,
            authCookieName: '.WUPINKUAUTH'
        };
    }
    if (window.console == undefined) {
        window.console = {
            log: function () {
            }
        };
    }
    window.mvcApp = {
        isPc: null,
        mock: window.appConfig.mock,
        guid: {
            empty: '00000000-0000-0000-0000-000000000000'
        },
        ajax: {},
        notification: {},
        utils: {},
        getImageUrl: function (imageKey, style) {
            return appConfig.cdn + "/" + imageKey + "?x-oss-process=style/" + style;
        },
        imageStyles: {
            banner: "banner",//首页轮播图，900X500
            s60x60: "s60x60", // 60X60像素，用于后台上传控件
            full: "full",//原始尺寸，不进行压缩
            max1024: "max-1024",//最大宽度1024
            avartar50: "avartar-50",//头像，100X100
            s450x450: "s450x450",//用于商品详情轮播图，宽度900X900
            s80x80: "s80x80",//160x160
            s100x100: "s100x100",//200x200
            s120x120: "s120x120",//240x240
            s150x150: "s150x150",//300x300
            w450: "w450",//用于商品详情，以及其他所有HTML页使用图片，最大宽度为900px,
            convertToFull: function (url) {
                if (url == undefined) {
                    return url;
                }
                var index = url.lastIndexOf('style/');
                if (index === -1) {
                    return url;
                }
                return url.substr(0, index + 6) + "full";
            }
        },
        resultCode: {
            WRONG_PASSWORD: '1001',//密码错误
            NOT_AUTHENTICATED: '4001',//未登录或session过期，但是访问的url需要登录
            INSUFFICIENT_PERMISSION: '4002',//已登录，但是权限不足
            REQUIRE_DSN: '4003'//请求中需要DSN信息
        },
        isInWeixin: function () {
            try {
                return navigator.appVersion.indexOf("MicroMessenger") > -1;
            }
            catch (e) {
                return false;
            }
        },
        isOrderSerialNumber: function (input) {
            // D170929000001999
            return input.indexOf("D") === 0 && input.length === 16;
        },
        isPhoneNumber: function (input) {
            return /^1[34578]\d{9}$/.test(input);
        },
        isIdCardNumber: function (input) {
            return /^\d{17}[\d|x]|\d{15}$/.test(input);
        },
        timeFormat: function (time) {
            return time.split(' ')[0];
        },
        cookieNames: {
            REMEMBERME: "REMEMBERED_ACCOUNT",
            WUPINKUAUTH: window.appConfig.authCookieName
        },
        dsnUrl: function (subUrl) {
            return "/dealer/dsn-" + getDsnFromLocation() + (subUrl == undefined ? "" : subUrl);
        },
        dealerPayOrderDsnUrl: function (subUrl) {
            if (subUrl.indexOf("/") == 0) {
                subUrl = subUrl.substring(1);
            }
            return "/dealer/payOrder/dsn-" + getDsnFromLocation() + "-" + (subUrl == undefined ? "" : subUrl);
        }
    };
    window.page = {};

    if (mvcApp.mock) {
        alert("使用mock数据！！！");
    }

    function getDsnFromLocation() {
        var url = "";
        if (location.pathname.indexOf("/dealer/dsn-") === 0) {
            url = location.pathname.substr(12);
        } else if (location.pathname.indexOf("/dealer/payOrder/dsn-") === 0) {
            url = location.pathname.substr(21);
        }
        url = url.split("-")[0];
        if (url.indexOf("/") > -1) {
            return url.substr(0, url.indexOf("/"));
        }
        return url;
    }
})();