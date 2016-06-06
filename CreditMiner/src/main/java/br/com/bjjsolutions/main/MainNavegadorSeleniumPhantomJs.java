package br.com.bjjsolutions.main;

import br.com.bjjsolutions.navegador.NavegadorSeleniumPhantomJs;

public class MainNavegadorSeleniumPhantomJs {
	
	public static void main(String[] args) {
		
		
		NavegadorSeleniumPhantomJs nav = new NavegadorSeleniumPhantomJs();
		
		String link = nav.getLinkImagemCaptcha();
		
		System.out.println(link);
		
	}

}
