package H2ChainUtil.configUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 * @Author zyh
 * @Date 2022/7/19 1:43 下午
 * @Version 1.0
 */
public class LoadSetting {
    public static void main(String[] args) throws IOException {
        String clientUrl = getSettingByName("clientUrl");
        System.out.println(clientUrl);

        String privateKey = getSettingByName("privateKey");
        System.out.println(privateKey);

        String contractAddress = getSettingByName("contractAddress");
        System.out.println(contractAddress);

        String contractName = getSettingByName("contractName");
        System.out.println(contractName);

    }

    public static String getSettingByName(String configName) throws IOException{
        FileInputStream fileInputStream = null;
        Properties properties = new Properties();
        fileInputStream = new FileInputStream("src/main/resources/appSettings.properties");
        //加载对应文件
        properties.load(fileInputStream);
        String property = properties.getProperty(configName);
        if(configName.equals("contractName")){
            String first = property.substring(0,1).toUpperCase(Locale.ROOT);
            String last = property.substring(1, property.length());
            property = first + last;
        }

        //读取配置文件中的属性
        return property;
    }
}
