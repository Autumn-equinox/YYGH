package gitee.equinox.yygh.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gitee.equinox.yygh.enums.PaymentStatusEnum;
import gitee.equinox.yygh.model.order.OrderInfo;
import gitee.equinox.yygh.model.order.PaymentInfo;
import gitee.equinox.yygh.order.mapper.PaymentInfoMapper;
import gitee.equinox.yygh.order.service.PaymentService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentService {

    //向支付记录表添加数据
    @Override
    public void savePaymentInfo(OrderInfo order, Integer status) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", order.getId());
        queryWrapper.eq("payment_type", status);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) return;
        // 保存交易记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(order.getId());
        paymentInfo.setPaymentType(status);
        paymentInfo.setOutTradeNo(order.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        //交易内容
        String subject = new DateTime(order.getReserveDate()).toString("yyyy-MM-dd") + "|" + order.getHosname() + "|" + order.getDepname() + "|" + order.getTitle();
        paymentInfo.setSubject(subject);
        paymentInfo.setTotalAmount(order.getAmount());
        baseMapper.insert(paymentInfo);
    }
}

