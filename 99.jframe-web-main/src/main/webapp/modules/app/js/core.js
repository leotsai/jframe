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
(function() {
    var _busyTimer = null;

    mvcApp.notification.toast = function (message) {
        var options = {
            showDuration: 500,
            displayDuration: 2000,
            hideDuration: 500,
            css: ""
        };

        var $toast = $("<div class='toast'><div class='toast-inner'>" + message + "</div></div>");
        if (options.css !== "") {
            $toast.addClass(options.css);
        }
        $toast.appendTo("#body");
        var centerX = $(window).width() / 2;
        var left = centerX - $toast.outerWidth() / 2;
        var bottom = $(window).height() / 10;
        $toast.css("bottom", 0 + "px");
        $toast.css("left", left + "px");
        $toast.animate({
            bottom: "+=" + bottom,
            opacity: 1
        }, {
            duration: options.showDuration,
            complete: function () {
                setTimeout(function () {
                    $toast.animate({
                        bottom: "-=35",
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

    mvcApp.notification.toastSuccess = function (message) {
        this.toast(message);
    };

    mvcApp.notification.toastError = function (message) {
        this.toast(message);
    };

    mvcApp.notification.busy = function (text, delay) {
        $("#busy").remove();
        if (text === false) {
            clearTimeout(_busyTimer);
            return;
        }

        $('<div id="busy"></div>').appendTo("#body");
        var html = '<div class="busy-inner"><i class="icon-busy"></i><label>' + (text == undefined ? "请稍候..." : text) + '</label></div>';
        if (delay == undefined || delay === 0) {
            $("#busy").html(html);
        } else {
            _busyTimer = setTimeout(function () {
                $("#busy").html(html);
            }, delay);
        }
    };

    mvcApp.notification.dialog = function (id, title, content, buttons, css) {
        var htmlCss = css == undefined ? "" : " " + css;
        var html = '<div id="' + id + '" class="dialog' + htmlCss + '">\
                            <div class="dialog-wrap">\
                                <div class="dialog-title">' + title + '</div>\
                                <div class="dialog-content">' + content + '</div>\
                                <div class="dialog-buttons">\
                                    <ul></ul>\
                                </div>\
                            </div>\
                        </div>';
        $("#body").append(html);
        var $btns = $("#" + id + " .dialog-buttons > ul");
        var btnIndex = 0;
        for (var button in buttons) {
            if (buttons.hasOwnProperty(button) === false) {
                return;
            }
            var $li = $('<li class="btn-' + btnIndex + '"><a href="javascript:;">' + button + '</a></li>');
            $li.appendTo($btns);
            (function ($a, func) {
                $a.click(function () {
                    func.call(this);
                });
            })($li.find("a"), buttons[button]);
            btnIndex++;
        }
        $btns.addClass("btns-" + $btns.children().length);
        var $wrap = $("#" + id + " > .dialog-wrap");
        $wrap.css("margin-top", -$wrap.outerHeight() / 2).css("left", ($("#" + id).innerWidth() - $wrap.outerWidth()) / 2);
    };

    mvcApp.notification.alert = function (title, content, onClose) {
        this.dialog("dialogAlert" + new Date().getTime(), title, content, {
            "OK": function () {
                $(this).closest(".dialog").remove();
                onClose && onClose();
            }
        }, "dialog-alert");
    };

    mvcApp.notification.alertError = function (content, onClose) {
        this.alert('出错啦', content, onClose);
    };

    mvcApp.notification.messageBox = function (title, content, buttons) {
        for (var button in buttons) {
            if (!buttons.hasOwnProperty(button)) {
                return;
            }
            (function (btns, btn) {
                var func = btns[btn];
                btns[btn] = function () {
                    $(this).closest(".dialog").remove();
                    func();
                };
            })(buttons, button);
        }
        this.dialog("dialogConfirm" + new Date().getTime(), title, content, buttons, "dialog-confirm");
    };

})();
(function() {
    mvcApp.weixin = {
        showShareTip: function(message) {
            if (message == undefined) {
                message = "请点击右上角“...”，然后选择分享到朋友圈";
            }
            var html = '<div id="shareTip">\
                            <div class="message">' + message + '</div>\
                            <div class="btn-close">轻按退出</div>\
                        </div>';
            $("#body").append(html);
            $("#shareTip").click(function() {
                $(this).remove();
            });
        }
    };
})();
(function() {
    $(document).ready(function () {
        $("a.nohistory").on("click", function(e) {
            e.preventDefault();
            window.location.replace($(this).attr("href"));
        });

        $(".header > .btn-back").on("click", function (e) {
            e.preventDefault();
            history.go(-1);
        });
    });
})();