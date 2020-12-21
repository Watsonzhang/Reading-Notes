package com.zhang;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @Author: zhangwei
 * @Description:
 * @Date:Create：2020/12/3 下午12:44
 */
public class TestClient {
    public static void main(String[] args) throws IOException {
        final String myUrl="https://bkimg.cdn.bcebos.com/pic/279759ee3d6d55fb22bcda0961224f4a20a4dda3?x-bce-process=image/resize,m_lfit,w_268,limit_1/format,f_jpg";
        URL url = new URL(myUrl);
        InputStream inputStream = url.openStream();
        System.out.println(inputStream);
        byte[] bytes = readInputStream(inputStream);
        java.lang.String s = new java.lang.String(bytes);
        System.out.println(s);

    }
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer=new byte[1024];
        int len=0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len=inputStream.read(buffer))!=-1){
            bos.write(buffer,0,len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
