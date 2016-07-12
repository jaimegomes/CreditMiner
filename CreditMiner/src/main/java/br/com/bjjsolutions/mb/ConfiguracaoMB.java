package br.com.bjjsolutions.mb;

import java.io.IOException;
import java.util.Scanner;

import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;

@ManagedBean(name = "configuracaoMB")
public class ConfiguracaoMB {

	private Part file;
	private static boolean isLogin = false;

	private static final String PATH_CONTENT_LOGIN = "pages/login_content.xhtml";
	private static final String PATH_CONTENT_CONFIGURACAO = "pages/configuracao_content.xhtml";

	public void upload() {
		try {
			if (file != null) {
				new Scanner(file.getInputStream()).useDelimiter("\\A").next();
			}
		} catch (IOException e) {

		}
	}

	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public String getPathPage() {
		if (!isLogin) {
			return PATH_CONTENT_LOGIN;
		}
		return PATH_CONTENT_CONFIGURACAO;
	}

	public static void setIsLogin(boolean isLoginP) {
		isLogin = isLoginP;
	}
}
