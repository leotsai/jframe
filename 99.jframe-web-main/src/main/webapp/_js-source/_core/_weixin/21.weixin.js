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
        },
        showImages: function ($images) {
            var imageUrls = [];
            $images.each(function(){
                var src = mvcApp.utils.getFullUrl($(this).attr('src'));
                imageUrls.push(mvcApp.imageStyles.convertToFull(src));
            }).click(function () {
                var currentUrl = mvcApp.utils.getFullUrl($(this).attr('src'));
                WeixinJsSdk.ready(function () {
                    wx.previewImage({
                        current: mvcApp.imageStyles.convertToFull(currentUrl),
                        urls: imageUrls
                    });
                });
            });
        }
    };
})();