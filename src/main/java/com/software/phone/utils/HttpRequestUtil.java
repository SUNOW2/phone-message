package com.software.phone.utils;

import com.software.phone.po.LoginPo;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 徐旭
 * @data 2018/6/6 15:45
 */
@Component
public class HttpRequestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestUtil.class);

    @Value("${dingdong.apikey}")
    private String apikey;

    /**
     * 描述：向短信平台发送
     * @param url
     * @param loginPo
     * @return
     */
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

    /**
     * 描述：获取客户端的ip
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        return IPUtil.getIp(request);
    }

    /**
     * 描述：获取请求路径
     * @param request
     * @return
     */
    public static String getRestURL(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * 描述：
     * @param request
     * @return
     */
    public static String getRestPatternURL(HttpServletRequest request) {
        return request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
    }

    /**
     * Convenience method to set a cookie
     *
     * @param response the current response
     * @param name     the name of the cookie
     * @param value    the value of the cookie
     * @param path     the path to set it on
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String path) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting cookie '" + name + "' on path '" + path + "'");
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(false);
        cookie.setPath(path);
        // 30 days
        cookie.setMaxAge(AppConstantsUtil.NUM60 * AppConstantsUtil.NUM60 * AppConstantsUtil.NUM24 * AppConstantsUtil.NUM30);

        response.addCookie(cookie);
    }

    /**
     * Convenience method to set a cookie
     *
     * @param response the current response
     * @param name     the name of the cookie
     * @param value    the value of the cookie
     * @param path     the path to set it on
     */
    public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Setting cookie '" + name + "' on path '" + path + "'");
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(false);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    /**
     * Convenience method to get a cookie by name
     *
     * @param request the current request
     * @param name    the name of the cookie to find
     * @return the cookie (if found), null if not found
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        Cookie returnCookie = null;

        if (cookies == null) {
            return returnCookie;
        }

        for (final Cookie thisCookie : cookies) {
            if (thisCookie.getName().equals(name) && !"".equals(thisCookie.getValue())) {
                returnCookie = thisCookie;
                break;
            }
        }

        return returnCookie;
    }

    /**
     * Convenience method for deleting a cookie by name
     *
     * @param response the current web response
     * @param cookie   the cookie to delete
     * @param path     the path on which the cookie was set (i.e. /appfuse)
     */
    public static void deleteCookie(HttpServletResponse response, Cookie cookie, String path) {
        if (cookie != null) {
            // Delete the cookie by setting its maximum age to zero
            cookie.setMaxAge(0);
            cookie.setPath(path);
            response.addCookie(cookie);
        }
    }

    /**
     *
     * 验证是否是ajax请求
     * 常用的jquery之类的框架，ajax使用都会带上header [X-Requested-With=XMLHttpRequest]
     *
     * @author 彭文哲[OF789]
     * @date 2013-10-15 下午3:08:04
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return true;
        }
        return false;
    }

    /**
     * Convenience method to get the application's URL based on request
     * variables.
     *
     * @param request the current request
     * @return URL to application
     */
    public static String getAppURL(HttpServletRequest request) {
        StringBuffer url = new StringBuffer();
        int port = request.getServerPort();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        String scheme = request.getScheme();
        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(request.getContextPath());
        return url.toString();
    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    public static String getRemoteIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Real-IP");

        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip.split(",")[0];
    }
}
