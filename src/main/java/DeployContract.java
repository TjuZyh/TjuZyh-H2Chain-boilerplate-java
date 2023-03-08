import contractAPI.H2ChainDevAPI;

import java.io.File;

/**
 * @Author zyh
 * @Date 2022/6/24 3:37 下午
 * @Version 1.0
 */
public class DeployContract {
    /**
     * 合约部署 args[0] JAR文件目录
     * @param args
     */
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("no params...");
            System.exit(0);
        }

        String filePath = args[0];
        File JARFile = new File(filePath);
        String contractAddress = H2ChainDevAPI.deployContract(JARFile);
        System.out.println("合约地址：" + contractAddress);
    }
}
