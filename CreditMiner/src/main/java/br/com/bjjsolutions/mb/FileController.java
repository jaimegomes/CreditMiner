package br.com.bjjsolutions.mb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.Part;

import br.com.bjjsolutions.dto.CsvDTO;
import br.com.bjjsolutions.util.Util;

public class FileController {

	private Part file;
	private List<CsvDTO> listCsvProcess;

	/**
	 * M�todo respons�vel por ler o arquivo inserido no bot�o de upload e
	 * salv�-lo no diret�rio /home/CreditMiner/leitura.
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
	 * M�todo respons�vel por salvar o arquivo no diret�rio
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
	 * M�todo que retorna no nome do arquivo que foi feito upload
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
	 * M�todo que retorna a lista de arquivos csv no diret�rio
	 * /home/CreditMiner/cache
	 * 
	 * @return
	 */
	public List<CsvDTO> listCSVDir() {
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
	 * @return the listCsvProcess
	 */
	public List<CsvDTO> getListCsvProcess() {
		return listCsvProcess;
	}

	/**
	 * @param listCsvProcess
	 *            the listCsvProcess to set
	 */
	public void setListCsvProcess(List<CsvDTO> listCsvProcess) {
		this.listCsvProcess = listCsvProcess;
	}

}
