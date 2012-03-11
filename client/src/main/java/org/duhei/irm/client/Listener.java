package org.duhei.irm.client;

import java.io.IOException;
import java.io.InputStream;
import org.duhei.commons.util.StringUtil;

public class Listener implements Runnable {

	private Client client;

	public Listener(Client client) {
		this.client = client;
	}

	public void run() {
		boolean isQuit = false;

		while (!isQuit) {

			InputStream is = null;

			try {
				is = client.getSocket().getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
				isQuit = true;
			}

			byte[] inBuffer = new byte[1024];
			int count = 0;
			StringBuffer sb = StringUtil.initBuffer();

			try {
				do {
					count = is.read(inBuffer);
					sb.append(new String(inBuffer, 0, count, Client.CHARSET));
				} while (count >= 1024);
			} catch (IOException e) {
				e.printStackTrace();
				isQuit = true;
			}

			client.addReceiving(sb.toString());
		}
	}

}
