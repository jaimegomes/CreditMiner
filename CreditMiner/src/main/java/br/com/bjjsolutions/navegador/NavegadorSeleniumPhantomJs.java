package br.com.bjjsolutions.navegador;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import br.com.bjjsolutions.model.LoginMB;

/**
 * Classe de navega��o utilizando Selenium + PhantomJS
 * 
 * @author Jaime Gomes
 * 
 */
@ManagedBean(name = "navegadorSeleniumPhantomJsBean")
public class NavegadorSeleniumPhantomJs {

	private final static String URL_INICIAL_CONSIGNUM = "http://sc.consignum.com.br/wmc-sc/login/selecao_parceiro.faces";
	private final static String URL_DISPONIBILIDADE_MARGEM = "http://sc.consignum.com.br/wmc-sc/pages/consultas/historico/pesquisa_colaborador.faces";
	private final static String URL_BYPASS = "http://sc.consignum.com.br/wmc-sc/pages/consultas/disponibilidade_margem/visualiza_margem_colaborador.faces";
	private final static String PATH_DOWNLOAD_IMG = "src/main/java/resources/captcha/";
	private final static String NAME_IMG = "captcha.png";
	private final static String PATH_ARQUIVO_HTML = "src/main/java/resources/htmls";
	private SetupSelenium setupSelenium = SetupSelenium.getInstance();
	private LoginMB loginMB;

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
	 * M�todo que navega pelo site e busca a imagem do captcha.
	 * 
	 * @return String SlinkImagem
	 * 
	 */
	@SuppressWarnings("static-access")
	public String getLinkImagemCaptcha() {

		long start = System.currentTimeMillis();

		StringBuilder linkImagem = new StringBuilder();

		try {
			// Acessa a p�gina do consignum
			setupSelenium.getWebDriver().get(URL_INICIAL_CONSIGNUM);
		} catch (Exception e) {
			System.out.println("Erro ao entrar na p�gina do consignum.\n"
					+ e.getMessage());
		}

		try {
			// Busca link Governo Estadual de Santa Catarina
			WebElement element = setupSelenium.getWebDriver().findElement(
					By.tagName("a").className("loginInicio"));

			/*
			 * Clica no link encontrado acima
			 */
			element.click();

		} catch (Exception e) {
			System.out
					.println("Erro ao pegar elemento que representa o link para p�gina de login.\n"
							+ e.getMessage());
		}

		// Executa pausa para dar tempo de carregar a o widget de login
		pause(500);

		try {

			// Obt�m o elemento img do recaptcha
			WebElement imgElement = setupSelenium.getWebDriver().findElement(
					By.tagName("img").id("recaptcha_challenge_image"));
			// Obt�m o link da imagem.
			linkImagem.append(imgElement.getAttribute("src"));

		} catch (Exception e) {
			new Exception(
					"Erro ao pegar elemento que representa o link do captcha.\n"
							+ e.getMessage());
		}

		long end = System.currentTimeMillis();

		System.out.println("tempo execu��o m�todo getLinkImagemCaptcha: "
				+ calculaTempoExecucao(start, end));

		return linkImagem.toString();

	}

