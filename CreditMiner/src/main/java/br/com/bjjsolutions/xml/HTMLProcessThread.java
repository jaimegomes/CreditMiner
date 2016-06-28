package br.com.bjjsolutions.xml;

import java.io.IOException;

/**
 * Classe que inicializa o processo de leitura dos arquivos HTMLS
 * 
 * @author Marcelo Lopes Nunes</br> bjjsolutions.com.br - 23/06/2016</br> <a
 *         href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public class HTMLProcessThread implements Runnable {

	@Override
	public void run() {
		FactoryHTMLJsoup factoryHTMLJsoup = new FactoryHTMLJsoup();
		try {
			factoryHTMLJsoup.travelFilesHTML();
		} catch (IOException e) {
			System.err.println("Erro ao percorrer os arquivos HTML");
			e.printStackTrace();
		}
	}
}
