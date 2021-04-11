package gitee.equinox.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import gitee.equinox.yygh.cmn.client.DictFeignClient;
import gitee.equinox.yygh.hosp.repository.HospitalRepository;
import gitee.equinox.yygh.hosp.service.HospitalService;
import gitee.equinox.yygh.model.hosp.Hospital;
import gitee.equinox.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> map) {
        //1.把参数map集合转换为对象hospital
        String jsonString = JSONObject.toJSONString(map);//map -- string
        Hospital hospital = JSONObject.parseObject(jsonString, Hospital.class);//string--obj
        //2.判断对象是否在数据库中存在
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);
        //3.如果存在，进行修改
        if (hospitalExist != null) {
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
            //4.如果不存在，进行添加
            //0：未上线 1：已上线
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);

        }
    }

    //删除
    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    //医院列表(条件查询带分页)
    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        //创建pageable对象
        Pageable pageable = PageRequest.of(page - 1,limit);
        //创建条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase(true);
        //hospitalQueryVo==>hospital
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        //创建example对象
        Example<Hospital> example = Example.of(hospital,exampleMatcher);
        //调用方法
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        //  feign
        pages.getContent().stream().forEach(item -> {
            this.setHospitalHosType(item);
        });

        return pages;
    }

    //更新医院上线状态
    @Override
    public void updateStatus(String id, Integer status) {
        //查询
        Hospital hospital = hospitalRepository.findById(id).get();
        //设置
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    //医院详情
    @Override
    public Map<String, Object> showHospitalDetail(String id) {
        Map<String, Object> result = new HashMap<>();

        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
        //医院的基本信息（包含等级）
        result.put("hospital", hospital);
        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());//预约规则
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    //获取医院名称
    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if (hospital != null) {
            return hospital.getHosname();
        }
        return null;
    }

    //根据医院名称获取医院列表,模糊查询
    @Override
    public List<Hospital> findByHosName(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    //医院预约挂号详情
    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.setHospitalHosType(this.getByHoscode(hoscode));
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    //获取查询list集合，遍历进行医院等级的封装
    private Hospital setHospitalHosType(Hospital hospital) {
        //根据dictcode和value查询医院的等级名称
        String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
        //查询 省 市 地区
        String provinceString =dictFeignClient.getName(hospital.getProvinceCode());
        String cityString = dictFeignClient.getName(hospital.getCityCode());
        String districtString = dictFeignClient.getName(hospital.getDistrictCode());
        //放入param中
        hospital.getParam().put("fullAddress",provinceString+cityString+districtString);
        hospital.getParam().put("hostypeString",hostypeString);
        return hospital;
    }
}
