package com.software.phone.utils;

import com.software.phone.po.LoginPo;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 徐旭
 * @data 2018/6/6 15:45
 */
@Component
public class HttpRequestUtil {

    @Value("${dingdong.apikey}")
    private String apikey;

    public String postRequest(String url, LoginPo loginPo) {
//        构造HttpClient实例
        HttpClient httpClient = new HttpClient();

//        构造POST方法的实例
        PostMethod postMethod = new PostMethod(url);

//        设置请求头信息
        postMethod.setRequestHeader("Connection", "false");

//        添加参数
        postMethod.addParameter("apikey", apikey);
        postMethod.addParameter("mobile", loginPo.getPhone());
        postMethod.addParameter("content", loginPo.getContent());

//        使用系统提供的默认的恢复策略,设置请求重试处理，用的是默认的重试处理：请求三次
        httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
        String result = null;
        try {
            httpClient.executeMethod(postMethod);

            result = postMethod.getResponseBodyAsString();
        } catch (IOException e) {
            System.out.println("请检查url");
            e.printStackTrace();
        } finally {
//            释放链接
            postMethod.releaseConnection();

//            关闭HttpClient实例
            if(httpClient != null) {
                ((SimpleHttpConnectionManager)httpClient.getHttpConnectionManager()).shutdown();
                httpClient = null;
            }
        }
        return result;
    }

    private static String doPost(String url, NameValuePair[] data) {

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.setRequestBody(data);
        client.getParams().setConnectionManagerTimeout(10000);
        try {
            client.executeMethod(method);
            return method.getResponseBodyAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
