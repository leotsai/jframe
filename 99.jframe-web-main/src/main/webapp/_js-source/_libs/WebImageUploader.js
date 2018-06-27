//2017-02-19 14:00, 增加reset方法以及更新上传后的li
//2017-08-23 11:19  修复了IE浏览器上传bug
//2017-12-08 12:08  增加排序功能
(function () {
    if (window.WebImageUploader) {
        return;
    }
    window.WebImageUploader = function (wrapperSelector, imgKeys) {
        this.wrapperSelector = wrapperSelector;
        this.imgKeys = imgKeys == undefined ? [] : imgKeys;
        this.maxImages = 6;
        this.style = mvcApp.imageStyles.s60x60;
        this.imageType = 0;
        this.maxLength = 200;
        this.showTip = true;
        this.isDisabled = false;
        this.multiple = false;
        this.sortable = true;

        this.url = '/img/upload';
        this.extensions = [".jpg", ".jpeg", ".bmp", ".gif", ".png"];
        this.accept = 'image/jpg, image/jpeg, image/png, image/bmp, image/gif';
        this.idPrefix = 'multiFileUpload';
        this.tipText = '点击添加照片';

        this._uploadCount = 0;
    };

    WebImageUploader.prototype = {
        init: function () {
            var $wrapper = $(this.wrapperSelector);
            var $ul = $('<ul class="files-uploader"></ul>');
            $wrapper.append($ul);

            for (var i = 0; i < this.imgKeys.length; i++) {
                this.append$Li(this.imgKeys[i]);
            }
            this.appendNewUploaderIfNeeded();
            if (this.isDisabled) {
                this.disable();
            }
            else{
                if(this.sortable&& $ul.sortable != undefined){
                    $ul.sortable();

                }
            }
        },
        disable: function () {
            this.isDisabled = true;
            var $ul = $(this.wrapperSelector).find("ul.files-uploader");
            $ul.addClass("disabled");
            $ul.find(":file").attr("disabled", "disabled");
        },
        enable: function () {
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
        append$Li: function (imgKey, $prevLi) {
            var me = this;
            var id = this.idPrefix + this.getAndIncreamentCount();
            var multiple = this.multiple ? ' multiple="multiple" ' : '';
            var imgHtml = imgKey == null ? "+" : '<img src="' + this.getSrc(imgKey) + '"/>';
            var html = '<li data-key="' + imgKey + '" class="uploader-file' + (imgKey == null ? ' uploader-new' : '') + '">\
                            <input type="file" id="' + id + '" name="files" ' + multiple + ' accept="' + me.accept + '"/>\
                            <label for="' + id + '">' + imgHtml + '</label>\
                            <a class="btn-delete"></a>\
                            <span class="success-icon"></span>\
                        </li>';
            var $li = $(html);
            $li.find("a.btn-delete").click(function () {
                $(this).closest("li").remove();
                me.onImageRemoved();
                me.appendNewUploaderIfNeeded();
            });
            $li.find('span.success-icon').click(function () {
                $li.find('label').eq(0).trigger('click');
            });
            if ($prevLi == undefined) {
                $(this.wrapperSelector).find("ul").append($li);
            }
            else {
                $prevLi.after($li);
            }
            this._initUploader(id);
            return $li;
        },
        appendUploadedImage: function (imageKey) {
            var $new = $(this.wrapperSelector).find("li.uploader-new");
            if ($new.length > 0) {
                $new.remove();
                this.append$Li(imageKey);
                this.appendNewUploaderIfNeeded();
            }
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
            if ($wrap.find("li.uploader-new").length === 0 && $wrap.find("li.uploader-file").length < this.maxImages) {
                this.append$Li(null);
            }
            if (this.showTip == false) {
                return;
            }
            $wrap.find(".upload-tip").remove();
            $wrap.find(".files-uploader > li").removeClass("has-tip");
            if ($wrap.find(".files-uploader > li").length === 1) {
                var $li = $wrap.find(".files-uploader > li").eq(0).addClass("has-tip");
                $li.append('<label class="upload-tip" for="' + $li.find(":file").attr("id") + '">' + me.tipText + '</label>');
            }
        },
        onImageRemoved: function () {

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
                var imgKey = $.trim($(this).attr("data-key"));
                if (imgKey == '' || imgKey == 'null' || imgKey == 'undefined') {
                    return;
                }
                imgs.push(imgKey);
            });
            return imgs;
        },
        getAndIncreamentCount: function () {
            this._uploadCount++;
            return this._uploadCount;
        },
        _initUploader: function (fileId) {
            var me = this;
            (function (fid) {
                var uploader = new FileUpload(fid, me.url);
                uploader.multiple = me.multiple;
                uploader.extensions = me.extensions;
                var $file = $("#" + fid);
                var $li = $file.closest("li");

                uploader._showProgress = function () {
                    $li.find("label").html("上传中...");
                    $li.removeClass("uploader-new");
                    me.appendNewUploaderIfNeeded();
                };
                uploader._changeProgress = function (loaded, total) {
                    var text = ((loaded * 1) * 100 / (total * 1)).toFixed(1) + "%";
                    $li.find("label").html(text);
                };
                uploader.onUploaded = function (json, fileNames) {
                    $file.val("");
                    if (json.success) {
                        me._updateUploadedLi($li, json.value[0]);
                        if (json.value.length > 1) {
                            var $lastLi = $li;
                            for (var vi = 1; vi < json.value.length; vi++) {
                                $lastLi = me.append$Li(json.value[vi], $lastLi);
                            }
                        }
                    } else {
                        alert(json.message);
                    }
                };
                uploader.init();
            })(fileId);
        },
        _updateUploadedLi: function ($li, imgKey) {
            $li.find("label").html('<img src="' + this.getSrc(imgKey) + '" />');
            $li.attr("data-key", imgKey);
            this.onImageUploaded($li, imgKey);
        },
        onImageUploaded: function ($li, imgKey) {

        }
    };

})();