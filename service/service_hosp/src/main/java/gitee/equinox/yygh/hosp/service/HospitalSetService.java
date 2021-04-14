package gitee.equinox.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gitee.equinox.yygh.model.hosp.HospitalSet;
import gitee.equinox.yygh.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);//实体类

    //获取医院签名信息
    SignInfoVo getSignInfoVo(String hoscode);
}
