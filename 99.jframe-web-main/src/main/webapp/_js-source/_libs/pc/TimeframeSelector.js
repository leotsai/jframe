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
        var me = this;
        var selectedDate = $(".date-options").val();
        $(".date-selector .date-from, .date-selector .date-to, .custom-time").hide();
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
                $(".date-selector .date-from, .date-selector .date-to,.custom-time").removeAttr("disabled").show();
                break;
            case "ThisYear":
                bindTextboxes(new Date().toYearStart(), new Date().toYearEnd());
                break;
            default:
                $(".date-selector .date-from, .date-selector .date-to").val('');
                break;
        }

        function bindTextboxes(from, to) {
            $(".date-from").val(from.toCNDateString());
            $(".date-to").val(to.toCNDateString());
            var event = $.Event("onSelectionChanged");
            event.value = {
                from:from.toCNDateString(),
                to:to.toCNDateString()
            };
            $(me).trigger(event);
        }

    };
    TimeframeSelector.prototype.onSelectionChanged = function(handler){
        $(this).bind("onSelectionChanged", handler);
    };

})();