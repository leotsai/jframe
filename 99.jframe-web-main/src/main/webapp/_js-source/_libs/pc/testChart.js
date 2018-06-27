/**
 * Created by Leo on 2017/12/27.
 */

(function () {

    if(window.PageDataPointViewer){
        return;
    }
    window.PageDataPointViewer = function ($wrap) {
        this.$wrap = $wrap;
        this.$typesWrap = $types;
        this.timeframe = null;
        this.$ddlFrequencies = null;
        this.dataTypes = null;
        this.echart = null;
        this.getDataTypesUrl = null;
        this.getTotalsUrl = null;
    };
    PageDataPointViewer.prototype = {
        init: function () {
            var me = this;
            me.timeframe = new TimeframeSelector();
            me.timeframe.onSelectionChanged(function (from, to) {
                me.renderData();
                me.getTotals();
            });
            me.timeframe.init();
            me.$ddlFrequencies.change(function () {
                me.renderData();
            });
            me.getDataTypes(function () {
                me.renderTypes();
            });
            me.echart = new Echart();
            me.setEchartOptions();
        },
        renderData: function () {
            
        },
        getDataTypes: function (callback) {
            var me = this;
            if(me.dataTypes){
                return;
            }
            mvcApp.ajax.post(me.getDataTypesUrl, '', function (result) {
                me.dataTypes = result.value;
                callback();
            },true);
        },
        renderTypes: function () {
            var me = this;
            for(var i = 0; i < me.dataTypes.length; i++ ){
                var li = '<li data-key="'+me.dataTypes[i].key+'"><a href="javascript:;">'+me.dataTypes[i].text+'</a><label>---</label></li>';
                var $li = $(li);
                $li.find('a').click(function () {
                    var $parent = $(this).closest('li');
                    if($parent.hasClass('selected')){
                        $parent.removeClass('selected');
                    } else {
                        $parent.addClass('selected');
                    }
                    me.renderData();
                });
                me.$typesWrap.append($li);
            }

        },
        getTotals: function () {
            var me = this;
            mvcApp.ajax.post(me.getTotalsUrl,'', function (result) {
                var totals = result.value;
                for(var i = 0; i < totals.length; i++){
                    $('li[data-key = '+totals.key+']').find('label').html(totals[i].value);
                }
            }, true)
        },
        setEchartOptions: function () {

        }
    };

})();

var pageViewer = new PageDataPointViewer($wrap);
pageViewer.getDataTypesUrl = 'test.json';
pageViewer.getTotalsUrl = 'test1.json';
pageViewer.$ddlFrequencies = $('#ddlFrequencies');
pageViewer.init();