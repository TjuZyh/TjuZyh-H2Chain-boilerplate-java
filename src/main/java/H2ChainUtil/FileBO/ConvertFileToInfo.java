package H2ChainUtil.FileBO;

import H2ChainUtil.SHA.HashJAR;
import contractAPI.Entity.ContractInfo;

import java.io.File;
import java.io.IOException;

/**
 * @Author zyh
 * @Date 2022/6/22 3:03 下午
 * @Version 1.0
 */
public class ConvertFileToInfo {
    public static ContractInfo convertFileToInfo(File file , String OSSUrl){
        ContractInfo contractInfo = new ContractInfo();
        contractInfo.setCodeType("java");
        contractInfo.setCodeAddress(OSSUrl);
        try {
            String hashJAR = HashJAR.hashJAR(file);
            contractInfo.setCodeHash(hashJAR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        contractInfo.setCodeName(file.getName().substring(0 , file.getName().length()-4));
        //System.out.println(contractInfo);
        return contractInfo;
    }

    /*public static void main(String[] args) {
        convertFileToInfo(new File("/Users/zyh/Desktop/JAR_demo.jar") , "https://tanklab-oss-demo.oss-cn-beijing.aliyuncs.com/test1655880710632.jar");
    }*/
}
