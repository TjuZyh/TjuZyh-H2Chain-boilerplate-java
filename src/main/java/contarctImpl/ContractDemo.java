package contarctImpl;

import com.alibaba.fastjson.JSONObject;
import contractAPI.H2ChainAPI;

/**
 * @Author zyh
 * @Date 2022/6/19 7:11 下午
 * @Version 1.0
 */
public class ContractDemo {

    public static Boolean updateState(String info) throws Exception {

        //向链上发起请求，获取链上当前状态
        StateDemo stateDemo = (StateDemo)H2ChainAPI.getInfoByClass(StateDemo.class);

        //编写逻辑
        //StateDemo updateDemo = JSONObject.parseObject("{\"hash\":\"j28hd3eud2hd878cdh\",\"id\":\"1\"}", StateDemo.class);
        StateDemo updateDemo = JSONObject.parseObject(info, StateDemo.class);
        stateDemo.setId(updateDemo.getId());
        stateDemo.setHash(updateDemo.getHash());

        //向链上发起请求，变更链上状态
        H2ChainAPI.setInfo(StateDemo.class.getName() , JSONObject.toJSONString(stateDemo));
        return true;
    }

    public static String testReflect1(){
        return "true";
    }

    public static String testReflect2(String param){
        return "testReflect2 " + param;
    }

    public static String testReflect3(String param1 , String param2){
        return "testReflect3 " + param1 + " " + param2;
    }
}
