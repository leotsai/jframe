/*
更新记录：
2017-07-14 13:27    增加了onIndexChanged处理方法，同时增加了enableWeixinImageViewer（微信内使用微信图片预览）
                    增加了TouchManager.bindClick方法
2017-07-18 22:29    修复了legend当前高亮的bug
*/
(function () {
    if (window.LoopShow) {
        return;
    }
    window.LoopShow = function ($wrap, $wrapLegend) {
        this._$wrap = $wrap;
        this._$wrapLegend = $wrapLegend;
        this.height = $wrap.height();
        this.width = $wrap.width();
        this.autoPlay = true;
        this.autoPlayInterval = 3500;
        this.enableWeixinImageViewer = false;
        this.items = [];
        this._indexData = {
            current: -1,
            previous: -1,
            next: -1
        };
        this._$ul = null;
        this._touchManager = null;
        this.onIndexChanged = null;
        this._autoPlayTimer = null;
    };

    LoopShow.prototype = {
        init: function () {
            var me = this;
            if(mvcApp.utils.getFullUrl == undefined && this.enableWeixinImageViewer){
                alert("mvcApp.utils.getFullUrl == undefined");
            }
            this._$wrap.addClass("loopshow")
                .html('<ul class="loopshow-items" style="width:' + this.width * 3 + 'px;"></ul>');
            this._$ul = this._$wrap.find("ul.loopshow-items");
            this._touchManager = new TouchManager(this._$ul);
            this._touchManager.isVerticalEnabled = false;
            this._touchManager.onTouchStart = function () {
                clearInterval(me._autoPlayTimer);
            };
            this._touchManager.onTouchEnd = function (isToLeft) {
                var targetLeft = -me.width;
                if (isToLeft != null) {
                    targetLeft = isToLeft ? targetLeft - me.width : targetLeft + me.width;
                }
                me._moveToLeft(targetLeft, function () {
                    var index = me._indexData.current;
                    if (isToLeft != null) {
                        index = isToLeft === true ? me._indexData.current + 1 : me._indexData.current - 1;
                    }
                    me.show(index);
                    if (me.autoPlay) {
                        me._startAutoPlay();
                    }
                });
            };
            this._touchManager.init();
            this._buildLegend();
            this.fixIndexData(0);
            this.show(0);
            if (me.autoPlay) {
                me._startAutoPlay();
            }
        },
        show: function (index) {
            var me = this;
            if (me._$ul.children().length === 0) {
                var indexes = [this._indexData.previous, this._indexData.current, this._indexData.next];
                for (var ii = 0; ii < indexes.length; ii++) {
                    me._$ul.append(this.renderItem(this.items[indexes[ii]]));
                }
            } else {
                var currentLeft = me._$ul.position().left;
                if (currentLeft === -2 * me.width) {
                    me._$ul.children().first().remove();
                    var nextNext = this._indexData.next + 1;
                    if (nextNext >= this.items.length) {
                        nextNext = 0;
                    }
                    me._$ul.append(this.renderItem(this.items[nextNext]));
                }
                else if (currentLeft === 0) {
                    me._$ul.children().last().remove();
                    var prevPrevious = this._indexData.previous - 1;
                    if (prevPrevious < 0) {
                        prevPrevious = this.items.length - 1;
                    }
                    me._$ul.prepend(this.renderItem(this.items[prevPrevious]));
                }
            }
            me._$ul.css("left", -me.width);
            this.fixIndexData(index);
            var $lis = this._$wrapLegend.find("ul.loopshow-legend").children("li");
            $lis.removeClass("selected");
            $lis.eq(this._indexData.current).addClass("selected");
        },
        _moveToLeft: function (targetLeft, callback) {
            var me = this;
            var startLeft = me._$ul.position().left;
            if (startLeft === targetLeft) {
                callback();
                return;
            }

            function slow() {
                if (me._touchManager.isTouching()) {
                    return;
                }
                var x = me._$ul.position().left;
                x = targetLeft > startLeft ? x + 25 : x - 25;
                if ((targetLeft > startLeft && x > targetLeft) || (targetLeft < startLeft && x < targetLeft)) {
                    x = targetLeft;
                }
                me._$ul.css("left", x);
                if (x === targetLeft) {
                    callback();
                    return;
                }
                setTimeout(function () {
                    slow();
                }, 5);
            }
            slow();
        },
        _startAutoPlay: function () {
            var me = this;
            clearInterval(me._autoPlayTimer);
            me._autoPlayTimer = setInterval(function () {
                if (me._touchManager.isTouching()) {
                    return;
                }
                me._moveToLeft(-me.width * 2, function () {
                    me.show(me._indexData.current + 1);
                });
            }, this.autoPlayInterval);
        },
        renderItem: function (dataItem) {
            var me = this;
            var height = this.height;
            var width = this.width;
            var html = '<li style="width:' + width + 'px; height:' + height + 'px;">' + this.buildItemContentHtml(dataItem, width, height) + '</li>';

            var $li = $(html);
            $li.find("img").load(function () {
                $(this).closest(".img-wrap").find(".loading").remove();
            });
            if (this.enableWeixinImageViewer) {
                TouchManager.bindClick($li.find("img"), function () {
                    var imageUrls = [];
                    for (var i = 0; i < me.items.length; i++) {
                        imageUrls.push(mvcApp.utils.getFullUrl(me.items[i].src));
                    }
                    WeixinJsSdk.ready(function () {
                        wx.previewImage({
                            current: mvcApp.utils.getFullUrl(dataItem.src),
                            urls: imageUrls
                        });
                    });
                });
            }
            return $li;
        },
        buildItemContentHtml: function (dataItem, width, height) {
            return '<div class="img-wrap">\
                    <img src="' + (dataItem.src ? dataItem.src : dataItem) + '"/><span class="loading" style="line-height:' + height + 'px;">loading...</span>\
                </div>';
        },
        fixIndexData: function (index) {
            if (index >= this.items.length) {
                index = 0;
            }
            if (index < 0) {
                index = this.items.length - 1;
            }
            var previous = index - 1;
            if (previous < 0) {
                previous = this.items.length - 1;
            }

            var next = index + 1;
            if (next >= this.items.length) {
                next = 0;
            }
            var oldIndex = this._indexData.current;
            this._indexData.current = index;
            this._indexData.previous = previous;
            this._indexData.next = next;
            if (oldIndex !== index) {
                this.onIndexChanged && this.onIndexChanged(oldIndex, index);
            }
        },
        _buildLegend: function () {
            if (this._$wrapLegend == null) {
                return;
            }
            var html = '<ul class="loopshow-legend">';
            for (var i = 0; i < this.items.length; i++) {
                html += '<li><span></span></li>';
            }
            html += '</ul>';
            this._$wrapLegend.append(html);
            var $ul = this._$wrapLegend.find("ul.loopshow-legend");
            $ul.css("margin-left", -$ul.width() / 2);
        }
    };

})();
