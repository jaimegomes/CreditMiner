package br.com.bjjsolutions.navegador;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;

public class NavegadorHTMLUnit {

	public void gotToLoginPage() {
		// Obtém a página de login.
		HtmlPage firstPage;
		WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
		try {
			
			//ignora erros
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setJavaScriptEnabled(true);
			
			firstPage = webClient.getPage("http://sc.consignum.com.br/wmc-sc/pages");
			
			HtmlTableBody table = firstPage.getHtmlElementById("standardTable_Col_L2");
			System.out.println(table.getLocalName());
			
			HtmlAnchor anchor = (HtmlAnchor) table.getElementsByTagName("a");
			
			System.out.println(anchor);
			
			HtmlPage loginPage = anchor.click();

			// Mostra o código html da página
			System.out.println(loginPage.asXml());

			// HtmlTextInput inputNomeDeUsuario = paginaDeLogin
			// .querySelector("class=loginInicio");
			// HtmlPasswordInput inputSenha = paginaDeLogin
			// .querySelector("input[name='pass']");
			// HtmlSubmitInput botaoEnviar = paginaDeLogin
			// .querySelector("#form-login > input[type='submit']");
			//
			// // Define o valor do atributo 'value' dos inputs.
			// inputNomeDeUsuario.setValueAttribute("joao");
			// inputSenha.setValueAttribute("joao1234");

			// Simula o "click" no botão de submit e aguarda retorno
			// HtmlPage paginaAposOLogin = botaoEnviar.click();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
