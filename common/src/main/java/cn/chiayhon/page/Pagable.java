package cn.chiayhon.page;

public interface Pagable {
    int getOffset();

    int getLimit();

    boolean isAutoCount();
}
