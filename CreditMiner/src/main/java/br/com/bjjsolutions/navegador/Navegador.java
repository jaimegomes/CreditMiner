package br.com.bjjsolutions.navegador;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Navegador {

	private final DefaultHttpClient client = new DefaultHttpClient();

	/**
	 * Faz o login no site desejado
	 * 
	 * @param url
	 * @param user
	 * @param password
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public boolean login(String url, String user, String password)
			throws ClientProtocolException, IOException {

		/* M�todo POST */
		final HttpPost post = new HttpPost(url);
		boolean result = false;

		/* Configura os par�metros do POST */
		final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("usuario", user));
		nameValuePairs.add(new BasicNameValuePair("senha", password));
		nameValuePairs.add(new BasicNameValuePair("ac", "1"));

		/*
		 * Codifica os parametros.
		 * 
		 * Antes do encoder: fulano@email.com Depois do enconder:
		 * fulano%40email.com
		 */
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8));

		/* Define navegador */
		post.addHeader("User-Agent",
				"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0");

		/* Efetua o POST */
		HttpResponse response = client.execute(post);

		/*
		 * Resposta HTTP: Sempre imprimir� "HTTP/1.1 302 Object moved" (no caso
		 * da devmedia)
		 */
		System.out.println("Login form get: " + response.getStatusLine());

		/*
		 * Consome o conte�do retornado pelo servidor Necess�rio esvaziar o
		 * response antes de usar o httpClient novamente
		 */
		EntityUtils.consume(response.getEntity());

		/*
		 * Testar se o login funcionou.
		 * 
		 * Estrat�gia: acessar uma p�gina que s� est� dispon�vel quando se est�
		 * logado Em caso de erro, o servidor ir� redirecionar para a p�gina de
		 * login A pagina de login contem uma string: "Login DevMedia" Se esta
		 * String estiver presente, significa que o login n�o foi efetuado com
		 * sucesso
		 */
		final HttpGet get = new HttpGet(
				"http://www.devmedia.com.br/include/mynotes.asp");
		response = client.execute(get);

		/*
		 * Verifica se a String: "Login" est� presente, caso esteja sginifica
		 * que est� deslogado
		 */
		if (checkSuccess(response)) {
			System.out.println("Login n�o-efetuado!");

		} else {
			System.out.println("Conex�o Estabelecida!");
			result = true;
		}

		return result;
	}

	/**
	 * Abre p�gina
	 * 
	 * @param url
	 *            - P�gina a acessar
	 * @throws IOException
	 */
	public void openPage(final String url) throws IOException {
		final HttpGet get = new HttpGet(url);
		final HttpResponse response = client.execute(get);
		saveHTLM(response);
	}

	/**
	 * Encerra conex�o
	 */
	public void close() {
		client.getConnectionManager().shutdown();
	}

	/**
	 * Busca por String que indica se o usu�rio est� logado ou n�o
	 * 
	 * @param response
	 * @return false - N�o achou String | true - Achou String
	 * @throws IOException
	 */
	private boolean checkSuccess(final HttpResponse response)
			throws IOException {
		final BufferedReader rd = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		String line;
		boolean found = false;
		/*
		 * Deixa correr todo o laco, mesmo achando a String, para consumir o
		 * content
		 */
		while ((line = rd.readLine()) != null) {
			System.out.println("linha: " + line);
			if (line.contains("Login")) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Salva a p�gina
	 * 
	 * @param response
	 * @throws IOException
	 */
	private void saveHTLM(final HttpResponse response) throws IOException {
		final BufferedReader rd = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent()));
		String line;
		File arquivo = new File("/home/jaime/html");
		PrintWriter writer = new PrintWriter(arquivo);
		while ((line = rd.readLine()) != null) {
			// System.out.println(line);
			writer.println(line);
		}
		writer.flush();
		writer.close();
	}

}
