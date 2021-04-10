package gitee.equinox.yygh.hosp.service;

import gitee.equinox.yygh.model.hosp.Hospital;
import gitee.equinox.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {
    void save(Map<String, Object> map);

    Hospital getByHoscode(String hoscode);

    //医院列表(条件查询带分页)
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    //更新医院上线状态
    void updateStatus(String id, Integer status);

    //医院详情
    Map<String, Object> showHospitalDetail(String id);

    //获取医院名称
    String getHospName(String hoscode);
}
