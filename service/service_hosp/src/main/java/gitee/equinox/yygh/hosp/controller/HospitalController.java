package gitee.equinox.yygh.hosp.controller;

import com.baomidou.mybatisplus.extension.api.R;
import gitee.equinox.yygh.common.result.Result;
import gitee.equinox.yygh.hosp.service.HospitalService;
import gitee.equinox.yygh.model.hosp.Hospital;
import gitee.equinox.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@CrossOrigin
@RestController
@RequestMapping(value = "/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    //医院列表(条件查询带分页)
    @GetMapping(value = "/list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageModel = hospitalService.selectHospPage(page,limit,hospitalQueryVo);
        return Result.ok(pageModel);
    }

    //更新医院上线状态
    @ApiOperation(value = "更新上线状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable String id,@PathVariable Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    //医院详情
    @ApiOperation(value = "医院详情")
    @GetMapping(value = "/showHospitalDetail/{id}")
    public Result showHospitalDetail(@PathVariable String id){
        Map<String, Object> map = hospitalService.showHospitalDetail(id);
        return Result.ok(map);
    }

}
