package gitee.equinox.yygh.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import gitee.equinox.yygh.model.order.OrderInfo;
import gitee.equinox.yygh.model.order.PaymentInfo;

import java.util.Map;

public interface PaymentService extends IService<PaymentInfo> {
    //向支付记录表添加数据
    void savePaymentInfo(OrderInfo order, Integer status);

    //更改订单状态，处理支付结果
    void paySuccess(String out_trade_no, Map<String, String> resultMap);

    /**
     * 获取支付记录
     * @param orderId
     * @param paymentType
     * @return
     */
    PaymentInfo getPaymentInfo(Long orderId, Integer paymentType);

}
