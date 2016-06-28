package br.com.bjjsolutions.util;

import java.util.Locale;
import java.util.ResourceBundle;

import br.com.bjjsolutions.enumerator.SystemEnum;

/**
 * Classe Util para criação de métodos utilitarios do sistema
 * 
 * @author Marcelo Lopes Nunes</br>
 *         bjjsolutions.com.br - 30/05/2016</br>
 *         <a href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public class Util {
	
	private static Locale localePtBR = null; 
	
	public static String getProperty(String key) {
		return getResourceBundle().getString(key);
	}
	
	private static ResourceBundle getResourceBundle() {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.messageJSF", getLocalePtBR());
		return resourceBundle;
	}
	
	private static Locale getLocalePtBR() {
		if (localePtBR == null) {
			localePtBR = new Locale("pt", "BR");
		}
		return localePtBR;
	}
	
	public static String getDirectorySO() {
		String directory = "";
		if (System.getProperty("os.name").toUpperCase().equals(SystemEnum.LINUX.getSystem())) {
			directory = Util.getProperty("prop.diretorio.home");
		} else {
			directory = Util.getProperty("prop.diretorio.d");
		}
		return directory;
	}
}