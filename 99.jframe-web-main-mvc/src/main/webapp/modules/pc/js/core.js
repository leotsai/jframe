(function() {
    if (window.mvcApp) {
        return;
    }
    window.mvcApp = {
        guid: {
            empty: '00000000-0000-0000-0000-000000000000'
        },
        ajax: {
            
        },
        notification: {
            
        },
        utils: {
            
        }
    };
    window.page = {
    
    };
})();
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


(function() {
    jQuery.prototype.cssDisable = function() {
        return this.each(function() {
            $(this).addClass("disabled");
        });
    };

    jQuery.prototype.cssEnable = function () {
        return this.each(function () {
            $(this).removeClass("disabled");
        });
    };

    jQuery.prototype.isCssDisabled = function () {
        return this.hasClass("disabled");
    };

    if (!window.Number) {
        window.Number = function () {

        };
    }

    Number.get2Digits = function (number) {
        return number < 10 ? "0" + number : number;
    };

})();
(function() {
    mvcApp.utils = {
        onEnterKeydown: function(inputSelector, callback) {
            $(inputSelector).keydown(function(e) {
                if (e.keyCode === 13) {
                    callback();
                }
            });
        },
        getCookieValue: function(name) {
            var cookieValue = null;
            if (document.cookie && document.cookie !== '') {
                var cookies = document.cookie.split(';');
                for (var i = 0; i < cookies.length; i++) {
                    var cookie = jQuery.trim(cookies[i]);
                    if (cookie.substring(0, name.length + 1) === (name + '=')) {
                        cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                        break;
                    }
                }
            }
            return cookieValue;
        },
        removeCookie: function(name) {
            var value = this.getCookieValue(name);
            if (value != null) {
                var exp = new Date();
                exp.setFullYear(2000);
                document.cookie = name + "=" + value + ";path=/;expires=" + exp.toGMTString();
            }
        },
        setCookie: function(name, value, expire) {
            document.cookie = name + "=" + value + ";path=/;expires=" + expire.toGMTString();
        },
        centerImagesOnLoaded: function ($imgs, size) {
            $imgs.load(function () {
                var w = this.naturalWidth;
                var h = this.naturalHeight;
                var $img = $(this);
                $img.css("width", "auto");
                $img.css("height", "auto");
                $img.css("max-width", "inherit");
                $img.css("max-height", "inherit");
                if (w > h) {
                    $img.height(size);
                    w = size * w / h;
                    $img.css("margin-left", ((size - w) / 2) + "px");
                } else {
                    $img.width(size);
                    h = size * h / w;
                    $img.css("margin-top", ((size - h) / 2) + "px");
                }
            });
        },
        bindImagesViewer: function ($imgs, size, sizeEnum) {
            var fullSrcs = [];
            var thumbnailSrcs = [];
            size = size == undefined ? 80 : size;
            sizeEnum = sizeEnum == undefined ? "s" + size + "x" + size : sizeEnum;
            $imgs.each(function () {
                $(this).css("cursor", "pointer");
                var src = $(this).attr("src").toLowerCase();
                thumbnailSrcs.push(location.origin + src);
                fullSrcs.push(location.origin + src.replace(new RegExp(sizeEnum, "g"), 'full'));
            });

            (function ($imgItems, fullSrcArray, thumbnailSrcArray) {
                mvcApp.utils.centerImagesOnLoaded($imgItems, size);

                $imgItems.click(function (e) {
                    e.stopPropagation();
                    if (WeixinJsSdk != undefined && WeixinJsSdk.isInWeixin()) {
                        var src = location.origin + $(this).attr("src").toLowerCase().replace(new RegExp(sizeEnum, "g"), 'full');
                        WeixinJsSdk.ready(function () {
                            wx.previewImage({
                                current: src,
                                urls: fullSrcArray
                            });
                        });
                    } else {
                        var viewer = new ImageViewer();
                        viewer.getFullUrl = function (url) {
                            return url.replace(new RegExp(sizeEnum, "g"), 'full');
                        };
                        viewer.show(thumbnailSrcArray, thumbnailSrcArray.indexOf(location.origin + $(this).attr("src").toLowerCase()));
                    }
                });
            })($imgs, fullSrcs, thumbnailSrcs);
        },
        getImgUrl: function (key, size) {
            if (key == null || key === '') {
                return '/_storage/css/404.png';
            }
            if (key.indexOf('/') === -1) {
                return '/img/' + size + '/' + key;
            }
            return key;
        },
        bindListOnce: function ($ul, handleItemCallback) {
            $ul.children(":not(.initialized)").each(function () {
                if ($(this).hasClass("btn-loadmore")) {
                    return;
                }
                $(this).addClass("initialized");
                handleItemCallback(this);
            });
        },
        isTouchable: function () {
            var iosDevices = ['iphone', 'ipad', 'ipod'];
            var touchDevices = ['android', 'phone', 'blackbery'].concat(iosDevices);
            var appVersion = navigator.appVersion.toLowerCase();
            for (var i = 0; i < touchDevices.length; i++) {
                if (appVersion.indexOf(touchDevices[i]) > -1) {
                    return true;
                }
            }
            return false;
        },
        getTouchPosition: function (event) {
            try {
                var touch = event.targetTouches[0];
                if (touch == undefined) {
                    return null;
                }
                return {
                    x: touch.pageX,
                    y: touch.pageY
                };
            } catch (ex) {
                return {
                    x: event.clientX,
                    y: event.clientY
                };
            }
        },
        serializeObject: function (obj, lineBreak) {
            var text = '';
            for (var p in obj) {
                if (obj.hasOwnProperty(p)) {
                    text += p + ":" + obj[p] + lineBreak;
                }
            }
            return text;
        }
    };
})();
(function() {
    window.onerror = function(err) {
        if (location.search.indexOf("debug=true") > -1) {
            setTimeout(function () {
                alert("出错啦：" + err);
            }, 1000);
        }
    };

})();
(function ($) {
    $.fn.busy = function (options) {
        var timer = null;
        var BusyOptions = function (isbusy) {
            this.isBusy = isbusy;
            this.busyContent = "Loading...";
            this.request = null;
            this.showCancel = true;
            this.modal = true;
            this.delay = 0;
            this.css = "";
        };

        BusyOptions.Fix = function (obj) {
            if (obj.constructor === Boolean) {
                return new BusyOptions(obj);
            }
            var result = new BusyOptions(true);
            for (var property in obj) {
                if (obj.hasOwnProperty(property)) {
                    result[property] = obj[property];
                }
            }
            return result;
        };

        function createIndicator(opts) {
            var container = $("<div class='busy-indicator'></div>");
            if (opts.css != null && opts.css !== "") {
                container.addClass(opts.css);
            }
            var contentWrapper = $("<div class='busy-content'></div>");
            contentWrapper.appendTo(container);
            if (opts.showCancel) {
                var cancelLink = $("<a href='javascript:void(0)' title='cancel' class='busy-cancel'></a>");
                cancelLink.appendTo(contentWrapper);
                cancelLink.click(function () {
                    if (opts.request != undefined && opts.request != null) {
                        opts.request.abort();
                    }
                    container.remove();
                });
            }
            $("<div class='busy-text'></div>").appendTo(contentWrapper);
            return container;
        }

        return $(this).each(function (i, elem) {
            var item = $(elem);
            options = BusyOptions.Fix(options);

            if (options.isBusy) {
                var indicator = item.find(".busy-indicator");
                if (indicator.length === 0) {
                    indicator = createIndicator(options);
                    item.prepend(indicator);
                    var $content = item.find(".busy-content");
                    var contentHeight = $content.height();
                    var contentWidth = $content.width();
                    var windowHeight = $(window).height();
                    var windowWidth = $(window).width();
                    if (options.modal) {
                        indicator.addClass("busy-modal");
                        var itemHeight = item.height();
                        var itemWidth = item.width();
                        if (item.is("body")) {
                            indicator.css("position", "absolute");
                            var itemContentsHeight = 0;
                            var itemContentsWidth = 0;
                            if (item.find("#wrapper").length > 0) {
                                itemContentsHeight = item.find("#wrapper").height();
                                itemContentsWidth = item.find("#wrapper").width();
                            }
                            indicator.height(Math.max(itemHeight, windowHeight, itemContentsHeight) + "px");
                            indicator.width(Math.max(itemWidth, windowWidth, itemContentsWidth) + "px");
                            $content.css("position", "fixed");
                            contentWidth = $content.width();
                            $content.css("top", (windowHeight - contentHeight) / 2 + "px");
                            $content.css("left", (windowWidth - contentWidth) / 2 + "px");
                        } else {
                            indicator.height(itemHeight + "px");
                            indicator.width(itemWidth + "px");
                            $content.css("top", (indicator.height() - contentHeight) / 2 + "px");
                        }
                    }
                    else {
                        var wrapperHeight = 0;
                        var wrapperWidth = 0;
                        if (item.is("body")) {
                            indicator.css("position", "fixed");
                            wrapperHeight = windowHeight;
                            wrapperWidth = windowWidth;
                        } else {
                            wrapperHeight = item.height();
                            wrapperWidth = item.width();
                        }

                        var top = (wrapperHeight - contentHeight) / 2;
                        var left = (wrapperWidth - contentWidth) / 2;
                        indicator.css("left", left + "px");
                        indicator.css("top", top + "px");
                    }
                }
                indicator.find(".busy-text").html(options.busyContent);
                indicator.hide();

                timer = setTimeout(function () {
                    if (options.modal === false) {
                        indicator.css("display", "table");
                    }
                    else {
                        indicator.show();
                    }
                }, options.delay);
            } else {
                item.find(".busy-indicator").remove();
                clearTimeout(timer);
            }
        });
    };

    $.busy = function (options) {
        $("body").busy(options);
    };

    $.toast = function (options) {
        var ToastOptions = function (message) {
            this.message = message;
            this.showDuration = 500;
            this.displayDuration = 2000;
            this.hideDuration = 500;
            this.css = "";
            this.showClose = true;
        };

        ToastOptions.Fix = function (obj) {
            if (obj.constructor === String) {
                return new ToastOptions(obj);
            }
            var result = new ToastOptions("");
            for (var property in obj) {
                if (obj.hasOwnProperty(property)) {
                    result[property] = obj[property];
                }
            }
            return result;
        };


        options = ToastOptions.Fix(options);
        var $toast = $("<div class='toast'>" + options.message + "</div>");
        if (options.css !== "") {
            $toast.addClass(options.css);
        }
        $toast.appendTo("body");
        if (options.showClose) {
            var $close = $("<a class='close' href='javascript:void(0)'></a>");
            $close.appendTo($toast);
            $close.click(function () {
                $toast.animate({
                    top: "-=35",
                    opacity: 0
                }, {
                    duration: options.hideDuration,
                    complete: function () {
                        $toast.remove();
                    }
                });
            });
        }
        var centerX = $(window).width() / 2;
        var centerY = $(window).height() / 2;
        var left = centerX - $toast.width() / 2;
        var top = centerY - $($toast).height() / 2 + 50;
        $toast.css("top", top + "px");
        $toast.css("left", left + "px");
        $toast.animate({
            top: "-=50",
            opacity: 1
        }, {
            duration: options.showDuration,
            complete: function () {
                setTimeout(function () {
                    $toast.animate({
                        top: "-=35",
                        opacity: 0
                    }, {
                        duration: options.hideDuration,
                        complete: function () {
                            $toast.remove();
                        }
                    });
                }, options.displayDuration);
            }
        });
    };

})(jQuery);


