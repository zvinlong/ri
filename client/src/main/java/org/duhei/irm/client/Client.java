package org.duhei.irm.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import org.duhei.commons.util.ThreadUtil;

public class Client {

	public static final String CHARSET = "UTF-8";

	private static final int RECEIVING_CAPACITY = 10;
	private static final int MESSAGE_CAPACITY = 10;

	private String clientId;
	private Socket socket;
	private Queue<String> receivings;
	private Queue<Message> messages;
	private Processor processor;

	public Client(String host, int port) {
		InetAddress bindpoint = null;
		try {
			bindpoint = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		try {
			socket = new Socket(bindpoint, port);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		init();
	}

	private void init() {

		receivings = new ArrayDeque<String>(RECEIVING_CAPACITY);
		messages = new ArrayDeque<Message>(MESSAGE_CAPACITY);

		Listener listener = new Listener(this);
		ThreadUtil.start(listener, true);

		Convertor convertor = new Convertor(messages, receivings);
		ThreadUtil.start(convertor, true);

		processor = new Processor(messages);
		ThreadUtil.start(processor, true);
	}

	public void bindMessageProcessor(MessageProcessor receptionProcessor) {
		processor.setReceptionProcessor(receptionProcessor);
	}

	public void send(String message) {
		try {
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(message.getBytes(CHARSET));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void addReceiving(String receiving) {
		receivings.add(receiving);
	}

	public Queue<String> getReceivings() {
		return receivings;
	}

	private List<String> clientIds = new ArrayList<String>(1);

	public List<String> getClientIds() {
		return clientIds;
	}

	public void startUpdateClientIds() {

		//
		// Socket aSocket = null;
		//
		// try {
		// aSocket = new Socket(socket.getInetAddress(), port);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// final Socket socket = aSocket;

		Thread thread = new Thread(new Runnable() {

			public void run() {

				int port = 11112;

				boolean isQuit = false;

				DatagramSocket datagramSocket = null;
				try {
					datagramSocket = new DatagramSocket(port);
				} catch (SocketException e) {
					e.printStackTrace();
					isQuit = true;
				}

				while (!isQuit) {

					int length = 1024;

					byte[] buf = new byte[length];

					DatagramPacket datagramPacket = null;

					datagramPacket = new DatagramPacket(buf, length);

					try {
						datagramSocket.receive(datagramPacket);
					} catch (IOException e) {
						e.printStackTrace();
						isQuit = true;
					}

					String s = new String(datagramPacket.getData());
					String[] clientIdsx = s.split(",");
					clientIds = new ArrayList<String>(7);
					for (String clientId : clientIdsx) {
						clientIds.add(clientId);
					}

					long millis = 3000;
					try {
						Thread.currentThread().sleep(millis);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// try {
					// socket.getOutputStream().write("abc".getBytes());
					// } catch (IOException e) {
					// e.printStackTrace();
					// isQuit = true;
					// }
					// InputStream is = null;
					//
					// try {
					// is = socket.getInputStream();
					// } catch (IOException e) {
					// e.printStackTrace();
					// if (!socket.isConnected()) {
					// try {
					// socket.close();
					// } catch (IOException e1) {
					// e1.printStackTrace();
					// isQuit = true;
					// }
					// }
					// }
					//
					// byte[] inBuffer = new byte[1024];
					// int count = 0;
					// StringBuffer sb = StringUtil.initBuffer();
					//
					// try {
					// do {
					// count = is.read(inBuffer);
					// sb.append(new String(inBuffer, 0, count,
					// Server.CHARSET));
					// } while (count >= 1024);
					// } catch (IOException e) {
					// e.printStackTrace();
					// isQuit = true;
					// }
					//
					// String[] clientIdsx = sb.toString().split(",");
					// clientIds = new ArrayList<String>(7);
					// for (String clientId : clientIdsx) {
					// clientIds.add(clientId);
					// System.out.println(clientId);
					// }
				}
			}
		});

		thread.setDaemon(true);
		thread.start();
	}

	public Socket getSocket() {
		return socket;
	}

	public void register(String clientId) {
		send(clientId);
		this.clientId = clientId;
	}

	public static void main(String[] args) {
		String ip = args[0];
		int port = 0;
		try {
			port = Integer.valueOf(args[1]);
		} catch (Exception e) {
			System.out.println("端口号不合法！");
		}

		Client client = null;
		try {
			client = new Client(ip, port);
		} catch (Exception e) {
			System.out.println("网络异常");
			return;
		}

		Scanner scanner = new Scanner(System.in);

		System.out.print("请输入客户ID:");
		String clientId = scanner.nextLine();

		client.register(clientId);

		while (true) {
			String message = scanner.nextLine();
			client.send(message);
		}
	}
}
