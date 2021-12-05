package cn.chiayhon.concurrent.excel.sxxf;

import cn.chiayhon.excel.BigDataExcelUtils;
import cn.chiayhon.excel.ExcelColumnModel;
import cn.chiayhon.excel.ExcelModel;
import cn.chiayhon.page.PageCondition;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Excel批次数据执行器
 */
@Component
public class ExcelBatchProcessor<E> {

    private ExcelBatchConfig config;

    private List<BatchLine<E>> lines;

    @Resource(name = "excelBatchThreadPool")
    private ThreadPoolExecutor executor;

    public void init(ExcelBatchConfig config, PageCondition condition, DataSupplier<E> dataSupplier, ModelSupplier<ExcelModel> modelSupplier) {
        int totalSize = config.getTotalSize();
        int limitSize = config.getLimitSize();
        int batchSize = config.getBatchSize();
        int lineSize = (totalSize + limitSize - 1) / limitSize;
        // 创建批次流水线
        List<BatchLine<E>> list = new ArrayList<>();
        for (int i = 0; i < lineSize; i++, totalSize -= limitSize) {
            // 数据读任务，设置数据库查询任务次数
            PageCondition newCondition = new PageCondition();
            BeanUtils.copyProperties(condition, newCondition);
            ExcelDbTask<E> readTask = new ExcelDbTask<>(newCondition, dataSupplier);
            int dataSize = Math.min(totalSize, limitSize);
            readTask.setBatchSize(batchSize);
            readTask.setDataSize(dataSize);
            // 数据写任务
            ExcelWriteTask<E> writeTask = new ExcelWriteTask<>(modelSupplier);
            BatchLine<E> batchLine = BatchLine.init(readTask, writeTask, executor);
            list.add(batchLine);
        }
        this.setConfig(config);
        this.setLines(list);
    }

