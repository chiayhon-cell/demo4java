package cn.chiayhon.concurrent.excel.sxxf;

import cn.chiayhon.TaskHistory;
import cn.chiayhon.excel.ExcelModel;
import cn.chiayhon.page.PageCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class MyTest implements ApplicationRunner {

    private final AtomicInteger integer = new AtomicInteger(0);

    @Autowired
    private ExcelBatchProcessor<TaskHistory> processor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        start();
    }

    private void start() throws IOException {
        DataSupplier<TaskHistory> dataSupplier = condition -> {
            List<TaskHistory> data = new ArrayList<>();
            for (int i = 1, n = condition.getPageSize(); i <= n; i++) {
                TaskHistory taskHistory = new TaskHistory();
                taskHistory.setFirmwareId(String.valueOf(integer.getAndIncrement()));
                taskHistory.setFailReason("系统异常");
                taskHistory.setUpdateStatus("请求成功");
                taskHistory.setVin(String.format("1099728%08d", n));
                data.add(taskHistory);
            }
            return data;
        };

        String fileName = "TestPoi.xlsx";
        ModelSupplier<ExcelModel> modelSupplier = () -> {
            // 映射
            int width = 6;
            ExcelModel excelModel = new ExcelModel();
            excelModel.setName(fileName);
            excelModel.buildColumn()
                    .addColumn("vin", "vin", width)
                    .addColumn("设备ID", "firmwareId", width)
                    .addColumn("升级状态", "updateStatus", width)
                    .addColumn("失败原因", "failReason", width);
            return excelModel;
        };

        ExcelBatchConfig config = new ExcelBatchConfig(2000, 1000, 100,"表单");
        try (OutputStream outputStream = new FileOutputStream("D:/" + fileName)) {
            processor.init(
                    config,
                    new PageCondition(),
                    dataSupplier,
                    modelSupplier,
                    outputStream
            );
            log.info("批处理器开始执行");
            processor.execute();
            log.info("批处理器执行完毕");
        }
    }
}
