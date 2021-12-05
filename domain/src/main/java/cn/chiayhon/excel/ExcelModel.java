package cn.chiayhon.excel;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
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


}
