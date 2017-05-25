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