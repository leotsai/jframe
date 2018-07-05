package org.jframe.core.extensions;


import org.apache.commons.lang3.StringUtils;
import org.jframe.core.helpers.HttpHelper;

import java.util.List;
import java.util.function.Function;

/**
 * Created by leo on 2017-05-26.
 */
public class PageResult<T> {
    /**
     * 分页插件中最多显示的快捷页码个数
     */
    private static final int MAX_TAG_COUNT = 10;

    private int totalRows;
    private int totalPages;
    private int pageIndex;
    private int pageSize;
    private JList<T> list;

    public PageResult() {
        this.list = new JList<>();
    }

    public PageResult(PageRequest request, List<T> list) {
        this.pageIndex = request.getPageIndex();
        this.pageSize = request.getPageSize();
        this.totalRows = list.size();
        this.totalPages = this.totalRows / request.getPageSize();
        if (totalRows % request.getPageSize() > 0) {
            this.totalPages++;
        }
        this.list = new JList<>();
        for (int i = request.getSkip(); i < request.getSkip() + request.getPageSize() && i < this.totalRows; i++) {
            this.list.add(list.get(i));
        }
    }

    public PageResult(PageRequest request, int totalRows, JList<T> pagedList) {
        this.pageIndex = request.getPageIndex();
        this.pageSize = request.getPageSize();
        this.totalRows = totalRows;
        this.totalPages = totalRows / request.getPageSize();
        if (totalRows % request.getPageSize() > 0) {
            this.totalPages++;
        }
        this.list = pagedList;
    }

    public <TSource> PageResult(PageResult<TSource> page, Function<TSource, T> convert) {
        this.pageIndex = page.getPageIndex();
        this.pageSize = page.getPageSize();
        this.totalRows = page.getTotalRows();
        this.totalPages = page.getTotalPages();
        this.list = new JList<>();
        for (TSource item : page.getList()) {
            this.list.add(convert.apply(item));
        }
    }

    public PageResult<T> calculateRank() {
        if (null == this.getList()) {
            return this;
        }
        int i = 1;
        for (T item : this.getList()) {
            ((RankEntity) item).setRank(this.getPageIndex() * this.pageSize + i++);
        }
        return this;
    }

    public <TResult> PageResult<TResult> to(Function<T, TResult> convert) {
        return new PageResult<>(this, convert);
    }

    public boolean hasNextPage() {
        return this.totalPages > this.pageIndex + 1;
    }

    public boolean hasPreviousPage() {
        return this.pageIndex > 0;
    }

    public String renderPager() {
        return "<input type=\"hidden\" class=\"total-pages\" value=\"" + this.totalPages + "\"/>"
                + "<input type=\"hidden\" class=\"page-index\" value=\"" + this.pageIndex + "\"/>"
                + "<input type=\"hidden\" class=\"page-rows\" value=\"" + this.totalRows + "\"/>";
    }

    public String render() {
        return render("pageIndex");
    }

    public String render(String paramName) {
        String uri = getUriAndParams(paramName);

        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        appendPrevious(uri, sb);
        int start = pageIndex / MAX_TAG_COUNT * MAX_TAG_COUNT;
        int nextStart = (pageIndex / MAX_TAG_COUNT + 1) * MAX_TAG_COUNT;
        int end = nextStart > totalPages ? totalPages : nextStart;
        do {
            sb.append("<li class=\"page");
            if (start == pageIndex) {
                sb.append(" selected");
            }
            sb.append("\"><a href=\"").append(assembleUrl(uri, start)).append("\">").append(++start).append("</a></li>");
        } while (start < end);
        appendNext(uri, sb);
        sb.append("</ul>");
        sb.append("<div class=\"pager-summary\">");
        sb.append("<span>共").append(totalRows).append("条记录</span>");
        sb.append("<span>").append(pageIndex + 1).append("/").append(0 == totalPages ? 1 : totalPages).append("页</span>");
        sb.append("</div>");

        return sb.toString();
    }

    private void appendNext(String uri, StringBuilder sb) {
        sb.append("<li class=\"next");
        if (totalPages <= pageIndex + 1) {
            sb.append(" disabled");
        }
        sb.append("\"><a href=\"");
        if (totalPages <= pageIndex + 1) {
            sb.append("javascript:void(0);");
        } else {
            sb.append(assembleUrl(uri, pageIndex < totalPages ? pageIndex + 1 : totalPages));
        }
        sb.append("\">下一页</a></li>");
    }

    private void appendPrevious(String uri, StringBuilder sb) {
        sb.append("<li class=\"previous");
        if (0 == pageIndex) {
            sb.append(" disabled");
        }
        sb.append("\"><a href=\"");
        if (0 == pageIndex) {
            sb.append("javascript:void(0);");
        } else {
            sb.append(assembleUrl(uri, pageIndex > 1 ? pageIndex - 1 : 0));
        }
        sb.append("\">上一页</a></li>");
    }

    private String getUriAndParams(String paramName) {
        String requestURI = HttpHelper.getCurrentRequest().getRequestURI();
        String queryString = HttpHelper.getCurrentRequest().getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            if (queryString.indexOf(paramName) > -1) {
                queryString = queryString.replaceAll("^&*" + paramName + "=\\d+$", "");
            }
            return requestURI + "?" + queryString + "&" + paramName + "=";
        } else {
            return requestURI + "?" + paramName + "=";
        }
    }

    private String assembleUrl(String uri, int pageIndex) {
        return uri + pageIndex;
    }

    //-----------------------------------


    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public JList<T> getList() {
        return list;
    }

    public void setList(JList<T> list) {
        this.list = list;
    }

}
