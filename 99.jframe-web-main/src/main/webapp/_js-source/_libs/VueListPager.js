(function () {
    if (window.VueListPager) {
        return;
    }

    window.VueListPager = function (listSelector, url) {
        ListPager.call(this, listSelector, url);
        this.getMockDataFunction = null;
        this.rows = [];
    };

    VueListPager.prototype = new ListPager('', '');
    VueListPager.constructor = VueListPager;

    VueListPager.prototype.ajax = function (pageIndex, data, callback) {
        console.log("传送数据为：" + data);
        var me = this;
        var url = me._getFixedUrl(pageIndex);
        if (this.getMockDataFunction != null) {
            var message = "loading list pager: " + url + "\n";
            if (data != undefined && data != '' && typeof (data) === "string") {
                message += data.replace(/&/g, '\n');
            }
            console.log(message);
            var list = me.getMockDataFunction(pageIndex, me.pageSize);
            if (list != null) {
                setTimeout(function () {
                    callback && callback({
                        totalRows: 100 * me.pageSize,
                        totalPages: 100,
                        pageIndex: pageIndex,
                        pageSize: me.pageSize,
                        list: list
                    });
                }, 100);
                return;
            }
        }

        this._request = mvcApp.ajax.post(url, data, function (result) {
            if (result.success) {
                console.log("result:" + result);
                callback && callback(result.value);
            }
            else {
                mvcApp.notification.alertError(result.message);
            }
        });
    };

    VueListPager.prototype.renderHtml = function (data) {
        var me = this;
        if (data.pageIndex === 0) {
            this.rows._clear();
        }
        this.rows._addMany(data.list);
        if (data.totalPages > data.pageIndex + 1) {
            $(this.listSelector).append('<li class="list-loadmore" data-pageIndex="' + data.pageIndex + '"><a>加载更多</a></li>');
            me._bindLoadMore();
        }
        if (this.rows.length === 0) {
            me.renderEmpty();
        }
    };

    VueListPager.prototype.reload = function(){
        var me = this;
        this.rows._clear();
        $(me.listSelector).children("li.list-loadmore,li.loading").remove();
        Vue.nextTick(function(){
            $(me.listSelector).append('<li class="loading">加载中...</li>');
            me.load();
        });
    };

    VueListPager.prototype.onLoaded = function () {

    };

    VueListPager.prototype.useMockData = function (getMockDataFunction) {
        this.getMockDataFunction = getMockDataFunction;
    };


})();