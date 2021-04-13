package gitee.equinox.yygh.order.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "gitee.equinox.yygh.order.mapper")
public class OrderConfig {
}
