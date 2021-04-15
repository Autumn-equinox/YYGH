package gitee.equinox.yygh.task.scheduled;

import gitee.equinox.common.rabbit.constant.MqConst;
import gitee.equinox.common.rabbit.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling//开启定时任务
public class ScheduledTask {

    @Autowired
    private RabbitService rabbitService;

    //每天八点执行方法，就医提醒
    //cron表达式，设置执行间隔
    //@Scheduled(cron = "0 0 8 * * ?")//每天八点
    @Scheduled(cron = "0/10 * * * * ?")//10一次，测试方便
    public void task1() {
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_8, "");
    }
}


