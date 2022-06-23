package cn.chiayhon.excel.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExcelModel {
    private String name; // 文件名
    private List<ExcelColumnModel> columns; // 列名映射对象

    public ExcelModel buildColumn(){
        this.columns = new ArrayList<>();
        return this;
    }

    public ExcelModel addColumn(String name,String propertyName,int width){
        if (!Objects.isNull(columns)){
            columns.add(new ExcelColumnModel(name, propertyName, width));
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExcelColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<ExcelColumnModel> columns) {
        this.columns = columns;
    }
}
