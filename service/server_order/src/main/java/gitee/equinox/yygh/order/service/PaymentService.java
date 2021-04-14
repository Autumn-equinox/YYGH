package gitee.equinox.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gitee.equinox.yygh.model.order.OrderInfo;
import gitee.equinox.yygh.model.order.PaymentInfo;

public interface PaymentService extends IService<PaymentInfo> {
    //向支付记录表添加数据
    void savePaymentInfo(OrderInfo order, Integer status);
}
