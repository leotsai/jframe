
(function(){

    mvcApp.nav = function () {

    };

    $(document).ready(function(){
        var windowWidth = $(window).width();
        if(windowWidth < 1920){
            $("#wrapper").css("zoom", windowWidth/1920);
        }
        $('ul#sidebar > li > a').live("click",function () {
            var $li= $(this).closest('li');
            var isHide = $li.find('.arrow').hasClass('arrow-rote');
            if(isHide){
                $li.find('ul').slideDown(300);
                $li.find('.arrow').removeClass('arrow-rote');
            } else {
                $li.find('ul').slideUp(300);
                $li.find('.arrow').addClass('arrow-rote');
            }
        });
        mvcApp.utils.bindLeftNav();

        $(window).resize(function(){
            page.resize();
        });
        page.resize();
    });

    page.resize =function(){
        var contentHeight = $("#wrapper").height()-$(".header").height();
        $(".side").height(contentHeight);
        var mainHeight = contentHeight - ($("#topBar").height() == 0 ? 50 : $("#topBar").height() );
        $(".main-w1").height(mainHeight);
    };

})();
