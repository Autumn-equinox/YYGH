package gitee.equinox.yygh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import gitee.equinox.yygh.model.order.OrderInfo;
import gitee.equinox.yygh.vo.order.OrderCountQueryVo;
import gitee.equinox.yygh.vo.order.OrderCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    //获取医院每天平台预约数据
    List<OrderCountVo> selectOrderCount(@Param("vo") OrderCountQueryVo orderCountQueryVo);
}
