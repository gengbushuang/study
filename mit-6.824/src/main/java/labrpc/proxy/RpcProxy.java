package labrpc.proxy;

public class RpcProxy<T> {

	private Class<T> protocol;
	private T proxy;

	public RpcProxy(Class<T> protocol, T proxy) {
		this.protocol = protocol;
		this.proxy = proxy;
	}

	public T getProxy() {
		return proxy;
	}

}
