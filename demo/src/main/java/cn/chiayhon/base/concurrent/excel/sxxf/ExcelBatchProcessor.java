package cn.chiayhon.base.concurrent.excel.sxxf;

import cn.chiayhon.excel.BigDataExcelUtils;
import cn.chiayhon.excel.ExcelColumnModel;
import cn.chiayhon.excel.ExcelModel;
import cn.chiayhon.page.PageCondition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Excel批次数据执行器
 */
@Component
@Data
@Slf4j
public class ExcelBatchProcessor<E> {

    public static final int WINDOW_SIZE = 200;

    public static final Object lock = new Object();

    private ExcelBatchConfig config;

    private List<BatchLine<E>> lines;

    private OutputStream outputStream;

    @Resource(name = "excelBatchThreadPool")
    private ThreadPoolExecutor executor;

    public void init(ExcelBatchConfig config, PageCondition condition, DataSupplier<E> dataSupplier, ModelSupplier<ExcelModel> modelSupplier, OutputStream outputStream) {
        int totalSize = config.getTotalSize();
        int limitSize = config.getLimitSize();
        int batchSize = config.getBatchSize();
        String sheetName = config.getSheetName();
        int lineSize = (totalSize + limitSize - 1) / limitSize;
        // 创建批次流水线
        List<BatchLine<E>> list = new ArrayList<>();
        for (int i = 1; i <= lineSize; i++, totalSize -= limitSize) {
            // 数据读任务，设置查询条件、数据总数、每批次处理数
            PageCondition newCondition = new PageCondition();
            BeanUtils.copyProperties(condition, newCondition);
            ExcelDbTask<E> readTask = new ExcelDbTask<>(newCondition, dataSupplier);
            int dataSize = Math.min(totalSize, limitSize);
            readTask.setDataSize(dataSize);
            readTask.setBatchSize(batchSize);
            // 数据写任务，设置表单名
            ExcelWriteTask<E> writeTask = new ExcelWriteTask<>(modelSupplier);
            writeTask.setSheetName(sheetName + "-" + i);

            BatchLine<E> batchLine = BatchLine.init(readTask, writeTask, executor);
            list.add(batchLine);
        }
        this.setConfig(config);
        this.setLines(list);
        this.setOutputStream(outputStream);
    }

    public void execute() {
        ExecutorService executorService = Executors.newFixedThreadPool(lines.size());
        CountDownLatch processorLatch = new CountDownLatch(lines.size());
        SXSSFWorkbook workbook = new SXSSFWorkbook(WINDOW_SIZE);

        for (BatchLine<E> line : lines) {
            executorService.submit(
                    () -> line.startBatch(processorLatch, workbook)
            );
        }

        try {
            processorLatch.await();
            workbook.write(outputStream);
            log.info("批处理器结束");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workbook.dispose();
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
        }
    }
}

@Getter
@Setter
@Slf4j
class ExcelWriteTask<E> extends ExcelTask<E> implements Runnable, ModelSupplier<ExcelModel> {

    private String sheetName;

    private ModelSupplier<ExcelModel> modelSupplier;

    private SXSSFWorkbook workbook;

    private static final String PREFIX_OF_GET_METHOD = "get";

    public ExcelWriteTask(ModelSupplier<ExcelModel> modelSupplier) {
        this.modelSupplier = modelSupplier;
    }

    @Override
    public ExcelModel getModel() {
        ExcelModel model = null;
        if (!Objects.isNull(modelSupplier)) {
            model = modelSupplier.getModel();
        }
        return model;
    }

    @Override
    public void run() {
        ExcelModel excelModel = getModel();

        // 从数据对象中获取列值使用的getter方法名集合
        List<ExcelColumnModel> cols = excelModel.getColumns();
        List<String> methodNames = new ArrayList<>();
        String propertyName;
        for (ExcelColumnModel column : cols) {
            propertyName = PREFIX_OF_GET_METHOD + BigDataExcelUtils.upperCaseHead(column.getPropertyName());
            methodNames.add(propertyName);
        }

        // 创建sheet
        SXSSFSheet sxssfSheet;
        synchronized (ExcelBatchProcessor.lock) {
            sxssfSheet = BigDataExcelUtils.createSheet(workbook, excelModel.getColumns(), sheetName);
        }
        CountDownLatch latch = getLatch();
        List<E> data;
        try {
            do {
                data = getTaskQueue().take();
                log.info("开始消费任务");
                BigDataExcelUtils.export2Sheet(sxssfSheet, methodNames, data);
                data.clear();
                log.info("任务消费完成!");
                // 当还有任务或者dbTask未完成时,writeTask不能停止
            } while (!getTaskQueue().isEmpty() || latch.getCount() > 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown();
        }
    }
}

@FunctionalInterface
interface DataSupplier<E> {
    List<E> getDataByCondition(PageCondition condition);
}

@Slf4j
@Getter
@Setter
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
    public List<E> call() {
        List<E> data = null;
        int batchTimes = (dataSize + batchSize - 1) / batchSize;
        int newPageNo = (condition.getOffset() / batchSize) + 1;
        try {
            for (int i = 0; i < batchTimes; i++, dataSize -= batchSize) {
                Integer integer = batchNo.get();
                log.info("准备生产任务..");
                int realSize = Math.min(dataSize, batchSize);
                condition.setPageNo(newPageNo);
                condition.setPageSize(realSize);
                log.info("newPageNo=" + newPageNo + ",realSize=" + realSize);
                data = this.getDataByCondition(condition);
                getTaskQueue().put(data);
                log.info("生产任务成功！！！目前批次:{},剩余批次:{}", integer++, batchTimes - i - 1);
                batchNo.set(integer);
            }
        } catch (Exception e) {
            log.error("读取数据时发生异常", e);
            Thread.currentThread().interrupt();
        } finally {
            // 数据读取完成
            getLatch().countDown();
            batchNo.remove();
        }

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

    public void startBatch(CountDownLatch processorLatch, SXSSFWorkbook workbook) {
        executor.submit(readTask);
        writeTask.setWorkbook(workbook);
        executor.submit(writeTask);
        try {
            this.lineLatch.await();
            processorLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
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

@Data
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
    /**
     * 表单名
     */
    private String sheetName;


    public ExcelBatchConfig(int totalSize, int limitSize, int batchSize, String sheetName) {
        this.totalSize = totalSize;
        this.limitSize = limitSize;
        this.batchSize = batchSize;
        this.sheetName = sheetName;
    }
}
