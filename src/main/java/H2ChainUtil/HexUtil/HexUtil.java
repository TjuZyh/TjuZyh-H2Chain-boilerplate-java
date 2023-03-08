package H2ChainUtil.HexUtil;

/**
 * @Author zyh
 * @Date 2022/6/18 10:12 下午
 * @Version 1.0
 */
public class HexUtil {
    public static String HexStringToString( String  hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 4; i < hex.length(); i+=2) {
            String str = hex.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        return output.toString();
    }
}
