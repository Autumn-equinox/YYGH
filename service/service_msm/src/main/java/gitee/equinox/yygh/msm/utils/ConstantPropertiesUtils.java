package gitee.equinox.yygh.msm.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 短信配置类
 *
 * 为了获取配置文件中的内容
 */
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    //为了获取配置文件中的内容
    @Value("${aliyun.sms.regionId}")
    private String regionId;

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.secret}")
    private String secret;

    public static String REGION_Id;
    public static String ACCESS_KEY_ID;
    public static String SECRECT;

    @Override
    public void afterPropertiesSet() throws Exception {
        //赋值
        REGION_Id=regionId;
        ACCESS_KEY_ID=accessKeyId;
        SECRECT=secret;
    }
}

