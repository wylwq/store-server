package com.ly.storeserver.ext.open.request;

import lombok.Data;

import java.util.List;

/**
 * 同步员工信息
 *
 * @author : wangyu
 * @since :  2020/12/25/025 15:27
 */
@Data
public class SyncEmployRequest {

    /**
     * 员工信息列表
     */
    List<EmployeeInfo> employeeInfoList;


    @Data
    public static class EmployeeInfo {

        private String period;

        private String employeeNumber;

        private String employeeName;

        private String sex;

        private String phone;

        private String email;

        private String enabledFlag;

        private String birthday;

    }

}
