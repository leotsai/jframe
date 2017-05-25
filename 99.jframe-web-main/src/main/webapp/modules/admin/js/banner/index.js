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
        this.optimizeImage = false;
        this.imageWidth = 1024;
        this.imageHeight = 768;
    };

    window.FileUpload.prototype.init = function () {
        var obj = this;
        $('#' + this.id).change(function () {
            if (obj.autoUpload) {
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

    FileUpload.prototype.upload = function() {
        var args = {
            isCancelled: false
        };
        this.onBeforeUploading(args);
        if (args.isCancelled) {
            return;
        }
        if (this.supportsFormData()) {
            this._uploadUsingFormData($("#" + this.id)[0].files[0]);
        } else {
            this._uploadUsingFrame($("#" + this.id)[0].files[0]);
        }
    };

    FileUpload.prototype._uploadUsingFrame = function (file) {
        var obj = this;
        var $frame = $('#uploadFrame');
        if ($frame.length == 0) {
            $frame = $('<iframe id="uploadFrame" width="0" height="0" name="uploadFrame" src=""></iframe>');
            $frame.appendTo("body");
            $frame.load(function () {
                obj._removeProgress();
                obj._resetUploadButton();
                var response = $frame.contents().text();
                try {
                    var json = $.parseJSON(response);
                    obj.onUploaded(json, file.name);
                } catch (ex) {
                    $(obj).trigger("onError", response);
                    alert("解析JSON错误，请重试。");
                }
            });
        }
        this._showProgress();
        var $form = $("#" + this.id).closest("form");
        var action = $form.attr("action");
        $form.attr("action", this._getActualUrl());
        var form = $form[0];
        form.target = 'uploadFrame';
        form.submit();
        $form.attr("action", action);
    };

    FileUpload.prototype._getActualUrl = function() {
        if (typeof (this.url) == "function") {
            return this.url();
        }
        return this.url;
    };

    FileUpload.prototype._uploadUsingFormData = function (file) {
        var obj = this;
        var xhr = new XMLHttpRequest();
        this._xhr = xhr;
        xhr.addEventListener("load", function (e) {
            obj._removeProgress();
            var json = '';
            try {
                json = $.parseJSON(xhr.response);
            } catch (ex) {
                $(obj).trigger("onError", 'JSON序列化错误');
                alert("解析JSON错误，请重试。");
            }
            obj.onUploaded(json, file.name);
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
        this._showProgress();
        var formData = new FormData();
        if (isImage && obj.optimizeImage) {
            ImageOptimizer.getBase64(file, obj.imageWidth, obj.imageHeight, function(base64) {
                formData.append("base64image", base64);
                xhr.send(formData);
            });
        } else {
            formData.append("file", file);
            xhr.send(formData);
        }
    };

    FileUpload.prototype._isImage = function(fileName) {
        var ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        return [".jpg", ".jpeg", ".bmp", ".gif", ".png"].indexOf(ext) > -1;
    };

    FileUpload.prototype._showProgress = function() {
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
    };

    FileUpload.prototype._changeProgress = function(loaded, total) {
        var width = loaded * 100 / total;
        $(".fileupload .progress-value").width(width + "%");
    };

    FileUpload.prototype._resetUploadButton = function() {
        $("#" + this.id).val("");
    };

    FileUpload.prototype.onUploaded = function (json) {
        
    };

    FileUpload.prototype.onBeforeUploading = function (args) {
        
    };

})();
(function() {
    if (window.WebImageUploader) {
        return;
    }
    window.WebImageUploader = function (wrapperSelector, imgKeys) {
        this.wrapperSelector = wrapperSelector;
        this.imgKeys = imgKeys == undefined ? [] : imgKeys;
        this.maxImages = 6;
        this.size = 's80x80';
        this.imageType = 0;
        this.cropCenter = false;
        this.maxLength = 200;
        this.showTip = true;

        this.url = '/img/upload';
        this.extensions = [".jpg", ".jpeg", ".bmp", ".gif", ".png"];
        this.accept = 'image/jpg, image/jpeg, image/png, image/bmp, image/gif';
        this.idPrefix = 'multiFileUpload';
        this.optimizeImage = false;

        this._uploadCount = 0;
    };

    WebImageUploader.prototype = {
        init: function() {
            var $wrapper = $(this.wrapperSelector);
            var $ul = $('<ul class="files-uploader"></ul>');
            $wrapper.append($ul);

            for (var i = 0; i < this.imgKeys.length; i++) {
                this.append$Li(this.imgKeys[i]);
            }
            this.appendNewUploaderIfNeeded();
        },
        append$Li: function (imgKey) {
            var me = this;
            var id = this.idPrefix + this.getAndIncreamentCount();
            var imgHtml = imgKey == null ? "+" : '<img src="' + this.getSrc(imgKey) + '"/>';
            var html = '<li data-key="' + imgKey + '" class="uploader-file' + (imgKey == null ? ' uploader-new' : '') + '">\
                            <input type="file" id="' + id + '" name="webImageUploader" accept="' + me.accept + '"/>\
                            <label for="' + id + '">' + imgHtml + '</label>\
                            <a class="btn-delete"></a>\
                        </li>';
            var $li = $(html);
            $li.find("a.btn-delete").click(function () {
                $(this).closest("li").remove();
                me.onImageRemoved();
                me.appendNewUploaderIfNeeded();
            });
            $(this.wrapperSelector).find("ul").append($li);
            this._initUploader(id);
        },
        getSrc: function (imgKey) {
            if (imgKey == null || imgKey == '') {
                return '';
            }
            if (imgKey.indexOf('/') > -1) {
                return imgKey;
            }
            return "/img/" + this.size + "/" + imgKey;
        },
        appendNewUploaderIfNeeded: function () {
            var $wrap = $(this.wrapperSelector);
            if ($wrap.find("li.uploader-new").length == 0 && $wrap.find("li.uploader-file").length < this.maxImages) {
                this.append$Li(null);
            }
            if (this.showTip == false) {
                return;
            }
            $wrap.find(".upload-tip").remove();
            $wrap.find(".files-uploader > li").removeClass("has-tip");
            if ($wrap.find(".files-uploader > li").length == 1) {
                var $li = $wrap.find(".files-uploader > li").eq(0).addClass("has-tip");
                $li.append('<label class="upload-tip" for="' + $li.find(":file").attr("id") + '">点击添加照片</label>');
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
        _getActualUrl: function() {
            if (typeof (this.url) == "function") {
                return this.url();
            }
            return this.url + "?type=" + this.imageType + "&cropCenter=" + this.cropCenter + "&maxLength=" + this.maxLength;
        },
        _initUploader: function(fileId) {
            var me = this;
            (function(fid) {
                var uploader = new FileUpload(fid, me._getActualUrl());
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
                uploader.onUploaded = function (json) {
                    $file.val("");
                    if (json.success) {
                        me._updateUploadedLi($li, json.value);
                    } else {
                        alert(json.message);
                    }
                };
                uploader.optimizeImage = me.optimizeImage;
                uploader.init();
            })(fileId);
        },
        _updateUploadedLi: function ($li, imgKey) {
            $li.find("label").html('<img src="' + this.getSrc(imgKey) + '" />');
            $li.attr("data-key", imgKey);
        }
    };

})();
(function () {
    if (window.Grid) {
        return;
    }
    window.Grid = function (dataWrapper, pagerWarper, sortWrapper) {
        this.url = '';
        this.dataWrapper = dataWrapper;
        this.pagerWarper = pagerWarper;
        this.sortWrapper = sortWrapper == undefined ? ".grid > thead" : sortWrapper;
        this.pageIndex = 0;
        this.pageSize = 15;
        this.maxPageNumbers = 10;
        this.totalPages = 0;
        this.totalRows = 0;
        this.getUrlFunc = null;
        this.getDataFunc = null;
        this.showRefresh = false;
        this.showSummary = true;
        this.selectorTotalPages = ".total-pages";
        this.selectorPageIndex = ".page-index";
        this.selectorTotalRows = ".page-rows";
        this.isSeletable = true;

        this._request = null;
    };

    Grid.prototype = {
        init: function () {
            var me = this;
            var $pager = $(this.pagerWarper);
            if ($pager.hasClass("pager") == false) {
                $pager.addClass("pager");
            }
            if (this.showRefresh) {
                $pager.append('<a href="javascript:void(0);" class="refresh">刷新</a>');
                $pager.find(".refresh").click(function () {
                    me.refresh();
                });
            }
            if (this.showSummary) {
                $pager.append('<span class="pager-summary"></span>');
            }
            $pager.append("<ul></ul>");
            $(this.sortWrapper).find(".sortable").click(function () {
                var $dom = $(this);
                var newDirection = "asc";
                if ($dom.hasClass("sort-asc")) {
                    newDirection = "desc";
                }
                else if ($dom.hasClass("sort-desc")) {
                    newDirection = "asc";
                }
                $(me.sortWrapper).find(".sortable").removeClass("sort-asc").removeClass("sort-desc");
                $dom.addClass("sort-" + newDirection);
                me.pageTo(0);
            });
        },
        pageTo: function (pageIndex) {
            var url = '';
            var me = this;
            if (me._request != null) {
                me._request.abort();
                me._request = null;
                me.removeBusy();
            }
            url = me._getUrl(pageIndex, me);
            var data = me.getDataFunc == null ? null : me.getDataFunc();
            me.showBusy();
            me._request = mvcApp.ajax.post(url, data, function (html) {
                me.removeBusy();
                me._triggerSuccess(html);
                $(me.dataWrapper).html(html);
                me._triggerComplete(me);
                me.totalPages = parseInt($(me.dataWrapper).find(me.selectorTotalPages).val());
                me.pageIndex = parseInt($(me.dataWrapper).find(me.selectorPageIndex).val());
                me.totalRows = parseInt($(me.dataWrapper).find(me.selectorTotalRows).val());
                me._renderPages();
                if (me.isSeletable) {
                    $(me.dataWrapper).find("tr").click(function () {
                        $(this).addClass("selected").siblings().removeClass("selected");
                    });
                }
            });
        },
        refresh: function () {
            this.pageTo(this.pageIndex);
        },
        showBusy: function () {
            var $pager = $(this.pagerWarper);
            var $busy = $pager.find(".busy");
            if ($busy.length == 0) {
                $busy = $('<span class="busy">加载中...</span>');
                $pager.prepend($busy);
            }
            $pager.find(".refresh").hide();
        },
        removeBusy: function () {
            var $pager = $(this.pagerWarper);
            $pager.find(".busy").remove();
            $pager.find(".refresh").show();
        },
        parse: function () {
            var me = this;
            me.totalPages = parseInt($(me.dataWrapper).find(me.selectorTotalPages).val());
            me.pageIndex = parseInt($(me.dataWrapper).find(me.selectorPageIndex).val());
            me._bindPageClicks();
        },
        _getUrl: function (pageIndex, me) {
            var url = '';
            if (me.getUrlFunc != null) {
                url = me.getUrlFunc(pageIndex);
            } else {
                url = me.url;
            }
            if (me.url.indexOf("?") < 0) {
                url += "?";
            }
            url += "&pageIndex=" + pageIndex + "&pageSize=" + me.pageSize;
            var sortDirection = "asc";
            var $sort = $(this.sortWrapper).find(".sort-asc");
            if ($sort.length == 0) {
                $sort = $(this.sortWrapper).find(".sort-desc");
                sortDirection = "desc";
            }
            if ($sort.length > 0) {
                url += "&sort=" + $sort.attr("data-sort")
                    + "&sortDirection=" + sortDirection;
            }
            return url;
        },
        _renderPages: function () {
            var me = this;
            var html = "";
            var startPageNumber = me.pageIndex + 1 - (Math.floor(me.maxPageNumbers / 2));

            if (me.pageIndex + 1 + Math.floor(me.maxPageNumbers / 2) >= me.totalPages) {
                startPageNumber = me.totalPages - me.maxPageNumbers + 1;
            }
            startPageNumber = startPageNumber < 1 ? 1 : startPageNumber;
            var endPageNumber = startPageNumber - 1 + (me.totalPages > me.maxPageNumbers ? me.maxPageNumbers : me.totalPages);
            endPageNumber = endPageNumber < 1 ? 1 : endPageNumber;
            html += '<li class="previous' + (me.pageIndex > 0 ? "" : " disabled") + '"><a href="javascript:void(0);">上一页</a></li>';
            for (var i = startPageNumber; i <= endPageNumber; i++) {
                var css = i == me.pageIndex + 1 ? "selected" : "";
                html += '<li class="page ' + css + '"><a href="javascript:void(0);">' + i + '</a></li>';
            }
            html += '<li class="next' + (me.pageIndex < me.totalPages - 1 ? "" : " disabled") + '"><a href="javascript:void(0);">下一页</a></li>';
            var $pager = $(me.pagerWarper);
            $pager.find("ul").html(html);

            if (me.showSummary) {
                var start = me.pageIndex * me.pageSize + 1;
                var end = Math.min((me.pageIndex + 1) * me.pageSize, me.totalRows);
                $pager.find(".pager-summary").html(start + " - " + end + "/" + me.totalRows);
            }
            me._bindPageClicks();
        },
        _bindPageClicks: function () {
            var me = this;
            var $pager = $(me.pagerWarper);
            $pager.find(".previous a").click(function (e) {
                e.preventDefault();
                if (me.pageIndex > 0) {
                    me.pageIndex--;
                    me.pageTo(me.pageIndex);
                }
            });

            $pager.find(".next a").click(function (e) {
                e.preventDefault();
                if (me.pageIndex < me.totalPages - 1) {
                    me.pageIndex++;
                    me.pageTo(me.pageIndex);
                }
            });

            $pager.find(".page a").click(function (e) {
                e.preventDefault();
                me.pageIndex = parseInt($(this).text()) - 1;
                me.pageTo(me.pageIndex);
            });
        },
        _triggerSuccess: function (data) {
            var evt = $.Event("Success");
            evt.value = data;
            $(this).trigger(evt);
        },
        _triggerComplete: function (data) {
            var evt = $.Event("Complete");
            evt.value = data;
            $(this).trigger(evt);
        },
        success: function (callback) {
            if (callback == null) {
                return;
            }
            $(this).bind("Success", callback);
        },
        beforeSend: function (callback) {
            if (callback == null) {
                return;
            }
            $(this).bind("BeforeSend", callback);
        },
        complete: function (callback) {
            if (callback == null) {
                return;
            }
            $(this).bind("Complete", callback);
        }
    };

})();