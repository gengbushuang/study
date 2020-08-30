package labrpc.mobel;

public class RpcResponse {
    //版本号
    private int version;
    //请求标识
    private String requestId;
    //异常
    private Throwable error;
    //返回
    private Object result;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "version=" + version +
                ", requestId='" + requestId + '\'' +
                ", error=" + error +
                ", result=" + result +
                '}';
    }
}
