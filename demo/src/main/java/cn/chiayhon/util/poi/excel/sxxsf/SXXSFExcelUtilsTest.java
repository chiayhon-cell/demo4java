package cn.chiayhon.util.poi.excel.sxxsf;

import cn.chiayhon.TaskHistory;
import cn.chiayhon.excel.BigDataExcelUtils;
import cn.chiayhon.excel.ExcelBatchProcessor;
import cn.chiayhon.excel.ExcelColumnModel;
import cn.chiayhon.excel.ExcelModel;
import cn.chiayhon.page.PageCondition;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试excel操作工具类
 */
@Slf4j
public class SXXSFExcelUtilsTest {

    /**
     * 文件保存目录
     */
    private static final String UPLOAD_PATH = "D:" + File.pathSeparator;

    public static void main(String[] args) {
        new SXXSFExcelUtilsTest().testExport();
    }

    /**
     * 测试excel导出
     */
    public void testExport() {

        // 打印一下运行内存
        long maxMemory = Runtime.getRuntime().maxMemory();
        log.info(maxMemory / 1024 / 1024 + "MB");

        String filename = "TestPoi1.xlsx";

        try (OutputStream outputStream = new FileOutputStream(UPLOAD_PATH + filename)) {
            int width = 10 * 512 + 500;
            ExcelModel excelModel = new ExcelModel();
            excelModel.setName(filename);
            List<ExcelColumnModel> cols = new ArrayList<>();
            cols.add(new ExcelColumnModel("vin", "vin", width));
            cols.add(new ExcelColumnModel("设备ID", "firmwareId", width));
            cols.add(new ExcelColumnModel("升级状态", "updateStatus", width));
            cols.add(new ExcelColumnModel("失败原因", "failReason", width));
            excelModel.setColumns(cols);
            int totalSize = 500000;
            int batchSize = 100000;
            PageCondition pageCondition = new PageCondition();
            ExcelBatchProcessor<TaskHistory> processor = ExcelBatchProcessor.init(totalSize, batchSize, pageCondition, condition -> {
                // 模拟获取数据
                List<TaskHistory> data = new ArrayList<>();
                for (int i = 1, n = condition.getLimit(); i <= n; i++) {
                    TaskHistory taskHistory = new TaskHistory();
                    taskHistory.setFirmwareId(String.format("11%08d", n));
                    taskHistory.setFailReason("系统异常");
                    taskHistory.setUpdateStatus("请求成功");
                    taskHistory.setVin(String.format("1099728%08d", n));
                    data.add(taskHistory);
                }
                return data;
            });
            BigDataExcelUtils.export(
                    processor,
                    excelModel,
                    outputStream);

        } catch (IOException e) {
            log.error("", e);
        }
    }

}