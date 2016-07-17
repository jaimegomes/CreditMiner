package br.com.mjsolutions.model;

/**
 * Classe que representa a entidade Usuario utilizada para se logar no sistema
 * Credit Miner / Consignum
 * 
 * @author Jaime Gomes
 * 
 */
public class Usuario {

	private String login;
	private String senha;
	private String captcha;

	/**
	 * Contrutor
	 */
	public Usuario() {

	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the senha
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * @param senha
	 *            the senha to set
	 */
	public void setSenha(String senha) {
		this.senha = senha;
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

}
