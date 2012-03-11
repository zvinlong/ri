package org.duhei.irm.server.util;

import java.util.regex.Pattern;

import org.duhei.irm.server.Server;
import org.duhei.irm.server.Session;
import org.duhei.irm.server.SessionType;

public class Messages {

	private static final String serverPrefix = ":SERVER:<serverId> ";
	private static final String clientPrefix = ":CLIENT:<clientId> ";

	public static String makeServerPrefix(String serverId) {
		Pattern pattern = Pattern.compile("<serverId>");
		String msg = pattern.matcher(serverPrefix).replaceFirst(serverId);
		return msg;
	}

	public static String makeSource(Session session) {
		if (session.getType().equals(SessionType.server)) {
			return makeServerPrefix(session.getId());
		} else if (session.getType().equals(SessionType.client)) {
			Pattern pattern = Pattern.compile("<clientId>");
			String msg = pattern.matcher(clientPrefix).replaceFirst(
					session.getId());
			return msg;
		} else {
			System.out.println("Prefix is valid.");
		}
		return null;
	}

}
