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
import br.com.bjjsolutions.xml.WriteFileCSV;
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
	private final static String URL_HISTORICO = "http://sc.consignum.com.br/wmc-sc/pages/consultas/historico/pesquisa_colaborador.faces";
	private final static String URL_BYPASS = "http://sc.consignum.com.br/wmc-sc/pages/consultas/disponibilidade_margem/visualiza_margem_colaborador.faces";
	// private SetupSelenium setupSelenium =
	// SetupSelenium.getInstance().getInstance();
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
	public String getLinkImagemCaptcha() {

		long start = System.currentTimeMillis();
		StringBuilder linkImagem = new StringBuilder();

		try {
			goTo(URL_INICIAL_CONSIGNUM);

			WebElement element = SetupSelenium.getInstance().getWait()
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_1088422203_1:j_id_jsp_1088422203_8:tbody_element']/tr/td[2]/a")));

			element.click();

			WebElement imgElement = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='recaptcha_challenge_image']")));

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

	/**
	 * Método que calcula o tempo de execução em mili segundos
	 * 
	 * @param start
	 * @param end
	 * @return tempo total de execução
	 */
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
			arquivo = new FileWriter(new File(Util.getDirectorySO() + nomeArquivo + ".html"));

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
	public void executeLogin() throws IOException {

		try {

			System.out.println("INICIO");

			long start = System.currentTimeMillis();

			insereCredenciais();

			processaCpfs(parseCsvFileToBeans(CsvDTO.class));

			long end = System.currentTimeMillis();

			System.out.println("tempo processamento total: " + calculaTempoExecucao(start, end));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SetupSelenium.getInstance().closeWebDriver();
			if (Cache.clientesDTOCache != null) {
				WriteFileXML.gravaXMLListaClientes(Cache.clientesDTOCache, Util.getProperty("prop.diretorio.cache"));
				WriteFileCSV.createCsvFile(Cache.clientesDTOCache, Util.getProperty("prop.diretorio.cache"));
			}
		}

	}

	/**
	 * Método que pega os elementos da página que representam os campos login,
	 * password, campo de resposta do captcha e o botão de entrar e adiciona os
	 * valores digitados em nossa página de login e se loga no site do consignum
	 */
	private void insereCredenciais() {

		WebElement inputUsuario = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_1179747809_21']")));
		WebElement inputPassword = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.name("j_id_jsp_1179747809_23")));
		WebElement inputCaptcha = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='recaptcha_response_field']")));
		WebElement btnEntrar = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_1179747809_27']")));

		inputUsuario.sendKeys(loginMB.getLogin());
		inputPassword.sendKeys(loginMB.getSenha());
		inputCaptcha.sendKeys(loginMB.getCaptcha());

		btnEntrar.click();
	}

	/**
	 * Método que transforma cada cpf do arquivo csv em um objeto CsvDTO, nele
	 * contem o cpf utilizado para as pesquisas
	 * 
	 * @param beanClass
	 * @return List<CsvDTO>
	 * @throws IOException
	 */
	@SuppressWarnings({ "hiding", "deprecation" })
	public static <CsvDTO> List<CsvDTO> parseCsvFileToBeans(final Class<CsvDTO> beanClass) throws IOException {
		CSVReader reader = null;

		try {
			reader = new CSVReader(new BufferedReader(new FileReader(Util.getDirectorySO() + "cpf.csv")), ';');

			Map<String, String> columnMapping = new HashMap<String, String>();
			columnMapping.put("CPF", "cpf");

			HeaderColumnNameTranslateMappingStrategy<CsvDTO> strategy = new HeaderColumnNameTranslateMappingStrategy<CsvDTO>();
			strategy.setType(beanClass);
			strategy.setColumnMapping(columnMapping);

			final CsvToBean<CsvDTO> csv = new CsvToBean<CsvDTO>();

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
	 * Método que captura os dados de acordo com os cpfs contidos na lista
	 * passada como parâmetro
	 * 
	 * @param List
	 *            <CsvDTO> list
	 */
	private void processaCpfs(List<br.com.bjjsolutions.dto.CsvDTO> list) {

		try {
			int total = list.size();
			int cont = 0;
			int qtdResultados = 0;

			goTo(URL_HISTORICO);

			for (CsvDTO csv : list) {

				String cpf = StringUtils.leftPad(csv.getCpf(), 11, "0");

				long start = System.currentTimeMillis();

				pesquisaCPF(cpf);

				try {
					qtdResultados = getQtdResultados();
				} catch (Exception e) {
					goTo(URL_HISTORICO);
					pesquisaCPF(cpf);
					qtdResultados = getQtdResultados();

				}

				System.out.println("cpf: " + cpf);
				System.out.println("qtd cpfs encontrados: " + qtdResultados);

				setMapJsoup(cpf, qtdResultados);

				long end = System.currentTimeMillis();
				cont++;

				long totalTempoCpfs = calculaTempoExecucao(start, end);
				System.out.println("tempo processamento cpfs: " + totalTempoCpfs);
				System.out.println("Status: " + cont + "/" + total);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Método que retorna a quantidade de resultados da pesquisa
	 * 
	 * @return
	 */
	private int getQtdResultados() {
		int qtdResultados = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(./@id, 'j_id_jsp_248910084_23')]"))).size();
		return qtdResultados;
	}

	/**
	 * Método que adiciona os dados pesquisados ao Map do Jsoup
	 * 
	 * @param cpf
	 * @param qtdResultados
	 */
	private void setMapJsoup(String cpf, int qtdResultados) {

		WebElement linkNome = null;
		for (int i = 0; i < qtdResultados; i++) {

			try {
				linkNome = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("j_id_jsp_248910084_1:tabelaListaCol:" + i + ":j_id_jsp_248910084_23")));
				linkNome.click();
			} catch (Exception e) {
				goTo(URL_HISTORICO);
				pesquisaCPF(cpf);
				linkNome = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("j_id_jsp_248910084_1:tabelaListaCol:" + i + ":j_id_jsp_248910084_23")));
				linkNome.click();
			}

			getInstanceHTMLJsoup().createObjectRecordHTML(SetupSelenium.getInstance().getWebDriver().getPageSource(), cpf + "-" + i);

			goTo(URL_BYPASS);

			getInstanceHTMLJsoup().createObjectRecordHTML(SetupSelenium.getInstance().getWebDriver().getPageSource(), cpf + "-" + i + "-margem");

			goTo(URL_HISTORICO);

		}
	}

	/**
	 * Método que pega os elementos inputCpf e btnPesquisar, insere o cpf no
	 * inputCpf e faz a pesquisa.
	 * 
	 * @param String
	 *            cpf
	 */
	private void pesquisaCPF(String cpf) {

		WebElement inputCpf = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_248910084_1:j_id_jsp_248910084_14']")));
		WebElement btnPesquisar = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_248910084_1:j_id_jsp_248910084_15']")));

		inputCpf.clear();
		inputCpf.sendKeys(cpf);
		btnPesquisar.click();

		pause(1000);

	}

	/**
	 * @return the loginMB
	 */
	public LoginMB getLoginMB() {
		return loginMB;
	}

	/**
	 * Singleton Jsoup
	 * 
	 * @return
	 */
	public HTMLJsoup getInstanceHTMLJsoup() {
		if (instanceHTMLJsoup == null) {
			instanceHTMLJsoup = new HTMLJsoup();
		}
		return instanceHTMLJsoup;
	}

	/**
	 * Método que redireciona para a url passada como parâmetro
	 * 
	 * @param url
	 */
	private void goTo(String url) {
		SetupSelenium.getInstance().getWebDriver().get(url);
	}
}
