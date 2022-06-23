package cn.chiayhon.excel.bean;

/**
 * 封装excel导出列信息
 */

public class ExcelColumnModel {

    /**
     * 列名
     */
    private String name;

    /**
     * 从数据对象中获取列值时使用的属性名
     */
    private String propertyName;

    /**
     * 列宽度
     */
    private int width;

    public ExcelColumnModel() {
    }

    public ExcelColumnModel(String name, String propertyName, int width) {
        this.name = name;
        this.propertyName = propertyName;
        this.width = width * 512 + 500;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}