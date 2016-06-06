package br.com.bjjsolutions.navegador;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import br.com.bjjsolutions.enumerator.DriverPhantomJSEnum;
import br.com.bjjsolutions.enumerator.SystemEnum;

public class SetupSelenium {

	private static SetupSelenium instance = null;
	private static DesiredCapabilities desiredCapabilities;
	private static WebDriver webDriver;

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
		String[] phantomJsArgs = { "--load-images=no", "--disk-cache=yes", "--web-security=false",
				"--ignore-ssl-errors=true" };
		desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, phantomJsArgs);

		if (System.getProperty("os.name").toUpperCase().equals(SystemEnum.LINUX.getSystem())) {
			desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
					DriverPhantomJSEnum.PATH_DRIVER_PHANTOMJS_LINUX.getPath());
		} else {
			desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, System
					.getProperty("phantomjs.binary.path", DriverPhantomJSEnum.PATH_DRIVER_PHANTOMJS_WINDOWS.getPath()));
		}

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
}
