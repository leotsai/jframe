(function () {
    if (window.ListPager) {
        return;
    }
    window.ListPager = function (listSelector, url) {
        this.url = url;
        this.listSelector = listSelector;
        this.emptyText = '暂无数据！';
        this.pageIndex = 0;
        this.pageSize = 20;
        this.autoLoadOnScrolling = true;
        this.isLoading = false;
        this._request = null;
    };

    ListPager.prototype = {
        init: function () {
            var me = this;
            if (me.autoLoadOnScrolling) {
                window.onscroll = function () {
                    if (!me._isScrolledToBottom()) {
                        return;
                    }
                    if (me._isLoading()) {
                        return;
                    }
                    me.loadNextPage();
                };
            }

            this.load();
        },
        load: function (pageIndex) {
            var me = this;
            pageIndex = pageIndex === undefined ? 0 : pageIndex;
            if (this._isLoading()) {
                try{
                    this._request && this._request.abort();
                }
                catch (e){
                    console.log(e);
                }
            }
            $(me.listSelector).children(".empty").remove();
            var $loadMore = $(me.listSelector).children(".list-loadmore");
            if ($loadMore.length === 0) {
                var $loading = $(me.listSelector).children(".loading");
                if ($loading.length === 0) {
                    $(me.listSelector).append('<li class="loading">加载中...</li>');
                }
            }
            else {
                $loadMore.addClass("loading").html("加载中...");
            }
            this.pageIndex = pageIndex;
            this.isLoading = true;
            me.ajax(pageIndex, me.getPostData(pageIndex), function (result) {
                me.isLoading = false;
                $(me.listSelector).children(".loading").remove();
                me.renderHtml(me.getFixedData(result));
                me.onLoaded && me.onLoaded();
            });
        },
        getFixedData: function (data) {
            return data;
        },
        ajax: function (pageIndex, data, callback) {
            var me = this;
            this._request = mvcApp.ajax.post(me._getFixedUrl(pageIndex), data, function (result) {
                callback && callback(result);
            });
        },
        renderHtml: function (result) {
            var me = this;
            var $list = $(me.listSelector);
            $list.children(".loading").remove();
            $list.append(result);
            if ($list.children().length === 0) {
                me.renderEmpty();
            } else {
                me._bindLoadMore();
            }
        },
        renderEmpty: function () {
            $(this.listSelector).append("<li class='empty'>" + this.emptyText + "</li>");
        },
        _getFixedUrl: function (pageIndex) {
            var url = this.url;
            if (typeof(this.url) === "function") {
                url = this.url();
            }
            if (url.indexOf("?") < 0) {
                url += "?";
            }
            return url + "pageIndex=" + pageIndex + "&pageSize=" + this.pageSize;
        },
        _bindLoadMore: function () {
            var me = this;
            var $list = $(me.listSelector);
            $list.children("li.list-loadmore").click(function () {
                var pageIndex = $(this).attr("data-pageIndex");
                if (pageIndex !== undefined) {
                    me.load(pageIndex * 1 + 1);
                }
            });
        },
        onLoaded: function () {

        },
        getPostData: function (pageIndex) {
            return null;
        },
        reload: function () {
            $(this.listSelector).empty().append('<li class="loading">加载中...</li>');
            this.load();
        },
        _isScrolledToBottom: function () {
            var dTop = $(document).scrollTop();
            var dH = $(document).height();
            var wH = $(window).height();
            var lH = $(this.listSelector).children('li.list-loadmore').height();
            if (dTop >= dH - wH - lH) {
                return true;
            }
            return false;
        },
        _isLoading: function () {
            return $(this.listSelector).children(".loading").length > 0;
        },
        loadNextPage: function () {
            var me = this;
            var $list = $(me.listSelector);
            var $li = $list.children("li.list-loadmore");
            if ($li.length === 0) {
                return;
            }
            var pageIndex = $li.attr("data-pageIndex");
            if (pageIndex !== undefined) {
                me.load(pageIndex * 1 + 1);
            }
        }
    };
})();