package com.ly.storeserver.ext.pay.common.constant;

import lombok.Getter;

/**
 * 供应商编码
 *
 * @author : wangyu
 * @since :  2020/12/31/031 15:04
 */
@Getter
public enum SupplierCode {

    UNION("union", "银联支付")
    ;

    private String code;

    private String name;

    SupplierCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SupplierCode getByCode(String code) {
        SupplierCode[] values = values();
        for (SupplierCode value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
