package gitee.equinox.yygh.hosp.repository;

import gitee.equinox.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    //Spring Data提供了对mongodb数据访问的支持，我们只需要继承MongoRepository类，按照Spring Data规范就可以了
    Hospital getHospitalByHoscode(String hoscode);

    //根据医院名称获取医院列表,模糊查询
    List<Hospital> findHospitalByHosnameLike(String hosname);
}
