(function () {
    if (window.Grid) {
        return;
    }
    window.Grid = function (dataWrapper, pagerWarper, sortWrapper) {
        this.url = '';
        this.dataWrapper = dataWrapper;
        this.pagerWarper = pagerWarper;
        if (sortWrapper == undefined && jQuery != undefined) {
            if ($(".grid").length > 1) {
                alert("页面中存在多个grid，请手工传入sortWrapper参数");
            }
        }
        this.sortWrapper = sortWrapper == undefined ? ".grid > thead" : sortWrapper;
        this.pageIndex = 0;
        this.pageSize = 15;
        this.maxPageNumbers = 10;
        this.totalPages = 0;
        this.totalRows = null;
        this.getUrlFunc = null;
        this.getDataFunc = null;
        this.showRefresh = false;
        this.showSummary = true;
        this.selectorTotalPages = ".total-pages";
        this.selectorPageIndex = ".page-index";
        this.selectorTotalRows = ".page-rows";
        this.isSeletable = true;
        this.isPriorToDesc = true;
        this.cookieKey = null;
        this.cookiePath = null;
        this.keepHtmlOnLoading = true;
        this.emptyText = '暂无数据';

        this._request = null;
        this._isEverLoaded = false;
    };

    Grid.prototype = {
        init: function () {
            var me = this;
            var $pager = $(this.pagerWarper);
            if ($pager.hasClass("pager") == false) {
                $pager.addClass("pager");
            }
            if (this.showRefresh) {
                $pager.append('<a href="javascript:void(0);" class="refresh">刷新</a>');
                $pager.find(".refresh").click(function () {
                    me.refresh();
                });
            }
            if (this.showSummary) {
                $pager.append('<div class="pager-summary"></div>');
            }
            $pager.prepend("<ul></ul>");
            $(this.sortWrapper).find(".sortable").click(function () {
                var $dom = $(this);
                var newDirection = me.isPriorToDesc ? "desc" : "asc";
                if ($dom.hasClass("sort-asc")) {
                    newDirection = "desc";
                }
                else if ($dom.hasClass("sort-desc")) {
                    newDirection = "asc";
                }
                $(me.sortWrapper).find(".sortable").removeClass("sort-asc").removeClass("sort-desc");
                $dom.addClass("sort-" + newDirection);
                me.cacheSort($dom, newDirection);
                me.pageTo(0);
            });
            if (this.cookieKey != null) {
                this.loadCachedSort();
            }
        },
        enableCacheSorts: function (cookieKey, cookiePath) {
            this.cookieKey = cookieKey;
            this.cookiePath = cookiePath;
        },
        disableCacheSorts: function (cookieKey, cookiePath) {
            this.cookieKey = null;
            this.cookiePath = null;
            mvcApp.utils.removeCookie(cookieKey, cookiePath);
        },
        loadCachedSort: function () {
            var value = mvcApp.utils.getCookieValue(this.cookieKey);
            if (value == null) {
                return;
            }
            var parts = value.split(',');
            var sortName = parts[0];
            var sortDirection = parts[1];
            var $foundDom = null;
            $(this.sortWrapper).find(".sortable").each(function () {
                var $dom = $(this);
                if ($dom.attr("data-sort") === sortName) {
                    $foundDom = $dom;
                }
            });
            if ($foundDom != null) {
                $(this.sortWrapper).find(".sortable").removeClass("sort-asc").removeClass("sort-desc");
                $foundDom.addClass("sort-" + sortDirection);
            }
        },
        cacheSort: function ($dom, direction) {
            if (this.cookieKey == null) {
                return;
            }
            var value = $dom.attr("data-sort") + "," + direction;
            var addYears = function (number) {
                var date = new Date();
                date.setFullYear(date.getFullYear() + number);
                return date;
            };
            mvcApp.utils.setCookie(this.cookieKey, value, addYears(10), this.cookiePath);
        },
        pageTo: function (pageIndex) {
            var me = this;
            if (me._request != null) {
                me._request.abort && me._request.abort();
                me._request = null;
                me.removeBusy();
            }
            var data = me.getDataFunc == null ? null : me.getDataFunc();
            me.showBusy();

            if (me.keepHtmlOnLoading == false || me._isEverLoaded == false) {
                me._renderLoading();
            }

            me.loadData(pageIndex, data, function (html) {
                if (me._request != null && me._request.status == 210) {
                    mvcApp.notification.alert("网络错误", me._request.responseText);
                    return;
                }
                me._isEverLoaded = true;
                me._removeLoading();
                me.removeBusy();
                me._triggerSuccess(html);
                me.renderHtml(html);
                me._renderPages();
                me._setAlterRow();
                me._triggerComplete(me);
                if (me.isSeletable) {
                    $(me.dataWrapper).find("tr").click(function () {
                        $(this).addClass("selected").siblings().removeClass("selected");
                    });
                }
                if (me.pageIndex > 0 && me.pageIndex >= me.totalPages) {
                    me.pageTo(me.totalPages === 0  ? 0 : me.totalPages - 1);
                }
            });
        },
        _renderLoading: function () {
            var $body = $(this.dataWrapper);
            if (!$body.is("tbody")) {
                return;
            }
            var colSpan = $body.closest("table").find("col").length;
            $body.html('<tr><td class="loading" colspan="' + colSpan + '">加载中...</td></tr>');
        },
        isTBody: function () {
            return $(this.dataWrapper).is("tbody");
        },
        getColumnsCount: function () {
            return $(this.dataWrapper).closest("table").children("colgroup").children("col").length;
        },
        _removeLoading: function () {
            if (this.isTBody()) {
                $(this.dataWrapper).children("tr").children("td.loading").remove();
            }
        },
        loadData: function (pageIndex, data, callback) {
            var url = this._getUrl(pageIndex);
            this._request = mvcApp.ajax.post(url, data, function (html) {
                callback && callback(html);
            });
        },
        renderHtml: function (html) {
            $(this.dataWrapper).html(html);
            this.totalPages = parseInt($(this.dataWrapper).find(this.selectorTotalPages).val());
            this.pageIndex = parseInt($(this.dataWrapper).find(this.selectorPageIndex).val());
            this.totalRows = parseInt($(this.dataWrapper).find(this.selectorTotalRows).val());
            if (this.isTBody() && $(this.dataWrapper).children().length === 0) {
                $(this.dataWrapper).html('<tr><td class="empty" colspan="' + this.getColumnsCount() + '">' + this.emptyText + '</td></tr>');
            }
        },
        refresh: function () {
            this.totalRows = null;
            this.pageTo(this.pageIndex);
        },
        showBusy: function () {
            var $pager = $(this.pagerWarper);
            var $busy = $pager.find(".busy");
            if ($busy.length == 0) {
                $busy = $('<span class="busy">加载中...</span>');
                $pager.prepend($busy);
            }
            $pager.find(".refresh").hide();
        },
        removeBusy: function () {
            var $pager = $(this.pagerWarper);
            $pager.find(".busy").remove();
            $pager.find(".refresh").show();
        },
        parse: function () {
            var me = this;
            me.totalPages = parseInt($(me.dataWrapper).find(me.selectorTotalPages).val());
            me.pageIndex = parseInt($(me.dataWrapper).find(me.selectorPageIndex).val());
            me._bindPageClicks();
        },
        _getUrl: function (pageIndex) {
            var url = '';
            var me = this;
            if (me.getUrlFunc != null) {
                url = me.getUrlFunc(pageIndex);
            } else {
                url = me.url;
            }
            if (me.url.indexOf("?") < 0) {
                url += "?";
            }
            url += "&pageIndex=" + pageIndex + "&pageSize=" + me.pageSize;
            var sortDirection = "ASC";
            var $sort = $(this.sortWrapper).find(".sort-asc");
            if ($sort.length == 0) {
                $sort = $(this.sortWrapper).find(".sort-desc");
                sortDirection = "DESC";
            }
            if ($sort.length > 0) {
                url += "&sort=" + $sort.attr("data-sort")
                    + "&sortDirection=" + sortDirection;
            }
            return url;
        },
        _renderPages: function () {
            var me = this;
            var html = "";
            var currentPage = "";
            var startPageNumber = me.pageIndex + 1 - (Math.floor(me.maxPageNumbers / 2));

            if (me.pageIndex + 1 + Math.floor(me.maxPageNumbers / 2) >= me.totalPages) {
                startPageNumber = me.totalPages - me.maxPageNumbers + 1;
            }
            startPageNumber = startPageNumber < 1 ? 1 : startPageNumber;
            var endPageNumber = startPageNumber - 1 + (me.totalPages > me.maxPageNumbers ? me.maxPageNumbers : me.totalPages);
            endPageNumber = endPageNumber < 1 ? 1 : endPageNumber;
            html += '<li class="previous' + (me.pageIndex > 0 ? "" : " disabled") + '"><a href="javascript:void(0);">上一页</a></li>';
            for (var i = startPageNumber; i <= endPageNumber; i++) {
                var css = i == me.pageIndex + 1 ? "selected" : "";
                if (i == me.pageIndex + 1) {
                    currentPage = i;
                }
                html += '<li class="page ' + css + '"><a href="javascript:void(0);">' + i + '</a></li>';
            }
            html += '<li class="next' + (me.pageIndex < me.totalPages - 1 ? "" : " disabled") + '"><a href="javascript:void(0);">下一页</a></li>';
            var $pager = $(me.pagerWarper);
            $pager.find("ul").html(html);

            if (me.showSummary) {
                var summaryHtml = '<span>共' + me.totalRows + '条记录</span>' +
                    '<span>' + currentPage + '/' + me.totalPages + '页</span>' +
                    '<div class="page-to">到<input id="toPageNumber" type="text" value=""/>页' +
                    '<a class="btn-page-to" href="javascript:;">跳转</a></div>';
                $pager.find(".pager-summary").html(summaryHtml);
            }
            me._bindPageClicks();
        },
        _bindPageClicks: function () {
            var me = this;
            var $pager = $(me.pagerWarper);
            $pager.find(".previous a").click(function (e) {
                e.preventDefault();
                if (me.pageIndex > 0) {
                    me.pageIndex--;
                    me.pageTo(me.pageIndex);
                }
            });

            $pager.find(".next a").click(function (e) {
                e.preventDefault();
                if (me.pageIndex < me.totalPages - 1) {
                    me.pageIndex++;
                    me.pageTo(me.pageIndex);
                }
            });

            $pager.find(".page a").click(function (e) {
                e.preventDefault();
                me.pageIndex = parseInt($(this).text()) - 1;
                me.pageTo(me.pageIndex);
            });

            $pager.find('.page-to a.btn-page-to').click(function (e) {
                e.preventDefault();
                var number = $pager.find('#toPageNumber').val() - 1;
                if (number >= 0 && number < me.totalPages) {
                    me.pageTo(number);
                } else {
                    $pager.find('#toPageNumber').val('');
                }
            })
        },
        _triggerSuccess: function (data) {
            var evt = $.Event("Success");
            evt.value = data;
            $(this).trigger(evt);
        },
        _triggerComplete: function (data) {
            var evt = $.Event("Complete");
            evt.value = data;
            $(this).trigger(evt);
        },
        _setAlterRow: function () {
            var me = this;
            $(me.dataWrapper).children("tr").each(function (i, elem) {
                if (i % 2 === 0) {
                    $(elem).addClass("alt");
                }
            });
        },
        success: function (callback) {
            if (callback == null) {
                return;
            }
            $(this).bind("Success", callback);
        },
        beforeSend: function (callback) {
            if (callback == null) {
                return;
            }
            $(this).bind("BeforeSend", callback);
        },
        complete: function (callback) {
            if (callback == null) {
                return;
            }
            $(this).bind("Complete", callback);
        }
    };

})();