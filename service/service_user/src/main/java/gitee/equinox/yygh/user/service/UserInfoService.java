package gitee.equinox.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gitee.equinox.yygh.model.user.UserInfo;
import gitee.equinox.yygh.vo.user.LoginVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    //手机号实现登录
    Map<String, Object> login(LoginVo loginVo);
}
