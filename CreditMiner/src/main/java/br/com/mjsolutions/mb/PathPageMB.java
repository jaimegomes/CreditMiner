package br.com.mjsolutions.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Classe responsável por administraro carregamento de páginas
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
