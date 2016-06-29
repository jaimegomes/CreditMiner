package br.com.bjjsolutions.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.bjjsolutions.util.Util;

public class FactoryHTMLJsoup extends HTMLJsoup {

	/**
	 * Método que realiza a leitura de todos os arquivos HTMLs no diretório
	 * 
	 * @throws IOException
	 */
	protected void travelFilesHTML() throws IOException {
		Map<String, String> htmlsSemMargem = new HashMap<String, String>();
		Map<String, String> htmlsComMargem = new HashMap<String, String>();

		File file = new File(Util.getDirectorySO() + "/htmls_processados");
		File afile[] = file.listFiles();
		int i = 0;
		for (int j = afile.length; i < j; i++) {
			File files = afile[i];

			String html = readFile(files);

			if (!files.getName().contains("margem")) {
				htmlsSemMargem.put(files.getName(), html);
			} else {
				htmlsComMargem.put(files.getName(), html);
			}
			file.delete();
		}

		for (String key : htmlsSemMargem.keySet()) {
			String value = htmlsSemMargem.get(key);
			createObjectRecordHTML(value, key);
		}

		for (String key : htmlsComMargem.keySet()) {
			String value = htmlsComMargem.get(key);
			createObjectRecordHTML(value, key);
		}

		if (Cache.clientesDTOCache != null) {
			WriteFileXML.gravaXMLListaClientes(Cache.clientesDTOCache, Util.getProperty("prop.diretorio.cache"));
			WriteFileCSV.createCsvFile(Cache.clientesDTOCache, Util.getProperty("prop.diretorio.cache"));
		}
	}

	/**
	 * Método que realiza a leitura de um arquivo especifico
	 * 
	 * @param file
	 * @return String
	 */
	private String readFile(File file) {
		StringBuilder sbReader = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while (br.ready()) {
				sbReader.append(br.readLine());
			}
			br.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return sbReader.toString();
	}

}
