package br.com.bjjsolutions.formatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import br.com.bjjsolutions.model.Pessoa;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

public class CsvFormatter {

	public static void main(String[] args) throws IOException {
		String filename = "/home/jaime/test.csv";
//		JOptionPane.showMessageDialog(null,
//				"Starting to parse CSV file and map to Java Bean");
		CSVWriter writer = new CSVWriter(new FileWriter(
				"/home/jaime/output.csv"), ';');

		List<Pessoa> persons = parseCsvFileToBeans(filename, ';', Pessoa.class);
		String[] cabecalho = ("Nome;Email;Data Nascimento;Margem").split(";");
		writer.writeNext(cabecalho);

		for (Pessoa p : persons) {

//			JOptionPane.showMessageDialog(null,
//					"Nome: " + p.getNome() + " Email: " + p.getEmail()
//							+ " Data Nascimento: " + p.getDataNascimento()
//							+ " Margem: " + p.getMargem());

			// feed in your array (or convert your data to an array)
			String[] entries = (p.getNome() + ";" + p.getEmail() + ";"
					+ p.getDataNascimento() + ";" + p.getMargem()).split(";");
			writer.writeNext(entries);

		}
		
		JOptionPane.showMessageDialog(null, "Fim");

		writer.close();
	}

	/**
	 * Parses a csv file into a list of beans.
	 * 
	 * @param <T>
	 *            the type of the bean
	 * @param filename
	 *            the name of the csv file to parse
	 * @param fieldDelimiter
	 *            the field delimiter
	 * @param beanClass
	 *            the bean class to map csv records to
	 * @return the list of beans or an empty list there are none
	 * @throws FileNotFoundException
	 *             if the file does not exist
	 */
	public static <Pessoa> List<Pessoa> parseCsvFileToBeans(
			final String filename, final char fieldDelimiter,
			final Class<Pessoa> beanClass) throws FileNotFoundException {
		CSVReader reader = null;
		try {
			reader = new CSVReader(
					new BufferedReader(new FileReader(filename)),
					fieldDelimiter);

			Map<String, String> columnMapping = new HashMap<String, String>();
			columnMapping.put("Nome", "nome");
			columnMapping.put("Email", "email");
			columnMapping.put("Data Nascimento", "dataNascimento");
			columnMapping.put("Margem", "margem");

			HeaderColumnNameTranslateMappingStrategy<Pessoa> strategy = new HeaderColumnNameTranslateMappingStrategy<Pessoa>();
			strategy.setType(beanClass);
			strategy.setColumnMapping(columnMapping);

			final CsvToBean<Pessoa> csv = new CsvToBean<Pessoa>();
			return csv.parse(strategy, reader);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
				}
			}
		}
	}

}