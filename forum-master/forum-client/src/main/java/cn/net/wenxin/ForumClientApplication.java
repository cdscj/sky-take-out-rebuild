package cn.net.wenxin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author forum.wenxin.net.cn
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ForumClientApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(ForumClientApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  客户服务启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
