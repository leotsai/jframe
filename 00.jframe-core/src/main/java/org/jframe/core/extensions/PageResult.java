package org.jframe.core.extensions;


import java.util.List;
import java.util.function.Function;

/**
 * Created by leo on 2017-05-26.
 */
public class PageResult<T> {
    private int totalRows;
    private int totalPages;
    private int pageIndex;
    private int pageSize;
    private JList<T> list;

    public PageResult() {

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
