package gitee.equinox.yygh.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gitee.equinox.yygh.model.hosp.HospitalSet;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);//实体类
}
