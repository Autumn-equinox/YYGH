package gitee.equinox.yygh.cmn.controller;

import gitee.equinox.yygh.cmn.service.DictService;
import gitee.equinox.yygh.common.result.Result;
import gitee.equinox.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

//@CrossOrigin//解决跨域访问问题
@RestController
@Api(tags = "数据字典模块")
@RequestMapping("/admin/cmn/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    //根据dictCode获取下级节点
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public Result<List<Dict>> findByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }


    //根据数据id查询子数据列表
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable Long id) {
        List<Dict> dictList = dictService.findChildData(id);
        return Result.ok(dictList);
    }

    //导出数据字典接口
    @ApiOperation(value = "导出数据字典接口")
    @GetMapping(value = "/exportData")
    public void exportData(HttpServletResponse response) {//方便用户修改文件名等，传入response
        dictService.exportDictData(response);//不用返回数据
    }

    //导入数据字典
    @ApiOperation(value = "导入数据字典")
    @PostMapping(value = "/importData")
    //MultipartFile  这个类一般是用来接受前台传过来的文件，接收前台传过来的excel，做个导入功能
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }

    //根据dictcode和value查询name
    @GetMapping(value = "/getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode, @PathVariable String value) {
        String dictName = dictService.getDictName(dictCode, value);
        return dictName;
    }

    //根据value查询name
    @GetMapping(value = "/getName/{value}")
    public String getName(@PathVariable String value) {
        String dictName = dictService.getDictName("", value);
        return dictName;
    }
}
