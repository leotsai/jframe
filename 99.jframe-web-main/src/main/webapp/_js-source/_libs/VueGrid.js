(function () {
    if (window.VueGrid) {
        return;
    }
    window.VueGrid = function (dataWrapper, pagerWarper, sortWrapper) {
        Grid.call(this, dataWrapper, pagerWarper, sortWrapper);
        this.getMockDataFunction = null;
        this.rows = [];
    };

    VueGrid.prototype = new Grid('', '', '');
    VueGrid.constructor = VueGrid;

    VueGrid.prototype.loadData = function (pageIndex, data, callback) {
        var me = this;
        var url = me._getUrl(pageIndex);
        if (this.getMockDataFunction != null) {
            var message = "loading grid: " + url + "\n";
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


        me._request = mvcApp.ajax.post(url, data, function (result) {
            if (result.success) {
                callback && callback(result.value);
            }
            else {
                mvcApp.notification.alertError(result.message);
            }
        });
    };

    VueGrid.prototype.renderHtml = function (data) {
        $(this.dataWrapper).children("tr").children("td.empty").remove();
        this.rows._clear();
        this.rows._addMany(data.list);
        this.totalPages = data.totalPages;
        this.pageIndex = data.pageIndex;
        this.totalRows = data.totalRows;
        if (this.isTBody() && this.rows.length === 0) {
            $(this.dataWrapper).append('<tr><td class="empty" colspan="' + this.getColumnsCount() + '">' + this.emptyText + '</td></tr>');
        }
    };

    VueGrid.prototype.useMockData = function (getMockDataFunction) {
        this.getMockDataFunction = getMockDataFunction;
    };

    VueGrid.prototype._renderLoading = function () {
        var me = this;
        if(this.isTBody()){
            this.rows._clear();
            Vue.nextTick(function(){
                $(this.dataWrapper).append('<tr><td class="loading" colspan="' + me.getColumnsCount() + '">加载中...</td></tr>');
            });
        }
    };

})();