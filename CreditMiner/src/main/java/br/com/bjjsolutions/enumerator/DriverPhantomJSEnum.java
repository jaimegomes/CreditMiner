package br.com.bjjsolutions.enumerator;

/**
 * Enum de definição dos caminhos absolutos do Driver PhantomJS
 * 
 * @author Marcelo Lopes Nunes</br>
 *         bjjsolutions.com.br - 30/05/2016</br>
 *         <a href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public enum DriverPhantomJSEnum {

	PATH_DRIVER_PHANTOMJS_WINDOWS("D:/Jaime/phantomjs-2.1.1-windows/bin/phantomjs.exe"), 
	PATH_DRIVER_PHANTOMJS_LINUX("/usr/local/bin/phantomjs");

	private String path;

	DriverPhantomJSEnum(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}