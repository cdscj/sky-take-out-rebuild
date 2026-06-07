package cn.net.wenxin.common.utils.http;

import cn.net.wenxin.common.utils.StringUtils;
import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTPS 工具类
 * 使用默认 JVM SSL 信任库进行证书验证（生产环境标准行为）
 */
public class HttpsUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpsUtils.class);

    /**
     * 创建标准 SSL 客户端 — 使用 JVM 默认信任库验证证书。
     */
    public static CloseableHttpClient createSSLClientDefault() {
        return HttpClients.createDefault();
    }

    /**
     * 发送 https POST 请求 (form-urlencoded)
     */
    public static String sendPostByHttps2(Map<String, Object> params, String url) {
        try (CloseableHttpClient httpClient = createSSLClientDefault()) {
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> listNVP = new ArrayList<>();
            if (params != null) {
                for (String key : params.keySet()) {
                    listNVP.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(listNVP, "UTF-8");
            logger.info("创建请求httpPost-URL={},params={}", url, listNVP);
            httpPost.setEntity(entity);
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    return EntityUtils.toString(httpEntity, "UTF-8");
                }
            }
        } catch (Exception e) {
            logger.error("sendPostByHttps2 请求失败, url={}", url, e);
        }
        return null;
    }

    /**
     * 发送 https GET 请求
     */
    public static String sendGetByHttps(String url) {
        try (CloseableHttpClient httpClient = createSSLClientDefault()) {
            HttpGet httpGet = new HttpGet(url);
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    return EntityUtils.toString(httpEntity, "UTF-8");
                }
            }
        } catch (Exception e) {
            logger.error("sendGetByHttps 请求失败, url={}", url, e);
        }
        return null;
    }

    /**
     * 发送 https POST 请求 (JSON body, with Bearer header)
     */
    public static String sendPostByHttps(Map<String, Object> params, String url, String header) {
        try (CloseableHttpClient httpClient = createSSLClientDefault()) {
            HttpPost httpPost = new HttpPost(url);
            JSONObject jsonObject = new JSONObject();
            if (params != null) {
                for (Map.Entry<String, Object> mapEntry : params.entrySet()) {
                    jsonObject.put(mapEntry.getKey(), mapEntry.getValue());
                }
            }
            logger.info("创建请求httpPost-URL={},params={}", url, jsonObject.toJSONString());
            httpPost.setHeader("Content-Type", "application/json");
            if (StringUtils.isNotEmpty(header)) {
                httpPost.setHeader("Authorization", "Bearer " + header);
            }
            httpPost.setEntity(new StringEntity(jsonObject.toString()));
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    return EntityUtils.toString(httpEntity, "UTF-8");
                }
            }
        } catch (Exception e) {
            logger.error("sendPostByHttps 请求失败, url={}", url, e);
        }
        return null;
    }

}
