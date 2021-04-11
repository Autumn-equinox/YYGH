package gitee.equinox.yygh.hosp.controller.api;

import gitee.equinox.yygh.common.result.Result;
import gitee.equinox.yygh.hosp.service.DepartmentService;
import gitee.equinox.yygh.hosp.service.HospitalService;
import gitee.equinox.yygh.model.hosp.Hospital;
import gitee.equinox.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {

    @Autowired
    private HospitalService hospitalService;

    //医院分页列表
    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @PathVariable Integer page,
            @PathVariable Integer limit,
            HospitalQueryVo hospitalQueryVo) {
        //显示上线的医院
        //hospitalQueryVo.setStatus(1);
        Page<Hospital> pageModel = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }

    //根据医院名称获取医院列表,模糊查询
    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("findByHosName/{hosname}")
    public Result findByHosName(@PathVariable String hosname) {
        System.out.println(hosname);
        List<Hospital> hospitalList = hospitalService.findByHosName(hosname);
        return Result.ok(hospitalList);
    }

    @Autowired
    private DepartmentService departmentService;

    //获取科室列表
    @ApiOperation(value = "获取科室列表")
    @GetMapping("/department/{hoscode}")
    public Result index(@PathVariable String hoscode) {
        return Result.ok(departmentService.findDeptTree(hoscode));//List<DepartmentVo>
    }

    //医院预约挂号详情
    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("{hoscode}")
    public Result item(@PathVariable String hoscode) {
        Map<String,Object> map = hospitalService.item(hoscode);
        return Result.ok(map);
    }


}

