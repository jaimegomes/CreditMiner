package br.com.bjjsolutions.navegador;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVReader;

import br.com.bjjsolutions.enumerator.SystemEnum;
import br.com.bjjsolutions.model.LoginMB;
import br.com.bjjsolutions.util.Util;
import br.com.bjjsolutions.xml.Cache;
import br.com.bjjsolutions.xml.HTMLJsoup;
import br.com.bjjsolutions.xml.WriteFileXML;

/**
 * Classe de navegação utilizando Selenium + PhantomJS
 * 
 * @author Jaime Gomes
 * 
 */
@ManagedBean(name = "navegadorSeleniumPhantomJsBean")
@RequestScoped
public class NavegadorSeleniumPhantomJs {

	private final static String URL_INICIAL_CONSIGNUM = "http://sc.consignum.com.br/wmc-sc/login/selecao_parceiro.faces";
	private final static String URL_DISPONIBILIDADE_MARGEM = "http://sc.consignum.com.br/wmc-sc/pages/consultas/historico/pesquisa_colaborador.faces";
	private final static String URL_BYPASS = "http://sc.consignum.com.br/wmc-sc/pages/consultas/disponibilidade_margem/visualiza_margem_colaborador.faces";
	private final static String PATH_DOWNLOAD_IMG = "src/main/java/resources/captcha/";
	private final static String NAME_IMG = "captcha.png";
	private final static String PATH_ARQUIVO_HTML = "src/main/java/resources/htmls";

	private SetupSelenium setupSelenium = SetupSelenium.getInstance();
	private LoginMB loginMB;
	private HTMLJsoup instanceHTMLJsoup;

	/**
	 * Construtor
	 */
	public NavegadorSeleniumPhantomJs() {

	}

	@PostConstruct
	public void init() {
		this.loginMB = new LoginMB();
	}

	/**
	 * Método que navega pelo site e busca a imagem do captcha.
	 * 
	 * @return String SlinkImagem
	 * 
	 */
	@SuppressWarnings("static-access")
	public String getLinkImagemCaptcha() {

		long start = System.currentTimeMillis();
		StringBuilder linkImagem = new StringBuilder();

		try {
			setupSelenium.getWebDriver().get(URL_INICIAL_CONSIGNUM);

			WebElement element = (new WebDriverWait(setupSelenium.getWebDriver(), 10)).until(ExpectedConditions.visibilityOfElementLocated(By.className("loginInicio")));

			element.click();

			WebElement imgElement = (new WebDriverWait(setupSelenium.getWebDriver(), 10)).until(ExpectedConditions.visibilityOfElementLocated(By.id("recaptcha_challenge_image")));

			linkImagem.append(imgElement.getAttribute("src"));

		} catch (Exception e) {
			System.err.println("Erro ao capturar link do captcha.\n" + e.getMessage());
			e.printStackTrace();
		} finally {
			long end = System.currentTimeMillis();
			System.out.println("tempo execução método getLinkImagemCaptcha: " + calculaTempoExecucao(start, end));
		}
		return linkImagem.toString();
	}

	private long calculaTempoExecucao(long start, long end) {
		return (end - start);
	}

