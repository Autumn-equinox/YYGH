package gitee.equinox.yygh.order.service;

import java.util.Map;

public interface WeixinService {
    //下单 生成二维码
    Map createNative(Long orderId);
}
