(function () {
    jQuery.prototype.cssDisable = function () {
        return this.each(function () {
            $(this).addClass("disabled");
        });
    };

    jQuery.prototype.cssEnable = function () {
        return this.each(function () {
            $(this).removeClass("disabled");
        });
    };

    jQuery.prototype.isCssDisabled = function () {
        return this.hasClass("disabled");
    };

    if (!window.Number) {
        window.Number = function () {

        };
    }

    Number.get2Digits = function (number) {
        return number < 10 ? "0" + number : number;
    };

    Number.prototype.formatMoney = function (places) {
        places = !isNaN(places = Math.abs(places)) ? places : 2;
        var number = this,
        negative = number < 0 ? "-" : "",
        i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
        j = (j = i.length) > 3 ? j % 3 : 0;
        return negative + (j ? i.substr(0, j) + "," : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1,") + (places ? "." + Math.abs(number - i).toFixed(places).slice(2) : "");
    };

})();