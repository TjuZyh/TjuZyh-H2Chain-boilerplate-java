package H2ChainUtil.generateUtil;

import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @Author zyh
 * @Date 2022/7/28 3:53 下午
 * @Version 1.0
 */
public class ContractGenerator {
    public static void main(String[] args) {
        try {
            CodeGenerator.generateState();
            CodeGenerator.generateImplement();
            CodeGenerator.generateExeContract();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
