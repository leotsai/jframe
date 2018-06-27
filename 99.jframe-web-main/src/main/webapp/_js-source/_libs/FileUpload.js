//2017-08-23 11:19  修复了IE浏览器上传bug
//2017-11-23 16:52  增加支持多文件上传
//2017-12-14 21:11  移除了不需要的属性
(function () {
    if (window.FileUpload) {
        return;
    }
    window.FileUpload = function (id, url) {
        this.id = id;
        this.autoUpload = true;
        this.url = url;
        this.maxSize = 10485760;
        this.maxSizeError = "文件大小不能超过10MB";
        this.extensions = [".jpg", ".jpeg", ".bmp", ".gif", ".png"];
        this.dropId = null;
        this.canCancel = true;
        this._xhr = null;
    };

    window.FileUpload.prototype.init = function () {
        var obj = this;

        $('#' + this.id).change(function () {
            if (obj.autoUpload){
                obj.upload();
            }
        });

        if (this.supportsFormData()) {
            if (this.dropId != null) {
                var drop = $('#' + this.dropId)[0];
                drop.addEventListener("dragover", function (e) {
                    e.stopPropagation();
                    e.preventDefault();
                    $('#' + obj.dropId).addClass("dragover");
                }, false);
                drop.addEventListener("dragout", function (e) {
                    $('#' + obj.dropId).removeClass("dragover");
                }, false);
                drop.addEventListener("drop", function (e) {
                    e.stopPropagation();
                    e.preventDefault();
                    $('#' + obj.dropId).removeClass("dragover");
                    obj._uploadUsingFormData(e.dataTransfer.files[0]);
                }, false);
            }
        } else {
            if (this.dropId != null) {
                $('#' + this.dropId).hide();
            }
        }
    };

    FileUpload.prototype.supportsFormData = function () {
        return window.FormData != undefined;
    };

    FileUpload.prototype.upload = function () {
        var args = {
            isCancelled: false
        };
        this.onCancelUploading(args);
        if (args.isCancelled) {
            return;
        }
        if (this.supportsFormData()) {
            this._uploadUsingFormData($("#" + this.id)[0].files);
        } else {
            var filename = $("#" + this.id).val();
            filename = filename.substr(filename.lastIndexOf("\\") + 1, filename.length);
            this._uploadUsingFrame(filename);
        }
    };

    FileUpload.prototype._uploadUsingFrame = function (fileName) {
        var obj = this;
        var $frame = $('#uploadFrame');
        if ($frame==0) {
            $frame = $('<iframe id="uploadFrame" width="0" height="0" name="uploadFrame" src=""></iframe>');
            $frame.appendTo("body");
            $frame.load(function () {
                obj._removeProgress();
                obj._resetUploadButton();
                var response = $frame.contents().text();
                try {
                    var json = $.parseJSON(response);
                    obj.onUploaded(json, fileName);
                } catch (ex) {
                    $(obj).trigger("onError", response);
                    alert("解析JSON错误，请重试。");
                }
                $('#uploadFrame').remove();
            });
        }
        this._showProgress();
        var $file = $("#" + this.id);
        var $form = $file.wrap('<form method="post" target="uploadFrame" enctype="multipart/form-data" action="' + this._getActualUrl() + '">').closest("form");
        var form = $form[0];
        form.submit();
        $file.unwrap();
    };

    FileUpload.prototype._getActualUrl = function () {
        if (typeof (this.url) == "function") {
            return this.url();
        }
        return this.url;
    };

    FileUpload.prototype._uploadUsingFormData = function (files) {
        var obj = this;
        var xhr = new XMLHttpRequest();
        this._xhr = xhr;
        var fileNames = [];
        for (var i = 0; i < files.length; i++) {
            fileNames.push(files[i].name);
        }
        xhr.addEventListener("load", function (e) {
            obj._removeProgress();
            var json = '';
            try {
                json = $.parseJSON(xhr.response);
            } catch (ex) {
                $(obj).trigger("onError", 'JSON序列化错误');
                alert("解析JSON错误，请重试。");
            }
            obj.onUploaded(json, fileNames);
        }, false);
        xhr.addEventListener("error", function (e) {
            alert("网络或服务器错误，请重试。");
            obj._removeProgress();
        }, false);
        xhr.upload.addEventListener("progress", function (e) {
            if (e.lengthComputable) {
                obj._changeProgress(e.loaded, e.total);
            }
        }, false);
        xhr.open("POST", obj._getActualUrl());

        for (var fi = 0; fi < files.length; fi++) {
            var file = files[fi];
            var isImage = obj._isImage(file.name);
            if (isImage && obj.maxSize != null && file.size > obj.maxSize) {
                alert(obj.maxSizeError);
                return;
            }
            if (obj.extensions != null) {
                var name = file.name;
                var ext = name.substring(name.lastIndexOf("."), name.length).toLowerCase();
                if (obj.extensions.indexOf(ext) < 0) {
                    alert("文件格式错误，支持的格式为：" + obj.extensions.join());
                    return;
                }
            }
        }
        this._showProgress();
        var formData = new FormData();
        for (var di = 0; di < files.length; di++) {
            formData.append("files", files[di]);
        }
        this.onBeforeUploading(fileNames);
        xhr.send(formData);
    };

    FileUpload.prototype._isImage = function (fileName) {
        var ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        return [".jpg", ".jpeg", ".bmp", ".gif", ".png"].indexOf(ext) > -1;
    };

    FileUpload.prototype._showProgress = function () {
        var me = this;
        var filename = $("#" + this.id).val();
        filename = filename.substr(filename.lastIndexOf("\\") + 1);
        var close = '';
        if (this.canCancel) {
            close = '<a class="file-close" title="取消">X</a>';
        }
        var height = $(window).height();
        if (height < $("body").height()) {
            height = $("body").height();
        }
        var frameCss = this.supportsFormData() ? "" : " frame";
        var html = '<div class="fileupload' + frameCss + '" style="height:' + height + 'px;">\
                        <div class="file-dialog">\
                            ' + close + '\
                            <div class="file-title">上传中...</div>\
                            <div class="file-content">\
                                <h4>' + filename + '</h4>\
                                <div class="progress">\
                                    <div class="progress-value"></div>\
                                </div>\
                            </div>\
                        </div>\
                    </div>';
        $("body").append(html);
        var $close = $(".fileupload .file-close");
        if ($close.length > 0) {
            $close.click(function () {
                me._removeProgress();
                if (me._xhr) {
                    me._xhr.abort();
                }
            });
        }
    };

    FileUpload.prototype._removeProgress = function () {
        $(".fileupload").remove();
        $("#" + this.id).val("");
    };

    FileUpload.prototype._changeProgress = function (loaded, total) {
        var width = loaded * 100 / total;
        $(".fileupload .progress-value").width(width + "%");
    };

    FileUpload.prototype._resetUploadButton = function () {
        $("#" + this.id).val("");
    };

    FileUpload.prototype.onUploaded = function (json, fileNames) {

    };

    FileUpload.prototype.onCancelUploading = function (args) {

    };

    FileUpload.prototype.onBeforeUploading = function (fileNames) {

    };

})();