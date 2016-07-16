package br.com.bjjsolutions.mb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import br.com.bjjsolutions.dto.FileDTO;
import br.com.bjjsolutions.util.Util;

public class FileController {

	private Part file;
	private List<FileDTO> listArquivosProcessados;

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
	 * @return List<CsvDTO>
	 */
	public List<FileDTO> listCSVDir() {
		listArquivosProcessados = new ArrayList<FileDTO>();
		File baseFolder = new File(Util.getProperty("prop.diretorio.cache"));
		File[] files = baseFolder.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.getPath().endsWith(".csv")) {
				FileDTO fileDTO = new FileDTO();
				fileDTO.setFileName(file.getName());
				listArquivosProcessados.add(fileDTO);
			}
		}
		return listArquivosProcessados;
	}

	public boolean excluir() {

		String nomeArquivoExclusao = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fileName");
		File baseFolder = new File(Util.getProperty("prop.diretorio.cache"));
		File arquivoExcluir = new File(baseFolder + nomeArquivoExclusao);

		if (arquivoExcluir.exists()) {
			arquivoExcluir.delete();
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
	public List<FileDTO> getListArquivosProcessados() {
		return listArquivosProcessados;
	}

	/**
	 * @param listArquivosProcessados
	 *            the listArquivosProcessados to set
	 */
	public void setListArquivosProcessados(List<FileDTO> listArquivosProcessados) {
		this.listArquivosProcessados = listArquivosProcessados;
	}

}
