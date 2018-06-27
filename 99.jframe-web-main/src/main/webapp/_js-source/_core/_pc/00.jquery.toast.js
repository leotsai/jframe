(function ($) {

    $.toast = function (options) {
        var ToastOptions = function (message) {
            this.message = message;
            this.showDuration = 200;
            this.displayDuration = 2000;
            this.hideDuration = 200;
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

