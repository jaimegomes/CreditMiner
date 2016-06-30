package br.com.bjjsolutions.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import br.com.bjjsolutions.enumerator.SystemEnum;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

/**
 * Classe Util para criação de métodos utilitarios do sistema
 * 
 * @author Marcelo Lopes Nunes</br> bjjsolutions.com.br - 30/05/2016</br> <a
 *         href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public class Util {

	private static Locale localePtBR = null;

	public static String getProperty(String key) {
		return getResourceBundle().getString(key);
	}

	private static ResourceBundle getResourceBundle() {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.messageJSF", getLocalePtBR());
		return resourceBundle;
	}

	private static Locale getLocalePtBR() {
		if (localePtBR == null) {
			localePtBR = new Locale("pt", "BR");
		}
		return localePtBR;
	}

	/**
	 * Método que transforma cada cpf do arquivo csv em um objeto CsvDTO, nele
	 * contem o cpf utilizado para as pesquisas
	 * 
	 * @param beanClass
	 * @return List<CsvDTO>
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static <CsvDTO> List<CsvDTO> parseCsvFileToBeans(final Class<CsvDTO> beanClass) throws IOException {
		CSVReader reader = null;

		try {
			reader = new CSVReader(new BufferedReader(new FileReader(Util.getDirectorySO() + "cpf.csv")), ';');

			Map<String, String> columnMapping = new HashMap<String, String>();
			columnMapping.put("CPF", "cpf");

			HeaderColumnNameTranslateMappingStrategy<CsvDTO> strategy = new HeaderColumnNameTranslateMappingStrategy<CsvDTO>();
			strategy.setType(beanClass);
			strategy.setColumnMapping(columnMapping);

			final CsvToBean<CsvDTO> csv = new CsvToBean<CsvDTO>();

			return csv.parse(strategy, reader);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Método que recebe como parâmetro o tempo de pausa em mili segundos.
	 * 
	 * @param timeMiliSec
	 */
	public static  void pause(long timeMiliSec) {
		try {
			Thread.sleep(timeMiliSec);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	public static String getDirectorySO() {
		String directory = "";
		if (System.getProperty("os.name").toUpperCase().equals(SystemEnum.LINUX.getSystem())) {
			directory = Util.getProperty("prop.diretorio.home");
		} else {
			directory = Util.getProperty("prop.diretorio.d");
		}
		return directory;
	}

	/**
	 * Método que salva o HTML da página que está sendo navegada, no caminho
	 * indicado pela variável
	 * 
	 * @param html
	 */
	public void salvaHtml(String html, String nomeArquivo) {
		FileWriter arquivo;
		try {
			arquivo = new FileWriter(new File(Util.getDirectorySO() + nomeArquivo + ".html"));

			arquivo.write(html);
			arquivo.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que calcula o tempo de execução em mili segundos
	 * 
	 * @param start
	 * @param end
	 * @return tempo total de execução
	 */
	public static long calculaTempoExecucao(long start, long end) {
		return (end - start);

	}
}