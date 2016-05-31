package br.com.bjjsolutions.enumerator;

/**
 * Enum de definição do S.O que executará o bot
 * 
 * @author Marcelo Lopes Nunes</br>
 *         bjjsolutions.com.br - 30/05/2016</br>
 *         <a href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public enum SystemEnum {

	WINDOWS("WINDOWS"), LINUX("LINUX");

	private String system;

	SystemEnum(String system) {
		this.system = system;
	}

	public String getSystem() {
		return system;
	}

}