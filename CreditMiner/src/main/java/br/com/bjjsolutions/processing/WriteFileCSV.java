package br.com.bjjsolutions.processing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import br.com.bjjsolutions.dto.ClienteDTO;
import br.com.bjjsolutions.dto.SolicitacaoDTO;

/**
 * Classe que gera o csv com os dados processados
 * 
 * @author Marcelo Lopes Nunes</br>
 *         bjjsolutions.com.br - 30/06/2016</br>
 *         <a href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public class WriteFileCSV {

	private static final String COMMA_DELIMITER = ";";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "cpf;matricula;colaborador;secretaria;nascimento;margem;banco;valorautorizado;parcelas;parcelaspagas;pesquisado";

	public static void createCsvFile(Map<String, ClienteDTO> clientes, String fileName) {

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName + "/clientes.csv", true);

			fileWriter.append(FILE_HEADER.toString());

			fileWriter.append(NEW_LINE_SEPARATOR);
			
	        for (ClienteDTO clienteDTO : clientes.values()) {

	        	if (clienteDTO.getSolicitacaes() != null)
	        	if (!clienteDTO.getSolicitacaes().isEmpty()) {
	        		
	        		for (SolicitacaoDTO solicitacaoDTO : clienteDTO.getSolicitacaes()) {
	        			fileWriter.append(clienteDTO.getCpf());
	        			fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(clienteDTO.getMatricula());
	        			fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(clienteDTO.getColaborador());
	        			fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(clienteDTO.getSecretaria());
	        			fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(clienteDTO.getNascimento());
	        			fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(clienteDTO.getMargem());
	        			fileWriter.append(COMMA_DELIMITER);
	        			
	        			fileWriter.append(solicitacaoDTO.getBanco());
	        			fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(solicitacaoDTO.getValorAutorizado());
	        			fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(solicitacaoDTO.getParcelas());
	        			fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(solicitacaoDTO.getPagas());
	        			fileWriter.append(COMMA_DELIMITER);
	        			fileWriter.append(solicitacaoDTO.getPesquisado());
	        			fileWriter.append(NEW_LINE_SEPARATOR);
	        		}
	        	}

			}

			System.out.println("Arquivo CSV criado com sucesso");

		} catch (Exception e) {
			System.err.println("Erro na criação do arquivo");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.err.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
	}
}
