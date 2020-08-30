package labrpc.mobel;

import java.util.Arrays;

public class RpcRequest {
    //版本号
    private int version;
    //请求标识
    private String requestId;
    //类名
    private String className;
    //类方法名
    private String methodName;
    //方法的参数类型
    private Class<?>[] parameterClasses;
    //方法参数
    private Object[] parameters;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterClasses() {
        return parameterClasses;
    }

    public void setParameterClasses(Class<?>[] parameterClasses) {
        this.parameterClasses = parameterClasses;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "version=" + version +
                ", requestId='" + requestId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterClasses=" + Arrays.toString(parameterClasses) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
