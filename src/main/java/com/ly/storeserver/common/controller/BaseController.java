package com.ly.storeserver.common.controller;

import com.ly.storeserver.common.bean.PhoneCode;
import com.ly.storeserver.common.constant.AdminConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author ly
 * @Date 2020/4/3 14:41
 * @Version V1.0.0
 **/
@Slf4j
public class BaseController {

    /**
     * 用来存储用户发送的手机验证码
     */
    private Map<String, PhoneCode> phoneMap = new ConcurrentHashMap<>();

    /**
     * 获取用户手机验证码,有效期是十分钟
     * @param phone
     * @return
     */
    public String getCode(String phone) {
        if (!StringUtils.isBlank(phone)) {
            PhoneCode phoneCode = phoneMap.get(phone);
            if (null != phoneCode) {
                LocalDateTime dateTime = phoneCode.getDateTime();
                String code = phoneCode.getCode();
                LocalDateTime nowDateTime = LocalDateTime.now();
                int timeGap = nowDateTime.getSecond() - dateTime.getSecond();
                if (timeGap <= AdminConstants.CODE_EXPIRES) {
                    if (!checkIsDel(phone)) delCode(phone);
                    return code;
                }
                if (!checkIsDel(phone)) delCode(phone);
                return null;
            }
        }
        return null;
    }

    /**
     * 检查是否要删除用户验证码相关信息
     * @param phone
     * @return
     */
    private boolean checkIsDel(String phone) {
        PhoneCode phoneCode = phoneMap.get(phone);
        if (phoneCode != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime dateTime = phoneCode.getDateTime();
            if (now.getSecond() - dateTime.getSecond() >= AdminConstants.PHONECODE_EXPIRES) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查用户发送验证是否超过发送次数限制
     * @param phone
     * @return
     */
    private boolean checkLimit(String phone) {
        PhoneCode phoneCode = phoneMap.get(phone);
        if (phoneCode != null) {
            Integer limitCount = phoneCode.getLimitCount();
            if (limitCount >= AdminConstants.CODE_LIMIT_HOUR) return false;
            return true;
        }
        return true;
    }

    /**
     * 存储验证码
     * @param phone
     * @param code
     */
    public boolean putCode(String phone, String code) {
        if (!StringUtils.isBlank(phone) && !StringUtils.isBlank(code)) {
            if (!checkLimit(phone)) return false;
            if (!checkIsDel(phone)) delCode(phone);
            PhoneCode phoneCode = phoneMap.get(phone);
            if (phoneCode == null) {
                phoneCode = new PhoneCode();
                phoneCode.setCode(code);
                phoneCode.setDateTime(LocalDateTime.now());
                phoneCode.setLimitCount(0);
                phoneMap.put(phone, phoneCode);
            }
            phoneCode.setCode(code);
            phoneCode.setDateTime(LocalDateTime.now());
            phoneCode.setLimitCount(new AtomicInteger(phoneCode.getLimitCount()).addAndGet(1));
        }
        return true;
    }

    /**
     * 删除用户手机验证码
     * @param phone
     */
    public void delCode(String phone) {
        if (!StringUtils.isBlank(phone)) {
            phoneMap.remove(phone);
        }
    }
}
