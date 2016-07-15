package br.com.bjjsolutions.mb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;

import br.com.bjjsolutions.dto.CsvDTO;
import br.com.bjjsolutions.util.Util;

@ManagedBean(name = "configuracaoMB")
@ViewScoped
public class ConfiguracaoMB {

	private Part file;
	private static boolean isLogin = false;

	private static final String PATH_CONTENT_LOGIN = "pages/login_content.xhtml";
	private static final String PATH_CONTENT_CONFIGURACAO = "pages/configuracao_content.xhtml";
	
	@PostConstruct
	public void init() {
		
	}

	@SuppressWarnings("resource")
	public void upload() {
		try {
			if (file != null) {
				String conteudo = new Scanner(file.getInputStream()).useDelimiter("\\A").next();
				salvarArquivo(conteudo, getFileNameFromPart(file));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void salvarArquivo(String conteudo, String nomeArquivo) {
		FileWriter arquivo;
		try {
			arquivo = new FileWriter(new File("/home/CreditMiner/leitura/" + nomeArquivo));
			arquivo.write(conteudo);
			arquivo.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
	
	private static String getFileNameFromPart(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		for (String content : partHeader.split(";")) {
			if (content.trim().startsWith("filename")) {
				String fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
				return fileName;
			}
		}
		return null;
	}
}
