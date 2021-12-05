//package cn.chiayhon.concurrent.excel.sxxf;
//
//import cn.chiayhon.excel.ExcelColumnModel;
//import cn.chiayhon.excel.ExcelModel;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.xssf.streaming.SXSSFCell;
//import org.apache.poi.xssf.streaming.SXSSFRow;
//import org.apache.poi.xssf.streaming.SXSSFSheet;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.apache.poi.xssf.usermodel.XSSFRichTextString;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 大数据量Excel导出工具类
// */
//public class ExcelUtils {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);
//
//    /**
//     * 默认内存缓存数据量
//     */
//    public static final int BUFFER_SIZE = 1000;
//
//    /**
//     * 默认工作表名称
//     */
//    public static final String DEFAULT_SHEET_NAME = "sheet";
//
//
//    public static <T> void export(ExcelBatchProcessor<T> excelBatchProcessor, ExcelModel excelModel, OutputStream outputStream) {
//
//
//        SXSSFWorkbook workbook = new SXSSFWorkbook(BUFFER_SIZE);
//
//        try {
//            // 从数据对象中获取列值使用的getter方法名集合
//            List<String> methodNames = new ArrayList<>();
//            List<ExcelColumnModel> cols = excelModel.getColumns();
//            String sheetName = excelModel.getName();
//
//            String propertyName;
//            for (ExcelColumnModel column : cols) {
//                propertyName = "get" + upperCaseHead(column.getPropertyName());
//                methodNames.add(propertyName);
//            }
//
//            List<?> objects;
//            int i = 0;
//            while (excelBatchProcessor.hasNextBatch()) {
//                objects = excelBatchProcessor.nextBatch();
//                SXSSFSheet sxssfSheet = createSheet(workbook, cols, sheetName + i);
//                export2Sheet(sxssfSheet, methodNames, objects);
//                objects.clear();
//                System.out.print("\r" + "Current batch >> " + (i + 1));
//                i++;
//            }
//
//            // 输出
//            workbook.write(outputStream);
//
//        } catch (Exception e) {
//            LOGGER.error("导出失败", e);
//        } finally {
//            // dispose of temporary files backing this workbook on disk
//            workbook.dispose();
//            try {
//                workbook.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    /**
//     * 把数据导出到sheet中
//     *
//     * @param sheet   sheet
//     * @param getters 从数据对象中获取列值使用的getter方法名集合
//     * @param data    数据
//     */
//    public static void export2Sheet(SXSSFSheet sheet, List<String> getters, List<?> data) {
//
//        try {
//
//            // 记录当前sheet的数据量
//            int sheetRowCount = sheet.getLastRowNum();
//
//            SXSSFRow dataRow;
//
//            // 遍历数据集合
//            for (Object datum : data) {
//
//                // 创建一行
//                dataRow = sheet.createRow(++sheetRowCount);
//
//                Class<?> clazz = datum.getClass();
//                Method readMethod;
//                Object o;
//                XSSFRichTextString text;
//                Cell cell;
//
//                // 遍历methodNames集合，获取每一列的值
//                for (int i = 0; i < getters.size(); i++) {
//                    // 从Class对象获取getter方法
//                    readMethod = clazz.getMethod(getters.get(i));
//                    // 获取列值
//                    o = readMethod.invoke(datum);
//                    if (o == null) {
//                        o = "";
//                    }
//                    text = new XSSFRichTextString(o.toString());
//                    // 创建单元格并赋值
//                    cell = dataRow.createCell(i);
//                    cell.setCellValue(text);
//                }
//            }
//
//        } catch (Exception e) {
//            LOGGER.error("导出时错误", e);
//        }
//    }
//
//    /**
//     * 创建一个工作表
//     *
//     * @param workbook  SXSSFWorkbook对象
//     * @param cols      Excel导出列信息
//     * @param sheetName 工作表名称
//     * @return SXSSFSheet
//     */
//    public static SXSSFSheet createSheet(SXSSFWorkbook workbook, List<ExcelColumnModel> cols,
//                                          String sheetName) {
//
//        // 创建一个sheet对象
//        SXSSFSheet sheet = workbook.createSheet(sheetName);
//
//        // 生成表头
//        SXSSFRow row = sheet.createRow(0);
//
//        ExcelColumnModel column;
//        SXSSFCell cell;
//        XSSFRichTextString text;
//        for (int i = 0; i < cols.size(); i++) {
//            // 获取列信息
//            column = cols.get(i);
//            // 创建单元格
//            cell = row.createCell(i);
//            // 为单元格赋值
//            text = new XSSFRichTextString(column.getName());
//            cell.setCellValue(text);
//            // 设置列宽
//            int width = column.getWidth();
//            if (width > 0) {
//                sheet.setColumnWidth(i, width);
//            }
//        }
//        return sheet;
//    }
//
//    /**
//     * 首字母转大写
//     *
//     * @param word 单词
//     * @return String
//     */
//    public static String upperCaseHead(String word) {
//        char[] chars = word.toCharArray();
//        int j = chars[0] - 32;
//        chars[0] = (char) j;
//        return new String(chars);
//    }
//}
//
