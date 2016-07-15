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
	private List<CsvDTO> listCsvProcess;

	private static final String PATH_CONTENT_LOGIN = "pages/login_content.xhtml";
	private static final String PATH_CONTENT_CONFIGURACAO = "pages/configuracao_content.xhtml";
	
	@PostConstruct
	public void init() {
		listCSVDir();
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
	
	public List<CsvDTO> listCSVDir(){
		listCsvProcess = new ArrayList<CsvDTO>();
        File baseFolder = new File(Util.getProperty("prop.diretorio.cache"));
        File[] files = baseFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.getPath().endsWith(".csv")) {
            	CsvDTO csvDTO = new CsvDTO();
            	csvDTO.setCpf(file.getName());
                listCsvProcess.add(csvDTO);
            }
        }
        return listCsvProcess;
	}
	
	public List<CsvDTO> getListCsvProcess() {
		return listCsvProcess;
	}
	
	public void setListCsvProcess(List<CsvDTO> listCsvProcess) {
		this.listCsvProcess = listCsvProcess;
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