(function() {
    mvcApp.notification.toast = function (message) {
        $.toast({
            message: message,
            css: "info"
        });
    };

    mvcApp.notification.toastSuccess = function (message) {
        $.toast({
            message: message,
            css: "success"
        });
    };

    mvcApp.notification.toastError = function (message) {
        if (message == undefined || message === "") {
            message = "出错啦";
        }
        $.toast({
            message: message,
            css: "error",
            displayDuration: 3000
        });
    };

    mvcApp.notification.busy = function(text, delay) {
        if (text === false) {
            $.busy(false);
            return;
        }
        $.busy({
            busyContent: text,
            delay: delay
        });
    };

    mvcApp.notification.dialog = function (id, title, options) {
        var $dialog = $("#" + id);
        if ($dialog.length === 0) {
            $dialog = $("<div id='" + id + "' class='dialog' title='" + title + "'></div>").appendTo("body");
        }
        if (options.autoOpen == undefined) {
            options.autoOpen = false;
        }
        if (options.modal == undefined) {
            options.modal = true;
        }
        if (options.close == undefined) {
            options.close = function () {
                $dialog.dialog("destroy");
                $dialog.remove();
            };
        } else {
            (function (closeFunc) {
                options.close = function () {
                    closeFunc();
                    $dialog.dialog("destroy");
                    $dialog.remove();
                };
            })(options.close);
        }
        var createFunc = options.create;
        options.create = function () {
            createFunc && createFunc();
        };
        $dialog.dialog(options);
        $dialog.dialog("open");
    };

    mvcApp.notification.alert = function (title, content, onClose, width, height) {
        mvcApp.notification.messageBox(title, content, {
            "OK": function () {
                $(this).dialog("close");
            }
        }, onClose, width, height);
    };

    mvcApp.notification.alertError = function (message, title) {
        if (title == undefined) {
            title = "出错啦";
        }
        if (message == undefined || message === '') {
            message = "糟了，服务器出错了~请再试试吧，如果一直出错，还可以联系客服解决哦！";
        }
        message = "<p class='error'>" + message + "</p>";
        mvcApp.notification.alert(title, message);
    };

    mvcApp.notification.messageBox = function (title, content, buttons, width, height) {
        var id = "messagebox" + new Date().getTime();
        width = width == undefined ? 400 : width;
        height = height == undefined ? 250 : height;
        var $dialog = $("<div id='" + id + "' class='message-box'>" + content + "</div>");
        $dialog.appendTo("body");
        $dialog.dialog({
            modal: true,
            title: title,
            width: width,
            minHeight: height,
            height: 'auto',
            buttons: buttons,
            close: function () {
                $(this).dialog("destroy").remove();
            }
        });
    };

})();







(function() {
    mvcApp.utils.bindDatePicker = function (input) {
        $(input).datepicker({
            dateFormat: "yy-m-d",
            monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            monthNamesShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
            changeYear: true,
            changeMonth: true
        });
    };

    mvcApp.utils.onEnterKeydown = function (inputSelector, callback) {
        $(inputSelector).keydown(function (e) {
            if (e.keyCode == 13) {
                callback();
            }
        });
    };

    mvcApp.utils.getCookieValue = function (name) {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    };

    mvcApp.utils.removeCookie = function (name) {
        var value = this.getCookieValue(name);
        if (value != null) {
            var exp = new Date();
            exp.setFullYear(2000);
            document.cookie = name + "=" + value + ";path=/;expires=" + exp.toGMTString();
        }
    };

    mvcApp.utils.setCookie = function (name, value, expire) {
        document.cookie = name + "=" + value + ";path=/;expires=" + expire.toGMTString();
    };

})();