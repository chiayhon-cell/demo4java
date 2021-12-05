package cn.chiayhon.excel;

import cn.chiayhon.page.PageCondition;

import java.util.List;

@FunctionalInterface
public interface DataSupplier<E> {

    List<E> getDataByCondition(PageCondition condition);
}
