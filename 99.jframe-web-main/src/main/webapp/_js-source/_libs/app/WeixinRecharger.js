(function() {
    if (window.WeixinRecharger) {
        return;
    }
    var _successCallback = null;
    var _errorCallback = null;
    var _payRequestUuid = null;

    window.WeixinRecharger = function() {
        
    };

    WeixinRecharger.post = function (url, data, successCallback, errorCallback) {
        _successCallback = successCallback;
        _errorCallback = errorCallback;
        mvcApp.ajax.post(url, data, function (result) {
            if (result.success) {
                _payRequestUuid = result.value.payRequestUuid;
                showWeixinPay({
                    appId: result.value.appId,
                    "package": "prepay_id=" + result.value.prePayId,
                    paySign: result.value.paySign,
                    timeStamp: result.value.timeStamp,
                    nonceStr: result.value.nonceStr,
                    signType: result.value.signType
                });
            } else {
                _errorCallback && _errorCallback(result.message);
            }
        });

    };

    function showWeixinPay(data) {
        if (window.WeixinJSBridge == undefined) {
            _errorCallback && _errorCallback("找不到微信组件，请在微信中打开");
            return;
        }
        WeixinJSBridge.invoke(
            'getBrandWCPayRequest', {
                "appId": data.appId, //公众号名称，由商户传入     
                "timeStamp": data.timeStamp, //时间戳，自1970年以来的秒数     
                "nonceStr": data.nonceStr, //随机串     
                "package": data.package,
                "signType": data.signType, //微信签名方式:     
                "paySign": data.paySign //微信签名 
            },
            function(res) {
                if (res.err_msg == "get_brand_wcpay_request:ok") {
                    _successCallback && _successCallback(_payRequestUuid);
                } else {
                    if(res.err_msg == "get_brand_wcpay_request:cancel"){
                        return;
                    }
                    _errorCallback && _errorCallback(res.err_msg);
                }
            });
    }
})();