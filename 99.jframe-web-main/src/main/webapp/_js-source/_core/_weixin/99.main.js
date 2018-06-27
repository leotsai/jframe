(function() {

    $(document).ready(function () {
        var bodyWidth = $('#body').width();
        var documentWidth = $('body').width();
        var right = (documentWidth - bodyWidth)/2;
        $("html").css("font-size",bodyWidth/10);
        $('#easyTo').css('right', right);

        $("a.nohistory").on("click", function(e) {
            e.preventDefault();
            window.location.replace($(this).attr("href"));
        });

        $(".header > .btn-back").on("click", function (e) {
            e.preventDefault();
            history.go(-1);
        });
        if(mvcApp.isInWeixin()){
            $("#body").addClass("weixin");
        }
        else{
            $("#body").addClass("weixin-no");
        }
        reloadPage();

        $('#leadBtn').click(function () {
            var $toCart = $('.to-cart');
            if($(this).hasClass('show')){
                $(this).removeClass('show');
                $(this).closest('.lead-box').removeClass('show');
                $toCart.show();
                $('.easy-mask').hide();
            } else {
                $(this).addClass('show');
                $(this).closest('.lead-box').addClass('show');
                $toCart.hide();
                $('.easy-mask').show();
            }
        });
        $('.easy-mask').click(function () {
            $('#leadBtn').removeClass('show');
            $('.lead-box').removeClass('show');
            $('.to-cart').show();
            $('.easy-mask').hide();
        });
    });

    function reloadPage() {
        var isPageHide = false;
        window.addEventListener('pageshow', function () {
            if (isPageHide) {
                window.location.reload();
            }
        });
        window.addEventListener('pagehide', function () {
            isPageHide = true;
        });
    }
})();