package cn.chiayhon.poi.excel.sxxsf;

import java.util.List;

@FunctionalInterface
public interface DataSupplier<E> {

    List<E> getDataByCondition(PageCondition condition);
}
