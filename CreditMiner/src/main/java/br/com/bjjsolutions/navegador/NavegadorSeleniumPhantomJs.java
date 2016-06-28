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

import br.com.bjjsolutions.model.LoginMB;
import br.com.bjjsolutions.util.Util;
import br.com.bjjsolutions.xml.Cache;
import br.com.bjjsolutions.xml.HTMLJsoup;
import br.com.bjjsolutions.xml.WriteFileXML;

import com.opencsv.CSVReader;

/**
 * Classe de navega��o utilizando Selenium + PhantomJS
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
			setupSelenium.getWebDriver().get(URL_INICIAL_CONSIGNUM);

			WebElement element = setupSelenium
					.getWait()
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(".//*[@id='j_id_jsp_1088422203_1:j_id_jsp_1088422203_8:tbody_element']/tr/td[2]/a")));

			element.click();

			WebElement imgElement = setupSelenium.getWait().until(
					ExpectedConditions.visibilityOfElementLocated(By
							.xpath(".//*[@id='recaptcha_challenge_image']")));

			linkImagem.append(imgElement.getAttribute("src"));

		} catch (Exception e) {
			System.err.println("Erro ao capturar link do captcha.\n"
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			long end = System.currentTimeMillis();
			System.out.println("tempo execu��o m�todo getLinkImagemCaptcha: "
					+ calculaTempoExecucao(start, end));
		}
		return linkImagem.toString();
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
			arquivo = new FileWriter(new File(Util.getDirectorySO()
					+ nomeArquivo + ".html"));

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
	 */
	@SuppressWarnings("static-access")
	public void executeLogin() throws IOException {

		try {

			System.out.println("INICIO");

			long start = System.currentTimeMillis();

			/*
			 * Pega os elementos que representam os campos de
			 * Usu�rio/Senha/Captcha/Bot�o de Entrar
			 */
			WebElement inputUsuario = setupSelenium.getWait().until(
					ExpectedConditions.visibilityOfElementLocated(By
							.xpath(".//*[@id='j_id_jsp_1179747809_21']")));
			WebElement inputPassword = setupSelenium.getWait().until(
					ExpectedConditions.visibilityOfElementLocated(By
							.name("j_id_jsp_1179747809_23")));
			WebElement inputCaptcha = setupSelenium.getWait().until(
					ExpectedConditions.visibilityOfElementLocated(By
							.xpath(".//*[@id='recaptcha_response_field']")));
			WebElement btnEntrar = setupSelenium.getWait().until(
					ExpectedConditions.visibilityOfElementLocated(By
							.xpath(".//*[@id='j_id_jsp_1179747809_27']")));

			/*
			 * Seta valores aos campos Usu�rio/Senha/CAPTCHA
			 */
			inputUsuario.sendKeys(loginMB.getLogin());
			inputPassword.sendKeys(loginMB.getSenha());
			inputCaptcha.sendKeys(loginMB.getCaptcha());

			// Clica no bot�o entrar
			btnEntrar.click();

			// Processa os cpfs que est�o noi arquivo indicado
			processaCpfs(getListCpfsByFile());

			long end = System.currentTimeMillis();

			System.out.println("tempo processamento total: "
					+ calculaTempoExecucao(start, end));

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (Cache.clientesDTOCache != null) {
			WriteFileXML.gravaXMLListaClientes(Cache.clientesDTOCache,
					Util.getProperty("prop.diretorio.cache"));
		}

	}

	/**
	 * M�todo que l� os cpfs do arquivo csv.
	 * 
	 * TODO: esse m�todo deve receber o caminnho do arquivo como par�metro.
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
			reader = new CSVReader(new FileReader(new File(
					Util.getDirectorySO() + "cpf.csv")));
			listCpf = reader.readAll();

		} catch (Exception e) {
			throw new Exception("Erro: " + e.getMessage());
		} finally {
			reader.close();
		}

		return listCpf;
	}

	/**
	 * M�todo que recebe como par�metro o tempo de pausa em mili segundos.
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
			// Redireciona para a p�gina de disponibilidade de margem
			setupSelenium.getWebDriver().get(URL_DISPONIBILIDADE_MARGEM);

			for (String[] cpf : listCpf) {

				long start = System.currentTimeMillis();

				salvaHtml(setupSelenium.getWebDriver().getPageSource(), "teste");

				// Pega os elementos que representam o campo CPF e o bot�o
				// pesquisar
				WebElement inputCpf = setupSelenium
						.getWait()
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath(".//*[@id='j_id_jsp_248910084_1:j_id_jsp_248910084_14']")));
				WebElement btnPesquisar = setupSelenium
						.getWait()
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath(".//*[@id='j_id_jsp_248910084_1:j_id_jsp_248910084_15']")));

				// limpa o input caso tenha algum cpf
				inputCpf.clear();
				// Seta o valor do cpf
				inputCpf.sendKeys(cpf);

				// Clica no bot�o pesquisar
				btnPesquisar.click();

				/*
				 * Caso n�o encontre resultados, aumentar o tempo
				 */
				pause(1000);

				int qtdResultados = setupSelenium
						.getWait()
						.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
								.xpath("//*[contains(./@id, 'j_id_jsp_248910084_23')]")))
						.size();

				String strCpf = setupSelenium
						.getWait()
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath(".//*[@id='j_id_jsp_248910084_1:tabelaListaCol:tbody_element']/tr[1]/td[3]")))
						.getText();

				String matricula = setupSelenium
						.getWait()
						.until(ExpectedConditions.visibilityOfElementLocated(By
								.xpath(".//*[@id='j_id_jsp_248910084_1:tabelaListaCol:tbody_element']/tr/td[1]")))
						.getText();

				for (int i = 0; i < qtdResultados; i++) {

					// Pega o elemento que cont�m o link para exibir o hist�rico
					// do cliente
					WebElement linkNome = setupSelenium.getWait().until(
							ExpectedConditions.visibilityOfElementLocated(By
									.id("j_id_jsp_248910084_1:tabelaListaCol:"
											+ i + ":j_id_jsp_248910084_23")));

					// Clica no elemento para exibir o hist�rico
					linkNome.click();

					System.out.println("cpf: " + strCpf);

					System.out
							.println("qtd cpfs encontrados: " + qtdResultados);

					salvaHtml(setupSelenium.getWebDriver().getPageSource(),
							matricula + "-" + strCpf);

					// Redireciona para a p�gina do ByPass
					setupSelenium.getWebDriver().get(URL_BYPASS);

					// Salva o c�digo fonte da p�gina
					salvaHtml(setupSelenium.getWebDriver().getPageSource(),
							matricula + "-" + strCpf + "-margem");

					// volta para a p�gina de resultados
					setupSelenium.getWebDriver()
							.get(URL_DISPONIBILIDADE_MARGEM);

				}

				long end = System.currentTimeMillis();
				cont++;

				long totalTempoCpfs = calculaTempoExecucao(start, end);
				System.out.println("tempo processamento cpfs: "
						+ totalTempoCpfs);
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
