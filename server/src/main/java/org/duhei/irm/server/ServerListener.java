package org.duhei.irm.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ServerListener {

	private Server server;

	public ServerListener(Server server) {
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

			String serverId;

			try {
				serverId = new String(inBuffer, 0, count, Server.CHARSET);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

			Session session = null;

			if (server.isServerSessionExist(serverId)) {
				session = server.getServerSession(serverId);
			} else {
				session = new Session(server);
			}

			session.addSocket(socket);

			server.addServerSession(session);
			System.out.println(serverId + ":登录！");
		}
	}
}
