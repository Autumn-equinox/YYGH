package gitee.equinox.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gitee.equinox.yygh.model.order.OrderInfo;

public interface OrderService extends IService<OrderInfo> {
    //根据排班id和就诊人id创建订单
    Long saveOrder(String scheduleId, Long patientId);
}
