package br.com.mjsolutions.main;

import br.com.mjsolutions.mb.NavegadorMB;

public class MainNavegadorSeleniumPhantomJs {
	
	public static void main(String[] args) {
		
		
		NavegadorMB nav = new NavegadorMB();
		
		String link = nav.getLinkImagemCaptcha();
		
		System.out.println(link);
		
	}

}