    public void execute() {
        ExecutorService executorService = Executors.newFixedThreadPool(lines.size());
        CountDownLatch processorLatch = new CountDownLatch(lines.size());
        for (BatchLine<E> line : lines) {
            executorService.submit(
                    () -> line.startBatch(processorLatch)
            );
        }
        try {
            processorLatch.await();
            System.err.println("批处理器结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }


    public ExcelBatchConfig getConfig() {
        return config;
    }

    public void setConfig(ExcelBatchConfig config) {
        this.config = config;
    }

    public List<BatchLine<E>> getLines() {
        return lines;
    }

    public void setLines(List<BatchLine<E>> lines) {
        this.lines = lines;
    }
}

@Slf4j
class ExcelWriteTask<E> extends ExcelTask<E> implements Callable<SXSSFWorkbook>, ModelSupplier<ExcelModel> {

    private ModelSupplier<ExcelModel> modelSupplier;

    public ExcelWriteTask(ModelSupplier<ExcelModel> modelSupplier) {
        this.modelSupplier = modelSupplier;
    }

    @Override
    public SXSSFWorkbook call() throws Exception {

        ExcelModel excelModel = getModel();

        // 从数据对象中获取列值使用的getter方法名集合
        List<ExcelColumnModel> cols = excelModel.getColumns();
        List<String> methodNames = new ArrayList<>();
        String filename = excelModel.getName();
        String propertyName;
        for (ExcelColumnModel column : cols) {
            propertyName = "get" + BigDataExcelUtils.upperCaseHead(column.getPropertyName());
            methodNames.add(propertyName);
        }

        // 创建sheet
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        SXSSFSheet sxssfSheet = BigDataExcelUtils.createSheet(workbook, excelModel.getColumns(), filename + "" + Math.round(1000));

        // 创建输出流
        try (OutputStream outputStream = new FileOutputStream("D:/" + excelModel.getName())) {
            CountDownLatch latch = getLatch();
            List<E> data;
            do {
                data = getTaskQueue().take();
                log.info("开始消费任务");
                BigDataExcelUtils.export2Sheet(sxssfSheet, methodNames, data);
                data.clear();
                log.info("任务消费完成!");
                // 当还有任务或者dbTask未完成时,writeTask不能停止
            } while (!getTaskQueue().isEmpty() || latch.getCount() > 1);
            latch.countDown();
            // FIXME: 2021/12/5 只有一条数据时无法导出
            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("导出失败", e);
        } finally {
            // dispose of temporary files backing this workbook on disk
            workbook.dispose();
            workbook.close();
        }
        return null;
    }

    @Override
    public ExcelModel getModel() {
        ExcelModel model = null;
        if (!Objects.isNull(modelSupplier)) {
            model = modelSupplier.getModel();
        }
        return model;
    }

    public ModelSupplier<ExcelModel> getModelSupplier() {
        return modelSupplier;
    }

    public void setModelSupplier(ModelSupplier<ExcelModel> modelSupplier) {
        this.modelSupplier = modelSupplier;
    }
}

@FunctionalInterface
interface DataSupplier<E> {
    List<E> getDataByCondition(PageCondition condition);
}

@Data
@Slf4j
class ExcelDbTask<E> extends ExcelTask<E> implements Callable<List<E>>, DataSupplier<E> {
    /**
     * 批次编号:默认为1
     */
    private ThreadLocal<Integer> batchNo = ThreadLocal.withInitial(() -> 1);
    /**
     * 处理的数据量
     */
    private Integer dataSize;
    /**
     * 每批次处理的数据数量
     */
    private Integer batchSize;
    /**
     * 数据库查询条件
     */
    private PageCondition condition;
    /**
     * 查询逻辑
     */
    private DataSupplier<E> supplier;

    public ExcelDbTask(PageCondition condition, DataSupplier<E> supplier) {
        this.condition = condition;
        this.supplier = supplier;
    }

    @Override
    public List<E> call() throws Exception {
        List<E> data = null;
        int batchTimes = (dataSize + batchSize - 1) / batchSize;
        for (int i = 0; i < batchTimes; i++, dataSize -= batchSize) {
            Integer integer = batchNo.get();
            log.info("准备生产任务..");
            condition.setPageNo(i + 1);
            condition.setPageSize(Math.min(dataSize, batchSize));
            data = this.getDataByCondition(condition);
            getTaskQueue().put(data);
            log.info("生产任务成功！！！目前批次:{},剩余批次:{}", integer++, batchTimes - i - 1);
            batchNo.set(integer);
        }
        // 数据读取完成
        getLatch().countDown();
        batchNo.remove();
        return data;
    }

    @Override
    public List<E> getDataByCondition(PageCondition c) {
        List<E> data = null;
        if (!Objects.isNull(supplier)) {
            data = supplier.getDataByCondition(c);
        }
        return data;
    }
}

@Data
class BatchLine<E> {

    /**
     * 队列容量
     */
    private static final int QUEUE_SIZE = 2;
    /**
     * 倒计时，与读任务相关联
     */
    private static final int LATCH_SIZE = 2;

    private ExcelDbTask<E> readTask;

    private ExcelWriteTask<E> writeTask;

    private ThreadPoolExecutor executor;

    private CountDownLatch lineLatch;

    public BatchLine(ExcelDbTask<E> readTask, ExcelWriteTask<E> writeTask, ThreadPoolExecutor executor) {
        this.readTask = readTask;
        this.writeTask = writeTask;
        this.executor = executor;
    }

    /**
     * 初始化批次流水线
     *
     * @param readTask  数据读任务
     * @param writeTask 数据写任务
     * @return
     */
    public static <E> BatchLine<E> init(ExcelDbTask<E> readTask, ExcelWriteTask<E> writeTask, ThreadPoolExecutor executor) {
        BatchLine<E> line = new BatchLine<>(readTask, writeTask, executor);
        // 设置任务队列
        BlockingQueue<List<E>> es = new ArrayBlockingQueue<>(QUEUE_SIZE);
        readTask.setTaskQueue(es);
        writeTask.setTaskQueue(es);

        CountDownLatch latch = new CountDownLatch(LATCH_SIZE);
        line.setLineLatch(latch);
        readTask.setLatch(latch);
        writeTask.setLatch(latch);
        return line;
    }

    public void startBatch(CountDownLatch processorLatch) {
        executor.submit(readTask);
        executor.submit(writeTask);
        try {
            this.lineLatch.await();
            processorLatch.countDown();
            System.err.println("流水线结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

abstract class ExcelTask<E> {
    /**
     * 任务队列
     */
    private BlockingQueue<List<E>> taskQueue;

    private CountDownLatch latch;

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public BlockingQueue<List<E>> getTaskQueue() {
        return taskQueue;
    }

    public void setTaskQueue(BlockingQueue<List<E>> taskQueue) {
        this.taskQueue = taskQueue;
    }
}

class ExcelBatchConfig {

    /**
     * 总数量
     */
    private int totalSize;
    /**
     * 线程处理的上限
     */
    private int limitSize;

    /**
     * 线程每批次处理数量
     */
    private int batchSize;


    public ExcelBatchConfig(int totalSize, int limitSize, int batchSize) {
        this.totalSize = totalSize;
        this.limitSize = limitSize;
        this.batchSize = batchSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
