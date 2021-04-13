package gitee.equinox.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import gitee.equinox.yygh.model.user.UserInfo;
import gitee.equinox.yygh.vo.user.LoginVo;
import gitee.equinox.yygh.vo.user.UserAuthVo;
import gitee.equinox.yygh.vo.user.UserInfoQueryVo;

import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {
    //手机号实现登录
    Map<String, Object> login(LoginVo loginVo);

    //根据openid判断，数据库中是否已经存在微信扫描人的信息
    UserInfo selectWxInfoOpenId(String openid);

    //用户认证接口
    void userAuth(Long userId, UserAuthVo userAuthVo);

    //用户列表（条件查询带分页）
    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    //用户锁定
    void lock(Long userId, Integer status);

    //用户详情
    Map<String, Object> show(Long userId);

    //认证审批
    void approval(Long userId, Integer authStatus);
}
