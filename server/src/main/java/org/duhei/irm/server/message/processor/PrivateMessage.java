package org.duhei.irm.server.message.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.duhei.commons.util.Datas;
import org.duhei.irm.server.Message;
import org.duhei.irm.server.Server;
import org.duhei.irm.server.Session;
import org.duhei.irm.server.message.Id;
import org.duhei.irm.server.message.MessageProcessor;
import org.duhei.irm.server.util.Messages;

/**
 * @author zvin
 * 
 */
@Id("PRIVMSG")
public class PrivateMessage extends MessageProcessor {

	private static final String CLIENT = "CLIENT";
	private static final String CHANNEL = "CHANNEL";

	public void process(Session session, Server server, Message message) {
		String params = message.getParams();
		if (null != params) {

			Pattern pattern = Pattern.compile("^\\S+\\s+");
			Matcher matcher = pattern.matcher(params);
			if (!matcher.find()) {
				return;
			}

			String target = matcher.group();
			int targetEnd = matcher.end();

			String[] parts = target.trim().split("@");

			if (parts.length != 2) {
				return;
			}

			String left = parts[0];

			String right = parts[1];

			if ((!Datas.isEmpty(left))
					&& (Datas.isEmpty(right) || right.equals(server.getId()) || right
							.equals("*"))) {

				String msg = params.substring(targetEnd);

				if (left.contains(CLIENT)) {
					Pattern patternT = Pattern.compile(CLIENT + ":");
					Matcher matcherT = patternT.matcher(left);
					if (matcherT.find()) {
						int end = matcherT.end();
						String clientId = left.substring(end);

						String prefix = Messages.makeSource(session);

						server.sendToOne(clientId, prefix + msg);
					}
				} else if (left.contains(CHANNEL)) {

				} else if (left.equals("*")) {

				} else {
					warnTarget(target);
				}
			} else {
				warnTarget(target);
			}
		}
	}

	private void warnTarget(String target) {
		System.out.println("Target:" + target + " is not valid.");
	}
}
