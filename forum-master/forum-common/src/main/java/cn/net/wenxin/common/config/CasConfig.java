package cn.net.wenxin.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: CasConfig
 * @Description: 项目相关配置
 * @Author forum.wenxin.net.cn
 * @Date 2023/8/23 17:27
 */
@Component
public class CasConfig {

    @Value("${aliyun.accessKeyID:}")
    private String accessKeyID;
    @Value("${aliyun.accessKeySecret:}")
    private String accessKeySecret;
    @Value("${aliyun.signName:}")
    private String signName;
    @Value("${aliyun.templateCode:}")
    private String templateCode;

    @Value("${email.code:}")
    private String code;
    @Value("${email.passWord:}")
    private String passWord;
    @Value("${email.host:}")
    private String host;
    @Value("${email.port:}")
    private String port;

    public String getAccessKeyID() {
        return accessKeyID;
    }

    public void setAccessKeyID(String accessKeyID) {
        this.accessKeyID = accessKeyID;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
