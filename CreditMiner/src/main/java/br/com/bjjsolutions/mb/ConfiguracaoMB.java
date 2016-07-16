package br.com.bjjsolutions.mb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;

@ManagedBean(name = "configuracaoMB")
@SessionScoped
public class ConfiguracaoMB {

	private static Part file;
	private static boolean isLogin = false;

	private static final String PATH_CONTENT_LOGIN = "pages/login_content.xhtml";
	private static final String PATH_CONTENT_CONFIGURACAO = "pages/configuracao_content.xhtml";

	/**
	 * Método responsável por ler o arquivo inserido no botão de upload e
	 * salvá-lo no diretório /home/CreditMiner/leitura.
	 */
	@SuppressWarnings("resource")
	public static void upload() {
		try {
			if (file != null) {
				String conteudo = new Scanner(file.getInputStream()).useDelimiter("\\A").next();
				salvarArquivo(conteudo, getFileNameFromPart(file));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por salvar o arquivo no diretório
	 * 
	 * @param conteudo
	 * @param nomeArquivo
	 */
	private static void salvarArquivo(String conteudo, String nomeArquivo) {
		FileWriter arquivo;
		try {
			arquivo = new FileWriter(new File("/home/CreditMiner/leitura/" + nomeArquivo));
			arquivo.write(conteudo);
			arquivo.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método responsável por retornar a página atual do sistema de acordo com a
	 * flag isLogin, se isLogin = true exibe a página de configuração, se
	 * isLogin = false exibe a página de login
	 * 
	 * @return página do sistema
	 */
	public String getPathPage() {
		if (!isLogin) {
			return PATH_CONTENT_LOGIN;
		}
		return PATH_CONTENT_CONFIGURACAO;
	}

	/**
	 * Método que retorna no nome do arquivo que foi feito upload
	 * 
	 * @param part
	 * @return String fileName
	 */
	private static String getFileNameFromPart(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		for (String content : partHeader.split(";")) {
			if (content.trim().startsWith("filename")) {
				String fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
				return fileName;
			}
		}
		return null;
	}

	/**
	 * @return the file
	 */
	public static Part getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public static void setFile(Part file) {
		ConfiguracaoMB.file = file;
	}

	/**
	 * @return the isLogin
	 */
	public static boolean isLogin() {
		return isLogin;
	}

	/**
	 * @param isLogin
	 *            the isLogin to set
	 */
	public static void isLogin(boolean isLogin) {
		ConfiguracaoMB.isLogin = isLogin;
	}

}
