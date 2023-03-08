import java.lang.reflect.Method;
import java.util.ArrayList;

public class ExecuteContract {
    /**
    * 执行合约 arg[0] 函数名 arg[1] 参数名1 arg[2]参数名2 ...
    * @param args
    */
    public static void main(String[] args) {
    if(args.length < 1){
        System.out.println("no params...");
        System.exit(0);
    }

    String classPath = "${executeClassPath}";
    String methodName = args[0];
    ArrayList<Class> paramsTypes = new ArrayList<>();
        String param1 = "";
        String param2 = "";
        String param3 = "";
        if(args.length == 1){
        //do nothing...
        }else if(args.length == 2){
        param1 = args[1];
        }else if(args.length == 3){
        param1 = args[1];
        param2 = args[2];
        }else{
        param1 = args[1];
        param2 = args[2];
        param3 = args[3];
        }

        try {
            Object instance = getInstance(classPath);
            Class<?> instanceClass = instance.getClass();

            Method[] declaredMethods = instanceClass.getDeclaredMethods();
            for(Method dm : declaredMethods){
                if(dm.getName().equals(methodName)){
                    //System.out.println(dm.getName());
                    Class<?>[] parameterTypes = dm.getParameterTypes();
                    for(Class pt : parameterTypes){
                        paramsTypes.add(pt);
                    }
                }
            }
            int paramsCounts = paramsTypes.size();

            if(paramsCounts == 0){
                Method instanceClassMethod = instanceClass.getMethod(methodName);
                Object returnValue = instanceClassMethod.invoke(instance);
                System.out.println("returnValue: " + returnValue);
            }else if(paramsCounts == 1){
                Method instanceClassMethod = instanceClass.getMethod(methodName, paramsTypes.get(0));
                Object returnValue = instanceClassMethod.invoke(instance, param1);
                System.out.println("returnValue: " + returnValue);
            }else if(paramsCounts == 2){
                Method instanceClassMethod = instanceClass.getMethod(methodName, paramsTypes.get(0) , paramsTypes.get(1));
                Object returnValue = instanceClassMethod.invoke(instance, param1 , param2);
                System.out.println("returnValue: " + returnValue);
            }else{
                Method instanceClassMethod = instanceClass.getMethod(methodName, paramsTypes.get(0) , paramsTypes.get(1) , paramsTypes.get(2));
                Object returnValue = instanceClassMethod.invoke(instance, param1 , param2 , param3);
                System.out.println("returnValue: " + returnValue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getInstance(String classPath) throws Exception {
        Class<?> aClass = Class.forName(classPath);
        Object newInstance = aClass.newInstance();
        return newInstance;
    }

}
