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
        this.items = [];
        this._indexData = {
            current: 0,
            previous: 0,
            next: 0
        };
        this._$ul = null;
        this._touchManager = null;
        this.onIndexChanged = null;
        this._autoPlayTimer = null;
    };

    LoopShow.prototype = {
        init: function () {
            var me = this;
            this._$wrap.addClass("loopshow")
                .html('<ul class="loopshow-items" style="width:' + this.width * 3 + 'px;"></ul>');
            this._$ul = this._$wrap.find("ul.loopshow-items");
            this._touchManager = new TouchManager(this._$ul);
            this._touchManager.onTouchStart = function() {
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
            var $lis = this._$wrapLegend.find("ul.loopshow-legend").children("li");
            $lis.removeClass("selected");
            $lis.eq(this._indexData.current).addClass("selected");
            this.fixIndexData(index);
        },
        _moveToLeft: function(targetLeft, callback) {
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
                x = targetLeft > startLeft ? x + 50 : x - 50;
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
                }, 20);
            };
            slow();
        },
        _startAutoPlay: function() {
            var me = this;
            clearInterval(me._autoPlayTimer);
            me._autoPlayTimer = setInterval(function() {
                if (me._touchManager.isTouching()) {
                    return;
                }
                me._moveToLeft(-me.width * 2, function() {
                    me.show(me._indexData.current + 1);
                });
            }, this.autoPlayInterval);
        },
        renderItem: function (dataItem) {
            var height = this.height;
            var width = this.width;
            var html = '<li style="width:' + width + 'px; height:' + height + 'px;">' + this.buildItemContentHtml(dataItem, width, height) + '</li>';

            var $li = $(html);
            $li.find("img").load(function () {
                $(this).closest(".img-wrap").find(".loading").remove();
            });

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

            this._indexData.current = index;
            this._indexData.previous = previous;
            this._indexData.next = next;
        },
        _buildLegend: function() {
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

(function() {
    window.TouchManager = function ($content) {
        this._$content = $content;
        this._isTouching = false;
        this._startPosition = null;
        this._lastTouchPosition = null;
        this._isMovingVertical = null;
    };

    TouchManager.prototype = {
        init: function () {
            var me = this;
            var dom = me._$content[0];
            dom.addEventListener("touchstart", function (e) {
                me._handleTouchstart(e);
            });
            dom.addEventListener("touchmove", function (e) {
                me._handleTouchmove(e);
            });
            dom.addEventListener("touchend", function (e) {
                me._handleTouchend(e);
            });
        },
        _getTouchPosition: function (event) {
            try {
                var touch = event.targetTouches[0];
                if (touch == undefined) {
                    return null;
                }
                return {
                    x: touch.pageX,
                    y: touch.pageY
                };
            } catch (ex) {
                return {
                    x: event.clientX,
                    y: event.clientY
                };
            }
        },
        _handleTouchstart: function (e) {
            this._isTouching = true;
            var position = this._getTouchPosition(e);
            this._startPosition = position;
            this._lastTouchPosition = position;
            this._isMovingVertical = null;
            this.onTouchStart();
        },
        _handleTouchmove: function (e) {
            var position = this._getTouchPosition(e);
            if (position == null) {
                return;
            }
            if (this._lastTouchPosition == null) {
                this._lastTouchPosition = position;
                this._startPosition = position;
                return;
            }
            if (this._isMovingVertical == null) {
                this._isMovingVertical = Math.abs(position.y - this._startPosition.y) > Math.abs(position.x - this._lastTouchPosition.x);
            }
            if (this._isMovingVertical === true) {
                return;
            }
            e.preventDefault();

            var movedX = position.x - this._lastTouchPosition.x;
            this._$content.css("left", this._$content.position().left + movedX);
            this._lastTouchPosition = position;
        },
        _handleTouchend: function (e) {
            this._isTouching = false;
            var position = this._getTouchPosition(e);
            if (position != null) {
                this._lastTouchPosition = position;
            }
            var isToLeft = null;
            if (this._lastTouchPosition != null) {
                var movedX = this._lastTouchPosition.x - this._startPosition.x;
                isToLeft = movedX === 0 ? null : movedX < 0;
            }
            this.onTouchEnd(isToLeft);
        },
        onTouchStart: function () {

        },
        onTouchEnd: function (isToLeft) {

        },
        isTouching: function () {
            return this._isTouching;
        }
    };
})();