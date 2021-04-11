package gitee.equinox.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "gitee.equinox.yygh.user.mapper")
public class UserConfig {
}
