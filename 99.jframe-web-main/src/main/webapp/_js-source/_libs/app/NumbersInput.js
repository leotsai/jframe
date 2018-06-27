/**
 * 密码键盘
**author: huxiaoli
**created： 20170930
 */


(function(){

	if(window.NumberKeyboard){
		return;
	}

	window.NumberKeyboard = function($input, $container){
		this.$input = $input;
		this.$container = $container == undefined ? $("#body") : $container;
		this.totalNumbers = 6;
		this.numbers = [];
		this._callback = null;
	};

	NumberKeyboard.prototype = {
		init: function(){
			var me = this;
			var ul = me._buildCell();
			me.$input.html(ul);
			me.$input.click(function(){
				me.show();
			});
		},
		show: function(callback){
			this.numbers = [];
			this._callback = callback;
			this.remove();
			this._buildKeyboard();
			this.$input.find("ul.number-list > li").removeClass("key-dot");
		},
		onInputCompleted: function(numbers){
			
		},
		remove: function(){
			this.$container.children('.keyboard').remove();
		},
		_buildCell: function(){
			var wid = parseInt(100 / this.totalNumbers);
			var ul = '<ul class="number-list">';
			for(var i = 0; i < this.totalNumbers; i++) {
				if( i === this.totalNumbers - 1) {
					ul += '<li class="last" style="width:'+wid+'%"></li>';
				} else {
					ul += '<li class="" style="width:'+wid+'%"></li>';
				}
			}
			return ul + "</ul>";
		},
		_buildKeyboard: function(){
			var me = this;
			var html = '<div class="keyboard">'+
						'<div class="k-close"></div>'+
			            '<ul><li class="k-number"><a href="javascript:;">1</a></li>'+
			            '<li class="k-number"><a href="javascript:;">2</a></li>'+
			            '<li class="k-number nobr"><a href="javascript:;">3</a></li>'+
			            '<li class="k-number"><a href="javascript:;">4</a></li>'+
			            '<li class="k-number"><a href="javascript:;">5</a></li>'+
			            '<li class="k-number nobr"><a href="javascript:;">6</a></li>'+
			            '<li class="k-number "><a href="javascript:;">7</a></li>'+
			            '<li class="k-number "><a href="javascript:;">8</a></li>'+
			            '<li class="k-number nobr"><a href="javascript:;">9</a></li>'+
			            '<li class="bg-gray nobt"><a href="javascript:;">&nbsp</a></li>'+
			            '<li class="k-number nobt"><a href="javascript:;">0</a></li>'+
			            '<li class="bg-gray k-del nobt nobr"><a href="javascript:;"></a></li>'+
			            '</ul></div>';
			var $keyboard = $(html);
            $keyboard.find('.k-number a').click(function(){
				me._onClickNumber($(this));
			});
            $keyboard.find('.k-del a').click(function(){
				me._onDeleteNumber();
			});
            $keyboard.find('.k-close').click(function(){
				me.remove();
			});
			me.$container.append($keyboard);
		},
		_onClickNumber: function($element){
			var me = this;
			if(me.numbers.length >= me.totalNumbers){
				return;
			}
			var txt = $element.html();
			me.numbers.push(txt);
			me._inputValue(me.numbers);
		},
		_onDeleteNumber: function(){
			this.numbers.pop();
			this._inputValue(this.numbers);

		},
		_inputValue: function($numbers){
			var me = this;
			var $li = me.$input.find('.number-list li');
			for (var i = 0; i < me.totalNumbers; i++){
				if($numbers[i]) {
					$li.eq(i).addClass('key-dot');
				} else {
					$li.eq(i).removeClass('key-dot');
				}
			}
			if($numbers.length === me.totalNumbers){
				me._callback && me._callback(me.numbers);
				me.onInputCompleted(me.numbers);
			}
		}
	}

})();