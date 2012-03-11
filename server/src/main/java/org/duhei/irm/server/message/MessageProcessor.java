package org.duhei.irm.server.message;

import org.duhei.irm.server.Message;
import org.duhei.irm.server.Server;
import org.duhei.irm.server.Session;

/**
 * @author zvin
 * 
 */
public abstract class MessageProcessor {

	public abstract void process(Session session, Server server, Message message);

}
