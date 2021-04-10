package gitee.equinox.yygh.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class EasyExcelTestWrite {
    public static void main(String[] args) {
        //构建数据list集合
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setAge(i);
            user.setName("hello"+i);
            userList.add(user);
        }
        //设置excel文件路径和名称
        String fileName = "D:\\Personal\\WorkSpace\\yygh_excelTest\\test.xlsx";
        //调用方法实现写操作
        EasyExcel.write(fileName,User.class).sheet("用户信息").doWrite(userList);
    }
}
