(function() {


    mvcApp.notification.toast = function (message, css) {
        var options = {
            showDuration: 500,
            displayDuration: 2000,
            hideDuration: 500,
            css:  css == undefined ? "" : css
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

    mvcApp.notification.dialog = function (id, title, content, buttons, css) {
        $("#"+id).remove();
        var htmlCss = css == undefined ? "" : " " + css;
        if(title == null || title === '') {
            htmlCss += htmlCss === '' ? ' dialog-no-title' : ' dialog-no-title';
        }
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
            "确认": function () {
                $(this).closest(".dialog").remove();
                onClose && onClose();
            }
        }, "dialog-alert");
    };

    mvcApp.notification.alertError = function (content, onClose) {
        this.alert('出错啦', content, onClose);
    };

    mvcApp.notification.alertInfo = function (content,onClose) {
        this.alert('', content, onClose);
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

    mvcApp.notification.wxToastSuccess = function (message) {
        var $toast = $("<div class='wx-toast'><div class='wx-toast-inner'>" + message + "</div></div>");
        $toast.appendTo("#body");
        setTimeout(function () {
            $toast.remove();
        },1500);
    };

})();