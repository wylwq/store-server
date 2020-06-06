package com.ly.storeserver.utils;

import java.util.function.Function;

/**
 * @Description:
 * @Author wangy
 * @Date 2020/6/6 14:10
 * @Version V1.0.0
 **/
public class EntityConverter<Source, Target> {

    private Function<Source, Target> function;

    public EntityConverter(Function<Source, Target> function) {
        this.function = function;
    }

    public final Target converter(Source source) {
        if (source == null) {
            return null;
        }
        Target target = function.apply(source);
        return target;
    }
}
