package cn.chiayhon.poi.excel.sxxsf;

public interface Pagable {
    int getOffset();

    int getLimit();

    boolean isAutoCount();
}
