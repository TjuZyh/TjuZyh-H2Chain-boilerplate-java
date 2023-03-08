package H2ChainUtil.SHA;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author zyh
 * @Date 2022/6/20 2:24 下午
 * @Version 1.0
 */
public class HashJAR {
    /**
     * 输入jar文件，输出jar文件的hash
     * @param file
     * @return
     * @throws IOException
     */
    public static String hashJAR(File file) throws IOException {
        String curFilePath = file.getAbsolutePath();
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] fileBytes = new byte[fileInputStream.available()];
        fileInputStream.read(fileBytes);
        fileInputStream.close();
        String JARString = Arrays.toString(fileBytes);
        String hashJAR = SHA.sha_func(JARString, "SHA-256");
        return hashJAR;
    }
}
