package cn.chiayhon.poi.excel.sxxsf;

import lombok.Data;

import java.util.List;

@Data
public class ExcelModel {
    private String name; // 文件名
    private List<ExcelColumnModel> columns; // 列名映射对象

}
