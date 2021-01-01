package com.ly.storeserver.exception;

import com.ly.storeserver.common.enums.RStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/2 23:44
 * @Version V1.0.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public final class ServiceException extends RuntimeException {

    private Integer code = RStatus.FAIL.getCode();

    private String msg = RStatus.FAIL.getMessage();

    public ServiceException() {
        super("服务异常");
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, RStatus status) {
        super(message);
        this.code = status.getCode();
        this.msg = message;
    }

    public ServiceException(RStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
        this.msg = status.getMessage();
    }
}
