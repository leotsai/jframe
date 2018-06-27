(function() {
    mvcApp.utils.bindDatePicker = function (input) {
        $(input).datepicker({
            dateFormat: "yy-m-d",
            monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            monthNamesShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
            dayNamesMin: ["日", "一", "二", "三", "四", "五", "六"],
            changeYear: true,
            changeMonth: true,
            beforeShow: function (input2) {
                var zoom = $("#wrapper").css('zoom')*1;
                var top = ($(input2).offset().top + $(input2).outerHeight()) * zoom;
                var left = $(input2).offset().left * zoom;
                $.datepicker._pos = [left, top];
            }
        });
    };
    mvcApp.utils.bindDateTimePicker = function (input,isHms,alignId) {
        xvDate({
            'targetId':input,
            'triggerId':input,
            'alignId':alignId || input,
            'hms':isHms
        });
    };

    mvcApp.utils.bindValidate = function (form, callback) {
        var requireElements = $('[required]', $(form));
        var messages = {};
        $.each(requireElements, function (index, el) {
            var name = $(el).attr('name');
            var msg = $(el).attr('data-msg');
            messages[name] = msg;
        });
        $(form).validate({
            errorClass:'form-error',
            errorElement: 'span',
            ignore: ".ignore",
            messages: messages,
            errorPlacement: callback
        });
    };

    mvcApp.utils.bindLeftNav = function(){

        $("#btnToggle").toggle(function(){
            $("#side").css("margin-left","-280px");
            $("#topBar").css("left","0");
            $("#main").css("padding-left","40px");
            $(".icon-arrow").css("transform","rotate(-180deg)")
        },function(){
            $("#side").css("margin-left","0px");
            $("#topBar").css("left","280px");
            $("#main").css("padding-left","320px");
            $(".icon-arrow").css("transform","rotate(0deg)");
        });
    };

})();