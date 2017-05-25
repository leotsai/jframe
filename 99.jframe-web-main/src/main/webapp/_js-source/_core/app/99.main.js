(function() {
    $(document).ready(function () {
        $("a.nohistory").on("click", function(e) {
            e.preventDefault();
            window.location.replace($(this).attr("href"));
        });

        $(".header > .btn-back").on("click", function (e) {
            e.preventDefault();
            history.go(-1);
        });
    });
})();