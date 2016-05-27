package br.com.bjjsolutions.navegador;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.faces.bean.ManagedBean;
import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Classe de navegação utilizando Selenium + PhantomJS
 * 
 * @author Jaime Gomes
 *
 */
@ManagedBean(name="navegadorSeleniumPhantomJsBean")
public class NavegadorSeleniumPhantomJs {

	private DesiredCapabilities desiredCapabilities;
	private WebDriver webDriver;
	private static String URL_INICIAL_CONSIGNUM = "https://sc.consignum.com.br/wmc-sc/pages/";
	private final static String PATH_DOWNLOAD_IMG = "src/main/java/resources/captcha/";
	private final static String NAME_IMG = "captcha.png";
	private final static String PATH_ARQUIVO_HTML = "src/main/java/resources/htmls";
	private final static String PATH_DRIVER_PHANTOMJS = "D:/Jaime/phantomjs-2.1.1-windows/bin/phantomjs.exe";

	/**
	 * Construtor
	 */
	public NavegadorSeleniumPhantomJs() {

	}

	/**
	 * Método que navega pelo site e busca a imagem do captcha e faz download
	 * para exibir em nossa tela de login.
	 * 
	 */
	@SuppressWarnings("static-access")
	public String getLinkImagemCaptcha() {

		this.desiredCapabilities = getDesiredCapabilities();
		this.webDriver = getWebDriver();

		StringBuilder linkImagem = new StringBuilder();

		try {
			this.webDriver.get(URL_INICIAL_CONSIGNUM);

			// Busca link Governo Estadual de Santa Catarina
			WebElement element = this.webDriver.findElement(By.tagName("a").className("loginInicio"));

			// Clica no link encontrado acima
			element.click();

			// Executa pausa para dar tempo de carregar a o widget de login
			pause(2000);

			// Obtém o elemento img do recaptcha
			WebElement imgElement = this.webDriver.findElement(By.tagName("img").id("recaptcha_challenge_image"));
			// // Obtém o link da imagem.
			linkImagem.append(imgElement.getAttribute("src"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return linkImagem.toString();

	}

	/**
	 * Método que recebe como parâmetro o tempo de pausa em mili segundos.
	 * 
	 * Pausa utilizada para que dê tempo de executar o javascript do link da
	 * página de login.
	 * 
	 * @param timeMiliSec
	 */
	private void pause(long timeMiliSec) {
		try {
			Thread.sleep(timeMiliSec);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Método que salva o HTML da página que está sendo navegada, no caminho
	 * indicado pela variável
	 * 
	 * @param html
	 */
	private void salvaHtml(String html) {

		FileWriter arquivo;
		try {
			arquivo = new FileWriter(new File(PATH_ARQUIVO_HTML));
			arquivo.write(html);
			arquivo.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Método que faz o download da imagem do captcha
	 * 
	 * @param linkImagem
	 *            - Url da imagem que deseja fazer download
	 * @param targetDirectory
	 *            - Local onde será salva
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void downloadImage(StringBuilder linkImagem, String targetDirectory)
			throws MalformedURLException, IOException, FileNotFoundException {

		URL url = new URL(linkImagem.toString());
		BufferedImage bufImgOne = ImageIO.read(url);
		ImageIO.write(bufImgOne, "png", new File(targetDirectory));

	}

	/**
	 * @return the desiredCapabilities
	 */
	public DesiredCapabilities getDesiredCapabilities() {

		if (this.desiredCapabilities == null) {
			this.desiredCapabilities = new DesiredCapabilities();

			// ativa o javascript
			this.desiredCapabilities.setJavascriptEnabled(true);

			// Colocar o caminho do driver em uma propertie
			this.desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
					PATH_DRIVER_PHANTOMJS);

		}

		return this.desiredCapabilities;
	}

	public void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
		this.desiredCapabilities = desiredCapabilities;
	}

	/**
	 * @return the webDriver
	 */
	public WebDriver getWebDriver() {

		if (this.webDriver == null) {
			this.webDriver = new PhantomJSDriver(this.desiredCapabilities);
		}
		return this.webDriver;

	}

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	/**
	 * @return the pathDownloadImg
	 */
	public static String getPathDownloadImg() {
		return PATH_DOWNLOAD_IMG;
	}

	/**
	 * @return the nameImg
	 */
	public static String getNameImg() {
		return NAME_IMG;
	}

	/**
	 * @return the uRL_INICIAL_CONSIGNUM
	 */
	public static String getURL_INICIAL_CONSIGNUM() {
		return URL_INICIAL_CONSIGNUM;
	}

	/**
	 * @param uRL_INICIAL_CONSIGNUM
	 *            the uRL_INICIAL_CONSIGNUM to set
	 */
	public static void setURL_INICIAL_CONSIGNUM(String uRL_INICIAL_CONSIGNUM) {
		URL_INICIAL_CONSIGNUM = uRL_INICIAL_CONSIGNUM;
	}

	/**
	 * @return the pathArquivoHtml
	 */
	public static String getPathArquivoHtml() {
		return PATH_ARQUIVO_HTML;
	}

	/**
	 * @return the pathDriverPhantomjs
	 */
	public static String getPathDriverPhantomjs() {
		return PATH_DRIVER_PHANTOMJS;
	}

}
