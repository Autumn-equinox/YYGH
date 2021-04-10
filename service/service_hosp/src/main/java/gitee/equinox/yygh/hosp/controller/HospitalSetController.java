package gitee.equinox.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gitee.equinox.yygh.common.exception.YyghException;
import gitee.equinox.yygh.common.result.Result;
import gitee.equinox.yygh.common.utils.MD5;
import gitee.equinox.yygh.hosp.service.HospitalSetService;
import gitee.equinox.yygh.model.hosp.HospitalSet;
import gitee.equinox.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

//@CrossOrigin//解决跨域访问问题
@Api(tags = "医院设置管理")
@RestController
@RequestMapping(value = "/admin/hosp/hospitalSet")
public class HospitalSetController {
    //注入service
    @Autowired
    private HospitalSetService hospitalSetService;

    //查询所有信息  http://localhost:8201/admin/hosp/hospitalSet/findAll
    @GetMapping(value = "/findAll")
    @ApiOperation(value = "获取所有医院设置")
    public Result findAllHospitalSet() {
        //调用方法
        List<HospitalSet> list = hospitalSetService.list();
        //System.out.println(list);//返回json
        Result<List<HospitalSet>> ok = Result.ok(list);
        return ok;
    }

    //逻辑删除
    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "逻辑删除医院设置")
    public Result del(@PathVariable(value = "id") Long id) {
        boolean flag = hospitalSetService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //条件查询带分页
    @ApiOperation(value = "条件查询带分页")
    //@RequestMapping(value = "/findPageHospSet/{current}/{limit}")
    @PostMapping(value = "/findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current,
                                  @PathVariable long limit,//@RequestBody(required = false)表示以json形式传数据，此时不能使用@RequestMapping
                                  @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        //创建page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);
        //条件查询
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        //构建条件
        String hosname = hospitalSetQueryVo.getHosname();//医院名称
        String hoscode = hospitalSetQueryVo.getHoscode();//医院编号
        if (!StringUtils.isEmpty(hosname)) {
            queryWrapper.like("hosname", hospitalSetQueryVo.getHosname());//模糊查
        }
        if (!StringUtils.isEmpty(hoscode)) {
            queryWrapper.eq("hoscode", hospitalSetQueryVo.getHoscode());//等
        }
        //调用方法实现分页
        Page<HospitalSet> pageHospSet = hospitalSetService.page(page, queryWrapper);//传入的是page对象。以及条件查询对象
        //返回结果
        return Result.ok(pageHospSet);
    }

    //添加设置
    @PostMapping(value = "/saveHospitalSet")
    @ApiOperation(value = "添加医院设置")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //设置状态--0不可用
        hospitalSet.setStatus(1);
        //签名密钥--MD5算法
        Random random = new Random();//随机数
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        //调用service
        boolean save = hospitalSetService.save(hospitalSet);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //根据id获取医院设置
    @GetMapping(value = "/getHospitalSet/{id}")
    @ApiOperation(value = "根据id获取医院设置")
    public Result getHospitalSet(@PathVariable(value = "id") Long id) {
        //模拟异常---全局异常
        //int i = 10/0;
//        try{
//            //模拟异常---自定义异常
//            int i = 10/0;
//        }catch (Exception e){
//            throw new YyghException("程序异常了！",201);
//        }
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }

    //修改医院设置
    @PostMapping(value = "/updateHospitalSet")
    @ApiOperation(value = "修改医院设置")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //批量删除医院设置
    @DeleteMapping("/batchRemove")
    @ApiOperation(value = "批量删除医院设置")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }

    //医院设置锁定和解锁
    @PutMapping("/lockHospitalSet/{id}/{status}")
    @ApiOperation(value = "医院设置锁定和解锁")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    //发送签名密钥
    @PutMapping("/sendKey/{id}")
    @ApiOperation(value = "发送签名密钥")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }
}
