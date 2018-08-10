/**
 * Dates
 * author       : xv
 * description  : '日期控件'
 * date         : 2014-9-20
 * version      : 1.0
 */
;
(function (factory) {
    if (typeof define === 'function') {
        define('xvDate', [], function (require, exports, module) {
            var xvDate = {};
            factory(xvDate);
            module.exports = xvDate.xvDate;
        })
    } else {
        factory(window);
    }
})(function (win) {
    var Calendar = {};
    win.xvDate = function (opts) {
        var triggerId = opts.triggerId || opts.targetId;
        if (typeof triggerId === 'object' && triggerId.length) {
            var triggerEls = [];
            for (var i = 0; i < triggerId.length; i++) {
                triggerEls.push(Calendar.getId(triggerId[i]));
                Calendar.enable(triggerEls[i], triggerEls, opts);
            }
        } else {
            triggerEls = Calendar.getId(triggerId);
            Calendar.enable(triggerEls, triggerEls, opts);
        }
    };
    Calendar.fn = Calendar.prototype = {
        init: function (opts) {
            var isToday = false;
            this.creatView(opts);
            this.initTimes(isToday);
        },
        gb: {},
        creatView: function (settings) {
            var opts = settings || {},
                _this = this,
                iptDatesId = opts.targetId || 'dates',
                iptDates = Calendar.getId(iptDatesId),
                docOpts;
            _this.gb.algin = opts.alignId ? Calendar.getId(opts.alignId) : iptDates;
            _this.gb.format = opts.format || '-';
            _this.gb.hms = (opts.hms === 'off' ? null : true);
            _this.gb.zIndex = opts.zIndex || 9999;
            _this.gb.trigger = opts.trigger;
            _this.limitTime(opts.min, 'minTime');
            _this.limitTime(opts.max, 'maxTime');

            if (!_this.gb.datesBox) {
                var datesBox = _this.gb.datesBox = _this.creatEle("div");
                _this.gb.datesBox.id = 'xv_Dates_box';
                _this.gb.target = iptDates;
                _this.gb.datesBox.className = 'dates_box';
                _this.gb.datesBox.style.display = 'block';
                _this.gb.datesBox.style.zIndex = _this.gb.zIndex;
                $(iptDates).after(_this.gb.datesBox);
                datesBox.innerHTML = "<div class='dates_box_top'>" +
                "<div class='dates_yy dates_choose'>" +
                "<span class='prev_choose choose_btn' id='xv_Yy_prev'><i class='sign'></i></span>" +
                "<span class='ipt_wrap' id='xv_Year_wrap'><input class='dlt_status' style='width:32px;margin-left:4px;' id='xv_Ipt_year' type='text'><small>年</small><i class='sign'></i></span>" +
                "<span class='next_choose choose_btn' id='xv_Yy_next'><i class='sign'></i></span>" +
                "<div class='dates_yy_list' id='xv_Dates_yy_list' style='display:none'>" +
                function () {
                    var str = '';
                    for (var i = 1970; i < 2100; i++) {
                        str += '<span dateValue = ' + i + '>' + i + '</span>';
                    }
                    return str;
                }() +
                "</div>" +
                "</div>" +
                "<div class='dates_mm dates_choose'>" +
                "<span class='prev_choose choose_btn' id='xv_Mm_prev'><i class='sign'></i></span>" +
                "<span class='ipt_wrap' id='xv_Moth_wrap'><input class='dlt_status' style='width:16px;margin-left:10px;' id='xv_Ipt_month' type='text'><small>月</small><i class='sign'></i></span>" +
                "<span class='next_choose choose_btn' id='xv_Mm_next'><i class='sign'></i></span>" +
                "<div class='dates_mm_list' id='xv_Dates_mm_list' style='display:none'>" +
                function () {
                    var str = '';
                    for (var i = 1; i < 13; i++) {
                        if (i < 10) {
                            str += '<span dateValue =' + i + '>0' + i + '月</span>';
                        } else {
                            str += '<span dateValue =' + i + '>' + i + '月</span>';
                        }
                    }
                    return str;
                }() +
                "</div>" +
                "</div>" +
                "</div>" +
                this.viewTb() +
                "<div class='dates_bottom'>" +
                "<ul id='dates_hms' class='dates_hms' style='display:block' >" +
                "<li class='time_tag'>时分秒</li>" +
                "<li><input type='text' id='xv_Hours' class='dlt_status'><span>:</span></li>" +
                "<li><input type='text' id='xv_Minutes' class='dlt_status'><span>:</span></li>" +
                "<li><input type='text' id='xv_Seconds' class='dlt_status'><span>&nbsp;</span></li>" +
                "</ul>" +
                "<div id='dates_btn' class='dates_btn'>" +
                "<a id='xv_Dates_clear'>清空</a>" +
                "<a id='xv_Dates_today'>今天</a>" +
                "<a id='xv_Dates_ok'>确认</a>" +
                "</div>" +
                "</div>";
                _this.handleTime();
                _this.gb.datesHms = Calendar.getId('dates_hms');
                _this.setPosition(_this.gb.algin, _this.gb.datesBox, {left: 0, top: 0});
                _this.setPosition(_this.gb.algin, _this.gb.datesBox);
                docOpts = {that: _this};
                Calendar.addEvent(document, 'click', _this.docArea, docOpts);
            } else {
                if (_this.gb.target !== iptDates && !_this.ishd(_this.gb.datesBox)) {
                    Calendar.removeEvent(document, 'click', _this.docArea);
                    _this.shde(_this.gb.datesBox, 2);
                } else {
                    _this.shde(_this.gb.datesBox);
                }
                _this.gb.target = iptDates;
                // _this.setPosition(_this.gb.algin, _this.gb.datesBox, {left: 0, top: 0});
                // _this.setPosition(_this.gb.algin, _this.gb.datesBox);
                docOpts = {that: _this};
                Calendar.removeEvent(document, 'click', _this.docArea);
                Calendar.addEvent(document, 'click', _this.docArea, docOpts);
                _this.gb.datesBox.style.zIndex = _this.gb.zIndex;
                $(iptDates).after(_this.gb.datesBox);
            }

            var datesHms = this.gb.datesHms;
            var datesBtn = Calendar.getId('dates_btn');
            if (!datesHms || opts.hms === 'off') {
                this.shde(datesHms, 1);
            } else {
                this.shde(datesHms, 2);
            }
            this.shde(datesBtn, 1);

            var triggerId = opts.triggerId || opts.targetId;
            if (typeof triggerId === 'object' && triggerId.length) {
                _this.gb.trigger = [];
                for (var i = 0; i < triggerId.length; i++) {
                    _this.gb.trigger.push(Calendar.getId(triggerId[i]));
                }
            } else {
                _this.gb.trigger = Calendar.getId(triggerId);
            }

        },

        docArea: function (e) {
            var cuElt = e.srcElement || e.target;
            var that = e.datas.that;
            var cond = !that.ishd(that.gb.datesBox) && !that.contain(that.gb.datesBox, cuElt) && cuElt !== that.gb.target && !that.isExistObj(that.gb.trigger, cuElt);
            if (cond) {
                that.shde(that.gb.datesBox, 1);
            }
        },

        limitTime: function (time, timeType) {
            var times = this.checkTime(time);
            if (times) {
                this.gb[timeType] = this.datesSplit(times);
            } else {
                this.gb[timeType] = null;
            }
        },

        datesSplit: function (time) {
            if (time) {
                    datesTimeArr = time.split(' '),
                    datesArr = datesTimeArr[0].split(/[-\/]/),
                    timesArr = (this.gb.hms && datesTimeArr[1]) ? datesTimeArr[1].split(':') : null;
                return {
                    'year': datesArr[0],
                    'month': datesArr[1],
                    'date': datesArr[2],
                    'hours': timesArr ? timesArr[0] : 0,
                    'minutes': timesArr ? timesArr[1] : 0,
                    'seconds': timesArr ? timesArr[2] : 0
                };
            } else {
                return time;
            }
        },

        handleTime: function () {
            var _this = this;
            _this.gb.td = Calendar.getId("dates_table").getElementsByTagName("td");
            var els = _this.gb.els = {
                'yearWrap': Calendar.getId("xv_Year_wrap"),
                'monthWrap': Calendar.getId("xv_Moth_wrap"),
                'yyList': Calendar.getId("xv_Dates_yy_list"),
                'mmList': Calendar.getId("xv_Dates_mm_list"),
                'yyConList': Calendar.getId("xv_Dates_yy_list").getElementsByTagName("span"),
                'mmConList': Calendar.getId("xv_Dates_mm_list").getElementsByTagName("span"),
                'yyPrev': Calendar.getId("xv_Yy_prev"),
                'yyNext': Calendar.getId("xv_Yy_next"),
                'mmPrev': Calendar.getId("xv_Mm_prev"),
                'mmNext': Calendar.getId("xv_Mm_next"),
                'datesClear': Calendar.getId("xv_Dates_clear"),
                'datesToday': Calendar.getId("xv_Dates_today"),
                'datesOk': Calendar.getId("xv_Dates_ok"),
                'iptYear': Calendar.getId("xv_Ipt_year"),
                'iptMonth': Calendar.getId("xv_Ipt_month"),
                'iptHours': Calendar.getId("xv_Hours"),
                'iptMinutes': Calendar.getId("xv_Minutes"),
                'iptSeconds': Calendar.getId("xv_Seconds")
            };

            //时分秒的编辑操作处理
            var timesArr = [els.iptYear, els.iptMonth, els.iptHours, els.iptMinutes, els.iptSeconds];
            for (var i = 0; i < timesArr.length; i++) {//时分秒获取焦点的操作
                Calendar.addEvent(timesArr[i], 'click', _this.focusStatus);
            }

            Calendar.addEvent(els.iptYear, 'blur', function () {//年自定义编辑输入处理
                var str = '^((19[0-9]{2})|(2[0-9]{3}))$',
                    that = this,
                    year = _this.checkFocus(str, that, _this.gb.time.year);
                if (year) {
                    _this.gb.time.year = year;
                    _this.insertDate();
                }
            });

            Calendar.addEvent(els.iptMonth, 'blur', function () {//月自定义编辑输入处理
                var str = '^((0[1-9])|(1[0-2])|[1-9])$',
                    that = this,
                    month = _this.checkFocus(str, that, _this.formatTime(_this.gb.time.month));
                if (month) {
                    _this.gb.time.month = month;
                    _this.insertDate();
                }
            });

            Calendar.addEvent(els.iptHours, 'blur', function () {//小时自定义编辑输入处理
                var str = '^(([0-1][0-9])|(2[0-3])|[0-9])$';
                var that = this;
                _this.gb.time.hours = _this.checkFocus(str, that, '00');
            });

            Calendar.addEvent(els.iptMinutes, 'blur', function () {//分钟自定义编辑输入处理
                var str = '^(([0-5][0-9])|[0-9])$';
                var that = this;
                _this.gb.time.minutes = _this.checkFocus(str, that, '00');
            });

            Calendar.addEvent(els.iptSeconds, 'blur', function () {//秒自定义编辑输入处理
                var str = '^(([0-5][0-9])|[0-9])$';
                var that = this;
                _this.gb.time.seconds = _this.checkFocus(str, that, '00');
            });

            //时间面板的控制按钮
            Calendar.addEvent(els.datesClear, 'click', function () {//清空当前时间
                _this.gb.target.value = '';
            });

            Calendar.addEvent(els.datesOk, 'click', function () {
                var type = _this.gb.hms ? 'hms' : 'ymd';
                var datesT = _this.gb.time;
                if (_this.compareTime(datesT.year, datesT.month, datesT.date, type)) {
                    _this.getOkTime();
                }
            });

            Calendar.addEvent(els.datesToday, 'click', function () {//获取当天时间
                var isToday = true;
                _this.initTimes(isToday);
            });

            //处理列表年月box
            Calendar.addEvent(els.yearWrap, 'click', _this.yearLst, {els: els, that: _this});
            Calendar.addEvent(_this.gb.datesBox, 'click', _this.ymLst, {els: els, that: _this});

            //点击列表年月切换
            _this.ymClick(els.yyConList, els.yyList, 'year');
            _this.ymClick(els.mmConList, els.mmList, 'month');

            Calendar.addEvent(els.monthWrap, 'click', function () {
                if (els.mmList.style.display === 'none') {
                    if (document.activeElement !== els.iptMonth) {
                        els.mmList.style.display = 'block';
                    }
                } else {
                    els.mmList.style.display = 'none';
                }
            });
            //点击月份按钮切换

            var btnList = {
                mmPrev: [-1, 'month'],
                mmNext: [1, 'month'],
                yyPrev: [-1, 'year'],
                yyNext: [1, 'year']
            };

            for (i in btnList) {
                Calendar.addEvent(els[i], 'click', _this.datesBtn, {
                    that: _this,
                    direction: btnList[i][0],
                    type: btnList[i][1]
                });
            }

            for (i = 0; i < _this.gb.td.length; i++) {//日期点击切换
                Calendar.addEvent(_this.gb.td[i], 'click', _this.getDate, {
                    that: _this
                })
            }
        },

        getDate:function(e){
            var date = this.innerHTML,
                _this = e.datas.that,
                month = parseInt(this.getAttribute('m'), 10),
                year;
            if (_this.gb.time.month === 1 && month === 12) {
                year = _this.gb.time.year - 1;
            } else if (_this.gb.time.month === 12 && month === 1) {
                year = _this.gb.time.year + 1;
            } else {
                year = _this.gb.time.year;
            }
            if (_this.compareTime(year, month, date)) {
                _this.gb.time.year = year;
                _this.gb.time.month = month;
                _this.gb.time.date = date;
                _this.insertDate();
                _this.getOkTime();
            }
        },
        yearLst: function (e) {//年下拉列表的处理（每次默认日期显示在可视区域）
            var els = e.datas.els,
                _this = e.datas.that;
            if (els.yyList.style.display === 'none') {
                if (document.activeElement !== els.iptYear) {
                    els.yyList.style.display = 'block';
                    for (var i = 0; i < els.yyConList.length; i++) {
                        if ((els.yyConList[i].getAttribute('dateValue')) === _this.gb.time.year) {
                            var dTop = els.yyConList[i].offsetTop;
                            if (dTop > 170) {
                                els.yyList.scrollTop = dTop;
                            }
                        }
                    }
                }
            } else {
                els.yyList.style.display = 'none';
            }
        },
        ymLst: function (e) {
            var cuElt = e.srcElement || e.target,
                els = e.datas.els,
                _this = e.datas.that;
            if ((!_this.contain(els.yearWrap, cuElt)) && (!_this.ishd(els.yyList))) {
                els.yyList.style.display = 'none';
            }
            if ((!_this.contain(els.monthWrap, cuElt)) && (!_this.ishd(els.mmList))) {
                els.mmList.style.display = 'none';
            }
        },

        focusStatus: function () {
            if (this.focus) {
                this.select();
                this.className = '';
            }
        },
        combineTime: function (time, type) {
            var times;
            if (!time) {
                return false;
            }
            var yy = time.year.toString(),
                mm = this.formatTime(time.month.toString()),
                dd = this.formatTime(time.date.toString()),
                h = this.formatTime(time.hours.toString()),
                m = this.formatTime(time.minutes.toString()),
                s = this.formatTime(time.seconds.toString());
            switch (type) {
                case 'hms':
                    times = yy + mm + dd + h + m + s;
                    break;
                case 'ymd':
                    times = yy + mm + dd;
                    break;
                case 'ym':
                    times = yy + mm;
                    break;
                case 'y':
                    times = yy;
                    break;
                default :
                    times = yy + mm + dd;
            }
            return parseInt(times, 10);
        },

        compareTime: function (year, month, date, type) {
            var _this = this,
                dT = _this.gb.time,
                curTime = _this.combineTime({
                    'year': year,
                    'month': month,
                    'date': date,
                    'hours': dT.hours,
                    'minutes': dT.seconds,
                    'seconds': dT.minutes
                }, type),
                minT = _this.combineTime(_this.gb.minTime, type) || '',
                maxT = _this.combineTime(_this.gb.maxTime, type) || '';
            if (minT && !maxT) {
                return curTime < minT ? null : true;
            }
            if (!minT && maxT) {
                return curTime > maxT ? null : true;
            }
            if (minT && maxT) {
                return (curTime > maxT) || (curTime < minT) ? null : true;
            }
            return true;
        },

        initTimes: function (isToday) {
            var iptDates = this.gb.target,
                datesTime = this.checkTime(iptDates.value),
                curDay,
                year,
                month,
                hours,
                minutes,
                seconds, minTime, dates;
            if (datesTime) {
                datesTime = this.datesSplit(datesTime);
            }

            if (isToday) {
                dates = new Date();
                curDay = dates.getDate();
                year = dates.getFullYear();
                month = dates.getMonth() + 1;
                hours = dates.getHours();
                minutes = dates.getMinutes();
                seconds = dates.getSeconds();
            } else if (!isToday && datesTime) {
                curDay = datesTime.date;
                year = datesTime.year;
                month = datesTime.month;
                hours = datesTime.hours;
                minutes = datesTime.minutes;
                seconds = datesTime.seconds;
            } else if (!isToday && !this.gb.minTime && !this.gb.maxTime && !datesTime) {
                dates = new Date();
                curDay = dates.getDate();
                year = dates.getFullYear();
                month = dates.getMonth() + 1;
                hours = 0;
                minutes = 0;
                seconds = 0;
            } else if (!datesTime && (this.gb.minTime || this.gb.maxTime)) {
                minTime = this.gb.minTime || this.gb.maxTime;
                curDay = minTime.date;
                year = minTime.year;
                month = minTime.month;
                hours = minTime.hours;
                minutes = minTime.minutes;
                seconds = minTime.seconds;
            }

            this.gb.time = {
                'year': year,
                'month': month,
                'date': curDay,
                'hours': hours,
                'minutes': minutes,
                'seconds': seconds
            };
            this.insertDate();
        },

        viewTb: function () {
            var week = ['日', '一', '二', '三', '四', '五', '六'],
                tr = [],
                num = 0;
            var table = this.creatEle("table"),
                thead = this.creatEle("thead"),
                tbody = this.creatEle("tbody");
            table.className = "dates_table";
            table.id = "dates_table";
            for (var i = 1; i < 8; i++) {
                var th = this.creatEle('th');
                this.append(th, thead);
                th.innerHTML = week[i - 1];
            }

            for (i = 1; i < 7; i++) {
                tr[i] = this.creatEle('tr');
                this.append(tr[i], tbody);
                for (var j = 1; j < 8; j++) {
                    var ctTd = this.creatEle("td");
                    ctTd.setAttribute('index', num++);
                    this.append(ctTd, tr[i]);
                }
            }
            this.append(thead, table);
            this.append(tbody, table);
            return table.outerHTML;
        },

        insertDate: function () {
            var els = this.gb.els,
                datesTime = this.gb.time,
                yyConList = els.yyConList,
                mmConList = els.mmConList,
                iptYear = els.iptYear,
                iptMonth = els.iptMonth,
                iptHours = els.iptHours,
                iptMinutes = els.iptMinutes,
                iptSeconds = els.iptSeconds,
                year = datesTime.year,
                month = parseInt(datesTime.month, 10),
                date = datesTime.date,
                hours = datesTime.hours,
                minutes = datesTime.minutes,
                seconds = datesTime.seconds,
                td = this.gb.td,
                mVal,
                yVal;

            var dates = new Date();
            dates.setFullYear(year, month - 1);
            dates.setDate(1);
            var day = dates.getDay();
            var curMonthDays = this.getDays(year, month - 1);
            this.dateStatus(mmConList, month, 'current');
            this.dateStatus(yyConList, year, 'current');

            //日期列表
            for (var i = 0; i < curMonthDays; i++) {
                td[day + i].innerHTML = i + 1;
                td[day + i].className = '';
                if (i + 1 === date) {
                    td[day + i].className = 'current_day';
                } else if (date > curMonthDays) {
                    td[day + curMonthDays - 1].className = 'current_day';
                    this.gb.time.date = curMonthDays;
                }
                td[day + i].setAttribute('m', month);
                td[day + i].setAttribute('y', year);
            }

            for (i = 0; i < day; i++) {
                td[day - i - 1].className = "other_day";
                td[day - i - 1].innerHTML = this.getDays(year, month - 2) - i;
                if (!(month - 1 > 0)) {
                    mVal = 12;
                    yVal = year - 1;
                } else {
                    mVal = month - 1;
                    yVal = year;
                }
                td[day - i - 1].setAttribute('m', mVal);
                td[day - i - 1].setAttribute('y', yVal);
            }

            for (i = 0; i < td.length - day - curMonthDays; i++) {
                td[day + curMonthDays + i].className = "other_day";
                td[day + curMonthDays + i].innerHTML = i + 1;
                if (month + 1 > 12) {
                    mVal = 1;
                    yVal = year + 1;
                } else {
                    mVal = month + 1;
                    yVal = year;
                }
                td[day + curMonthDays + i].setAttribute('m', mVal);
                td[day + curMonthDays + i].setAttribute('y', yVal);
            }

            var minT = this.combineTime(this.gb.minTime) || '';
            var maxT = this.combineTime(this.gb.maxTime) || '';
            var curTime = {
                'year': year,
                'month': month,
                'date': date,
                'hours': this.gb.time.hours,
                'minutes': this.gb.time.minutes,
                'seconds': seconds
            };
            if (minT) {
                for (i = 0; i < td.length; i++) {//日期列表处理
                    curTime.year = td[i].getAttribute('y');
                    curTime.date = td[i].innerHTML;
                    curTime.month = td[i].getAttribute('m');
                    if (this.combineTime(curTime) < minT) {
                        td[i].className = "disable_day";
                    }
                }

                for (i = 0; i < yyConList.length; i++) {
                    if (yyConList[i].getAttribute('dateValue') < this.gb.minTime.year) {
                        yyConList[i].className = "disable_day";
                    }
                }
                for (i = 0; i < mmConList.length; i++) {//月份列表处理
                    if (datesTime.year + this.formatTime(mmConList[i].getAttribute('dateValue')) < this.combineTime(this.gb.minTime, 'ym')) {
                        mmConList[i].className = "disable_day";
                    }
                }
            }

            if (maxT) {

                for (i = 0; i < td.length; i++) {//日期列表处理
                    curTime.year = td[i].getAttribute('y');
                    curTime.date = td[i].innerHTML;
                    curTime.month = td[i].getAttribute('m');

                    if (this.combineTime(curTime) > maxT) {
                        td[i].className = "disable_day";
                    }
                }
                for (i = 0; i < yyConList.length; i++) {//年列表处理
                    if (yyConList[i].getAttribute('dateValue') > this.gb.maxTime.year) {
                        yyConList[i].className = "disable_day";
                    }
                }
                for (i = 0; i < mmConList.length; i++) {//月份列表处理
                    if (datesTime.year + this.formatTime(mmConList[i].getAttribute('dateValue')) > this.combineTime(this.gb.maxTime, 'ym')) {
                        mmConList[i].className = "disable_day";
                    }
                }
            }
            iptYear.value = year;
            iptMonth.value = this.formatTime(month);
            iptHours.value = this.formatTime(hours);
            iptMinutes.value = this.formatTime(minutes);
            iptSeconds.value = this.formatTime(seconds);
        },

        ymClick: function (listObj, parent, type) {
            var _this = this, year, month, date;
            for (var i = 0; i < listObj.length; i++) {//月份点击切换
                listObj[i].onclick = changeM;
            }
            function changeM() {
                if (type === 'month') {
                    month = this.getAttribute('dateValue');
                    year = _this.gb.time.year;
                    date = _this.gb.time.date;
                } else if (type === 'year') {
                    year = this.getAttribute('dateValue');
                    month = _this.gb.time.month;
                    date = _this.gb.time.date;
                }
                if (_this.compareTime(year, month, date)) {
                    _this.gb.time.year = year;
                    _this.gb.time.month = month;
                    _this.gb.time.date = date;
                    _this.removeStatus(listObj);
                    this.className = 'current';
                    parent.style.display = 'none';
                    _this.insertDate();
                }
            }
        },

        ishd: function (obj) {
            return obj.style.display === 'none' ? true : null;
        },

        shde: function (obj, controller) {
            if (!controller) {
                obj.style.display = (obj.style.display === 'block' ? 'none' : 'block');
            } else if (controller === 1) {
                obj.style.display = 'none';
            } else if (controller === 2) {
                obj.style.display = 'block';
            }
        },

        isExistObj: function (arr, els) {
            if (typeof arr === 'object' && arr.length) {
                for (var i = 0; i < arr.length; i++) {
                    if (arr[i] === els) {
                        return true;
                    }
                }
            } else if (arr === els) {
                return true;
            }
            return false;
        },

        checkFocus: function (str, that, replaceStr, type) {
            that.className = 'dlt_status';
            var val = that.value;
            var reg = new RegExp(str);
            if (!(reg.test(val))) {
                that.value = replaceStr;
            } else {
                this.gb.time[type] = this.formatTime(val);
                that.value = this.gb.time[type];
            }
            return that.value;
        },

        datesBtn: function (e) {
            var _this = e.datas.that,
                year = _this.gb.time.year,
                month = _this.gb.time.month,
                yy = parseInt(year, 10),
                mm = parseInt(month, 10),
                dd = _this.gb.time.date,
                type = e.datas.type,
                direction = e.datas.direction;
            if (type === 'month') {
                if (mm === 1 && (direction < 0)) {
                    month = 12;
                    year = yy + direction;
                } else if (mm === 12 && (direction > 0)) {
                    month = 1;
                    year = yy + 1;
                } else {
                    month = mm + direction;
                }
                var lastDay = _this.getDays(year, month - 1);
                if (dd > lastDay) {
                    dd = lastDay;
                }
            } else if (type === "year") {
                year = yy + direction;
            }

            _this.gb.time.year = year;
            _this.gb.time.month = month;
            _this.gb.time.date = dd;
            _this.insertDate();
        },

        removeStatus: function (obj) {
            for (var i = 0; i < obj.length; i++) {
                obj[i].className = '';
            }
        },

        dateStatus: function (obj, matchValue, style) {//样式实时切换
            for (var i = 0; i < obj.length; i++) {
                obj[i].className = '';
                if (obj[i].getAttribute('dateValue') === matchValue) {
                    obj[i].className = style;
                }
            }
        },

        getOkTime: function () {
            var tmpArr = {},
                fmt = this.gb.format,
                Hms,
                hms;
            for (var i in this.gb.time) {
                tmpArr[i] = this.formatTime(this.gb.time[i]);
            }
            hms = ' ' + tmpArr.hours + ':' + tmpArr.minutes + ':' + tmpArr.seconds;
            Hms = this.gb.hms ? hms : '';
            this.gb.target.value = tmpArr.year + fmt + tmpArr.month + fmt + tmpArr.date + Hms;
            this.shde(this.gb.datesBox);
            $(this.gb.target).blur().change();
        },

        checkTime: function (datesTime) {
            var fmt = '[-\/]',
                timesFat = this.gb.hms ? '(\\s){1}((0?[0-9])|(1[0-9])|(2[0-3])):([0-5][0-9]):([0-5][0-9])' : '',
                datesTimes = datesTime ? (datesTime.replace(/^(\s*)|(\s*)$/g, '').replace(/(\s)+/g, ' ')) : '',
                datesPat = new RegExp("^((19[0-9]{2})|(2[0-9]{3}))" + fmt + "((0?[1-9])|(1[0-2]))" + fmt + "((0?[1-9])|(1[0-9])|(2[0-9])|(3[0-1]))" + timesFat + "$");

            if (datesPat.test(datesTimes)) {
                return datesTimes;
            } else {
                return false;
            }
        },

        formatTime: function (time) {
            return /^(\d){1}$/.test(time) ? '0' + time : time;
        },

        getDays: function (year, month) {
            var febDays;
            if (month < 0) {
                month = 11;
                year = year - 1;
            } else if (month > 11) {
                month = 0;
                year = year + 1;
            }

            if ((month + 1) === 2) {
                if ((year % 4 === 0 && year % 100 !== 0) || year % 400 === 0) {
                    febDays = 29;
                } else {
                    febDays = 28;
                }
            }
            var arrM = [31, febDays, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
            return arrM[month];
        },

        creatEle: function (context, parent) {
            var docEle = parent || document;
            return docEle.createElement(context);
        },

        append: function (context, parent) {
            var docEle = parent || document.body;
            return docEle.appendChild(context);
        },

        setPosition: function (target, dateBox, position) {
            // if (position && typeof position === 'object') {
            //     dateBox.style.left = 0;
            //     dateBox.style.top = 0;
            //     return false;
            // }
            // var iptP = this.getOffset(target),
            //     iptLeft = iptP.left,
            //     iptTop = iptP.top,
            //     dtH = dateBox.offsetHeight,
            //     tgtH = target.offsetHeight,
            //     scrtH = this.resetSizeAttr('scrollTop'),
            //     bdH = this.resetSizeAttr('offsetHeight');
            // var distTop = iptTop - scrtH;
            // if (bdH - distTop > dtH) {
            //     dateBox.style.top = iptTop + tgtH - 1+ 'px';
            // } else {
            //     dateBox.style.top = iptTop - dtH + 1+ 'px';
            // }
            // dateBox.style.left = iptLeft + 'px';
        },

        resetSizeAttr: function (attr, els) {
            var elt = els || document.body;
            return elt[attr] || (document.documentElement && document.documentElement[attr]);
        },

        getOffset: function (obj, objPosition) {
            var zoom = $("#wrapper").css('zoom')*1;
            var objP = objPosition || {left: 0, top: 0};
            if (obj) {
                objP.left += obj.offsetLeft * zoom;
                objP.top += obj.offsetTop * zoom;
                if (obj.offsetParent) {
                    obj = obj.offsetParent;
                    arguments.callee(obj, objP);
                }
            }

            return objP;
        },

        contain: function (parent, child) {//为了解决mouseover/mouseout 的时间重叠，所以判断是否包含
            return (document.all ? (parent.contains(child)) : (((parent.compareDocumentPosition(child) === 20) || (parent === child)) ? true : null));
        }
    };
    Calendar.enable = function (obj, trigger, opts) {
        Calendar.addEvent(obj, 'click', function () {
            if (!win.xvDate.Dates) {
                win.xvDate.Dates = new Calendar.fn.init(opts, trigger);
            } else {
                win.xvDate.Dates.init(opts, trigger);
            }
        });
    };
    Calendar.getId = function (id, parent) {
        var doc = parent ? parent : document;
        return doc.getElementById(id);
    };
    Calendar.addEvent = function (obj, type, fn, opts) {
        obj['evt' + type + fn] = obj['evt' + type + fn] || null;
        if (!obj['evt' + type + fn]) {
            obj['evt' + type + fn] = function (event) {
                event = event || window.event;
                event.datas = opts || {};
                fn.call(obj, event);
            };
        }
        if (obj.addEventListener) {//W3C
            obj.addEventListener(type, obj['evt' + type + fn], false);
        } else if (obj.attachEvent) {
            obj.attachEvent('on' + type, obj['evt' + type + fn]);
        }
    };
    Calendar.removeEvent = function (obj, type, fn) {//删除事件这个在上面的基础上就比较容易点了
        if (typeof obj.removeEventListener !== 'undefined') {
            obj.removeEventListener(type, obj['evt' + type + fn], false);
        } else if (typeof obj.detachEvent !== 'undefined') {
            if (fn && obj['evt' + type + fn]) {
                obj.detachEvent("on" + type, obj['evt' + type + fn]);
            }
        }
    };
    Calendar.fn.init.prototype = Calendar.fn;
});



