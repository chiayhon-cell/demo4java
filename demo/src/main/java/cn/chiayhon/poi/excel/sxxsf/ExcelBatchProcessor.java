package cn.chiayhon.poi.excel.sxxsf;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 批次数据生成器
 */
public class ExcelBatchProcessor<E> implements DataSupplier<E> {

    /**
     * 批次编号
     */
    protected int batchNO = 1;

    /**
     * 总数量
     */
    protected int totalSize;

    /**
     * 每批次处理数量
     */
    protected int batchSize;

    /**
     * 总批次
     */
    protected int totalBatch;

    /**
     * 查询条件
     */
    protected PageCondition condition;
    /**
     * 获取数据的逻辑
     */
    protected DataSupplier<E> supplier;

    private ExcelBatchProcessor(int totalSize, int batchSize, PageCondition condition,DataSupplier<E> supplier) {
        this.totalSize = totalSize;
        this.batchSize = batchSize;
        this.totalBatch = (totalSize + batchSize - 1) / batchSize;
        this.condition = condition;
        this.supplier = supplier;
    }

    public static <T> ExcelBatchProcessor<T> init(int totalSize, int batchSize, PageCondition condition,DataSupplier<T> supplier){
        if (totalSize <= 0 || batchSize <= 0){
            System.out.println("处理数目至少为1");
        }
        if (Objects.isNull(condition) || Objects.isNull(supplier)){
            System.out.println("参数不能传空");
        }
        return new ExcelBatchProcessor<>(totalSize,batchSize,condition,supplier);
    }

    /**
     * 是否有下一批数据
     *
     * @return boolean
     */
    public boolean hasNextBatch() {
        return this.batchNO <= this.totalBatch;
    }


    public List<E> nextBatch() {
        if (hasNextBatch()) {
            List<E> batch = null;
            if (!Objects.isNull(this.condition)) {
                this.condition.setPageNo(this.batchNO);
                this.condition.setPageSize(this.batchSize);
                batch = Objects.requireNonNull(this.supplier).getDataByCondition(condition);
            }
            this.batchNO++;
            return batch;
        } else {
            throw new NoSuchElementException();
        }
    }


    @Override
    public List<E> getDataByCondition(PageCondition condition) {
        List<E> data = null;
        if (this.supplier != null){
            data = this.supplier.getDataByCondition(condition);
        }
        return data;
    }
}