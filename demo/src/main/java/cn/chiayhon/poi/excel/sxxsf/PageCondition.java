package cn.chiayhon.poi.excel.sxxsf;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageCondition implements Serializable, Pagable {
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_PAGE_NO = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    protected int pageNo = 1;
    protected int pageSize = 10;
    protected boolean autoCount = true;

    public PageCondition() {
    }

    public PageCondition(int pageNo, int pageSize) {
        this.pageNo = pageNo < 1 ? 1 : pageNo;
        this.pageSize = pageSize < 2 ? 10 : pageSize;
    }

    public int getEnd() {
        return this.getLimit() + this.getOffset();
    }

    public int getOffset() {
        return (this.pageNo - 1) * this.pageSize;
    }

    public int getLimit() {
        return this.pageSize;
    }

}