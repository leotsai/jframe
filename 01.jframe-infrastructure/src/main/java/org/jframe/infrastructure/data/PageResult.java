package org.jframe.infrastructure.data;

import java.util.List;

/**
 * Created by leo on 2017-05-26.
 */
public class PageResult<T> {
    private int totalRows;
    private int totalPages;
    private int pageIndex;
    private int pageSize;
    private List<T> list;

    public PageResult(){

    }

    public PageResult(PageRequest request, int totalRows, List<T> pagedList) {
        this.pageIndex = request.getPageIndex();
        this.pageSize = request.getPageSize();
        this.totalRows = totalRows;
        this.totalPages = totalRows / request.getPageSize();
        if (totalRows % request.getPageSize() > 0) {
            this.totalPages++;
        }
        this.list = pagedList;
    }

    public boolean hasNextPage(){
        return this.totalPages >= this.pageIndex;
    }

    public boolean hasPreviousPage(){
        return this.pageIndex > 0;
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

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }


}