	/**
	 * M�todo que recebe como par�metro o tempo de pausa em mili segundos.
	 * 
	 * Pausa utilizada para que d� tempo de executar o javascript do link da
	 * p�gina de login.
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

	private long calculaTempoExecucao(long start, long end) {

		return (end - start);
	}

	/**
	 * M�todo que salva o HTML da p�gina que est� sendo navegada, no caminho
	 * indicado pela vari�vel
	 * 
	 * @param html
	 */
	private void salvaHtml(String html, String nomeArquivo) {

		FileWriter arquivo;
		try {
			// arquivo = new FileWriter(new File("D:/Jaime/" + nomeArquivo));
			arquivo = new FileWriter(new File("/portal/Jaime/" + nomeArquivo));
			arquivo.write(html);
			arquivo.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * M�todo que faz o download da imagem do captcha
	 * 
	 * @param linkImagem
	 *            - Url da imagem que deseja fazer download
	 * @param targetDirectory
	 *            - Local onde ser� salva
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void downloadImage(StringBuilder linkImagem,
			String targetDirectory) throws MalformedURLException, IOException,
			FileNotFoundException {

		URL url = new URL(linkImagem.toString());
		BufferedImage bufImgOne = ImageIO.read(url);
		ImageIO.write(bufImgOne, "png", new File(targetDirectory));

	}

	/**
	 * M�todo que representa o action do bot�o entrar da nossa tela de login.
	 * 
	 * Este m�todo injeta as credenciais digitadas em nossa tela de login na
	 * tela de login do consignum e se loga no sistema.
	 * 
	 * TO-DO: m�todo deve ser refatorando para receber um caminho do arquivo csv
	 * e fazer a leitura do mesmo e buscar pelos cpfs
	 * 
	 */
	@SuppressWarnings("static-access")
	public void executeLogin() throws IOException {

		/*
		 * Pega os elementos que representam os campos de
		 * Usu�rio/Senha/Captcha/Bot�o de Entrar
		 */
		WebElement inputUsuario = setupSelenium.getWebDriver().findElement(
				By.tagName("input").id("j_id_jsp_1179747809_21"));
		WebElement inputPassword = SetupSelenium
				.getInstance()
				.getWebDriver()
				.findElement(By.tagName("input").name("j_id_jsp_1179747809_23"));
		WebElement inputCaptcha = SetupSelenium
				.getInstance()
				.getWebDriver()
				.findElement(By.tagName("input").id("recaptcha_response_field"));
		WebElement btnEntrar = setupSelenium.getWebDriver().findElement(
				By.tagName("button").id("j_id_jsp_1179747809_27"));

		/*
		 * Seta valores aos campos Usu�rio/Senha/CAPTCHA
		 */
		inputUsuario.sendKeys(loginMB.getLogin());
		inputPassword.sendKeys(loginMB.getSenha());
		inputCaptcha.sendKeys(loginMB.getCaptcha());

		// Clica no bot�o entrar
		btnEntrar.click();

		// pausa para o navegador carregar o html da p�gina principal ap�s login
		// pause(500);

		List<String> listCpf = new ArrayList<String>();
		listCpf.add("51202158900");
		listCpf.add("51112426949");
		listCpf.add("41533135991");
		listCpf.add("67326501904");
		listCpf.add("89874102934");
		listCpf.add("53457838968");
		listCpf.add("54884020987");
		listCpf.add("75147653953");
		listCpf.add("67942385949");
		listCpf.add("63809885991");

		getHtmlClientes(listCpf);

	}

	@SuppressWarnings("static-access")
	private void getHtmlClientes(List<String> listCpf) {

		long start = System.currentTimeMillis();

		if (!listCpf.isEmpty()) {

			for (String cpf : listCpf) {

				// Redireciona para a p�gina de disponibilidade de margem
				setupSelenium.getWebDriver().get(URL_DISPONIBILIDADE_MARGEM);

				// pausa para carregar a p�gina
				pause(1000);

				// Pega os elementos que representam o campo CPF e o bot�o
				// pesquisar
				WebElement inputCpf = SetupSelenium
						.getInstance()
						.getWebDriver()
						.findElement(
								By.tagName("input")
										.id("j_id_jsp_248910084_1:j_id_jsp_248910084_14"));
				WebElement btnPesquisar = SetupSelenium
						.getInstance()
						.getWebDriver()
						.findElement(
								By.tagName("button")
										.id("j_id_jsp_248910084_1:j_id_jsp_248910084_15"));

				// limpa o input caso tenha algum cpf
				inputCpf.clear();
				// Seta o valor do cpf
				inputCpf.sendKeys(cpf);

				// Clica no bot�o pesquisar
				btnPesquisar.click();

				// salvaHtml(setupSelenium.getWebDriver().getPageSource(),
				// "pagina.html");

				// pausa para carregar a p�gina
				pause(500);

				// Pega o elemento que cont�m o link para exibir o hist�rico do
				// cliente
				WebElement linkNome = SetupSelenium
						.getInstance()
						.getWebDriver()
						.findElement(  
								By.id("j_id_jsp_248910084_1:tabelaListaCol:0:j_id_jsp_248910084_23"));

				// Clica no elemento para exibir o hist�rico
				linkNome.click();

				// Redireciona para a p�gina do ByPass
				setupSelenium.getWebDriver().get(URL_BYPASS);

				// Salva o c�digo fonte da p�gina
				salvaHtml(setupSelenium.getWebDriver().getPageSource(), cpf
						+ ".html");

			}

		}

		long end = System.currentTimeMillis();

		System.out.println("tempo execu��o m�todo getHtmlClientes(): "
				+ calculaTempoExecucao(start, end));
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
	 * @return the pathArquivoHtml
	 */
	public static String getPathArquivoHtml() {
		return PATH_ARQUIVO_HTML;
	}

	/**
	 * @return the loginMB
	 */
	public LoginMB getLoginMB() {
		return loginMB;
	}

	/**
	 * @param loginMB
	 *            the loginMB to set
	 */
	public void setLoginMB(LoginMB loginMB) {
		this.loginMB = loginMB;
	}

	/**
	 * @return the urlInicialConsignum
	 */
	public static String getUrlInicialConsignum() {
		return URL_INICIAL_CONSIGNUM;
	}

	/**
	 * @return the urlDisponibilidadeMargem
	 */
	public static String getUrlDisponibilidadeMargem() {
		return URL_DISPONIBILIDADE_MARGEM;
	}

	/**
	 * @return the urlBypass
	 */
	public static String getUrlBypass() {
		return URL_BYPASS;
	}

}
