package com.ly.storeserver.example;

import com.ly.storeserver.admin.models.entity.Store;
import com.ly.storeserver.admin.models.entity.Sysuser;
import com.ly.storeserver.utils.EntityConverter;


/**
 * @Description:
 * @Author wangy
 * @Date 2020/6/6 14:31
 * @Version V1.0.0
 **/
public class ConverterTest extends EntityConverter<Sysuser, Store> {

    public ConverterTest() {
        super(ConverterTest::conver);
    }

    private static Store conver(Sysuser sysuser) {
        Store store = new Store();
        System.out.println("111");
        return store;
    }
}
