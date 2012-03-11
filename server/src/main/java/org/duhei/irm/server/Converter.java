package org.duhei.irm.server;

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
public class Converter implements Runnable {

	private Queue<String> receivings;
	private Queue<Message> messages;

	public Converter(Queue<Message> messages, Queue<String> receivings) {
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
		String command = null;

		Pattern pattern = Pattern.compile("^:\\S+");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			prefix = matcher.group().substring(1);
		}

		pattern = Pattern.compile("^\\s*\\S+\\s+");
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			command = matcher.group(0).trim();
		} else {
			return null;
		}

		int end = matcher.end();

		Message message = new Message();
		message.setPrefix(prefix);
		message.setCommand(command);
		message.setParams(str.substring(end));
		return message;
	}

	public static void main(String[] args) {
		Converter converter = new Converter(null, null);
		String str = ":USER:JF PRIVMSG JF JDF  ";
		Message message = converter.parse(str);
		System.out.println(message.getPrefix());
		System.out.println(message.getCommand());
		System.out.println(message.getParams());
	}
}
