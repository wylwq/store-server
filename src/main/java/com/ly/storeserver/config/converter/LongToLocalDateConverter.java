package com.ly.storeserver.config.converter;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 时间戳转换为LocalDate时间类型转换器
 *
 * @author : wangyu
 * @since :  2020/12/25/025 15:14
 */
public class LongToLocalDateConverter extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext dContext) throws IOException, JsonProcessingException {
        long longValue = jsonParser.getLongValue();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(new Date(longValue).toInstant(), ZoneId.systemDefault());
        return localDateTime;
    }
}
