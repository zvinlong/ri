package org.duhei.irm.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Queue;

import org.duhei.commons.util.StringUtil;

public class Receiver implements Runnable {

	private Queue<String> receivings;
	private Socket socket;
	private Session session;

	public Receiver(Session session, Socket socket) {
		this.receivings = session.getReceivings();
		this.socket = socket;
		this.session = session;
	}

	public void run() {

		boolean isQuit = false;

		while (!isQuit) {

			InputStream is = null;

			try {
				is = socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
				if (!socket.isConnected()) {
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}

			byte[] inBuffer = new byte[1024];
			int count = 0;
			StringBuffer sb = StringUtil.initBuffer();

			try {
				do {
					count = is.read(inBuffer);
					sb.append(new String(inBuffer, 0, count, Server.CHARSET));
				} while (count >= 1024);
			} catch (Exception e) {
				e.printStackTrace();
				if (!session.getSockets().isEmpty()) {
					Server.removeSession(session.getId());
				}
				isQuit = true;
			}

			receivings.add(sb.toString());

		}
	}
}
