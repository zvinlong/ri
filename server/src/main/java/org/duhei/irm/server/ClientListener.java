package org.duhei.irm.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.regex.Pattern;

import org.duhei.irm.server.util.Messages;

public class ClientListener implements Runnable {

	private Server server;

	public ClientListener(Server server) {
		this.server = server;
	}

	public void run() {

		boolean isQuit = false;

		while (!isQuit) {
			Socket socket = null;
			try {
				socket = server.getServerSocket().accept();
			} catch (IOException e) {
				e.printStackTrace();
			}

			InputStream is = null;
			byte[] inBuffer = new byte[1024];
			int count = 0;
			try {
				is = socket.getInputStream();
				count = is.read(inBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (count == 0) {
				continue;
			}

			String clientId;

			try {
				clientId = new String(inBuffer, 0, count, Server.CHARSET);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

			Session session = null;

			if (server.isClientSessionExist(clientId)) {
				session = server.getClientSession(clientId);
			} else {
				session = new Session(server);
			}

			session.addSocket(socket);
			session.setId(clientId);
			session.setType(SessionType.client);
			server.addClientSession(session);

			String msg = "Welcome to the Internet Relay Network <clientId>@<serverId>";
			Pattern pattern = Pattern.compile("<clientId>");
			msg = pattern.matcher(msg).replaceFirst(clientId);
			pattern = Pattern.compile("<serverId>");
			msg = pattern.matcher(msg).replaceFirst(server.getId());

			String source = Messages.makeServerPrefix(server.getId());
			server.sendToOne(clientId, source + msg);
			System.out.println(clientId + ":登录！");
		}
	}
}
