package labrpc.proxy;

import labrpc.Transporter;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;

public interface RpcProxyFatory {

	public  <T>  RpcProxy<T> getProxy(Class<T> protocol, Transporter channel, InvocationHandler invocationHandler);

	public  <T>  RpcProxy<T> getProxy(Class<T> protocol, Transporter channel) ;
}
