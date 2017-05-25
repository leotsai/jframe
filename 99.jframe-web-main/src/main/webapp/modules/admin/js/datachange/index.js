
Date.prototype.addHours = function (number) {
    var date = new Date(this);
    date.setHours(date.getHours() + number);
    return date;
};

Date.prototype.addDays = function (number) {
    var date = new Date(this);
    date.setDate(date.getDate() + number);
    return date;
};

Date.prototype.addMonths = function (number) {
    var date = new Date(this);
    date.setMonth(date.getMonth() + number);
    return date;
};

Date.prototype.addYears = function (number) {
    var date = new Date(this);
    date.setFullYear(date.getFullYear() + number);
    return date;
};

Date.prototype.toWeekStart = function (startsFromSunday) {
    var dayOfWeek = this.getDay();
    if (startsFromSunday) {
        return this.addDays(-dayOfWeek);
    }
    if (dayOfWeek == 0) {
        dayOfWeek = 7;
    }
    return this.addDays(1 - dayOfWeek);
};

Date.prototype.toWeekEnd = function (startsFromSunday) {
    var dayOfWeek = this.getDay();
    if (startsFromSunday) {
        return this.addDays(6 - dayOfWeek);
    }
    if (dayOfWeek == 0) {
        dayOfWeek = 7;
    }
    return this.addDays(7 - dayOfWeek);
};

Date.prototype.toDate = function () {
    this.setHours(0);
    this.setMinutes(0);
    this.setSeconds(0);
    this.setMilliseconds(0);
    return this;
};

Date.prototype.toSunday = function () {
    var dayOfWeek = this.getDay();
    if (dayOfWeek == 0) {
        dayOfWeek = 7;
    }
    return this.addDays(7 - dayOfWeek);
};

Date.prototype.toMonday = function () {
    var dayOfWeek = this.getDay();
    if (dayOfWeek == 0) {
        return this.addDays(-6);
    }
    return this.addDays(1 - dayOfWeek);
};

Date.prototype.toMonthStart = function () {
    var date = new Date(this);
    date.setDate(1);
    return date;
};

Date.prototype.toMonthEnd = function () {
    var date = new Date(this);
    date.setMonth(date.getMonth() + 1);
    date.setDate(0);
    return date;
};

Date.prototype.toYearStart = function () {
    var date = new Date(this);
    date.setMonth(0);
    date.setDate(1);
    return date;
};

Date.prototype.toYearEnd = function () {
    var date = new Date(this);
    date.setMonth(12);
    date.setDate(0);
    return date;
};

Date.prototype.toUSDateString = function () {
    return (this.getMonth() + 1) + "/" + this.getDate() + "/" + this.getFullYear();
};

Date.prototype.toCNDateString = function () {
    return this.getFullYear() + "-" + (this.getMonth() + 1) + "-" + this.getDate();
};

Date.prototype.toChinaDateString = function() {
    return this.getFullYear() + "年" + (this.getMonth() + 1) + "月" + this.getDate() + "日";
};

Date.prototype.toTimeString = function () {
    var m = this.getMinutes();
    return this.getFullYear() + "/" + (this.getMonth() + 1) + "/" + this.getDate()
        + " " + this.getHours() + ":" + (m < 10 ? "0" : "") + m;
};

Date.prototype.toWeekString = function() {
    var weeks = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];
    return weeks[this.getDay()];
};

Date.prototype.isThisWeek = function() {
    return this - new Date().toDate().toMonday() >= 0;
};

Date.prototype.isLastWeek = function () {
    var today = new Date().toDate();
    return (this - today.addDays(-7).toMonday() >= 0) && (this - today.toMonday() < 0);
};

Date.prototype.toFullWeekDay = function () {
    var date = this.toDate();
    var today = new Date().toDate();
    if (date - today === 0) {
        return "今天";
    }
    if (date.addDays(1) - today === 0) {
        return "昨天";
    }
    if (date.isThisWeek()) {
        return "本" + date.toWeekString();
    }
    if (date.isLastWeek()) {
        return "上" + date.toWeekString();
    }
    return date.toWeekString();
};

Date.prototype.toHHmm = function() {
    return Number.get2Digits(this.getHours()) + ":" + Number.get2Digits(this.getMinutes());
};

Date.prototype.toMonthNumber = function () {
    return this.getFullYear() * 100 + (this.getMonth() + 1);
};

Date.prototype.toWeekNumber = function () {
    return this.toSunday().toDateNumber();
};

Date.prototype.toDateNumber = function () {
    return this.toMonthNumber() * 100 + this.getDate();
};

Date.prototype.toHourNumber = function() {
    return this.toDateNumber() * 100 + this.getHours();
};

Date.parseFromJsonFormat = function(dateStr) {
    // "/Date(1383667450669)/"
    var str = dateStr.substring(6, dateStr.length - 2);
    return new Date(str * 1);
};

Date.parseFromDateNumber = function(number) {
    if (number.length <= 6) {
        number += "01";
    }
    if (number.length <= 8) {
        number += "00";
    }
    return new Date(
        number.substring(0, 4),
        number.substring(4, 6) * 1 - 1,
        number.substring(6, 8),
        number.substring(8, 10), 0, 0);
};

Date.parseFromString = function(date) {
    return new Date(date.substring(0, 4), date.substring(5, 7) * 1 - 1, date.substring(8, 10));
};
(function() {

    if (window.TimeframeSelector) {
        return;
    }
    window.TimeframeSelector = function () {

    };

    TimeframeSelector.prototype.init = function () {
        var selector = this;
        $(".date-options").change(function () {
            selector.bindValues();
        });
        selector.bindValues();
    };

    TimeframeSelector.prototype.bindValues = function () {
        var selectedDate = $(".date-options").val();
        $(".date-selector .date-from, .date-selector .date-to").hide();
        switch (selectedDate) {
            case "Today":
                bindTextboxes(new Date(), new Date());
                break;
            case "Yesterday":
                bindTextboxes(new Date().addDays(-1), new Date().addDays(-1));
                break;
            case "ThisWeek":
                bindTextboxes(new Date().toWeekStart(false), new Date().toWeekEnd(false));
                break;
            case "LastWeek":
                bindTextboxes(new Date().toWeekStart(false).addDays(-7), new Date().toWeekEnd(false).addDays(-7));
                break;
            case "ThisMonth":
                bindTextboxes(new Date().toMonthStart(), new Date().toMonthEnd());
                break;
            case "LastMonth":
                bindTextboxes(new Date().addMonths(-1).toMonthStart(), new Date().addMonths(-1).toMonthEnd());
                break;
            case "Last7Days":
                bindTextboxes(new Date().addDays(-7), new Date());
                break;
            case "Last30Days":
                bindTextboxes(new Date().addDays(-30), new Date());
                break;
            case "Custom":
                $(".date-selector .date-from, .date-selector .date-to").removeAttr("disabled").show();
                break;
            case "":
            default:
                $(".date-selector .date-from, .date-selector .date-to").val('');
                break;
        }

        function bindTextboxes(from, to) {
            $(".date-from").val(from.toCNDateString());
            $(".date-to").val(to.toCNDateString());
        }
    };
})();