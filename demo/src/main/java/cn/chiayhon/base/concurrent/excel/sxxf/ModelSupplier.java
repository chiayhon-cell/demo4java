package cn.chiayhon.base.concurrent.excel.sxxf;

@FunctionalInterface
public interface ModelSupplier<T> {
    T getModel();
}
