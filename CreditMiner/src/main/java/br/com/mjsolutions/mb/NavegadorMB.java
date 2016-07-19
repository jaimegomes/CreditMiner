package br.com.mjsolutions.mb;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.mjsolutions.controller.FileController;
import br.com.mjsolutions.controller.UsuarioController;
import br.com.mjsolutions.dto.CsvDTO;
import br.com.mjsolutions.model.Usuario;
import br.com.mjsolutions.processing.Cache;
import br.com.mjsolutions.processing.HTMLJsoup;
import br.com.mjsolutions.processing.WriteFileCSV;
import br.com.mjsolutions.setup.SetupSelenium;
import br.com.mjsolutions.util.Util;

/**
 * Classe de navegação utilizando Selenium + PhantomJS
 * 
 * @author Jaime Gomes
 * 
 */
@ManagedBean(name = "navegadorMB")
@SessionScoped
public class NavegadorMB {

	private final static String URL_INICIAL_CONSIGNUM = "http://sc.consignum.com.br/wmc-sc/login/selecao_parceiro.faces";
	private final static String URL_HISTORICO = "http://sc.consignum.com.br/wmc-sc/pages/consultas/historico/pesquisa_colaborador.faces";
	private final static String URL_BYPASS = "http://sc.consignum.com.br/wmc-sc/pages/consultas/disponibilidade_margem/visualiza_margem_colaborador.faces";
	private UsuarioController usuarioController;
	private FileController fileController;
	private HTMLJsoup instanceHTMLJsoup;
	private String captcha;
	private Usuario usuario;

	private int cont = 0;
	private boolean finalizado;
	private int total = 0;
	private String mensagemDoStatus = "";

	/**
	 * Construtor
	 */
	public NavegadorMB() {

	}

	@PostConstruct
	public void init() {
		this.setUsuario(new Usuario());
		this.usuarioController = new UsuarioController();
		this.fileController = new FileController();
		PathPageMB.isLogin(false);
		this.fileController.listCSVDir();
	}

