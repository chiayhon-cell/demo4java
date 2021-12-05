package cn.chiayhon.concurrent.excel.sxxf;

import cn.chiayhon.TaskHistory;
import cn.chiayhon.excel.ExcelColumnModel;
import cn.chiayhon.excel.ExcelModel;
import cn.chiayhon.page.PageCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MyTest implements ApplicationRunner {


    @Autowired
    private ExcelBatchProcessor<TaskHistory> processor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        DataSupplier<TaskHistory> dataSupplier = condition -> {
            List<TaskHistory> data = new ArrayList<>();
            for (int i = 1, n = condition.getPageSize(); i <= n; i++) {
                TaskHistory taskHistory = new TaskHistory();
                taskHistory.setFirmwareId(String.format("11%08d", n));
                taskHistory.setFailReason("系统异常");
                taskHistory.setUpdateStatus("请求成功");
                taskHistory.setVin(String.format("1099728%08d", n));
                data.add(taskHistory);
            }
            return data;
        };

        ModelSupplier<ExcelModel> modelSupplier = () ->{
            // 映射
            int width = 10 * 512 + 500;
            ExcelModel excelModel = new ExcelModel();
            excelModel.setName("TestPoi" + (int) (Math.random() * 1000) + ".xlsx");
            List<ExcelColumnModel> columnModels = new ArrayList<>();
            columnModels.add(new ExcelColumnModel("vin", "vin", width));
            columnModels.add(new ExcelColumnModel("设备ID", "firmwareId", width));
            columnModels.add(new ExcelColumnModel("升级状态", "updateStatus", width));
            columnModels.add(new ExcelColumnModel("失败原因", "failReason", width));
            excelModel.setColumns(columnModels);
            return excelModel;
        };
        ExcelBatchConfig config = new ExcelBatchConfig(5000, 1000, 100);
        processor.init(
                config,
                new PageCondition(),
                dataSupplier,
                modelSupplier
                );
        System.err.println("批处理器开始执行");
        processor.execute();
        System.err.println("批处理器执行完毕");
    }
}
