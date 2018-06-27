/**
 * Created by Leo on 2017/10/17.
 */
(function () {
    var urls = {
        getByFullUrl: '/admin/scanIn/getByFullUrl'
    };

    var BATCH_LENGTH = 2;//批次位数
    var NUMBER_CODE_LENGTH = 5; //箱码和瓶码的62进制编码之后的位数
    var BOX_NUMBER_LENGTH = 6;//箱码明码数字位数
    var BOTTLE_NUMBER_LENGTH = 8;//箱码明码数字位数

    var CHARS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

    function decode62(code) {
        var result = 0;
        for (var ci = 0; ci < code.length; ci++) {
            var value = CHARS.indexOf(code.charAt(ci));
            result = result * CHARS.length + value;
        }
        return result;
    }
    function getFixedString(number, fixedLength) {
        var str = number + "";
        while (str.length < fixedLength) {
            str = "0" + str;
        }
        return str;
    }
    mvcApp.qr = {
        getFullCode: function (fullUrl) {
            var url = $.trim(fullUrl);
            return url.substr(url.lastIndexOf("/") + 1);
        },
        getCode: function (fullUrl, callback) {
            this.getCodeObject(fullUrl, function (codeObject) {
                var code = null != codeObject ? codeObject.code : null;
                callback(code);
            });
        },
        isBoxCode: function (codeObject) {
            return codeObject.code == codeObject.boxCode;
        },
        getCodeObject: function (fullUrl, callback) {
            if (null == fullUrl || '' == fullUrl) {
                mvcApp.notification.alertError("fullUrl不能为空");
                return;
            }
            if (fullUrl.indexOf("http://wly.wupincool.com") > -1) {
                mvcApp.ajax.busyPost(urls.getByFullUrl, "fullUrl=" + fullUrl, function (result) {
                    if (result.success) {
                        var data = result.value;
                        data.isBoxCode = data.boxCode == data.code;
                        callback(data);
                    } else {
                        mvcApp.notification.alert("提示", "当前二维码不是五品库的防伪溯源二维码：" + fullCode);

                    }
                }, "查询中", true);
                return;
            }
            var fullCode = this.getFullCode(fullUrl);
            var batch = fullCode.substr(0, BATCH_LENGTH);
            var encodedNumber = fullCode.substr(BATCH_LENGTH, NUMBER_CODE_LENGTH);
            var tag = fullCode.substr(BATCH_LENGTH + NUMBER_CODE_LENGTH, 1);
            if (tag !== 'X' && tag !== 'T') {
                mvcApp.notification.alertError("当前编码不是一个正确的二维码编码：" + fullCode);
                return;
            }
            var isBoxCode = tag === 'X';
            var number = decode62(encodedNumber);
            if (isBoxCode) {
                var code = batch + getFixedString(number, BOX_NUMBER_LENGTH);
                callback({
                    code: code,
                    boxCode: code,
                    isBoxCode: true
                });
            }
            else {
                var boxNumber = Math.floor(number / 100);
                var bottleNumber = number % 100;
                var boxCode = batch + getFixedString(boxNumber.toFixed(0), BOX_NUMBER_LENGTH);
                var bottleCode = boxCode + getFixedString(bottleNumber.toFixed(0), BOTTLE_NUMBER_LENGTH - BOX_NUMBER_LENGTH);
                callback({
                    code: bottleCode,
                    boxCode: boxCode,
                    isBoxCode: false
                });
            }
        },
        getScanQrParameter: function (fullUrl, callback) {
            mvcApp.qr.getCodeObject(fullUrl, function (result) {
                if (result.isBoxCode) {
                    mvcApp.notification.toast("请扫瓶码");
                    return;
                }
                var parameter = null;
                if (fullUrl.indexOf("http://wly.wupincool.com") > -1) {
                    var beginIndex = fullUrl.indexOf("=") + 1;
                    parameter = fullUrl.substring(beginIndex, beginIndex + 16);
                } else {
                    parameter = fullUrl.substring(fullUrl.lastIndexOf("/") + 1);
                }
                callback(parameter);
            });
        }

    };
})();