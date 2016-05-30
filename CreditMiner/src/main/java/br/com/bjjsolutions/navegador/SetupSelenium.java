package br.com.bjjsolutions.navegador;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SetupSelenium {

	private static SetupSelenium instance = null;
	private static DesiredCapabilities desiredCapabilities;
	private static WebDriver webDriver;
	private final static String PATH_DRIVER_PHANTOMJS = "D:/Jaime/phantomjs-2.1.1-windows/bin/phantomjs.exe";

	/**
	 * Singleton
	 * 
	 * @return instance
	 */
	public static synchronized SetupSelenium getInstance() {
		if (instance == null) {
			instance = new SetupSelenium();
			getWebDriverSetup();
		}

		return instance;

	}

	private static void getWebDriverSetup() {

		desiredCapabilities = new DesiredCapabilities();

		// ativa o javascript
		desiredCapabilities.setJavascriptEnabled(true);

		// Colocar o caminho do driver em uma propertie
		desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				PATH_DRIVER_PHANTOMJS);

		webDriver = new PhantomJSDriver(desiredCapabilities);

	}

	/**
	 * @return the desiredCapabilities
	 */
	public static DesiredCapabilities getDesiredCapabilities() {
		return desiredCapabilities;
	}

	/**
	 * @param desiredCapabilities
	 *            the desiredCapabilities to set
	 */
	public static void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
		SetupSelenium.desiredCapabilities = desiredCapabilities;
	}

	/**
	 * @return the webDriver
	 */
	public static WebDriver getWebDriver() {
		return webDriver;
	}

	/**
	 * @param webDriver
	 *            the webDriver to set
	 */
	public static void setWebDriver(WebDriver webDriver) {
		SetupSelenium.webDriver = webDriver;
	}

	/**
	 * @return the pathDriverPhantomjs
	 */
	public static String getPathDriverPhantomjs() {
		return PATH_DRIVER_PHANTOMJS;
	}

}
