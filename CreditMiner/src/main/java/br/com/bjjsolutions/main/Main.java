package br.com.bjjsolutions.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import br.com.bjjsolutions.navegador.Navegador;

public class Main {

	/**
	 * Roda aplicação
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Navegador navegador = new Navegador();

		try {
			// Tenta efetuar login
			boolean ok = navegador.login(
					"http://www.devmedia.com.br/login/login.asp",
					"wagmattei@gmail.com", "01780219");
			if (ok) {
				// Acessa pÃ¡gina restrita
				navegador.openPage("http://www.devmedia.com.br/guias/");
			}
			navegador.close();
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
