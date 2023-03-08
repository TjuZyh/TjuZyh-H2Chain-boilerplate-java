package contractAPI;

import H2ChainUtil.FileBO.ConvertFileToInfo;
import H2ChainUtil.OSS.UploadOSS;
import H2ChainUtil.SHA.SHA;
import H2ChainUtil.configUtil.LoadSetting;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import contractAPI.Entity.ContractInfo;
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

import java.io.File;
import java.io.IOException;

/**
 * @Author zyh
 * @Date 2022/6/19 6:58 下午
 * @Version 1.0
 */
public class H2ChainDevAPI {
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

    public static String setContract(ContractInfo contractInfo) throws Exception {
        String ownerAddress = multiLClient.getAddressFromPrivateKey(privateKey);
        Client.Address.Builder owner = Client.Address.newBuilder();
        owner.setValue(ByteString.copyFrom(Base58.decodeChecked(ownerAddress)));

        // 构建合约调用时需要传递的参数
        // 具体定义见 io.aelf 包中的 proto 文件
        MultiLanguageContractOuterClass.SetContractInput.Builder multiLanguageSetContractInfo = MultiLanguageContractOuterClass.SetContractInput.newBuilder();
        // 对不同字段设置相应值
        multiLanguageSetContractInfo.setParamsJson(JSONObject.toJSONString(contractInfo));
        String hash= SHA.sha_func(JSONObject.toJSONString(contractInfo),"SHA-256");
        multiLanguageSetContractInfo.setParamsHash(hash);
        MultiLanguageContractOuterClass.SetContractInput setContractObj = multiLanguageSetContractInfo.build();
        // 构建调用智能合约方法对应的参数
        Core.Transaction.Builder transactionDataStore = multiLClient.generateTransaction(ownerAddress, dataStoreContractAddress, "SetContract", setContractObj.toByteArray());
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
        System.out.println("合约正在被部署...");
        while (true) {
            transactionResult = multiLClient.getTransactionResult(sendResult.getTransactionId());
            if ("MINED".equals(transactionResult.getStatus())) {

                System.out.println("部署成功");
                // 当状态为MINED表示执行成功，直接返回

                return  hash ;
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
     * 合约部署：JAR文件OSS存储 以及 匹配对应proto字段上链
     * @param file
     * @return
     */
    public static String deployContract(File file){
        String addressHash = "";
        try {
            String ossUrl = UploadOSS.uploadOSS(file);
            ContractInfo contractInfo = ConvertFileToInfo.convertFileToInfo(file, ossUrl);
            addressHash = setContract(contractInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addressHash;
    }
}
