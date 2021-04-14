package gitee.equinox.yygh.order.controller;

import gitee.equinox.yygh.common.result.Result;
import gitee.equinox.yygh.order.service.WeixinService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/order/weixin")
public class WeixinController {
    @Autowired
    private WeixinService weixinService;

    /**
     * 下单 生成二维码
     */
    @GetMapping("/createNative/{orderId}")
    public Result createNative(@PathVariable("orderId") Long orderId) {
        Map map = weixinService.createNative(orderId);
        return Result.ok(map);
    }
}

