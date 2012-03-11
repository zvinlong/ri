package org.duhei.irm.server;

import java.net.Socket;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.duhei.commons.util.ThreadUtil;

/**
 * @author zvin
 * 
 */
public class Session {

	private static final int SENDING_CAPACITY = 10;
	private static final int RECEIVING_CAPACITY = 10;
	private static final int MESSAGE_CAPACITY = 10;
	private static final int SOCKET_INIT_CAPACITY = 2;

	private String id;
	private SessionType type;
	private Server server;
	private List<Socket> sockets;
	private Queue<String> sendings;
	private Queue<String> receivings;
	private Queue<Message> messages;

	public Session(Server server) {
		this.server = server;
		init();
	}

	private void init() {
		sendings = new ArrayDeque<String>(SENDING_CAPACITY);
		receivings = new ArrayDeque<String>(RECEIVING_CAPACITY);
		messages = new ArrayDeque<Message>(MESSAGE_CAPACITY);
		sockets = new ArrayList<Socket>(SOCKET_INIT_CAPACITY);

		Processor processor = new Processor(this, server);
		ThreadUtil.start(processor, true);
	}

	public void send(String message) {
		this.sendings.add(message);
	}

	public Queue<String> getSendings() {
		return sendings;
	}

	public Queue<String> getReceivings() {
		return receivings;
	}

	public Queue<Message> getMessages() {
		return messages;
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public void addSocket(Socket socket) {

		sockets.add(socket);

		Receiver receiver = new Receiver(this, socket);
		ThreadUtil.start(receiver, true);

		Converter converter = new Converter(messages, receivings);
		ThreadUtil.start(converter, true);

		Sender sender = new Sender(sendings, socket);
		ThreadUtil.start(sender, true);
	}
	
	public List<Socket> getSockets(){
		return sockets;
	}

	public String getId() {
		return id;
	}

	public void setId(String clientId) {
		this.id = clientId;
	}

	public Server getIRServer() {
		return server;
	}

	public SessionType getType() {
		return type;
	}

	public void setType(SessionType type) {
		this.type = type;
	}

}
