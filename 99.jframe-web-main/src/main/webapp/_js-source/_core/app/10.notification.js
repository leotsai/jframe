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