package br.com.bjjsolutions.mb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;  
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.faces.context.ExternalContext;  
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;  
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
	 * Metodo que inicializa a ação de download
	 * recupera o parametro setado na tela
	 * 
	 * @return gotoDownload
	 */
    public String download(){
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	
    	File file = (File) facesContext.getExternalContext().getRequestMap().get("arquivoProcessado");
    	
        downloadFile(file.getName(), file.getAbsolutePath(), "csv", facesContext);
        return "gotoDownload";
    }
	
    /**
     * Método que realiza o download do arquivo
     * e retorna o arquivo para o navegador
     * 
     * @param filename
     * @param fileLocation
     * @param mimeType
     * @param facesContext
     */
	public static synchronized void downloadFile(String filename, String fileLocation, String mimeType, FacesContext facesContext) {
		ExternalContext context = facesContext.getExternalContext();
		
		String path = fileLocation;
		String fullFileName = path;
		File file = new File(fullFileName);
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
		response.setContentLength((int) file.length());
		response.setContentType(mimeType);
		try {
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			byte[] buf = new byte[(int) file.length()];
			int count;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.flush();
			out.close();
			facesContext.responseComplete();
		} catch (IOException ex) {
			System.out.println("Error in downloadFile: " + ex.getMessage());
			ex.printStackTrace();
		}
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
