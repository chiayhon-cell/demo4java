package cn.chiayhon.concurrent.excel.sxxf;

@FunctionalInterface
public interface ModelSupplier<T> {
    T getModel();
}
