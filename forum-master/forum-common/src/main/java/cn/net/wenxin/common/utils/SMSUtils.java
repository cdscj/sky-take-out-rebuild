package cn.net.wenxin.common.utils;

import cn.net.wenxin.common.config.CasConfig;
import cn.net.wenxin.common.constant.Constants;
import com.alibaba.fastjson2.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName: SMSUtils
 * @Description: 阿里云短信服务工具
 * @Author forum.wenxin.net.cn
 * @Date 2023/8/23 17:19
 */
@Component
public class SMSUtils {
    private static final Logger log = LoggerFactory.getLogger(SMSUtils.class);

    private static CasConfig casConfig;

    @Autowired
    public SMSUtils(CasConfig casConfig) {
        SMSUtils.casConfig = casConfig;
    }

        /**
         * 对接阿里云发送短信验证码
         * @param phone 手机号码
         * @return 验证码
         */
    public static String SMSSend(String phone) {
        //CasConfig casConfig = new CasConfig();
        // 设置短信API的访问配置
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", casConfig.getAccessKeyID(), casConfig.getAccessKeySecret());
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
            DefaultAcsClient client = new DefaultAcsClient(profile);

            // 构造请求对象
            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(phone);
            request.setSignName(casConfig.getSignName());
            request.setTemplateCode(casConfig.getTemplateCode());
            Map<String,String> pmap = new HashMap<>();
            String code = RandomUtils.generateNumber(6);
            pmap.put("code",code);
            request.setTemplateParam(JSON.toJSONString(pmap));
            // 发送请求
            SendSmsResponse response = client.getAcsResponse(request);
            if ((response.getCode() != null) && (response.getCode().equals("OK"))) {
                log.info("发送成功,code:" + response.getCode());
                return code;
            } else {
                log.info("发送失败,code:" + response.getCode());
                return null;
            }
            // 打印请求结果
        } catch (Exception e) {
            log.error("发送失败,系统错误！", e);
            return null;
        }
    }

    /**
     * 发送邮件
     * @param email 目的邮箱
     * @return 验证码
     */
    public static String sendMail(String email){
       // CasConfig casConfig = new CasConfig();
        try {
            String title = "系统验证码";
            String code = RandomUtils.generateNumber(6);
            String content = Constants.EMAIL_CONTENT.replace("code",code);
            //content.replaceAll("code",code);
            Properties properties = System.getProperties();
            // 连接协议
            properties.put("mail.transport.protocol", "smtp");
            // 主机名
            properties.put("mail.smtp.host", casConfig.getHost());
            // 端口号
            properties.put("mail.smtp.port", casConfig.getPort());
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.socketFactory", sf);
            properties.put("mail.smtp.localhost", "localhost");
            properties.put("mail.smtp.auth", "true");
            // 设置是否使用ssl安全连接 ---一般都使用
            properties.put("mail.smtp.ssl.enable", "true");
            // 设置是否显示debug信息 true 会在控制台显示相关信息
            properties.put("mail.debug", "true");
            properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
            // 得到回话对象
            Authenticator auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(casConfig.getCode(), casConfig.getPassWord());
                }
            };
            Session session = Session.getInstance(properties,auth);
            // 获取邮件对象
            Message message = new MimeMessage(session);
            // 设置发件人邮箱地址
            message.setFrom(new InternetAddress(casConfig.getCode()));
            // 设置收件人邮箱地址   一个收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // 设置邮件标题
            message.setSubject(title);
            // 设置邮件内容
            //message.setText(content);
            message.setContent(content,"text/html;charset=UTF-8");
            //抄送给发件人
            message.addRecipients(MimeMessage.RecipientType.CC,InternetAddress.parse(casConfig.getCode()));
            // 得到邮差对象
            Transport transport = session.getTransport();
            // 连接自己的邮箱账户
            // 密码为邮箱开通的stmp服务后得到的客户端授权码
            transport.connect(casConfig.getCode(), casConfig.getPassWord());
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return code;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
