package org.xkidea.bams.web.util;

import javax.faces.model.DataModel;

public abstract class AbstractPaginationHelper {

    public static final int DEFAULT_SIZE = 19;
    private transient int pageSize;
    private transient int page;

    public AbstractPaginationHelper(int pageSize) {
        this.pageSize = pageSize;
    }

    public abstract int getItemsCount();

    public abstract DataModel createPageDataModel();

    /**
     * 取得当前页的第一条的index
     * @return
     */
    public int getPageFirstItem() {
        return page*pageSize;
    }

    /**
     * 取得当前页的最后一条的index
     * @return
     */
    public int getPageLastItem() {
        int i = getPageFirstItem() + pageSize -1;
        int count = getItemsCount() - 1;
        if (i > count) {
            i = count;
        }
        if (i < 0) {
            i = 0;
        }
        return i;
    }

    public boolean isHasNextPage() {
        return (page + 1)*pageSize+1 <= getItemsCount();
    }

    public void nextPage() {
        if (isHasNextPage()) {
            page++;
        }
    }

    public boolean isHasPreviousPage() {
        return page > 0;
    }

    public void firstPage() {
        page = 0;
    }

    public void lastPage() {
        page = getPageCount() - 1;
    }

    public void previousPage() {
        if (isHasPreviousPage()) {
            page--;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return page+1;
    }

    public int getPageCount() {
        if (getItemsCount() % pageSize == 0) {
            return getItemsCount() / pageSize;
        } else {
            return getItemsCount() / pageSize + 1;
        }
    }
}
