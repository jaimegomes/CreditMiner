package br.com.bjjsolutions.enumerator;

import br.com.bjjsolutions.util.Util;

/**
 * Enum de definição dos caminhos absolutos do Driver PhantomJS
 * 
 * @author Marcelo Lopes Nunes</br>
 *         bjjsolutions.com.br - 30/05/2016</br>
 *         <a href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public enum DriverPhantomJSEnum {

	PATH_DRIVER_PHANTOMJS_WINDOWS(Util.getInstanceProperties().getProperty("prop.path.driver.phantomjs.windows")), 
	PATH_DRIVER_PHANTOMJS_LINUX(Util.getInstanceProperties().getProperty("prop.path.driver.phantomjs.linux"));

	private String path;

	DriverPhantomJSEnum(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}