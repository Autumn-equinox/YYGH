package gitee.equinox.yygh.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

public class Test {
    public static void main(String[] args) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = "LTAI5t7Z9ManJYp548se9BhP";
        String accessKeySecret = "Pv0SgyFYjaJ5oBaT9xn5XDDaJ03r5c";
        String bucketName = "equinox";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //创建存储空间
        ossClient.createBucket(bucketName);

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
