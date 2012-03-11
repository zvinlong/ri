package org.duhei.irm.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageProcessor {

	private static MessageProcessor instance;

	protected MessageProcessor() {
	}

	public static MessageProcessor create() {
		if (null == instance) {
			instance = new MessageProcessor();
		}
		return instance;
	}

	public void process(Message message) {
		Pattern pattern = Pattern.compile("^\\S+:");
		Matcher matcher = pattern.matcher(message.getPrefix());
		int end = 0;
		if (matcher.find()) {
			end = matcher.end();
		}
		System.out.println(message.getPrefix().substring(end) + ":"
				+ message.getContent());
	}
}
