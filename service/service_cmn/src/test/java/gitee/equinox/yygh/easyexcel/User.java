package gitee.equinox.yygh.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class User {

    @ExcelProperty(value = "姓名",index = 0)//表头+索引，第几行
    private String name;

    @ExcelProperty(value = "年龄",index = 1)
    private Integer age;
}
