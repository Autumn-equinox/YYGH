package gitee.equinox.yygh.order.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import gitee.equinox.yygh.enums.PaymentTypeEnum;
import gitee.equinox.yygh.model.order.OrderInfo;
import gitee.equinox.yygh.order.service.OrderService;
import gitee.equinox.yygh.order.service.PaymentService;
import gitee.equinox.yygh.order.service.WeixinService;
import gitee.equinox.yygh.order.utils.ConstantPropertiesUtils;
import gitee.equinox.yygh.order.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WeixinServiceImpl implements WeixinService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RedisTemplate redisTemplate;

    //下单 生成二维码
    @Override
    public Map createNative(Long orderId) {
        try {
            Map payMap = (Map) redisTemplate.opsForValue().get(orderId.toString());
            if (null != payMap) return payMap;

            //根据id获取订单信息
            OrderInfo order = orderService.getById(orderId);
            //向支付记录表添加数据
            paymentService.savePaymentInfo(order, PaymentTypeEnum.WEIXIN.getStatus());//微信支付

            //设置参数，调用微信生成二维码接口
            //把参数转成xml格式，使用商户key进行加密

            //1、设置参数
            Map paramMap = new HashMap();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            String body = order.getReserveDate() + "就诊" + order.getDepname();
            paramMap.put("body", body);
            paramMap.put("out_trade_no", order.getOutTradeNo());
            //paramMap.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue()+"");

            paramMap.put("total_fee", "1");//一分钱
            paramMap.put("spbill_create_ip", "127.0.0.1");
            paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
            paramMap.put("trade_type", "NATIVE");

            //调用微信生成二维码接口，httpclient调用
            //2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);//支持https请求
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            //xml-----map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            System.out.println("resultMap 的数据为=========>" + resultMap);
            //4、封装返回结果集
            Map map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("totalFee", order.getAmount());
            map.put("resultCode", resultMap.get("result_code"));
            map.put("codeUrl", resultMap.get("code_url"));//二维码地址
            if(null != resultMap.get("result_code")) {
                //微信支付二维码2小时过期，可采取2小时未支付取消订单
                redisTemplate.opsForValue().set(orderId.toString(), map, 120, TimeUnit.MINUTES);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
