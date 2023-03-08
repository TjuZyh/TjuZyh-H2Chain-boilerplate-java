package H2ChainUtil.generateUtil;

import H2ChainUtil.configUtil.LoadSetting;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zyh
 * @Date 2022/7/27 10:39 下午
 * @Version 1.0
 */
public class CodeGenerator {
    public static void generateExeContract() throws IOException, TemplateException {

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setDirectoryForTemplateLoading(new File("/Users/zyh/Desktop/H2Chain-boilerplate-java 2/src/main/java/H2ChainUtil/generateUtil/freemark/templates"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Template temp = cfg.getTemplate("executeContract.ftl");  // load E:/Work/Freemarker/templates/person.ftl

        // Create the root hash
        Map<String, Object> root = new HashMap<String, Object>();

        root.put("executeClassPath", "contarctImpl." + LoadSetting.getSettingByName("contractName"));

        File dir = new File("/Users/zyh/Desktop/H2Chain-boilerplate-java 2/src/main/java/");
        if(!dir.exists()){
            dir.mkdirs();
        }
        OutputStream fos = new FileOutputStream( new File(dir, "ExecuteContract.java")); //java文件的生成目录
        Writer out = new OutputStreamWriter(fos);
        temp.process(root, out);

        fos.flush();
        fos.close();

        System.out.println("generate ExecuteContract success!");
    }

    public static void generateState() throws IOException, TemplateException {

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setDirectoryForTemplateLoading(new File("/Users/zyh/Desktop/H2Chain-boilerplate-java 2/src/main/java/H2ChainUtil/generateUtil/freemark/templates"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Template temp = cfg.getTemplate("state.ftl");  // load E:/Work/Freemarker/templates/person.ftl

        // Create the root hash
        Map<String, Object> root = new HashMap<String, Object>();

        //root.put("contractName", "DataStoreState");
        root.put("contractName", LoadSetting.getSettingByName("contractName")+"State");

        File dir = new File("/Users/zyh/Desktop/H2Chain-boilerplate-java 2/src/main/java/contarctImpl/");
        if(!dir.exists()){
            dir.mkdirs();
        }
        OutputStream fos = new FileOutputStream( new File(dir, LoadSetting.getSettingByName("contractName")+"State.java")); //java文件的生成目录
        Writer out = new OutputStreamWriter(fos);
        temp.process(root, out);

        fos.flush();
        fos.close();

        System.out.println("generate DataStoreState success!");
    }

    public static void generateImplement() throws IOException, TemplateException {

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setDirectoryForTemplateLoading(new File("/Users/zyh/Desktop/H2Chain-boilerplate-java 2/src/main/java/H2ChainUtil/generateUtil/freemark/templates"));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        Template temp = cfg.getTemplate("implement.ftl");  // load E:/Work/Freemarker/templates/person.ftl

        // Create the root hash
        Map<String, Object> root = new HashMap<String, Object>();

        root.put("implementContractName", LoadSetting.getSettingByName("contractName"));

        File dir = new File("/Users/zyh/Desktop/H2Chain-boilerplate-java 2/src/main/java/contarctImpl/");
        if(!dir.exists()){
            dir.mkdirs();
        }
        OutputStream fos = new FileOutputStream( new File(dir, LoadSetting.getSettingByName("contractName")+".java")); //java文件的生成目录
        Writer out = new OutputStreamWriter(fos);
        temp.process(root, out);

        fos.flush();
        fos.close();

        System.out.println("generate DataStore success!");
    }
}
