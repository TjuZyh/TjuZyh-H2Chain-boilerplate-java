package H2ChainUtil.OSS;

import H2ChainUtil.configUtil.LoadSetting;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Author zyh
 * @Date 2022/6/22 1:26 下午
 * @Version 1.0
 */
public class UploadOSS {
    public static String uploadOSS(File file) throws Exception {
        try{
            // 创建OSSClient实例
            //OSS ossClient = new OSSClientBuilder().build("oss-cn-beijing.aliyuncs.com", "LTAI5tLS2EmoBSDiEBnYgXkh", "Qz1Pdbdx3U56d803mNjXHXIoE1XuE9");
            OSS ossClient = new OSSClientBuilder().build(LoadSetting.getSettingByName("endpoint"), LoadSetting.getSettingByName("accessKeyId"), LoadSetting.getSettingByName("secretAccessKey"));

            // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            InputStream inputStream = file.toURL().openStream();
            String uuid= UUID.randomUUID().toString().replaceAll("-","");
            Long timeStamp = System.currentTimeMillis();
            String fileName = file.getName().substring(0 , file.getName().length()-4) + "_" + timeStamp + ".jar";
            ossClient.putObject("tanklab-oss-demo", fileName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            String url="https://"+"tanklab-oss-demo"+"."+"oss-cn-beijing.aliyuncs.com"+"/"+fileName;
            return url;
        }catch(Exception e){
            throw new Exception();
        }
    }
}
