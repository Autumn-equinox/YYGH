package gitee.equinox.yygh.msm.service;

import gitee.equinox.yygh.vo.msm.MsmVo;

public interface MsmService {
    //发送手机验证码
    boolean send(String phone, String code);

    //mq发送短信
    boolean send(MsmVo msmVo);
}
