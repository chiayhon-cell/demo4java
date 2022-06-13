package cn.chiayhon.jdk.concurrent.excel.sxxf;

@FunctionalInterface
public interface ModelSupplier<T> {
    T getModel();
}
