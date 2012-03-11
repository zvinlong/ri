package org.duhei.irm.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Queue;

/**
 * @author zvin
 * 
 */
public class Sender implements Runnable {

	private Queue<String> sendings;
	private Socket socket;

	public Sender(Queue<String> sendings, Socket socket) {
		this.sendings = sendings;
		this.socket = socket;
	}

	public void run() {

		boolean isQuit = false;

		while (!isQuit) {

			if (!sendings.isEmpty()) {

				try {

					OutputStream os = socket.getOutputStream();

					os.write(sendings.poll().getBytes(Server.CHARSET));
					
				} catch (IOException e) {
					e.printStackTrace();
					isQuit = true;
				}
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					isQuit = true;
				}
			}
		}

	}
}
