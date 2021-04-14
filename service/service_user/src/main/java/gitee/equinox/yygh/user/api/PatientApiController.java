package gitee.equinox.yygh.user.api;

import gitee.equinox.yygh.common.result.Result;
import gitee.equinox.yygh.common.utils.AuthContextHolder;
import gitee.equinox.yygh.model.user.Patient;
import gitee.equinox.yygh.user.service.PatientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//就诊人管理接口
@RestController
@RequestMapping(value = "/api/user/patient")
public class PatientApiController {
    @Autowired
    private PatientService patientService;

    //1.获取就诊人列表
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request) {
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.findAllUserId(userId);
        return Result.ok(list);
    }

    //2.添加就诊人
    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient, HttpServletRequest request) {
        //获取当前登录用户id
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    //3.根据id获取就诊人信息
    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable Long id) {
        Patient patient = patientService.getPatientId(id);
        return Result.ok(patient);
    }

    //4.修改就诊人
    @PostMapping("auth/update")
    public Result updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return Result.ok();
    }

    //5.删除就诊人
    @DeleteMapping("auth/remove/{id}")
    public Result removePatient(@PathVariable Long id) {
        patientService.removeById(id);
        return Result.ok();
    }

    //根据就诊人的id获取信息
    @ApiOperation(value = "获取就诊人")
    @GetMapping("inner/get/{id}")
    public Patient getPatientOrder(@PathVariable("id") Long id) {
        return patientService.getPatientId(id);
    }
}
