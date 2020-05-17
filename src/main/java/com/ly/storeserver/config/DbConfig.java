package com.ly.storeserver.config;

import com.ly.storeserver.common.enums.DBTypeEnum;

/**
 * @Description:
 * @Author ly
 * @Date 2020/5/17 16:20
 * @Version V1.0.0
 **/
public class DbConfig {

    private static ThreadLocal<DBTypeEnum> threadLocal = new ThreadLocal<>();

    public static void set(DBTypeEnum dbTypeEnum) {
        threadLocal.set(dbTypeEnum);
    }

    public static DBTypeEnum get() {
        return threadLocal.get();
    }

    public static void master() {
        set(DBTypeEnum.MASTER);
    }

    public static void slave() {
        set(DBTypeEnum.SLAVE);
    }
}
