package cn.chiayhon.poi.excel.sxxsf;

import java.util.List;

/**
 * Excel批处理器
 */
public interface ExcelBatchProcessor1<E> {

    /**
     * 是否还有下一批次
     * @return boolean
     */
    boolean hasNextBatch();


    /**
     * 获取下一批次数据
     * @return 数据集合
     */
    List<E> nextBatch(DataSupplier<E> supplier);


}