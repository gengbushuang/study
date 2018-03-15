package network.rdt.model;

import java.io.IOException;
import java.util.concurrent.Executors;

import network.aio.Service;
import network.message.impl.MessageHandlerImpl;

public class ServiceMain {

	public static void main(String[] args) throws IOException {
		
		int port = 12345;
		int processors = Runtime.getRuntime().availableProcessors();
		Service service = new Service(port, Executors.newFixedThreadPool(processors));
		
		MessageHandlerImpl handlerImpl = new MessageHandlerImpl();
		service.startListening(handlerImpl);
		System.in.read();
	}
}
