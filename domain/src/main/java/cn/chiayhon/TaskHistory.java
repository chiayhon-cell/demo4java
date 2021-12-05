package cn.chiayhon;

import lombok.Data;

/**
 * 封装测试数据
 */
@Data
public class TaskHistory {

    private String vin;

    private String updateStatus;

    private String firmwareId;

    private String failReason;
}