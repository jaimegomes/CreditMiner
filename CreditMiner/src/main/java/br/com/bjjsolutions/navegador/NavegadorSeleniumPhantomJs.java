package br.com.bjjsolutions.navegador;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import br.com.bjjsolutions.dto.CsvDTO;
import br.com.bjjsolutions.mb.ConfiguracaoMB;
import br.com.bjjsolutions.model.LoginMB;
import br.com.bjjsolutions.processing.Cache;
import br.com.bjjsolutions.processing.HTMLJsoup;
import br.com.bjjsolutions.processing.WriteFileCSV;
import br.com.bjjsolutions.processing.WriteFileXML;
import br.com.bjjsolutions.util.Util;

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
	 * @return String linkImagem
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
			System.out.println("tempo execução método getLinkImagemCaptcha: " + Util.calculaTempoExecucao(start, end));
		}
		return linkImagem.toString();
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
			ConfiguracaoMB.setIsLogin(true);

			insereCredenciais();

		} catch (Exception e) {
			ConfiguracaoMB.setIsLogin(false);
			e.printStackTrace();
		}

	}
	
	public void initMiner() throws IOException {

		try {

			System.out.println("INICIO");

			long start = System.currentTimeMillis();

			processaCpfs(Util.parseCsvFileToBeans(CsvDTO.class));

			long end = System.currentTimeMillis();

			System.out.println("tempo processamento total: " + Util.calculaTempoExecucao(start, end));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (Cache.clientesDTOCache != null) {
				WriteFileXML.gravaXMLListaClientes(Cache.clientesDTOCache, Util.getProperty("prop.diretorio.cache"));
				WriteFileCSV.createCsvFile(Cache.clientesDTOCache, Util.getProperty("prop.diretorio.cache"));
			}
		}

	}


	/**
	 * Método que pega os elementos da página de login que representam os campos
	 * login, password, campo de resposta do captcha e o botão de entrar,
	 * adiciona os valores digitados em nossa página de login e se loga no site
	 * do consignum
	 */
	private void insereCredenciais() {
		WebElement inputUsuario = null;
		WebElement inputPassword = null;
		WebElement inputCaptcha = null;
		WebElement btnEntrar = null;

		try {
			inputUsuario = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_1179747809_21']")));
			inputPassword = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.name("j_id_jsp_1179747809_23")));
			inputCaptcha = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='recaptcha_response_field']")));
			btnEntrar = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_1179747809_27']")));

		} catch (Exception e) {
			insereCredenciais();
		}

		inputUsuario.sendKeys(loginMB.getLogin());
		inputPassword.sendKeys(loginMB.getSenha());
		inputCaptcha.sendKeys(loginMB.getCaptcha());

		btnEntrar.click();
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

				qtdResultados = getQtdResultados(cpf);

				System.out.println("cpf: " + cpf);
				System.out.println("qtd cpfs encontrados: " + qtdResultados);

				setMapJsoup(cpf, qtdResultados);

				long end = System.currentTimeMillis();
				cont++;

				long totalTempoCpfs = Util.calculaTempoExecucao(start, end);
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
	 * @return int qtdResultados
	 */
	private int getQtdResultados(String cpf) {
		int qtdResultados = 0;
		try {
			qtdResultados = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[contains(./@id, 'j_id_jsp_248910084_23')]"))).size();
		} catch (Exception e) {
			goTo(URL_HISTORICO);
			pesquisaCPF(cpf);
			qtdResultados = getQtdResultados(cpf);
		}

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
				int qtdResults = getQtdResultados(cpf);
				setMapJsoup(cpf, qtdResults);
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
		WebElement inputCpf = null;
		WebElement btnPesquisar = null;
		try {
			inputCpf = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_248910084_1:j_id_jsp_248910084_14']")));
			btnPesquisar = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_248910084_1:j_id_jsp_248910084_15']")));

		} catch (Exception e) {
			goTo(URL_HISTORICO);
			pesquisaCPF(cpf);
		}

		inputCpf.clear();
		inputCpf.sendKeys(cpf);
		btnPesquisar.click();

		Util.pause(1000);

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
