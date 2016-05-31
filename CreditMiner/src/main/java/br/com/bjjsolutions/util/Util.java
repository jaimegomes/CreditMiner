package br.com.bjjsolutions.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe Util para criação de métodos utilitarios do sistema
 * 
 * @author Marcelo Lopes Nunes</br>
 *         bjjsolutions.com.br - 30/05/2016</br>
 *         <a href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public class Util {
	
	private static Properties properties; 
	
	/**
	 * Singleton de properties
	 * @return properties
	 * @throws IOException
	 */
	public static Properties getInstanceProperties() {
		
		if (properties == null) {
			properties = new Properties();
		}
		FileInputStream file;
		try {
			file = new FileInputStream("./properties/config.properties");
			properties.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return properties;
	}
}