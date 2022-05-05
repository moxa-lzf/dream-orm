package com.moxa.dream.driver.page;

import java.util.ArrayList;
import java.util.Collection;

public class Page<E> extends ArrayList<E> {
    private Long total;
    private final int pageNum;
    private final int pageSize;
    private Collection<E> row;
    private final long startRow;
    private boolean count = true;

    public Page() {
        this(1, Integer.MAX_VALUE);
    }

    public Page(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.startRow = (pageNum - 1) * pageSize;
    }

    public static Page of(int pageNum, int pageSize) {
        return new Page(pageNum, pageSize);
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }


    public Collection<E> getRow() {
        return row;
    }

    public void setRow(Collection<E> row) {
        this.row = row;
    }

    public Long getStartRow() {
        return startRow;
    }

    @Override
    public String toString() {
        return "Page{" +
                "total=" + total +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", startRow=" + startRow +
                ", count=" + count +
                ", row=" + row +
                '}';
    }
}
