package gitee.equinox.yygh.hosp.service;

import gitee.equinox.yygh.model.hosp.Schedule;
import gitee.equinox.yygh.vo.hosp.ScheduleOrderVo;
import gitee.equinox.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    //上传排班
    void save(Map<String, Object> map);

    Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    //根据医院编号 和 科室编号 ，查询排班规则数据
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);

    //根据医院编号 、科室编号和工作日期，查询排班详细信息
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    //获取排班可预约日期数据
    Map<String,Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    //1、根据排班id获取排班信息，在页面展示
    Schedule getById(String scheduleId);

    //根据排班id获取预约下单数据
    ScheduleOrderVo getScheduleOrderVo(String scheduleId);

    /**
     * mq修改排班
     */
    void update(Schedule schedule);

}
