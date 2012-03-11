package org.duhei.irm.server.util;

import java.util.Set;
import org.duhei.commons.util.ClassPathScanner;
import org.duhei.irm.server.message.Id;
import org.duhei.irm.server.message.MessageProcessor;

public class Hello {

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException {

		ClassPathScanner scanner = new ClassPathScanner();
		Set<Class<?>> processors = scanner.getPackageAllClasses(
				"org.duhei.irm.server.message.processor", false);
		for (Class classT : processors) {
			MessageProcessor messageProcessor = (MessageProcessor) classT
					.newInstance();
			Id id = messageProcessor.getClass().getAnnotation(Id.class);
			System.out.println(id.value());
		}
	}
}
