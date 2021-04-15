package gitee.equinox.yygh.order.service;

import java.util.Map;

public interface WeixinService {
    //下单 生成二维码
    Map createNative(Long orderId);

    //调用微信接口查询支付状态
    Map<String, String> queryPayStatus(Long orderId);

    /***
     * 退款
     * @param orderId
     * @return
     */
    Boolean refund(Long orderId);
}
