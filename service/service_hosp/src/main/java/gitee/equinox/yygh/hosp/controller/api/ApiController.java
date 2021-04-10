package gitee.equinox.yygh.hosp.controller.api;

import com.baomidou.mybatisplus.extension.api.R;
import gitee.equinox.yygh.common.exception.YyghException;
import gitee.equinox.yygh.common.helper.HttpRequestHelper;
import gitee.equinox.yygh.common.result.Result;
import gitee.equinox.yygh.common.result.ResultCodeEnum;
import gitee.equinox.yygh.common.utils.MD5;
import gitee.equinox.yygh.hosp.service.DepartmentService;
import gitee.equinox.yygh.hosp.service.HospitalService;
import gitee.equinox.yygh.hosp.service.HospitalSetService;
import gitee.equinox.yygh.hosp.service.ScheduleService;
import gitee.equinox.yygh.model.hosp.Department;
import gitee.equinox.yygh.model.hosp.Hospital;
import gitee.equinox.yygh.model.hosp.Schedule;
import gitee.equinox.yygh.vo.hosp.DepartmentQueryVo;
import gitee.equinox.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    //查询医院
    @ApiOperation(value = "查询医院")
    @PostMapping("/hospital/show")
    public Result hospital(HttpServletRequest request) {
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //获取医院编号
        String hoscode = (String) map.get("hoscode");

        //签名校验
        //1.获取医院系统传过来的签名，签名进行md5加密
        String hospSign = (String) map.get("sign");
        //System.out.println(hospSign);
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把查出来的key进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        System.out.println(encrypt);
        //4.判断是否一致
        if (!hospSign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);//签名错误
        }

        //调用方法，根据编号查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }


    //上传医院接口
    @PostMapping(value = "/saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        //获取传过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        //数组转换位object
        Map<String, Object> map = HttpRequestHelper.switchMap(requestMap);

        //签名校验
        //1.获取医院系统传过来的签名，签名进行md5加密
        String hospSign = (String) map.get("sign");
        System.out.println(hospSign);
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String hoscode = (String) map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把查出来的key进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        System.out.println(encrypt);
        //4.判断是否一致
        if (!hospSign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);//签名错误
        }

        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoData = (String) map.get("logoData");
        logoData = logoData.replaceAll(" ", "+");
        map.put("logoData", logoData);

        //调用service方法
        hospitalService.save(map);
        return Result.ok();
    }


    //上传科室接口
    @PostMapping(value = "/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        //获取传递过来的科室信息
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());

        //签名校验
        //1.获取医院系统传过来的签名，签名进行md5加密
        String hospSign = (String) map.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String hoscode = (String) map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把查出来的key进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        //4.判断是否一致
        if (!hospSign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);//签名错误
        }

        //调用service方法
        departmentService.save(map);
        return Result.ok();
    }

    //查询科室接口
    @PostMapping(value = "/department/list")
    public Result findDepartment(HttpServletRequest request){
        //获取传递过来的科室信息
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //医院编号
        String hoscode = (String) map.get("hoscode");
        //当前页，每页记录数
        int page = StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String)map.get("page"));
        int limit = StringUtils.isEmpty(map.get("limit")) ? 10 : Integer.parseInt((String)map.get("limit"));

        //签名校验
        //1.获取医院系统传过来的签名，签名进行md5加密
        String hospSign = (String) map.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把查出来的key进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        //4.判断是否一致
        if (!hospSign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);//签名错误
        }

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        Page<Department> pageModel = departmentService.selectPage(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }

    //删除科室接口
    @PostMapping(value = "/department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //获取传递过来的科室信息
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //医院编号与科室编号
        String hoscode = (String) map.get("hoscode");
        String depcode = (String) map.get("depcode");

        //签名校验
        //1.获取医院系统传过来的签名，签名进行md5加密
        String hospSign = (String) map.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把查出来的key进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        //4.判断是否一致
        if (!hospSign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);//签名错误
        }

        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }

    //上传排班
    @PostMapping(value = "/saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        //获取传递过来的科室信息
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());

        //签名校验
        //1.获取医院系统传过来的签名，签名进行md5加密
        String hospSign = (String) map.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String hoscode = (String) map.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把查出来的key进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        //4.判断是否一致
        if (!hospSign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);//签名错误
        }

        scheduleService.save(map);
        return Result.ok();
    }

    //查询排版
    @PostMapping(value = "/schedule/list")
    public Result findSchedule(HttpServletRequest request){
        //获取传递过来的科室信息
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //医院编号
        String hoscode = (String) map.get("hoscode");
        //科室编号
        String depcode = (String) map.get("depcode");

        //当前页，每页记录数
        int page = StringUtils.isEmpty(map.get("page")) ? 1 : Integer.parseInt((String)map.get("page"));
        int limit = StringUtils.isEmpty(map.get("limit")) ? 10 : Integer.parseInt((String)map.get("limit"));

        //签名校验
        //1.获取医院系统传过来的签名，签名进行md5加密
        String hospSign = (String) map.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把查出来的key进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        //4.判断是否一致
        if (!hospSign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);//签名错误
        }

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel = scheduleService.selectPage(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }

    //删除排版接口
    @PostMapping(value = "/schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        //获取传递过来的科室信息
        Map<String, Object> map = HttpRequestHelper.switchMap(request.getParameterMap());
        //医院编号与排版编号
        String hoscode = (String) map.get("hoscode");
        String hosScheduleId = (String) map.get("hosScheduleId");

        //签名校验
        //1.获取医院系统传过来的签名，签名进行md5加密
        String hospSign = (String) map.get("sign");
        //2.根据传递过来的医院编码，查询数据库，查询签名
        String signKey = hospitalSetService.getSignKey(hoscode);
        //3.把查出来的key进行MD5加密
        String encrypt = MD5.encrypt(signKey);
        //4.判断是否一致
        if (!hospSign.equals(encrypt)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);//签名错误
        }

        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }
}
