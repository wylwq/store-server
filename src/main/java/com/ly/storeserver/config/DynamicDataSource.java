package com.ly.storeserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Description:
 * @Author ly
 * @Date 2020/5/17 16:14
 * @Version V1.0.0
 **/
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.info("所使用的的数据源节点是：{}", DbConfig.get());
        return DbConfig.get();
    }
}
