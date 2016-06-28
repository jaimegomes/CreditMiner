package br.com.bjjsolutions.navegador;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.bjjsolutions.enumerator.DriverPhantomJSEnum;
import br.com.bjjsolutions.enumerator.SystemEnum;

public class SetupSelenium {

	private static SetupSelenium instance = null;
	private static DesiredCapabilities desiredCapabilities;
	private static WebDriver webDriver;
	private static WebDriverWait wait;

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
		desiredCapabilities.setCapability("takesScreenshot", false);
		desiredCapabilities.setCapability("diskCache", true);
		desiredCapabilities.setCapability("loadImages", false);
		desiredCapabilities.setCapability("webSecurity", false);
		desiredCapabilities.setCapability("ignoreSslErros", true);
		
//		String[] phantomJsArgs = { "--load-images=no", "--disk-cache=yes", "--web-security=false", "--ignore-ssl-errors=true" };
//		desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, phantomJsArgs);

		if (System.getProperty("os.name").toUpperCase().equals(SystemEnum.LINUX.getSystem())) {
			desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, DriverPhantomJSEnum.PATH_DRIVER_PHANTOMJS_LINUX.getPath());
		} else {
			desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
					System.getProperty("phantomjs.binary.path", DriverPhantomJSEnum.PATH_DRIVER_PHANTOMJS_WINDOWS.getPath()));
		}

		webDriver = new PhantomJSDriver(desiredCapabilities);
		wait = new WebDriverWait(webDriver, 20);

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
	 * @return the wait
	 */
	public static WebDriverWait getWait() {
		return wait;
	}

	/**
	 * @param wait
	 *            the wait to set
	 */
	public static void setWait(WebDriverWait wait) {
		SetupSelenium.wait = wait;
	}

	public void closeWebDriver() {
		webDriver.close();
	}

}
