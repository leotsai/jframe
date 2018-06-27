/**
 * Created by leo on 2018-03-31.
 */
(function () {

    $(document).ready(function () {
        if (Object.create == undefined) {
            $("#body").addClass("empty").html('您的浏览器太老了，建议使用最新版谷歌Chrome浏览器');
            return;
        }
        startMonitoringPageChange();
        loadPageContent();
    });

    function loadPageContent() {
        var hash = location.hash;
        if (hash.indexOf("#") === 0) {
            hash = hash.substr(1);
        }
        var path = hash;
        if (hash.indexOf("?") > -1) {
            path = hash.substr(0, hash.indexOf("?"));
        }
        if (path === '') {
            path = 'home/index';
        }
        var module = location.pathname.split('/')[1];
        mvcApp.ajax.load("#body", "/" + module + "/page?path=" + path, function () {
            console.log("page loaded.");
        });
    }
    function startMonitoringPageChange() {
        window.onhashchange = function (x) {
            loadPageContent();
        };
    }
})();