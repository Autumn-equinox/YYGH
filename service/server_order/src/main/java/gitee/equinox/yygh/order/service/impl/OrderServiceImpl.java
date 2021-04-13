package gitee.equinox.yygh.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gitee.equinox.yygh.model.order.OrderInfo;
import gitee.equinox.yygh.order.mapper.OrderInfoMapper;
import gitee.equinox.yygh.order.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderService {
    //根据排班id和就诊人id创建订单
    @Override
    public Long saveOrder(String scheduleId, Long patientId) {
        return null;
    }
}
