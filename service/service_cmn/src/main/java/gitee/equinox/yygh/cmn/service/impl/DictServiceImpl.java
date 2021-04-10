package gitee.equinox.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import gitee.equinox.yygh.cmn.listener.DictListener;
import gitee.equinox.yygh.cmn.mapper.DictMapper;
import gitee.equinox.yygh.cmn.service.DictService;
import gitee.equinox.yygh.model.cmn.Dict;
import gitee.equinox.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    /*
    @org.springframework.beans.factory.annotation.Autowired
            protected M baseMapper;
            baseMapper已被ServiceImpl注入
     */
    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")//数据放入缓存
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(queryWrapper);
        //向dict集合中每个dict对象设置hasChildren属性
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean res = isChildrenExist(dictId);
            dict.setHasChildren(res);
        }
        return dictList;
    }
    //导出数据字典接口
    @Override
    public void exportDictData(HttpServletResponse response) {
        //1.设置下载信息
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyExcel没有关系
        //String fileName = URLEncoder.encode("数据字典", "UTF-8");
        String fileName = "dict";
        //2.设置头信息，以下载方式打开
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
        //3.查询数据库
        List<Dict> dictList = baseMapper.selectList(null);//queryWrapper为null
        //4.dictList转换为============>DictEeVo
        List<DictEeVo> dictEeVoList = new ArrayList<>();
        for (Dict dict : dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            //dictEeVo.setId(dict.getId());工具类实现
            BeanUtils.copyProperties(dict,dictEeVo);
            dictEeVoList.add(dictEeVo);
        }
        //5.调用方法进行写操作
        //浏览器，这里使用流传输
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictEeVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //导入数据字典
    @Override
    @CacheEvict(value = "dict", allEntries=true)// allEntries = true: 方法调用后清空所有缓存
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断下面是否有子节点
    private boolean isChildrenExist(Long id){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    //根据dictcode和value查询name
    @Override
    public String getDictName(String dictCode, String value) {
        //如果dictCode为空，根据value查询
        if (StringUtils.isEmpty(dictCode)){
            QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(queryWrapper);
            return dict.getName();
        }else {
            //不为空，
            QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dict_code",dictCode);
            Dict selectOne = baseMapper.selectOne(queryWrapper);
            Long parentId = selectOne.getId();
            //根据value与parentId查询
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parentId)
                    .eq("value", value));
            return dict.getName();
        }
    }

    //根据dictCode获取下级节点
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictCode获取对应的id
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(queryWrapper);
        Long id = dict.getId();
        //根据id获取子节点
        List<Dict> childData = this.findChildData(id);
        return childData;
    }
}
