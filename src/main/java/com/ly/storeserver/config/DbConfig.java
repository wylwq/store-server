package com.ly.storeserver.config;

/**
 * @Description:
 * @Author ly
 * @Date 2020/5/17 16:20
 * @Version V1.0.0
 **/
public class DbConfig {

    public static String master = "master";

    public static String slave = "slave";

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setDb(String db) {
        threadLocal.set(db);
    }

    public static String getDb() {
        return threadLocal.get();
    }
}
