/**
 * Created by Administrator on 2017/12/8.
 */
/**
 * Created by Administrator on 2017/12/8.
 */

(function () {

    if(window.ListScroller){
        return;
    }

    window.ListScroller = function ($ul) {
        this.$container = $ul.parent();
        this.$ul = $ul;
        this.intervalSeconds = 5;
        this.scrollingCount = 2;
        this._timer = null;
    };

    ListScroller.prototype = {
        init: function () {
            if(this.$ul.children().length <= this.scrollingCount){
                return;
            }
            var me = this;
            me.$container.addClass('list-scroller').children().eq(0).addClass('list-scroller-content');
            this._timer = setInterval(function(){
                var top = me.getNextTop();
                me.$container.children().eq(0).css("top", top);
            }, this.intervalSeconds * 1000);
        },
        stop: function(){
            clearInterval(this._timer);
        },
        getNextTop: function () {
            var containerHeight = this.$container.innerHeight();
            var contentHeight = this.$container.children().eq(0).outerHeight();
            var minTop =  containerHeight - contentHeight;
            var currentTop = this.$container.children().eq(0).css('top').replace('px','')*1;
            if(currentTop <= minTop){
                return 0;
            }
            var nextTop = currentTop - containerHeight;
            if(nextTop < minTop){
                return minTop;
            }
            return nextTop;
        }
    }
})();