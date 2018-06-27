(function () {
    if (window.WeixinImageUploader) {
        return;
    }
    var urls = {
        getUploadWeixin: function (serverId, type) {
            return '/img/uploadWeixin?serverId=' + serverId + "&type=" + type;
        }
    };
    window.WeixinImageUploader = function (wrapperSelector, imgKeys) {
        this.wrapperSelector = wrapperSelector;
        this.imgKeys = imgKeys == undefined ? [] : imgKeys;
        this.maxImages = 6;
        this.style = mvcApp.imageStyles.s60x60;
        this.imageType = 0;

        this.showTip = true;
        this.canChooseFromAlbum = true;
    };

    WeixinImageUploader.isWkWebView = function(){
        return window.__wxjs_is_wkwebview === true;
    };

    WeixinImageUploader.prototype = {
        init: function () {
            var $wrapper = $(this.wrapperSelector);
            var $ul = $('<ul class="files-uploader"></ul>');
            $wrapper.append($ul);

            for (var i = 0; i < this.imgKeys.length; i++) {
                this.append$Li(this.imgKeys[i]);
            }
            this.appendNewUploaderIfNeeded();
            if(this.isDisabled){
                this.disable();
            }
        },
        disable: function(){
            this.isDisabled = true;
            var $ul = $(this.wrapperSelector).find("ul.files-uploader");
            $ul.addClass("disabled");
            $ul.find(":file").attr("disabled", "disabled");
        },
        enable: function(){
            this.isDisabled = false;
            var $ul = $(this.wrapperSelector).find("ul.files-uploader");
            $ul.removeClass("disabled");
            $ul.find(":file").removeAttr("disabled");
        },
        reset: function () {
            $(this.wrapperSelector).find(".files-uploader > li").each(function () {
                if ($(this).hasClass(".uploader-new")) {
                    return;
                }
                $(this).remove();
            });
            this.appendNewUploaderIfNeeded();
        },
        append$Li: function (imgKey) {
            var me = this;
            var imgHtml = imgKey == null ? "+" : '<img src="' + this.getSrc(imgKey) + '"/>';
            var liHtml = '<li data-id="' + imgKey + '" class="uploader-file' + (imgKey == null ? ' uploader-new' : '') + '">\
                            <label>' + imgHtml + '</label>\
                            <a class="btn-delete"></a>\
                            <span class="success-icon"></span>\
                            <div class="upload-busy"></div>\
                        </li>';
            var $li = $(liHtml);
            $li.find("a.btn-delete").click(function () {
                $(this).closest("li").remove();
                me.appendNewUploaderIfNeeded();
                me.onImageRemoved();
            });
            $li.find("label").click(function () {
                me._onUploadBtnClicked(this);
            });
            $li.find('span.success-icon').click(function () {
                $li.find('label').eq(0).trigger('click');
            });
            $(this.wrapperSelector).find("ul").append($li);
        },
        getSrc: function (imgKey) {
            if (imgKey == null || imgKey == '') {
                return '';
            }
            if (imgKey.indexOf('/') > -1) {
                return imgKey;
            }
            return mvcApp.getImageUrl(imgKey, this.style);
        },
        appendNewUploaderIfNeeded: function () {
            var me = this;
            var $wrap = $(this.wrapperSelector);
            if ($wrap.find("li.uploader-new").length == 0 && $wrap.find("li.uploader-file").length < this.maxImages) {
                this.append$Li(null);
            }
            $wrap.find(".upload-tip").remove();
            $wrap.find(".files-uploader > li").removeClass("has-tip");
            if (this.showTip && $wrap.find(".files-uploader > li").length == 1) {
                var $li = $wrap.find(".files-uploader > li").eq(0).addClass("has-tip");
                $li.append('<label class="upload-tip">点击添加照片</label>');
                $li.find("label.upload-tip").click(function () {
                    me._onUploadBtnClicked(this);
                });
            }
        },
        onImageRemoved: function () {

        },
        showHideTip: function () {

        },
        _onUploadBtnClicked: function (sender) {
            var me = this;
            if(this.isDisabled){
                return;
            }
            var isNewItemClicked = $(sender).closest("li").hasClass("uploader-new");
            var count = 1;
            if (isNewItemClicked) {
                var currentItems = $(this.wrapperSelector).find("li:not(.uploader-new)").length;
                count = this.maxImages - currentItems;
            }
            if(count > 9){
                count = 9;
            }
            WeixinJsSdk.ready(function () {
                var source = ['camera'];
                if (me.canChooseFromAlbum) {
                    source.push('album');
                }
                wx.chooseImage({
                    count: count,
                    sizeType: ['original', 'compressed'],
                    sourceType: source,
                    success: function (res) {
                        var localIds = res.localIds;
                        if (isNewItemClicked) {
                            me.appendNewImages(localIds);
                        } else {
                            me.replaceImage(sender, localIds[0]);
                        }
                    }
                });
            });
        },
        appendNewImages: function (localIds) {
            var me = this;
            $(this.wrapperSelector).find("li.uploader-new").remove();
            for (var i = 0; i < localIds.length; i++) {
                var localId = localIds[i];
                this.append$Li(localId);
                var $li = $(this.wrapperSelector).find("li").last();
                $li.addClass("uploading");
                me._setLocalImage($li.find("img"), localId);
            }
            me.appendNewUploaderIfNeeded();
            me.startUploadToWeixin();
        },
        _setLocalImage: function($img, localId){
            if(WeixinImageUploader.isWkWebView()){
                wx.getLocalImgData({
                    localId: localId, // 图片的localID
                    success: function (res) {
                        var localData = res.localData; // localData是图片的base64数据，可以用img标签显示
                        $img.attr("src", localData);
                    }
                });
            }
            else{
                $img.attr("src", localId);
            }
        },
        replaceImage: function (sender, localId) {
            var $li = $(sender).closest("li");
            $li.addClass("uploading").attr("data-id", localId).find("img");
            this._setLocalImage($li.find("img"), localId);
            this.startUploadToWeixin();
        },
        _doUpload: function ($li, localId, callback) {
            wx.uploadImage({
                localId: localId,
                isShowProgressTips: 0,
                success: function (res) {
                    callback && callback(res.serverId);
                }
            });
        },
        _getImgKey: function (serverId, callback) {
            var url = urls.getUploadWeixin(serverId, this.imageType);
            mvcApp.ajax.post(url, null, function (result) {
                if (result.success) {
                    callback(result.value);
                } else {
                    mvcApp.notification.toast("上传图片失败，请重试: " + result.message);
                }
            });
        },
        getImageKeys: function () {
            if ($(this.wrapperSelector).find("li.uploading").length > 0) {
                mvcApp.notification.toast("请等待图片上传完成");
                return null;
            }
            var imgs = [];
            $(this.wrapperSelector).find(".files-uploader > li").each(function () {
                if ($(this).hasClass(".uploader-new")) {
                    return;
                }
                var imgKey = $.trim($(this).attr("data-id"));
                if (imgKey == '' || imgKey == 'null' || imgKey == 'undefined') {
                    return;
                }
                imgs.push(imgKey);
            });
            return imgs;
        },
        startUploadToWeixin: function () {
            var me = this;
            var $li = $(this.wrapperSelector).find("ul > li.uploading").first();
            if ($li.length == 0) {
                return;
            }
            var localId = $li.attr("data-id");
            me._doUpload($li, localId, function (serverId) {
                me._getImgKey(serverId, function (imgKey) {
                    $li.removeClass("uploading").attr("data-id", imgKey);
                    me.onImageUploaded($li, imgKey);
                    me.startUploadToWeixin();
                });
            });
        },
        onImageUploaded: function ($li, imgKey) {

        }
    };

})();