	/**
	 * Método que salva o HTML da página que está sendo navegada, no caminho
	 * indicado pela variável
	 * 
	 * @param html
	 */
	private void salvaHtml(String html, String nomeArquivo) {
		FileWriter arquivo;
		try {
			if (System.getProperty("os.name").toUpperCase().equals(SystemEnum.LINUX.getSystem())) {
				arquivo = new FileWriter(new File(Util.getProperty("prop.diretorio.home") + nomeArquivo));
			} else {
				arquivo = new FileWriter(new File(Util.getProperty("prop.diretorio.d") + nomeArquivo));
			}

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
	public static void downloadImage(StringBuilder linkImagem, String targetDirectory) throws MalformedURLException, IOException, FileNotFoundException {
		URL url = new URL(linkImagem.toString());
		BufferedImage bufImgOne = ImageIO.read(url);
		ImageIO.write(bufImgOne, "png", new File(targetDirectory));
	}

	/**
	 * Método que representa o action do botão entrar da nossa tela de login.
	 * 
	 * Este método injeta as credenciais digitadas em nossa tela de login na
	 * tela de login do consignum e se loga no sistema.
	 * 
	 */
	@SuppressWarnings("static-access")
	public void executeLogin() throws IOException {

		try {

			System.out.println("INICIO");

			long start = System.currentTimeMillis();

			/*
			 * Pega os elementos que representam os campos de
			 * Usuário/Senha/Captcha/Botão de Entrar
			 */
			WebElement inputUsuario = setupSelenium.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.tagName("input").id("j_id_jsp_1179747809_21")));
			WebElement inputPassword = setupSelenium.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.tagName("input").name("j_id_jsp_1179747809_23")));
			WebElement inputCaptcha = setupSelenium.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.tagName("input").id("recaptcha_response_field")));
			WebElement btnEntrar = setupSelenium.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.tagName("button").id("j_id_jsp_1179747809_27")));

			/*
			 * Seta valores aos campos Usuário/Senha/CAPTCHA
			 */
			inputUsuario.sendKeys(loginMB.getLogin());
			inputPassword.sendKeys(loginMB.getSenha());
			inputCaptcha.sendKeys(loginMB.getCaptcha());

			// Clica no botão entrar
			btnEntrar.click();

			// Processa os cpfs que estão noi arquivo indicado
			processaCpfs(getListCpfsByFile());

			long end = System.currentTimeMillis();

			System.out.println("tempo processamento total: " + calculaTempoExecucao(start, end));

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (Cache.clientesDTOCache != null) {
			WriteFileXML.gravaXMLListaClientes(Cache.clientesDTOCache, Util.getProperty("prop.diretorio.cache"));
		}

	}

	/**
	 * Método que lê os cpfs do arquivo csv.
	 * 
	 * TODO: esse método deve receber o caminnho do arquivo como parâmetro.
	 * 
	 * @param listCpf
	 * @throws Exception
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private List<String[]> getListCpfsByFile() throws Exception {

		List<String[]> listCpf = null;
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(new File(Util.getProperty("prop.diretorio.d") + "cpf.csv")));
			listCpf = reader.readAll();

		} catch (Exception e) {
			throw new Exception("Erro: " + e.getMessage());
		} finally {
			reader.close();
		}

		return listCpf;
	}

	/**
	 * Método que recebe como parâmetro o tempo de pausa em mili segundos.
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
	 * 
	 * @param listCpf
	 */
	@SuppressWarnings("static-access")
	private void processaCpfs(List<String[]> listCpf) {

		try {
			int total = listCpf.size();
			int cont = 0;
			// Redireciona para a página de disponibilidade de margem
			setupSelenium.getWebDriver().get(URL_DISPONIBILIDADE_MARGEM);

			for (String[] cpf : listCpf) {

				salvaHtml(setupSelenium.getWebDriver().getPageSource(), "saida1.html");

				long start = System.currentTimeMillis();

				// Pega os elementos que representam o campo CPF e o botão
				// pesquisar
				WebElement inputCpf = setupSelenium.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.tagName("input").id("j_id_jsp_248910084_1:j_id_jsp_248910084_14")));
				WebElement btnPesquisar = setupSelenium.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.tagName("button").id("j_id_jsp_248910084_1:j_id_jsp_248910084_15")));

				// limpa o input caso tenha algum cpf
				inputCpf.clear();
				// Seta o valor do cpf
				inputCpf.sendKeys(cpf);
				// Clica no botão pesquisar
				btnPesquisar.click();

				/*
				 * Caso não encontre resultados, aumentar o tempo
				 */
				pause(1000);

				salvaHtml(setupSelenium.getWebDriver().getPageSource(), "saida2.html");

				int qtdResultados = setupSelenium.getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(./@id, 'j_id_jsp_248910084_23')]"))).size();

				System.out.println("qtd cpfs encontrados: " + qtdResultados);

				for (int i = 0; i < qtdResultados; i++) {

					// Pega o elemento que contém o link para exibir o histórico
					// do cliente
					WebElement linkNome = setupSelenium.getWait()
							.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_248910084_1:tabelaListaCol:" + i + ":j_id_jsp_248910084_23']")));

					// Clica no elemento para exibir o histórico
					linkNome.click();

					salvaHtml(setupSelenium.getWebDriver().getPageSource(), i + "saida.html");

					// Redireciona para a página do ByPass
					setupSelenium.getWebDriver().get(URL_BYPASS);

					// new
					// HTMLJsoup(setupSelenium.getWebDriver().getPageSource());
					// Salva o código fonte da página
					salvaHtml(setupSelenium.getWebDriver().getPageSource(), System.currentTimeMillis() + ".html");

					// volta para a página de resultados
					setupSelenium.getWebDriver().get(URL_DISPONIBILIDADE_MARGEM);

				}

				long end = System.currentTimeMillis();
				cont++;

				System.out.println("Status: " + cont + "/" + total);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setupSelenium.closeWebDriver();
		}

	}

	/**
	 * @return the loginMB
	 */
	public LoginMB getLoginMB() {
		return loginMB;
	}

	public HTMLJsoup getInstanceHTMLJsoup() {
		if (instanceHTMLJsoup == null) {
			instanceHTMLJsoup = new HTMLJsoup();
		}
		return instanceHTMLJsoup;
	}
}
