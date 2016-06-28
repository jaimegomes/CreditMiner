package br.com.bjjsolutions.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.com.bjjsolutions.xml.HTMLProcessThread;

public class ContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("Destroy aplication");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

//		Thread thread = new Thread(new HTMLProcessThread());
//		thread.start();
//		// dorme querida
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

	}

}
