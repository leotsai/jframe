(function() {

    function handleError(err, onlyCallbackOnSuccess, callback){
        if(err.status === 0 && err.statusText === "abort"){
            return;
        }
        if (onlyCallbackOnSuccess) {
            mvcApp.notification.alert("网络错误", "网络出错了，请重试");
        } else {
            callback({
                success: false,
                message: '网络出错了'
            });
        }
    };

    mvcApp.ajax = {
        post: function (url, data, callback, onlyCallbackOnSuccess) {
            data = "ajax=true&ts=" + new Date().getTime() + "&" + data;
            return $.ajax({
                type: "post",
                url: url,
                contentType: "application/x-www-form-urlencoded",
                data: data,
                success: function(result) {
                    if (onlyCallbackOnSuccess && result.success === false) {
                        mvcApp.notification.alertError(result.message);
                    } else {
                        callback(result);
                    }
                },
                error: function(err) {
                    handleError(err, onlyCallbackOnSuccess, callback);
                }
            });
        },
        busyPost: function (url, data, callback, busyText, onlyCallbackOnSuccess) {
            data = "ajax=true&ts=" + new Date().getTime() + "&" + data;
            return $.ajax({
                type: "post",
                url: url,
                contentType: "application/x-www-form-urlencoded",
                data: data,
                beforeSend: function () {
                    mvcApp.notification.busy(busyText);
                },
                success: function (result) {
                    mvcApp.notification.busy(false);
                    if (onlyCallbackOnSuccess == undefined || onlyCallbackOnSuccess === false) {
                        callback(result);
                    } else {
                        if (result.success === true) {
                            callback(result);
                        } else {
                            mvcApp.notification.alert("出错了", result.message);
                        }
                    }
                },
                error: function (err) {
                    mvcApp.notification.busy(false);
                    handleError(err, onlyCallbackOnSuccess, callback);
                }
            });
        },
        load: function (wrapper, url, successCallback, message) {
            var $wrapper = $(wrapper);
            $wrapper.empty();
            message = message == undefined ? "加载中..." : message;
            $wrapper.html("<div class=='loading'>" + message + "</div>");
            var loadUrl;
            if (url.indexOf("?") < 0) {
                loadUrl = url + "?ajax=true&ts=" + new Date().getTime();
            } else {
                loadUrl = url + "&ajax=true&ts=" + new Date().getTime();
            }
            $.ajax({
                type: "get",
                url: loadUrl,
                success: function (result) {
                    $wrapper.html(result);
                    if (successCallback != undefined && successCallback != null) {
                        successCallback();
                    }
                },
                error: function (err) {
                    handleError(err, false, function(){
                        $wrapper.html("<div class='load-error'><span class='error'>Error occuurred.</span><a href='javascript:void(0);'>Re-try</a></div>");
                        $wrapper.find("a").click(function () {
                            mvcApp.ajax.load(wrapper, url, successCallback);
                        });
                    });
                }
            });
        }
    };
})();

