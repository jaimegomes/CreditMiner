package br.com.mjsolutions.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Classe respons�vel por administraro carregamento de p�ginas
 * 
 * @author Jaime Gomes
 * 
 */
@ManagedBean(name="pathPageMB")
@SessionScoped
public class PathPageMB {

	private static boolean isLogin = false;

	private static final String PATH_CONTENT_LOGIN = "pages/login_content.xhtml";
	private static final String PATH_CONTENT_CONFIGURACAO = "pages/configuracao_content.xhtml";

	/**
	 * M�todo respons�vel por retornar a p�gina atual do sistema de acordo com a
	 * flag isLogin, se isLogin = true exibe a p�gina de configura��o, se
	 * isLogin = false exibe a p�gina de login
	 * 
	 * @return p�gina do sistema
	 */
	public String getPathPage() {
		if (!isLogin) {
			return PATH_CONTENT_LOGIN;
		}
		return PATH_CONTENT_CONFIGURACAO;
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
		PathPageMB.isLogin = isLogin;
	}
}
