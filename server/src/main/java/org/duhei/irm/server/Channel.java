package org.duhei.irm.server;

import java.util.Map;

public class Channel {

	private Map<String, Session> sessions;

	public void addSession(Session session) {
		Session addingSession = sessions.get(session.getId());
		if (null == addingSession) {
			sessions.put(session.getId(), session);
		}
	}

	public Map<String, Session> getSessions() {
		return sessions;
	}

	public void setSessions(Map<String, Session> sessions) {
		this.sessions = sessions;
	}

}
