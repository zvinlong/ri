package org.duhei.irm.client;

import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.duhei.commons.util.StringUtil;

/**
 * 转化receiving to message
 * 
 * @author zvin
 * 
 */
public class Convertor implements Runnable {

	private Queue<String> receivings;
	private Queue<Message> messages;

	public Convertor(Queue<Message> messages, Queue<String> receivings) {
		this.receivings = receivings;
		this.messages = messages;
	}

	public void run() {

		boolean isQuit = false;

		while (!isQuit) {
			if (!receivings.isEmpty()) {
				String receiving = receivings.poll();
				Message message = parse(receiving);
				if (null != message) {
					messages.add(message);
				}
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Message parse(String str) {
		if (StringUtil.isEmpty(str)) {
			throw new IllegalArgumentException(
					"[ERROR]:The parsing message can't be empty.");
		}
		String prefix = null;
		String content = null;
		int prefixEnd = 0;

		Pattern pattern = Pattern.compile("^:\\S+\\s+");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			prefix = matcher.group().substring(1);
			prefixEnd = matcher.end();
		}

		content = str.substring(prefixEnd);

		Message message = new Message();
		message.setPrefix(prefix);
		message.setContent(content);
		return message;
	}
	
}
