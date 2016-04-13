package br.com.bjjsolutions.formatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.bjjsolutions.model.Cliente;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

public class CsvFormatter {

	public static void main(String[] args) throws IOException {

		String filename = "/home/jaime/test.csv";
		System.out.println("Starting to parse CSV file and map to Java Bean");
		CSVWriter writer = new CSVWriter(new FileWriter(
				"/home/jaime/output.csv"), ';');

		List<Cliente> persons = parseCsvFileToBeans(filename, ';',
				Cliente.class);
		String[] cabecalho = ("CPF;Colaborador;Matricula;Secretaria;Nascimento;Margem;Banco;Valor Autorizado;Parcelas;Pagas;Pesquisado")
				.split(";");
		writer.writeNext(cabecalho);

		int linha = 1;
		for (int i = 0; i < persons.size(); i++) {

			Cliente c = persons.get(i);

			String nascimento = c.getNascimento();
			Double porcentagem = 0.0;
			String pagas = c.getPagas();
			String parcelas = c.getParcelas();
			int idade = 0;

			if (linha > 1) {

				if (!nascimento.equals("") && nascimento != null
						&& nascimento.length() == 12) {
					nascimento = c.getNascimento().substring(2, 12);
				} else {
					nascimento = "";
				}

				if (!pagas.equals("") && !parcelas.equals("")
						&& !parcelas.equals("Prazo Rotativo")) {
					porcentagem = (Double.parseDouble(c.getPagas()) * 100)
							/ Double.parseDouble(c.getParcelas());
				}

				if (!nascimento.equals("")) {
					idade = calculaIdade(nascimento);
				}

				if (porcentagem >= 30 && idade < 65) {
					// feed in your array (or convert your data to an array)
					String[] entries = (c.getCpf() + ";" + c.getColaborador()
							+ ";" + c.getMatricula() + ";" + c.getSecretaria()
							+ ";" + nascimento + ";" + c.getMargem() + ";"
							+ c.getBanco() + ";" + c.getValorAutorizado() + ";"
							+ c.getParcelas() + ";" + c.getPagas() + ";" + c
							.getPesquisado()).split(";");
					writer.writeNext(entries);
				}

			}

			linha++;

		}

		System.out.println("Fim");

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
	 * @throws IOException
	 */
	public static <Pessoa> List<Pessoa> parseCsvFileToBeans(
			final String filename, final char fieldDelimiter,
			final Class<Pessoa> beanClass) throws IOException {
		CSVReader reader = null;
		try {
			reader = new CSVReader(
					new BufferedReader(new FileReader(filename)),
					fieldDelimiter);

			Map<String, String> columnMapping = new HashMap<String, String>();
			columnMapping.put("CPF", "cpf");
			columnMapping.put("Colaborador", "colaborador");
			columnMapping.put("Matricula", "matricula");
			columnMapping.put("Secretaria", "secretaria");
			columnMapping.put("Nascimento", "nascimento");
			columnMapping.put("Margem", "margem");
			columnMapping.put("Banco", "banco");
			columnMapping.put("Valor Autorizado", "valorAutorizado");
			columnMapping.put("Parcelas", "parcelas");
			columnMapping.put("Pagas", "pagas");
			columnMapping.put("Pesquisado", "pesquisado");

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

	public static int calculaIdade(String dataNasc) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date dataNascInput = null;
		try {
			dataNascInput = sdf.parse(dataNasc);
		} catch (Exception e) {
		}

		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(dataNascInput);

		// Cria um objeto calendar com a data atual
		Calendar today = Calendar.getInstance();

		// Obtém a idade baseado no ano
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

		dateOfBirth.add(Calendar.YEAR, age);

		if (today.before(dateOfBirth)) {
			age--;
		}
		return age;

	}
}
