/**
 * Created by Leo on 2017/9/21.
 */

(function () {
    if(window.TouchManager){
        return;
    }

    window.TouchManager = function ($content) {
        this._$content = $content;
        this._isTouching = false;
        this._startPosition = null;
        this._lastTouchPosition = null;
        this._isMovingVertical = null;

        this.isVerticalEnabled = true;
        this.isHorizontalEnabled = true;
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

            if(this.isHorizontalEnabled){
                var movedX = position.x - this._lastTouchPosition.x;
                this.setLeft(this._$content,this._$content.position().left + movedX);
            }
            if(this.isVerticalEnabled){
                var movedY = position.y - this._lastTouchPosition.y;
                this.setTop(this._$content,this._$content.position().top + movedY );
            }

            this._lastTouchPosition = position;
        },
        _handleTouchend: function (e) {
            this._isTouching = false;
            var position = this._getTouchPosition(e);
            if (position != null) {
                this._lastTouchPosition = position;
            }
            var isToLeft = null;
            var isToTop = null;
            if (this._lastTouchPosition != null) {
                if(this.isHorizontalEnabled){
                    var movedX = this._lastTouchPosition.x - this._startPosition.x;
                    isToLeft = movedX === 0 ? null : movedX < 0;
                }
                if(this.isVerticalEnabled){
                    var movedY = this._lastTouchPosition.y - this._startPosition.y;
                    isToTop = movedY === 0 ? null : movedY < 0;
                }
            }
            this.onTouchEnd(isToLeft);
        },
        onTouchStart: function () {

        },
        onTouchEnd: function (isToLeft, isToTop) {

        },
        isTouching: function () {
            return this._isTouching;
        },
        setLeft: function($content, left){
            $content.css("left", left);
        },
        setTop: function($content, top){
            $content.css("top", top);
        }
    };

    TouchManager.bindClick = function(jqSelector, handler) {
        (function (selector, callback) {
            $(selector).bind("touchstart", function () {
                $(this).addClass("clicking");
                setTimeout(function() {
                    $(selector).removeClass("clicking");
                }, 2000);
            });
            $(selector).bind("touchmove", function (e) {
                $(this).removeClass("clicking");
            });
            $(selector).bind("touchend", function (e) {
                if ($(this).hasClass("clicking")) {
                    callback.call(this, e);
                }
                $(this).removeClass("clicking");
            });
        })(jqSelector, handler);
    };

})();