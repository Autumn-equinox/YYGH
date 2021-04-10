package gitee.equinox.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient("service-cmn")//服务名
public interface DictFeignClient {

    //根据dictcode和value查询name
    @GetMapping(value = "/admin/cmn/dict/getName/{dictCode}/{value}")//路径补充完整，@PathVariable要写具体变量名
    public String getName(@PathVariable("dictCode") String dictCode, @PathVariable("value") String value);

    //根据value查询name
    @GetMapping(value = "/admin/cmn/dict/getName/{value}")
    public String getName(@PathVariable("value") String value);
}
