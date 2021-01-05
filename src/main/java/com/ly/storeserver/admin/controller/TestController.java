package com.ly.storeserver.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 用来测试使用的controller,有关学习测试的代码的放在此处
 *
 * @author : wangyu
 * @since :  2021/1/4/004 16:15
 */
@RestController
@RequestMapping("/test/")
@Api(value = "测试相关API", tags = "测试相关API")
public class TestController {

    @PostMapping("test-zip")
    @ApiOperation(value = "下载zip压缩包", notes = "下载zip压缩包接口")
    public void testZip(HttpServletResponse response) throws IOException {
        StringWriter sw = new StringWriter();
        sw.write("hello world");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        zipOutputStream.putNextEntry(new ZipEntry("test.txt"));
        IOUtils.write(sw.toString(), zipOutputStream, "UTF-8");
        //读取完成流一定要记得关闭，否则会有bug
        IOUtils.closeQuietly(sw);
        zipOutputStream.closeEntry();
        IOUtils.closeQuietly(zipOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"test.zip\"");
        response.addHeader("Content-Length", "" + bytes.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(bytes, response.getOutputStream());
    }
}
