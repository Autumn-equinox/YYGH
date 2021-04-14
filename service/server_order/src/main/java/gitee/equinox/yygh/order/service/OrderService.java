package gitee.equinox.yygh.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import gitee.equinox.yygh.model.order.OrderInfo;
import gitee.equinox.yygh.vo.order.OrderQueryVo;

public interface OrderService extends IService<OrderInfo> {
    //根据排班id和就诊人id创建订单
    Long saveOrder(String scheduleId, Long patientId);

    //根据订单id查询订单详情
    OrderInfo getOrder(String orderId);

    //订单列表（条件查询带分页）
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);
}
