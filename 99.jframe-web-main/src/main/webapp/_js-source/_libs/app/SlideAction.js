/**
 * Created by Leo on 2017/9/21.
 */

(function(){

    if(window.SlideAction){
        return;
    }
    window.SlideAction = function($items){
        this.$items = $items;
        this.leftActionWidth = 0;
        this.rightActionWidth = 0;
    };
    SlideAction.isWindowEventAlreadyBound = false;

    SlideAction.prototype={
        init: function(){
            var me = this;

            me.$items.each(function(){
                me._bindItem($(this).find(".slide-action-wrap"));
            });
            if(SlideAction.isWindowEventAlreadyBound === false){
                var touchManager = new TouchManager($("body"));
                touchManager.onTouchStart = function(){
                    me.reset();
                };
                touchManager.init();
            }
        },
        reset: function(){

        },
        _bindItem: function($itemWrap){
            var InitialValue;
            var state = '01';  //状态码 01 初始状态 02左滑动成功 03左滑动失败；
            var me = this;
            var touchManager = new TouchManager($itemWrap);
            touchManager.isVerticalEnabled = false;

            $itemWrap[0].addEventListener("touchstart",function (e) {

                InitialValue = e.touches[0].clientX;

            },false);

            $itemWrap[0].addEventListener("touchmove",function (e) {
                var num = -(InitialValue-(e.touches[0].clientX));
                $itemWrap.css("transition","none");
                if(Math.abs(num)<=65  && num < 0){
                    $itemWrap.css('left',num+'px')
                }
                if(num < -32){
                    state = '02';
                }else if(num > -32 ){
                    state= '03'
                }


            },false);



            touchManager.onTouchEnd = function(isToLeft, isToTop){
                if(state === '03'){
                    $itemWrap.css({"transition": "0.3s","left":"0px"})
                }else if(state === '01'){

                }else{
                    $itemWrap.css({"transition": "0.3s","left":"-65px"})

                }
            };
            touchManager.setLeft = function($content, left){
                if(left < -me.rightActionWidth){
                    left = -me.rightActionWidth;
                }
                if(left > me.leftActionWidth){
                    left = me.leftActionWidth;
                }

            };
            touchManager.init();
        }

    };


})();