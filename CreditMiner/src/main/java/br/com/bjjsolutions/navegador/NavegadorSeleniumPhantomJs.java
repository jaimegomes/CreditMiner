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
import org.openqa.selenium.WebElement;

import br.com.bjjsolutions.model.LoginMB;

/**
 * Classe de navegação utilizando Selenium + PhantomJS
 * 
 * @author Jaime Gomes
 *
 */
@ManagedBean(name = "navegadorSeleniumPhantomJsBean")
public class NavegadorSeleniumPhantomJs {

	private final static String URL_INICIAL_CONSIGNUM = "https://sc.consignum.com.br/wmc-sc/login/selecao_parceiro.faces";
	private final static String URL_DISPONIBILIDADE_MARGEM = "http://sc.consignum.com.br/wmc-sc/pages/consultas/disponibilidade_margem/pesquisa_colaborador.faces";
	private final static String PATH_DOWNLOAD_IMG = "src/main/java/resources/captcha/";
	private final static String NAME_IMG = "captcha.png";
	private final static String PATH_ARQUIVO_HTML = "src/main/java/resources/htmls";
	private SetupSelenium setupSelenium = SetupSelenium.getInstance();
	private LoginMB loginMB;

	/**
	 * Construtor
	 */
	public NavegadorSeleniumPhantomJs() {
		this.loginMB = new LoginMB();

	}

	/**
	 * Método que navega pelo site e busca a imagem do captcha e faz download
	 * para exibir em nossa tela de login.
	 * 
	 */
	@SuppressWarnings("static-access")
	public String getLinkImagemCaptcha() {

		long start = System.currentTimeMillis();

		StringBuilder linkImagem = new StringBuilder();
		/*
		 * Acessa a página do consignum
		 */
		setupSelenium.getWebDriver().get(URL_INICIAL_CONSIGNUM);

		try {
			/*
			 * Busca link Governo Estadual de Santa Catarina
			 */
			WebElement element = setupSelenium.getWebDriver().findElement(By.tagName("a").className("loginInicio"));

			/*
			 * Clica no link encontrado acima
			 */
			element.click();
		} catch (Exception e) {
			new Exception("Erro ao pegar elemento que representa o link para página de login.\n" + e.getMessage());
		}

		/*
		 * Executa pausa para dar tempo de carregar a o widget de login
		 */
		pause(2000);

		try {
			/*
			 * Obtém o elemento img do recaptcha
			 */
			WebElement imgElement = setupSelenium.getWebDriver()
					.findElement(By.tagName("img").id("recaptcha_challenge_image"));
			/*
			 * Obtém o link da imagem.
			 */
			linkImagem.append(imgElement.getAttribute("src"));
		} catch (Exception e) {
			new Exception("Erro ao pegar elemento que representa o link do captcha.\n" + e.getMessage());
		}

		long end = System.currentTimeMillis();

		System.out.println("tempo execução método getLinkImagemCaptcha: " + calculaTempoExecucao(start, end));

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
			arquivo = new FileWriter(new File("D:/Jaime/" + nomeArquivo));
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
	 * Método que representa o action do botão entrar da nossa tela de login.
	 * 
	 * Este método injeta as credenciais digitadas em nossa tela de login na
	 * tela de login do consignum e se loga no sistema.
	 * 
	 */
	@SuppressWarnings("static-access")
	public void executeLogin() throws IOException {

		/*
		 * Pega os elementos que representam os campos de
		 * Usuário/Senha/Captcha/Botão de Entrar
		 */
		WebElement inputUsuario = setupSelenium.getWebDriver()
				.findElement(By.tagName("input").id("j_id_jsp_1179747809_21"));
		WebElement inputPassword = setupSelenium.getWebDriver()
				.findElement(By.tagName("input").name("j_id_jsp_1179747809_23"));
		WebElement inputCaptcha = setupSelenium.getWebDriver()
				.findElement(By.tagName("input").id("recaptcha_response_field"));
		WebElement btnEntrar = setupSelenium.getWebDriver()
				.findElement(By.tagName("button").id("j_id_jsp_1179747809_27"));

		/*
		 * Seta valores aos campos Usuário/Senha/CAPTCHA
		 */
		inputUsuario.sendKeys(loginMB.getLogin());
		inputPassword.sendKeys(loginMB.getSenha());
		inputCaptcha.sendKeys(loginMB.getCaptcha());

		/*
		 * Clica no botão entrar
		 */
		btnEntrar.click();

		/*
		 * pausa para o navegador carregar o html da página principal após login
		 */
		// pause(500);

		/*
		 * Redireciona para a página de disponibilidade de margem
		 */
		setupSelenium.getWebDriver().get(URL_DISPONIBILIDADE_MARGEM);

		long start = System.currentTimeMillis();

		/*
		 * Pega os elementos que representam o campo CPF e o bontão pesquisar
		 */
		WebElement inputCpf = setupSelenium.getWebDriver()
				.findElement(By.tagName("input").id("j_id_jsp_1326380073_14"));
		WebElement btnPesquisar = setupSelenium.getWebDriver()
				.findElement(By.tagName("button").id("j_id_jsp_1326380073_16"));

		/*
		 * Seta o valor do cpf
		 * 
		 * TO-DO: fazer lógica para ler o arquivo, adicionar os cpfs em uma
		 * lista e inserir todos no campo
		 */
		inputCpf.sendKeys("29072654900");

		/*
		 * Clica no botão pesquisar
		 */
		btnPesquisar.click();

		/*
		 * pausa para carregar a página
		 */
		pause(1500);

		salvaHtml(setupSelenium.getWebDriver().getPageSource(), "saida3.html");

		long end = System.currentTimeMillis();

		System.out.println("tempo execução método executaLogin(): " + calculaTempoExecucao(start, end));

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

}
