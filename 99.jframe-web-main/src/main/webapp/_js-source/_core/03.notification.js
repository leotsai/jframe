/**
 * Created by Leo on 2017/12/2.
 */
(function(){

    var _busyTimer = null;

    mvcApp.notification.busy = function (text, delay) {
        $("#busy").remove();
        if (text === false) {
            clearTimeout(_busyTimer);
            return;
        }
        var wrapper = $("#body").length === 0 ? "body" : "#body";
        $('<div id="busy"></div>').appendTo(wrapper);
        var html = '<div class="busy-inner"><i class="icon-busy"></i><label>' + (text == undefined ? "请稍候..." : text) + '</label></div>';
        if (delay == undefined || delay === 0) {
            $("#busy").html(html);
        } else {
            _busyTimer = setTimeout(function () {
                $("#busy").html(html);
            }, delay);
        }
    };


})();