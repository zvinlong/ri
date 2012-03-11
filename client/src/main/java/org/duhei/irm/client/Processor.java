package org.duhei.irm.client;

import java.util.Queue;

public class Processor implements Runnable {

	private MessageProcessor receptionProcessor = new MessageProcessor();
	private Queue<Message> messages;

	public Processor(Queue<Message> messages) {
		this.messages = messages;
	}

	public void run() {

		boolean isQuit = false;

		while (!isQuit) {

			if (!messages.isEmpty()) {
				Message message = messages.poll();
				receptionProcessor.process(message);
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void setReceptionProcessor(MessageProcessor receptionProcessor) {
		this.receptionProcessor = receptionProcessor;
	}

}
