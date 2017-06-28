package org.jframe.infrastructure.data;

/**
 * Created by leo on 2017-05-26.
 */
public class PageRequest {
    private int pageIndex;
    private int pageSize;
    private String sort;
    private SortDirection direction;

    public int getSkip(){
        return this.pageIndex * this.pageSize;
    }

    public String getSqlPageing(String originalSql) {
        String sqlSort = "";
        if (this.sort != null && this.sort.equals("") == false) {
            if(this.sort.length() > 50){
                sqlSort = "";
            }
            else{
                sqlSort = "order by " + this.sort.replace(" ", "") + " " + this.direction.toString();
            }
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

    public SortDirection getDirection() {
        return direction;
    }

    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }
}
