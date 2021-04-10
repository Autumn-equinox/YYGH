package gitee.equinox.yygh.hosp.service;

import gitee.equinox.yygh.model.hosp.Department;
import gitee.equinox.yygh.vo.hosp.DepartmentQueryVo;
import gitee.equinox.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    void save(Map<String, Object> map);

    //查询科室接口
    Page<Department> selectPage(int page, int limit, DepartmentQueryVo departmentQueryVo);

    void remove(String hoscode, String depcode);

    //根据医院编号，查询医院所有科室列表
    List<DepartmentVo> findDeptTree(String hoscode);

    //获取科室名称
    String getDepName(String hoscode, String depcode);
}
