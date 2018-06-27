/**
 * Created by leo on 2018-03-31.
 */
(function () {

    window.appInfo = null;
    window.vm = null;

    $(document).ready(function () {
        if (Object.create == undefined) {
            $(".layout-busy").html('您的浏览器太老了，建议使用最新版谷歌Chrome浏览器');
            return;
        }
        api.getMenuItems();
    });

    function renderMenu() {
        vm = new Vue({
            el: "#wrapper",
            data: {
                topMenuItems: appInfo.menuItems,
                name: appInfo.name,
                title: 'ADMIN'
            },
            computed: {
                leftMenuItems: function () {
                    for (var i = 0; i < this.topMenuItems.length; i++) {
                        var item = this.topMenuItems[i];
                        if (item.selected) {
                            return item.children;
                        }
                    }
                    return [];
                }
            }
        });
        $("#wrapper").show();
        $(".layout-busy").remove();
        mvcApp.utils.bindLeftNav();
    }
    var api = {
        getMenuItems: function () {
            mvcApp.ajax.post("/admin/api/app/init", "加载菜单...", function (result) {
                appInfo = result.value;
                for (var i = 0; i < appInfo.menuItems.length; i++) {
                    appInfo.menuItems[i].selected = false;
                }
                renderMenu();
                startMonitoringPageChange();
                loadPageContent();
            }, true);
        }
    };

    function loadPageContent() {
        var hash = location.hash;
        if (hash.indexOf("#") === 0) {
            hash = hash.substr(1);
        }
        var path = hash;
        var selectedPath = location.pathname + location.hash;
        if (hash.indexOf("?") > -1) {
            path = hash.substr(0, hash.indexOf("?"));
            selectedPath = selectedPath.substr(0, selectedPath.indexOf("?"));
        }

        mvcApp.setCurrentPage(selectedPath);

        mvcApp.ajax.load("#bodyContent", "/admin2/page?path=" + path, function () {
            console.log("page loaded.");
        });
    }
    mvcApp.setCurrentPage = function (path) {
        setSelectedMenuItems(path);
    };

    mvcApp.setPageTitle = function (title) {
        vm.title = title;
        $("title").html(title);
    };

    function setSelectedMenuItems(path) {
        for (var i = 0; i < appInfo.menuItems.length; i++) {
            var item1 = appInfo.menuItems[i];
            item1.selected = false;
            for (var j = 0; j < item1.children.length; j++) {
                var item2 = item1.children[j];
                item2.selected = false;
                for (var k = 0; k < item2.children.length; k++) {
                    var item3 = item2.children[k];
                    if (item3.url === path) {
                        item1.selected = true;
                        item2.selected = true;
                        item3.selected = true;
                    }
                    else {
                        item3.selected = false;
                    }
                }
            }
        }
    }
    function startMonitoringPageChange() {
        window.onhashchange = function (x) {
            loadPageContent();
        };
    }
    mvcApp.hasPermission = function (code) {
        if (appInfo.superAdmin) {
            return true;
        }
        return appInfo.permissions.indexOf(code) > -1;
    };


})();