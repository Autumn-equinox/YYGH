package gitee.equinox.yygh.easyexcel;

import com.alibaba.excel.EasyExcel;

public class EasyExcelTestRead {
    public static void main(String[] args) {
        //读取文件路径
        String fileName = "D:\\Personal\\WorkSpace\\yygh_excelTest\\test.xlsx";
        //调用方法实现读取操作
        EasyExcel.read(fileName,User.class,new ExcelListener()).sheet().doRead();
    }
}
