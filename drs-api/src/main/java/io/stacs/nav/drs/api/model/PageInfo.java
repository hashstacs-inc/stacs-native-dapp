package io.stacs.nav.drs.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author dekuofa <br>
 * @date 2019-12-11 <br>
 */
@Getter @Setter public class PageInfo<T> {
    protected long total;
    protected List<T> list;
    private int pageNum;
    private int pageSize;
    private int size;
    private int startRow;
    private int endRow;
    private int pages;
    private int prePage;
    private int nextPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private int[] navigatepageNums;
    private int navigateFirstPage;
    private int navigateLastPage;
}
