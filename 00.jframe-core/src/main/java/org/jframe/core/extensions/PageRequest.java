package org.jframe.core.extensions;

/**
 * Created by leo on 2017-05-26.
 */
public class PageRequest {
    private int pageIndex;
    private int pageSize;
    private String sort;
    private SortDirection sortDirection;

    public PageRequest(){

    }

    public PageRequest(int pageIndex, int pageSize){
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public int getSkip(){
        return this.pageIndex * this.pageSize;
    }

    public String getSqlPageing(String originalSql) {
        String sqlSort = "";
        if (this.sort != null && this.sort.isEmpty() == false) {
            if(this.sort.contains(" ")){
                throw new KnownException("排序字段错误");
            }
            sqlSort = "order by _tpage." + this.sort + " " + this.sortDirection.toString();
        }
        return String.format("select * from (%s) _tpage %s limit %d,%d", originalSql, sqlSort, this.getSkip(), this.pageSize);
    }

    //----------------------------------------

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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }
}
