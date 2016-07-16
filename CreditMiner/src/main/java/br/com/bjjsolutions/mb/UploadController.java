package br.com.bjjsolutions.mb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.Part;

public class UploadController {

	private Part file;

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
			arquivo = new FileWriter(new File("/home/CreditMiner/leitura/" + nomeArquivo));
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

}
