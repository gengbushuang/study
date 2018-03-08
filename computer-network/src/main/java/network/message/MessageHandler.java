package network.message;


public interface MessageHandler {
	public ResponseMessage processRequest(RequestMessage requestMessage);
}
