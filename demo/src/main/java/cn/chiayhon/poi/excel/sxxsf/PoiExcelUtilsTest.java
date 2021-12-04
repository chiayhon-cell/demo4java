package cn.chiayhon.poi.excel.sxxsf;

import lombok.Data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试excel操作工具类
 */
public class PoiExcelUtilsTest {

    /**
     * 文件保存目录
     */
    private static final String UPLOAD_PATH = "D:/";

    public static void main(String[] args) {
        new PoiExcelUtilsTest().testExport();
    }

    /**
     * 测试excel导出
     */
    public void testExport() {

        // 打印一下运行内存
        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println(maxMemory / 1024 / 1024 + "MB");

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
            throw new RuntimeException(e);
        }
    }

    /**
     * 封装测试数据
     */
    @Data
    public static class TaskHistory {

        private String vin;

        private String updateStatus;

        private String firmwareId;

        private String failReason;
    }
}