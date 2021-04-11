package gitee.equinox.yygh.user.controller;

import gitee.equinox.yygh.common.result.Result;
import gitee.equinox.yygh.user.service.UserInfoService;
import gitee.equinox.yygh.vo.user.LoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping(value = "/api/user")
@RestController
public class UserInfoApiController {
    @Autowired
    private UserInfoService userInfoService;

    //手机号实现登录
    @ApiOperation(value = "手机号实现登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        //loginVo.setIp(IpUtil.getIpAddr(request));
        Map<String, Object> info = userInfoService.login(loginVo);
        return Result.ok(info);
    }

}
