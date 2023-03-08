package contractAPI.Entity;

/**
 * @Author zyh
 * @Date 2022/6/20 2:02 下午
 * @Version 1.0
 */
public class ContractInfo {
    private String codeHash;
    private String codeType;
    private String codeName;
    private String codeAddress;

    public String getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeAddress() {
        return codeAddress;
    }

    public void setCodeAddress(String codeAddress) {
        this.codeAddress = codeAddress;
    }

    @Override
    public String toString() {
        return "ContractInfo{" +
                "codeHash='" + codeHash + '\'' +
                ", codeType='" + codeType + '\'' +
                ", codeName='" + codeName + '\'' +
                ", codeAddress='" + codeAddress + '\'' +
                '}';
    }
}