	/**
	 * Método que navega pelo site e busca a imagem do captcha.
	 * 
	 * @return String linkImagem
	 * 
	 */
	public String getLinkImagemCaptcha() {

		// long start = System.currentTimeMillis();
		StringBuilder linkImagem = new StringBuilder();

		try {
			goTo(URL_INICIAL_CONSIGNUM);

			Util.pause(1000);
			WebElement element = SetupSelenium.getInstance().getWait()
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_1088422203_1:j_id_jsp_1088422203_8:tbody_element']/tr/td[2]/a")));

			element.click();

			WebElement imgElement = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='recaptcha_challenge_image']")));

			linkImagem.append(imgElement.getAttribute("src"));

		} catch (Exception e) {
			System.err.println("Erro ao capturar link do captcha.\n" + e.getMessage());
			e.printStackTrace();
		} finally {
			// long end = System.currentTimeMillis();
			// System.out.println("tempo execução método getLinkImagemCaptcha: "
			// + Util.calculaTempoExecucao(start, end));
		}
		return linkImagem.toString();
	}

	/**
	 * Método que representa o action do botão entrar da nossa tela de login.
	 * 
	 * Este método injeta as usuarioController digitadas em nossa tela de login
	 * na tela de login do consignum e se loga no sistema.
	 * 
	 */
	public void executeLogin() throws IOException {

		insereCredenciais();

	}

	public void initMiner() throws IOException {
		String fileName = "";
		try {

			// System.out.println("INICIO");

			// long start = System.currentTimeMillis();

			fileName = fileController.upload();

			processaCpfs(Util.parseCsvFileToBeans(CsvDTO.class, fileName));
			PathPageMB.isLogin(true);

			// long end = System.currentTimeMillis();

			// System.out.println("tempo processamento total: " +
			// Util.calculaTempoExecucao(start, end));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (Cache.clientesDTOCache != null) {
				// WriteFileXML.gravaXMLListaClientes(Cache.clientesDTOCache,
				// Util.getProperty("prop.diretorio.cache"));
				WriteFileCSV.createCsvFile(Cache.clientesDTOCache, Util.getProperty("prop.diretorio.cache") + fileName);

				// Atualiza a lista da tela
				this.fileController.listCSVDir();
			}
		}

	}

	/**
	 * Método que pega os elementos da página de login que representam os campos
	 * login, password, campo de resposta do captcha e o botão de entrar,
	 * adiciona os valores digitados em nossa página de login e se loga no site
	 * do consignum
	 */
	@SuppressWarnings("unused")
	private void insereCredenciais() {
		WebElement inputUsuario = null;
		WebElement inputPassword = null;
		WebElement inputCaptcha = null;
		WebElement btnEntrar = null;
		WebElement logado = null;
		Usuario usuario = null;
		try {

			usuario = usuarioController.findUsuarioById(usuarioController.getUsuarioSelecionado());
			inputUsuario = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_1179747809_21']")));
			inputPassword = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.name("j_id_jsp_1179747809_23")));
			inputCaptcha = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='recaptcha_response_field']")));
			btnEntrar = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_1179747809_27']")));

			inputUsuario.sendKeys(usuario.getLogin());
			inputPassword.sendKeys(usuario.getSenha());
			inputCaptcha.sendKeys(this.captcha);

			btnEntrar.click();

			logado = new WebDriverWait(SetupSelenium.getInstance().getWebDriver(), 3).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(./@id, 'j_id_jsp_252844863_0pc3')]")));
			PathPageMB.isLogin(true);

		} catch (Exception e) {
			captcha = "";
			goTo(URL_INICIAL_CONSIGNUM);
			PathPageMB.isLogin(false);

		}

	}

	/**
	 * Método que captura os dados de acordo com os cpfs contidos na lista
	 * passada como parâmetro
	 * 
	 * @param List
	 *            <CsvDTO> list
	 */
	private void processaCpfs(List<br.com.mjsolutions.dto.CsvDTO> list) {

		try {

			finalizado = false;

			total = list.size();
			int qtdResultados = 0;

			goTo(URL_HISTORICO);

			for (CsvDTO csv : list) {

				String cpf = StringUtils.leftPad(csv.getCpf(), 11, "0");

				// long start = System.currentTimeMillis();

				pesquisaCPF(cpf);

				qtdResultados = getQtdResultados(cpf);

				// System.out.println("cpf: " + cpf);
				// System.out.println("qtd cpfs encontrados: " + qtdResultados);

				setMapJsoup(cpf, qtdResultados);

				// long end = System.currentTimeMillis();
				cont++;

				// long totalTempoCpfs = Util.calculaTempoExecucao(start, end);
				// System.out.println("tempo processamento cpfs: " +
				// totalTempoCpfs);
				// System.out.println("Status: " + cont + "/" + total);

			}

			finalizado = true;
			atualizaStatusProcesso();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void atualizaStatusProcesso() {
		if (finalizado) {
			mensagemDoStatus = "Arquivo criado com sucesso!";

		} else {
			mensagemDoStatus = "CPFs Processados " + cont + " de " + total;
		}
		System.out.println(mensagemDoStatus);
		PathPageMB.isLogin(true);
	}

	public String getMensagemDoStatus() {
		return mensagemDoStatus;
	}

	public void setMensagemDoStatus(String mensagemDoStatus) {
		this.mensagemDoStatus = mensagemDoStatus;
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

				getInstanceHTMLJsoup().createObjectRecordHTML(SetupSelenium.getInstance().getWebDriver().getPageSource(), cpf + "-" + i);
				goTo(URL_BYPASS);
				getInstanceHTMLJsoup().createObjectRecordHTML(SetupSelenium.getInstance().getWebDriver().getPageSource(), cpf + "-" + i + "-margem");
				goTo(URL_HISTORICO);

			} catch (Exception e) {
				goTo(URL_HISTORICO);
				pesquisaCPF(cpf);
				int qtdResults = getQtdResultados(cpf);
				setMapJsoup(cpf, qtdResults);
			}

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

			inputCpf.clear();
			inputCpf.sendKeys(cpf);
			btnPesquisar.click();

			Util.pause(1000);
		} catch (Exception e) {
			goTo(URL_HISTORICO);
			pesquisaCPF(cpf);
		}

	}

	/**
	 * Método para deslogar do sistema
	 */
	public void sair() {
		goTo(URL_HISTORICO);
		WebElement btnSair = SetupSelenium.getInstance().getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_id_jsp_252844863_0pc3:j_id_jsp_252844863_195pc3']")));
		btnSair.click();
		mensagemDoStatus = "";
		captcha = "";
		PathPageMB.isLogin(false);
		// getLinkImagemCaptcha();
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

	/**
	 * @return the usuarioController
	 */
	public UsuarioController getUsuarioController() {
		return usuarioController;
	}

	/**
	 * @param usuarioController
	 *            the usuarioController to set
	 */
	public void setUsuarioController(UsuarioController usuarioController) {
		this.usuarioController = usuarioController;
	}

	/**
	 * @return the uploadController
	 */
	public FileController getFileController() {
		return fileController;
	}

	/**
	 * @param uploadController
	 *            the uploadController to set
	 */
	public void setFileController(FileController fileController) {
		this.fileController = fileController;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the captcha
	 */
	public String getCaptcha() {
		return captcha;
	}

	/**
	 * @param captcha
	 *            the captcha to set
	 */
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	/**
	 * @return the finalizado
	 */
	public boolean isFinalizado() {
		return finalizado;
	}

	/**
	 * @param finalizado
	 *            the finalizado to set
	 */
	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}

}
