package com.ly.storeserver.utils;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.io.File;
import java.lang.reflect.Method;

/**
 * ip归属地解析工具
 * 项目地址 https://github.com/lionsoul2014/ip2region
 *
 * @author : wangyu
 * @since :  2021/1/11/011 18:56
 */
public class IPParseUtil {

    public static String parseIP(String ip){

        //db
        String dbPath = IPParseUtil.class.getResource("/ip2region.db").getPath();

        File file = new File(dbPath);
        if ( file.exists() == false ) {
            System.out.println("Error: Invalid ip2region.db file");
        }

        //查询算法 //B-tree
        int algorithm = DbSearcher.BTREE_ALGORITHM;
        //DbSearcher.BINARY_ALGORITHM //Binary
        //DbSearcher.MEMORY_ALGORITYM //Memory
        try {
            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, dbPath);

            //define the method
            Method method = null;
            switch ( algorithm )
            {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
            }

            DataBlock dataBlock = null;
            if ( Util.isIpAddress(ip) == false ) {
                System.out.println("Error: Invalid ip address");
            }

            dataBlock  = (DataBlock) method.invoke(searcher, ip);
            String IP = dataBlock.getRegion();
            StringBuilder sb = new StringBuilder(IP);
            sb.replace(IP.indexOf("|")+1,(IP.indexOf("|",IP.indexOf("|")+1)),"-");

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
