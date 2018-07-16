(function () {

    function handleError(err, onlyCallbackOnSuccess, callback) {
        if (err.status === 0 && err.statusText === "abort") {
            return;
        }
        if (onlyCallbackOnSuccess) {
            mvcApp.notification.alert("网络错误", err.responseText);
        } else {
            callback({
                success: false,
                message: err.responseText
            });
        }
    }

    function handleSuccess(result, successCallback, forbiddenCallback) {
        if (result.code === mvcApp.resultCode.NOT_AUTHENTICATED) {
            forbiddenCallback && forbiddenCallback();
            mvcApp.login._showLogin();
        } else if (result.code === mvcApp.resultCode.INSUFFICIENT_PERMISSION) {
            forbiddenCallback && forbiddenCallback();
            mvcApp.notification.alertInfo("权限不足，请注销登录后重新登录尝试")
        } else {
            successCallback && successCallback();
        }

    }

    function getFixedUrl(url) {
        if (url.indexOf("?") > -1) {
            return url + "&ts=" + new Date().getTime();
        }
        return url + "?ts=" + new Date().getTime();
    }

    mvcApp.ajax = {
        post: function (url, data, callback, onlyCallbackOnSuccess) {
            return $.ajax({
                type: "post",
                url: getFixedUrl(url),
                contentType: "application/x-www-form-urlencoded",
                data: data,
                success: function (result) {
                    handleSuccess(result, function () {
                        if (onlyCallbackOnSuccess && result.success === false) {
                            mvcApp.notification.alertInfo(result.message);
                        } else {
                            callback(result);
                        }
                    });
                },
                error: function (err) {
                    handleError(err, onlyCallbackOnSuccess, callback);
                }
            });
        },
        postJson: function (url, objectData, callback, onlyCallbackOnSuccess) {
            return $.ajax({
                type: "post",
                url: getFixedUrl(url),
                contentType: "application/json",
                data: typeof(objectData) === "object" ? JSON.stringify(objectData) : objectData,
                success: function (result) {
                    handleSuccess(result, function () {
                        if (onlyCallbackOnSuccess && result.success === false) {
                            mvcApp.notification.alertInfo(result.message);
                        } else {
                            callback(result);
                        }
                    });
                },
                error: function (err) {
                    handleError(err, onlyCallbackOnSuccess, callback);
                }
            });
        },
        busyPost: function (url, data, callback, busyText, onlyCallbackOnSuccess) {
            return $.ajax({
                type: "post",
                url: getFixedUrl(url),
                contentType: "application/x-www-form-urlencoded",
                data: data,
                beforeSend: function () {
                    mvcApp.notification.busy(busyText);
                },
                success: function (result) {
                    mvcApp.notification.busy(false);
                    handleSuccess(result, function () {
                        if (onlyCallbackOnSuccess == undefined || onlyCallbackOnSuccess === false) {
                            callback(result);
                        } else {
                            if (result.success === true) {
                                callback(result);
                            } else {
                                mvcApp.notification.alertInfo(result.message);
                            }
                        }
                    });
                },
                error: function (err) {
                    mvcApp.notification.busy(false);
                    handleError(err, onlyCallbackOnSuccess, callback);
                }
            });
        },
        busyPostJson: function (url, objectData, callback, busyText, onlyCallbackOnSuccess) {
            return $.ajax({
                type: "post",
                url: getFixedUrl(url),
                contentType: "application/json;charset=utf-8",
                data: typeof(objectData) === "object" ? JSON.stringify(objectData) : objectData,
                beforeSend: function () {
                    mvcApp.notification.busy(busyText);
                },
                success: function (result) {
                    mvcApp.notification.busy(false);
                    handleSuccess(result, function () {
                        if (onlyCallbackOnSuccess == undefined || onlyCallbackOnSuccess === false) {
                            callback(result);
                        } else {
                            if (result.success === true) {
                                callback(result);
                            } else {
                                mvcApp.notification.alertInfo(result.message);
                            }
                        }
                    });
                },
                error: function (err) {
                    mvcApp.notification.busy(false);
                    handleError(err, onlyCallbackOnSuccess, callback);
                }
            });
        },
        load: function (wrapper, url, successCallback, message, forbiddenCallback) {
            var $wrapper = $(wrapper);
            $wrapper.empty();
            message = message == undefined ? "加载中..." : message;
            $wrapper.html("<div class='loading'>" + message + "</div>");
            $.ajax({
                type: "get",
                url: getFixedUrl(url),
                success: function (result) {
                    handleSuccess(result, function () {
                        $wrapper.html(result);
                        if (successCallback != undefined && successCallback != null) {
                            successCallback();
                        }
                    }, forbiddenCallback);
                },
                error: function (err) {
                    handleError(err, false, function () {
                        $wrapper.html("<div class='load-error'><span class='error'>Error occuurred.</span><a href='javascript:void(0);'>Re-try</a></div>");
                        $wrapper.find("a").click(function () {
                            mvcApp.ajax.load(wrapper, url, successCallback);
                        });
                    });
                }
            });
        },
        loadPost: function (wrapper, url, data, successCallback, message) {
            var $wrapper = $(wrapper);
            $wrapper.empty();
            message = message == undefined ? "加载中..." : message;
            $wrapper.html("<div class='loading'>" + message + "</div>");
            $.ajax({
                type: "post",
                data: data,
                url: getFixedUrl(url),
                success: function (result) {
                    handleSuccess(result, function () {
                        $wrapper.html(result);
                        if (successCallback != undefined && successCallback != null) {
                            successCallback();
                        }
                    });
                },
                error: function (err) {
                    handleError(err, false, function () {
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

