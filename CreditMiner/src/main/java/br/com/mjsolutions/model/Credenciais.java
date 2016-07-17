package br.com.mjsolutions.model;

/**
 * Classe que contem as credênciais para login.
 * 
 * OBS: Colocar as credenciais em properties
 * 
 * @author Jaime Gomes
 *
 */
public class Credenciais {

	/*
	 * Paraná Banco
	 */
	private static String LOGIN_PARANA_BANCO = "SC01R021432";
	private static String SENHA_PARANA_BANCO = "joaonunes01";

	/*
	 * Pan Americano
	 */
	private static String LOGIN_PAN = "SC01C02775067";
	private static String SENHA_PAN = "joaonunes55";

	/*
	 * BMG
	 */
	private static String LOGIN_BMG = "SC01R02385";
	private static String SENHA_BMG = "joaonunes02";

	/*
	 * Bon Sucesso
	 */
	private static String LOGIN_BON_SUCESSO = "SC01C0290506708";
	private static String SENHA_BON_SUCESSO = "joaonunes01";

	/**
	 * Construtor
	 */
	public Credenciais() {

	}

	/**
	 * @return the lOGIN_PARANA_BANCO
	 */
	public static String getLOGIN_PARANA_BANCO() {
		return LOGIN_PARANA_BANCO;
	}

	/**
	 * @param lOGIN_PARANA_BANCO
	 *            the lOGIN_PARANA_BANCO to set
	 */
	public static void setLOGIN_PARANA_BANCO(String lOGIN_PARANA_BANCO) {
		LOGIN_PARANA_BANCO = lOGIN_PARANA_BANCO;
	}

	/**
	 * @return the sENHA_PARANA_BANCO
	 */
	public static String getSENHA_PARANA_BANCO() {
		return SENHA_PARANA_BANCO;
	}

	/**
	 * @param sENHA_PARANA_BANCO
	 *            the sENHA_PARANA_BANCO to set
	 */
	public static void setSENHA_PARANA_BANCO(String sENHA_PARANA_BANCO) {
		SENHA_PARANA_BANCO = sENHA_PARANA_BANCO;
	}

	/**
	 * @return the lOGIN_PAN
	 */
	public static String getLOGIN_PAN() {
		return LOGIN_PAN;
	}

	/**
	 * @param lOGIN_PAN
	 *            the lOGIN_PAN to set
	 */
	public static void setLOGIN_PAN(String lOGIN_PAN) {
		LOGIN_PAN = lOGIN_PAN;
	}

	/**
	 * @return the sENHA_PAN
	 */
	public static String getSENHA_PAN() {
		return SENHA_PAN;
	}

	/**
	 * @param sENHA_PAN
	 *            the sENHA_PAN to set
	 */
	public static void setSENHA_PAN(String sENHA_PAN) {
		SENHA_PAN = sENHA_PAN;
	}

	/**
	 * @return the lOGIN_BMG
	 */
	public static String getLOGIN_BMG() {
		return LOGIN_BMG;
	}

	/**
	 * @param lOGIN_BMG
	 *            the lOGIN_BMG to set
	 */
	public static void setLOGIN_BMG(String lOGIN_BMG) {
		LOGIN_BMG = lOGIN_BMG;
	}

	/**
	 * @return the sENHA_BMG
	 */
	public static String getSENHA_BMG() {
		return SENHA_BMG;
	}

	/**
	 * @param sENHA_BMG
	 *            the sENHA_BMG to set
	 */
	public static void setSENHA_BMG(String sENHA_BMG) {
		SENHA_BMG = sENHA_BMG;
	}

	/**
	 * @return the lOGIN_BON_SUCESSO
	 */
	public static String getLOGIN_BON_SUCESSO() {
		return LOGIN_BON_SUCESSO;
	}

	/**
	 * @param lOGIN_BON_SUCESSO
	 *            the lOGIN_BON_SUCESSO to set
	 */
	public static void setLOGIN_BON_SUCESSO(String lOGIN_BON_SUCESSO) {
		LOGIN_BON_SUCESSO = lOGIN_BON_SUCESSO;
	}

	/**
	 * @return the sENHA_BON_SUCESSO
	 */
	public static String getSENHA_BON_SUCESSO() {
		return SENHA_BON_SUCESSO;
	}

	/**
	 * @param sENHA_BON_SUCESSO
	 *            the sENHA_BON_SUCESSO to set
	 */
	public static void setSENHA_BON_SUCESSO(String sENHA_BON_SUCESSO) {
		SENHA_BON_SUCESSO = sENHA_BON_SUCESSO;
	}

}
