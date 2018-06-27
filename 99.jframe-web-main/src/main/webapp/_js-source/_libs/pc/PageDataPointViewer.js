/**
 * Created by Leo on 2017/12/27.
 */
(function () {
    if (window.PageDataPointViewer) {
        return;
    }
    window.PageDataPointViewer = function ($wrap) {
        this.$wrap = $wrap;
        this.timeframe = null;
        this.$ddlFrequencies = null;
        this.urlGetDataTypes = '';
        this.urlGetSeries = '';
        this.getTotalsUrl = '';
        this.dataTypes = null;
        this.$listTypes = null;
        this.chart = null;
        this.series = [{
            key: '',
            name:'',
            visible: true,
            list: []
        }];
        this._from = null;
        this._to = null;
        this._frequency = 'HOUR';   //显示的频率
        this.option = {
            series: []
        };
        this.legendData = [];
        this.legendSelected = {};
    };

    PageDataPointViewer.prototype = {
        init: function () {
            var me = this;
            me.chart = echarts.init(document.getElementById(me.$wrap.attr('id')));
            me.$ddlFrequencies.change(function () {
                me._frequency = $(this).val();
                me.renderChart();
            });
            me.getDataTypes(function () {
                me._renderTypes();
                for(var i = 0; i < me.dataTypes.length; i++){
                    me.legendData.push(me.dataTypes[i].text);
                    me.legendSelected[me.dataTypes[i].text] = false;
                }
            });
            me.timeframe = new TimeframeSelector();
            me.timeframe.onSelectionChanged(function (e) {
                me._from = e.value.from;
                me._to = e.value.to;
                me.loadTotals();
                me.renderChart();
            });
            me.timeframe.init();
            me._setChartOptions([]);
            me.chart.setOption(me.option);
        },
        renderChart: function () {
            this.series = [];
            this._setChartOptions([]);
            this.renderSeries();
        },
        getDataTypes: function (callback) {
            var me = this;
            if (me.dataTypes != null) {
                callback();
                return;
            }
            mvcApp.ajax.post(me.urlGetDataTypes, null, function (result) {
                me.dataTypes = result.value;
                callback();
            }, true);
        },
        _setChartOptions: function (xData) {
            this.option = {
                title: {
                    text: ''
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    show:false,
                    data: this.legendData,
                    selected: this.legendSelected
                },
                xAxis: {
                    type: 'category',
                    data: xData,
                    splitLine:{
                        show: true
                    }
                },
                yAxis: {
                    type: 'value'
                },
                series: []
            };
        },
        _renderTypes: function () {
            var me = this;
            for (var i = 0; i < me.dataTypes.length; i++) {
                var type = me.dataTypes[i];
                var li = '<li data-key="' + type.key + '"><a href="javascript:;">' + type.text + '</a><label>...</label></li>';
                var $li = $(li);
                $li.find("a").click(function () {
                    var $liParent = $(this).closest("li");
                    if ($liParent.hasClass("selected")) {
                        $liParent.removeClass("selected")
                    }
                    else {
                        $liParent.addClass("selected");
                    }
                    me.renderSeries();
                });
                me.$listTypes.append($li);
            }
        },
        renderSeries: function () {
            var me = this;
            this.$listTypes.children().each(function () {
                var key = $(this).attr("data-key");
                var value = $(this).find('a').text();
                if ($(this).hasClass("selected")) {
                    me._addChartSerie(key, value);
                }
                else {
                    me._hideChartSerie(key, value);
                }
            });
        },
        loadTotals: function () {
            var me = this;
            var totals= [];
            var customData = me.customData();
            var data =  "fromDate=" + me._from + "&toDate=" + me._to + "&frequency=" + me._frequency +'&'+customData;
            mvcApp.ajax.post(me.getTotalsUrl,data, function (result) {
                var totals = result.value;
                for(var i = 0; i < totals.length; i++){
                    $('li[data-key = '+totals[i].key+']').find('label').html(totals[i].value);
                }
            }, true);
            // setTimeout(function(){
            //     // var result={
            //     //     value: [
            //     //         {key: 'PV', value:110},
            //     //         {key:'Circel',value:120},
            //     //         {key:'Friend', value:130},
            //     //         {key:'join', value:140},
            //     //         {key:'Share',value:150}]
            //     // };
            //     // totals = result.value;
            //     for(var i = 0; i < totals.length; i++){
            //         $('li[data-key = '+totals[i].key+']').find('label').html(totals[i].value);
            //     }
            // },1000);
        },
        _hideChartSerie: function (key) {
            var serieItem = this.getSerieByKey(key);
            if (serieItem == null) {
                return;
            }
            serieItem.visible = false;
            this._chartApi_removeSerie(serieItem);
        },
        _addChartSerie: function (key, name) {
            var me = this;
            var serieItem = this.getSerieByKey(key);
            if (serieItem == null) {
                serieItem = {
                    key: key,
                    name:name,
                    list: null,
                    visible: true
                };
                me.series.push(serieItem);
                var customData = me.customData();
                var data = "key=" + key + "&fromDate=" + me._from + "&toDate=" + me._to + "&frequency=" + me._frequency +'&'+customData;
                mvcApp.ajax.post(me.urlGetSeries, data, function (result) {
                    serieItem.list = result.value;
                    if(serieItem.visible){
                        me._chartApi_addSerie(serieItem);
                    }
                }, true);
            }
            else {
                me._chartApi_addSerie(serieItem);
            }
        },
        getSerieByKey: function (key) {
            for (var i = 0; i < this.series.length; i++) {
                var item = this.series[i];
                if (item.key === key) {
                    return item;
                }
            }
            return null;
        },
        _chartApi_addSerie: function (serieItem) {
            if(serieItem.list == null){
                return;
            }
            var me = this;
            var list = serieItem.list;
            if(me._getItemByName(me.option.series, serieItem.name) === -1){
                var xData = [];
                var seriesData = [];
                for(var i = 0; i < list.length; i++){
                    xData.push(list[i].time);
                    seriesData.push(list[i].value);
                }
                if(me.option.xAxis.data.length === 0){
                    me._setChartOptions(xData);
                }
                me.option.series.push({
                    name:serieItem.name,
                    type:'line',
                    smooth: true,
                    data: seriesData
                });
                me.legendSelected[serieItem.name] = true;
            } else {
                me.legendSelected[serieItem.name] = true;
            }
            me.chart.setOption(me.option);
        },
        _chartApi_removeSerie: function (serieItem) {
            if(serieItem.list == null){
                return;
            }
            var me = this;
            me.legendSelected[serieItem.name] = false;
            me.chart.setOption(me.option);
        },
        _getItemByName: function (arr, name) {
            for(var i = 0; i < arr.length; i++){
                if(arr[i].name == name){
                    return i;
                }
            }
            return -1;
        },
        customData: function () {
            
        }
    };

})();
