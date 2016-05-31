package br.com.bjjsolutions.enumerator;

import br.com.bjjsolutions.util.Util;

/**
 * Enum de definição das credenciais do bot
 * 
 * @author Marcelo Lopes Nunes</br>
 *         bjjsolutions.com.br - 30/05/2016</br>
 *         <a href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public enum CredentialsEnum {
	
	CREDENTIALS_PARANA_BANCO(Util.getInstanceProperties().getProperty("prop.login.parana.banco"), Util.getInstanceProperties().getProperty("prop.password.parana.banco")),
	CREDENTIALS_PAN(Util.getInstanceProperties().getProperty("prop.login.pan"), Util.getInstanceProperties().getProperty("prop.password.pan")),
	CREDENTIALS_BMG(Util.getInstanceProperties().getProperty("prop.login.bmg"), Util.getInstanceProperties().getProperty("prop.password.bmg")),
	CREDENTIALS_BOM_SUCESSO(Util.getInstanceProperties().getProperty("prop.login.bom.sucesso"), Util.getInstanceProperties().getProperty("prop.password.bom.sucesso"));
	
	private String login;
	private String password;
	
	private CredentialsEnum(String login, String password) {
		this.login = login;
		this.password = password;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getPassword() {
		return password;
	}
}
