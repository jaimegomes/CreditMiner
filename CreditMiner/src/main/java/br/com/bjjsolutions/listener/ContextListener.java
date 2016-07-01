package br.com.bjjsolutions.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Classe que inicializa junto com o container do tomcat
 * 
 * @author Marcelo Lopes Nunes</br>
 *         bjjsolutions.com.br - 30/05/2016</br>
 *         <a href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public class ContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("Destroy aplication");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("=========== Inicializou CREDITMINER ===========");
	}

}
