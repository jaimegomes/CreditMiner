package br.com.bjjsolutions.navegador;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import br.com.bjjsolutions.dto.CsvDTO;
import br.com.bjjsolutions.model.LoginMB;
import br.com.bjjsolutions.util.Util;
import br.com.bjjsolutions.xml.Cache;
import br.com.bjjsolutions.xml.HTMLJsoup;
import br.com.bjjsolutions.xml.WriteFileXML;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

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
			System.out.println("tempo execução método getLinkImagemCaptcha: "
					+ calculaTempoExecucao(start, end));
		}
		return linkImagem.toString();
	}

	private static long calculaTempoExecucao(long start, long end) {
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
			arquivo = new FileWriter(new File(Util.getDirectorySO()
					+ nomeArquivo + ".html"));

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
	public static void downloadImage(StringBuilder linkImagem,
			String targetDirectory) throws MalformedURLException, IOException,
			FileNotFoundException {
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
			 * Seta valores aos campos Usuário/Senha/CAPTCHA
			 */
			inputUsuario.sendKeys(loginMB.getLogin());
			inputPassword.sendKeys(loginMB.getSenha());
			inputCaptcha.sendKeys(loginMB.getCaptcha());

			// Clica no botão entrar
			btnEntrar.click();

			// Processa os cpfs que estão noi arquivo indicado
			processaCpfs(parseCsvFileToBeans(CsvDTO.class));

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
	 * Método que transforma cada cpf do arquivo csv em um objeto CsvDTO, nele
	 * contem o cpf utilizado para as pesquisas
	 * 
	 * @param beanClass
	 * @return List<CsvDTO>
	 * @throws IOException
	 */
	public static <CsvDTO> List<CsvDTO> parseCsvFileToBeans(
			final Class<CsvDTO> beanClass) throws IOException {
		CSVReader reader = null;

		long start = System.currentTimeMillis();
		try {
			reader = new CSVReader(new BufferedReader(new FileReader(
					Util.getDirectorySO() + "cpf.csv")), ';');

			Map<String, String> columnMapping = new HashMap<String, String>();
			columnMapping.put("CPF", "cpf");

			HeaderColumnNameTranslateMappingStrategy<CsvDTO> strategy = new HeaderColumnNameTranslateMappingStrategy<CsvDTO>();
			strategy.setType(beanClass);
			strategy.setColumnMapping(columnMapping);

			final CsvToBean<CsvDTO> csv = new CsvToBean<CsvDTO>();

			long end = System.currentTimeMillis();

			System.out.println("tempo execução parseCsvFileToBeans: "
					+ calculaTempoExecucao(start, end));

			return csv.parse(strategy, reader);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}

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
	 * @param list
	 */
	@SuppressWarnings("static-access")
	private void processaCpfs(List<br.com.bjjsolutions.dto.CsvDTO> list) {

		try {
			int total = list.size();
			int cont = 0;
			// Redireciona para a página de disponibilidade de margem
			setupSelenium.getWebDriver().get(URL_DISPONIBILIDADE_MARGEM);

			for (CsvDTO csv : list) {

				String cpf = StringUtils.leftPad(csv.getCpf(), 11, "0");

				long start = System.currentTimeMillis();
				System.out.println("cpf: " + cpf);

				// Pega os elementos que representam o campo CPF e o botão
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

				// Clica no botão pesquisar
				btnPesquisar.click();

				/*
				 * Caso não encontre resultados, aumentar o tempo
				 */
				pause(1000);

				int qtdResultados = setupSelenium
						.getWait()
						.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By
								.xpath("//*[contains(./@id, 'j_id_jsp_248910084_23')]")))
						.size();

				System.out.println("qtd cpfs encontrados: " + qtdResultados);

				for (int i = 0; i < qtdResultados; i++) {

					// Pega o elemento que contém o link para exibir o histórico
					// do cliente
					WebElement linkNome = setupSelenium.getWait().until(
							ExpectedConditions.visibilityOfElementLocated(By
									.id("j_id_jsp_248910084_1:tabelaListaCol:"
											+ i + ":j_id_jsp_248910084_23")));

					// Clica no elemento para exibir o histórico
					linkNome.click();

					// Salva o código fonte da página sem a margem
					salvaHtml(setupSelenium.getWebDriver().getPageSource(), i
							+ "-" + cpf);

					// Redireciona para a página do ByPass
					setupSelenium.getWebDriver().get(URL_BYPASS);

					// Salva o código fonte da página com a margem
					salvaHtml(setupSelenium.getWebDriver().getPageSource(), i
							+ "-" + cpf + "-margem-");

					// volta para a página de resultados
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
