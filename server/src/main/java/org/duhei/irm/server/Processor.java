package org.duhei.irm.server;

import java.util.Queue;

import org.duhei.irm.server.message.MessageProcessor;

/**
 * 
 * @author zvin
 * 
 */
public class Processor implements Runnable {

	private Session session;
	private Server server;

	public Processor(Session session, Server server) {
		this.session = session;
		this.server = server;
	}

	public void run() {

		boolean isQuit = false;
		Queue<Message> messages = session.getMessages();

		while (!isQuit) {

			if (!messages.isEmpty()) {

				Message message = messages.poll();
				String command = message.getCommand();
				MessageProcessor messageProcessor = server
						.getMessageProcessor(command);

				if (messageProcessor != null) {
					messageProcessor.process(session, server, message);
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
