package br.com.bjjsolutions.mb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.Part;

import br.com.bjjsolutions.util.Util;

public class FileController {

	private Part file;
	private List<File> listArquivosProcessados;

	/**
	 * Método responsável por ler o arquivo inserido no botão de upload e
	 * salvá-lo no diretório /home/CreditMiner/leitura.
	 */
	@SuppressWarnings("resource")
	public String upload() {

		String fileName = "";
		try {
			if (file != null) {
				String conteudo = new Scanner(file.getInputStream()).useDelimiter("\\A").next();
				fileName = getFileNameFromPart(file);
				salvarArquivo(conteudo, fileName);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * Método responsável por salvar o arquivo no diretório
	 * 
	 * @param conteudo
	 * @param nomeArquivo
	 */
	private static void salvarArquivo(String conteudo, String nomeArquivo) {
		FileWriter arquivo;
		try {
			arquivo = new FileWriter(new File(Util.getProperty("prop.diretorio.leitura") + nomeArquivo));
			arquivo.write(conteudo);
			arquivo.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que retorna no nome do arquivo que foi feito upload
	 * 
	 * @param part
	 * @return String fileName
	 */
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

	/**
	 * Método que retorna a lista de arquivos csv no diretório
	 * /home/CreditMiner/cache
	 * 
	 * @return List<File>
	 */
	public List<File> listCSVDir() {
		listArquivosProcessados = new ArrayList<File>();
		File baseFolder = new File(Util.getProperty("prop.diretorio.cache"));
		File[] files = baseFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getPath().endsWith(".csv")) {
				listArquivosProcessados.add(file);
			}
		}
		return listArquivosProcessados;
	}

	public boolean excluir(File file) {

		if (file.exists()) {
			file.delete();
			
			System.out.println("item excluido");
			listCSVDir();
			return true;
		}

		return false;

	}
	/**
	 * @return the file
	 */
	public Part getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(Part file) {
		this.file = file;
	}

	/**
	 * @return the listArquivosProcessados
	 */
	public List<File> getListArquivosProcessados() {
		return listArquivosProcessados;
	}

	/**
	 * @param listArquivosProcessados
	 *            the listArquivosProcessados to set
	 */
	public void setListArquivosProcessados(List<File> listArquivosProcessados) {
		this.listArquivosProcessados = listArquivosProcessados;
	}

}
