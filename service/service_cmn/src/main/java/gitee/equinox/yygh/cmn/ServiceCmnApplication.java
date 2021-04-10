package gitee.equinox.yygh.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "gitee.equinox")//重设扫描规则，扫面swagger配置类
@EnableDiscoveryClient//启动service-hosp服务，在Nacos管理界面的服务列表中可以看到注册的服务
public class ServiceCmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnApplication.class,args);
    }
}
