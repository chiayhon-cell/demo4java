package cn.chiayhon.concurrent.excel.sxxf;

import cn.chiayhon.TaskHistory;
import cn.chiayhon.page.PageCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MyTest implements ApplicationRunner {


    @Autowired
    private ExcelBatchProcessor<TaskHistory> processor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ExcelBatchConfig config = new ExcelBatchConfig(500000, 100000, 1000);
        processor.init(
                config,
                new PageCondition(),
                condition -> {
                    List<TaskHistory> data = new ArrayList<>();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 1, n = condition.getPageSize(); i <= n; i++) {
                        TaskHistory taskHistory = new TaskHistory();
                        taskHistory.setFirmwareId(String.format("11%08d", n));
                        taskHistory.setFailReason("系统异常");
                        taskHistory.setUpdateStatus("请求成功");
                        taskHistory.setVin(String.format("1099728%08d", n));
                        data.add(taskHistory);
                    }
                    return data;
                });
        System.err.println("批处理器开始执行");
        processor.execute();
        System.err.println("批处理器执行完毕");
    }
}
