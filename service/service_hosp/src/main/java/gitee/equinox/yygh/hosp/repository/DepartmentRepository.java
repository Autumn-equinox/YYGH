package gitee.equinox.yygh.hosp.repository;

import gitee.equinox.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {
    //根据医院编号，与科室编号查询
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
