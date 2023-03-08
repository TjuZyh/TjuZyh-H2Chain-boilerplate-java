### 海河智链java版本脚手架

**版本：1.0 ** 

------

#### I. 目录结构

- H2Chain-boilerplate-java
  - src/main/java
    - contractImpl
      - Implement: 合约编写者在该文件夹中编写智能合约逻辑
      - state：合约编写者在该文件夹中创建合约所需状态，即自定义合约状态
    - contractAPI：合约编写以及合约部署相关API
      - Entity：合约实体BO
      - H2ChainApi：封装获取链上状态的相关API
      - H2ChainDevApi：封装合约部署（上链存储）的相关API
    - H2ChainUtil：H2Chain内部的一些工具类
      - FileBO：封装文件转换FileBO的API
      - HexUtil：封装hex解码的相关API
      - OSS：封装OSS上传的相关API
      - SHA：封装hash加密函数以及文件加密的相关API
    - io/aelf：java-SDK相关依赖资源
      - protobuf
        - proto：存放合约自定义状态
        - generated：protoc编译后的状态类文件
      - schemas：封装一些SDK所需VO以及BO
      - sdk：封装与链交互的相关API
      - util：封装SDK中所需的内部工具类
    - DeployContract.java：合约部署主程序入口
    - ExecuteContract.java：合约执行主程序入口

#### II. API介绍

介绍了海河智链java版本脚手架中相关API的输入输出、使用场景、目的等

##### 1. contractAPI中相关API

| API名称        | 所属位置           | 输入参数及类型                 | 输出参数及类型     | API描述               |
| -------------- | ------------------ | ------------------------------ | ------------------ | --------------------- |
| getInfo        | H2ChainAPI.java    | 状态名 String                  | 状态数据 String    | 实时获取链上指定状态  |
| setInfo        | H2ChainAPI.java    | 状态名 String；状态数据 String | 执行返回值 Boolean | 将状态数据更新到链上  |
| deployContract | H2ChainDevAPI.java | jar文件 File                   | 合约地址 String    | 合约部署到链以及OSS中 |

###### 1.1 getInfo

该API用于与H2Chain主链进行状态交互，用于实时获取链上指定状态

**位置：**H2Chain-boilerplate-java/src/main/java/contractAPI/H2ChainAPI.java

**输入：**

- 参数：状态名 String

**输出：**

- 参数：状态数据 String

###### 1.2 setInfo

该API用于与H2Chain主链进行状态交互，用于将状态数据更新到链上

**位置：**H2Chain-boilerplate-java/src/main/java/contractAPI/H2ChainAPI.java

**输入：**

- 参数1：状态名 String
- 参数2：状态数据 String

**输出：**

- 参数：执行返回值 Boolean

###### 1.3 deployContract

该API用于将合约部署到链以及OSS中，其中具体的实现逻辑为将脚手架编译的JAR文件存储到OSS中以及将JAR文件的hash等信息上链作为存证进行存储

**位置：**H2Chain-boilerplate-java/src/main/java/contractAPI/H2ChainDevAPI.java

**输入：**

- 参数：jar文件 File

**输出：**

- 参数：合约地址 String

#### III. 脚手架使用流程

##### 1. 克隆仓库项目

脚手架github仓库：https://github.com/TJUBlockchainLab/H2Chain-boilerplate-java

```shell
git clone git@github.com:TJUBlockchainLab/H2Chain-boilerplate-java.git
```

##### 2. 克隆mutilContract

github仓库：https://github.com/TJUBlockchainLab/MultiLanguage_Contract

```shell
git clone git@github.com:TJUBlockchainLab/MultiLanguage_Contract.git
```

##### 3. 测试链合约部署

利用aelf-boilerplate启动一条合约测试链，将mutilContract部署到测试链中，并启动测试链

合约创建、部署等具体操作流程，请见**智能合约开发手册**

##### 4. 利用java版本脚手架编写合约

在项目目录**H2Chain-boilerplate-java/src/main/java/contractImpl**中编写合约

其中：

- 在state目录中通过创建类文件方式来创建合约状态，即一个类文件为一个合约状态，注意需要编写Getter以及Setter，示例如下

  ```java
  public class StateDemo {
      private String id;
      private String hash;
  
      //省略getter、setter、tostring...
  }
  ```

- 在Implement目录中编写合约逻辑

  **合约编写的三步：**

  1. 利用getInfo获取链上状态数据
  2. 基于状态数据编写合约逻辑
  3. 利用setInfo更新链上状态数据

  **合约dem示例：**注意输入参数info格式为StateDemo状态JSON序列后的字符串

  数据格式示例：{'hash':'j28hd3eud2hd878cdh','id':'1'}

  ```java
  public static Boolean updateState(String info) throws Exception {
  
          //向链上发起请求，获取链上当前状态
          String curState = H2ChainAPI.getInfo(StateDemo.class.getName());
  
          //编写逻辑
          StateDemo stateDemo =   	JSONObject.parseObject(HexUtil.HexStringToString(curState),StateDemo.class);
          StateDemo updateDemo = JSONObject.parseObject(info, StateDemo.class);
    
          if(stateDemo == null){
              stateDemo = new StateDemo();
          }
    
          stateDemo.setId(updateDemo.getId());
          stateDemo.setHash(updateDemo.getHash());
  
          //向链上发起请求，变更链上状态
          H2ChainAPI.setInfo(StateDemo.class.getName() , JSONObject.toJSONString(stateDemo));
          return true;
      }
  ```

##### 5. 编写合约执行主程序入口

在ExecuteContract.java中编写自动执行合约的逻辑，示例如下：

```java
public class ExecuteContract {
    /**
     * 执行合约 arg[0] 函数名 arg[1] 参数名
     * @param args
     */
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("no params...");
            System.exit(0);
        }

        String contractFuncName = args[0];
        String params = args[1];
      
        //注意第二个参数为JSON串，在cmd传参时需要用双引号包起来，试做一个整体
        if(contractFuncName.equals("updateState")){
            try {
                Boolean updateState = ContractDemo.updateState(params);
                System.out.println(updateState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```

##### 6. 合约打成JAR包

将编写的合约项目打成Jar文件，在idea中进入maven/H2Chain-boilerplate-java/Plugins/assembly/assembly: assembly，双击即可将项目打成胖包，jar包存放位置：target/H2Chain-boilerplate-java-1.0-SNAPSHOT-jar-with-dependencies.jar

##### 7. 合约部署

将打好的JAR包进行部署

```shell
cd target
java -cp H2Chain-boilerplate-java-1.0-SNAPSHOT-jar-with-dependencies.jar deployContract jar包存放绝对路径
```

