package contractAPI;

import H2ChainUtil.HexUtil.HexUtil;
import H2ChainUtil.configUtil.LoadSetting;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import io.aelf.protobuf.generated.Client;
import io.aelf.protobuf.generated.Core;
import io.aelf.protobuf.generated.MultiLanguageContractOuterClass;
import io.aelf.schemas.SendTransactionInput;
import io.aelf.schemas.SendTransactionOutput;
import io.aelf.schemas.TransactionResultDto;
import io.aelf.sdk.AElfClient;
import io.aelf.utils.ByteArrayHelper;
import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;

/**
 * @Author zyh
 * @Date 2022/6/18 9:17 下午
 * @Version 1.0
 */
public class H2ChainAPI {

    /*private static AElfClient multiLClient = new AElfClient("http://127.0.0.1:1235");
    private static String privateKey = "b465f9d58248a49fd708fd1147b96e7e7be6f017e2c3beb6f529b1a6b0788b70";
    private static String dataStoreContractAddress = "2LUmicHyH4RXrMjG4beDwuDsiWJESyLkgkwPdGTR8kahRzq5XS";*/

    private static AElfClient multiLClient;
    private static String privateKey;
    private static String dataStoreContractAddress;

    static {
        try {
            multiLClient = new AElfClient(LoadSetting.getSettingByName("clientUrl"));
            privateKey = LoadSetting.getSettingByName("privateKey");
            dataStoreContractAddress = LoadSetting.getSettingByName("contractAddress");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向链上获取对应状态
     * @param className
     * @return
     * @throws Exception
     */
    public static String getInfo(String className) throws Exception {
        String ownerAddress = multiLClient.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        MultiLanguageContractOuterClass.GetInput.Builder multiLanguageGetInput = MultiLanguageContractOuterClass.GetInput.newBuilder();
        // 对不同字段设置相应值
        multiLanguageGetInput.setClassName(className);
        MultiLanguageContractOuterClass.GetInput getInputObj = multiLanguageGetInput.build();
        // 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = multiLClient.generateTransaction(ownerAddress, dataStoreContractAddress, "Get", getInputObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = multiLClient.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = multiLClient.sendTransaction(sendTransactionInputObj);
        //System.out.println(sendResult);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        System.out.println("交易正在被查询...");
        while (true) {
            transactionResult = multiLClient.getTransactionResult(sendResult.getTransactionId());
            //System.out.println(transactionResult);

            if ("MINED".equals(transactionResult.getStatus())) {

                // 当状态为MINED表示执行成功，直接返回
                //System.out.println("未解码："+transactionResult.getReturnValue());;
                return  transactionResult.getReturnValue() ;
            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new Exception();
            }
        }
    }

    /**
     * 将状态变更到链上
     * @param className
     * @param jsonString
     * @return
     * @throws Exception
     */
    public static Boolean setInfo(String className, String jsonString) throws Exception {
        // 通过节点 privateKey 获取节点地址，该地址即为合约的 owner
        String ownerAddress = multiLClient.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        MultiLanguageContractOuterClass.SetInput.Builder multiLanguageSetInput = MultiLanguageContractOuterClass.SetInput.newBuilder();
        // 对不同字段设置相应值
        multiLanguageSetInput.setClassName(className);
        multiLanguageSetInput.setJsonString(jsonString);
        MultiLanguageContractOuterClass.SetInput setInputObj = multiLanguageSetInput.build();
        // 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = multiLClient.generateTransaction(ownerAddress, dataStoreContractAddress, "Set", setInputObj.toByteArray());
        Core.Transaction transactionDataStoreObj = transactionDataStore.build();
        // 用 owner 地址对该交易签名
        String signature = multiLClient.signTransaction(privateKey, transactionDataStoreObj);
        transactionDataStore.setSignature(ByteString.copyFrom(ByteArrayHelper.hexToByteArray(signature)));
        transactionDataStoreObj = transactionDataStore.build();

        // 发送交易，该逻辑主要对应合约中的set方法
        SendTransactionInput sendTransactionInputObj = new SendTransactionInput();
        sendTransactionInputObj.setRawTransaction(Hex.toHexString(transactionDataStoreObj.toByteArray()));
        SendTransactionOutput sendResult = multiLClient.sendTransaction(sendTransactionInputObj);
        TransactionResultDto transactionResult;
        // 循环查询接口，根据id获得交易执行结果
        System.out.println("状态正在被上链...");
        while (true) {
            transactionResult = multiLClient.getTransactionResult(sendResult.getTransactionId());
            //System.out.println(transactionResult);
            if ("MINED".equals(transactionResult.getStatus())) {
                // 当状态为MINED表示执行成功，直接返回
                System.out.println("上链成功！");
                return true;

            } else if ("PENDING".equals(transactionResult.getStatus())) {
                // 当状态为PENDING表示还未获取到结果，等待
                Thread.sleep(300);
            } else {
                // 若其他结果则抛出异常
                throw new Exception();
            }
        }
    }

    public static Object getInfoByClass(Class className) throws Exception {
        String info = getInfo(className.getName());
        Object object = JSONObject.parseObject(HexUtil.HexStringToString(info), className);
        if(object == null){
            object = new Object();
        }

        return object;
    }


}
