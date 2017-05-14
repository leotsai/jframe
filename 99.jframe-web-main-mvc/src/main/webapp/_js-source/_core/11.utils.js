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