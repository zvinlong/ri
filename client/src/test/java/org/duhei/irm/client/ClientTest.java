package org.duhei.irm.client;

import java.util.Scanner;
import org.duhei.irm.server.Server;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {

	@Before
	public void setUp() {
		startServer();
	}
	
	public void HttpServerProvider(){
	}

	public void dupRegister() {
		
	}

	@Test
	public void testChat() throws InterruptedException {
		//PRIVMSG CLIENT:1@* HELLO
		String ip = "127.0.0.1";
		int port = 11111;

		Client client = new Client(ip, port);
		String clientId = "11";
		client.register(clientId);

		Client client2 = new Client(ip, port);
		String clientId2 = "12";
		client2.register(clientId2);

		client.startUpdateClientIds();
		
		Scanner scanner;
		while (true) {
			scanner = new Scanner(System.in);
			String message = scanner.nextLine();
			client2.send(message);
		}
	}

	private void startServer() {
		String id = "server001";
		Server server = new Server(id);
		String home = "D:\\workspace\\server";
		server.start(home);
	}

}
