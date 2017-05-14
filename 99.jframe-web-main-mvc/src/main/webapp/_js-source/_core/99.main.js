(function() {
    window.onerror = function(err) {
        if (location.search.indexOf("debug=true") > -1) {
            setTimeout(function () {
                alert("出错啦：" + err);
            }, 1000);
        }
    };

})();