package cn.net.wenxin.common.utils.http;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

import cn.net.wenxin.common.constant.Constants;
import cn.net.wenxin.common.utils.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通用http发送方法
 *
 * @author forum.wenxin.net.cn
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url) {
        return sendGet(url, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String header) {
        return sendGet(url, param, header, Constants.UTF8);
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url         发送请求的 URL
     * @param param       请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param contentType 编码类型
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String header, String contentType) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = StringUtils.isNotBlank(param) ? url + "?" + param : url;
            log.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            if (StringUtils.isNotBlank(header)) {
                connection.setRequestProperty("token", header);
            }
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("recv - {}", result);
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendGet ConnectException, url={}, param={}", url, param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, String header) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            log.info("sendPost - {}", url);
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            if (StringUtils.isNotBlank(header)) {
                conn.setRequestProperty("token", header);
                conn.setRequestProperty("Authorization", header);
            }
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("recv - {}", result);
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendPost ConnectException, url={}, param={}", url, param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendPost IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    public static String sendSSLPost(String url, String param) {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try {
            log.info("sendSSLPost - {}", urlNameString);
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            try (InputStream is = conn.getInputStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String ret;
                while ((ret = br.readLine()) != null) {
                    if (ret != null && !ret.trim().isEmpty()) {
                        result.append(new String(ret.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
                    }
                }
            }
            log.info("recv - {}", result);
            conn.disconnect();
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendSSLPost ConnectException, url={}, param={}", url, param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url={}, param={}", url, param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendSSLPost IOException, url={}, param={}", url, param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendSSLPost Exception, url={}, param={}", url, param, e);
        }
        return result.toString();
    }

    /**
     * 原生字符串发送put请求
     *
     * @param url
     * @param token
     * @param jsonStr
     * @return
     * @throws
     * @throws IOException
     */
    public static String sendHttpPut(String url, String jsonStr, String token) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPut httpPut = new HttpPut(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(35000).setConnectionRequestTimeout(35000)
                    .setSocketTimeout(60000).build();
            httpPut.setConfig(requestConfig);
            httpPut.setHeader("Content-type", "application/json");
            httpPut.setHeader("DataEncoding", "UTF-8");
            httpPut.setHeader("token", token);
            httpPut.setEntity(new StringEntity(jsonStr));
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpPut)) {
                HttpEntity entity = httpResponse.getEntity();
                return EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            log.error("sendHttpPut 请求失败, url={}", url, e);
        }
        return null;
    }

    public static String sendDelete(String url, String token) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete delete = new HttpDelete(url);
            if (StringUtils.isNotEmpty(token)) {
                delete.addHeader("token", token);
            }
            try (CloseableHttpResponse response = client.execute(delete)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            }
        } catch (Exception e) {
            log.error("sendDelete 请求失败, url={}", url, e);
        }
        return null;
    }
}