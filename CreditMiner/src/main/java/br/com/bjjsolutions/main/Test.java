package br.com.bjjsolutions.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.bjjsolutions.dto.ClienteDTO;
import br.com.bjjsolutions.dto.SolicitacaoDTO;
import br.com.bjjsolutions.xml.Cache;
import br.com.bjjsolutions.xml.Parametros;

/**
 * Classe teste de captura 
 * @author marcelo
 *
 */
public class Test {

	public static void main(String[] args) {

		try {

			File input = new File("/home/marcelo/75147653953.html");
			Document doc = Jsoup.parse(input, "ISO-8859-1", "http://example.com/");
			System.out.println(doc.html());
			

			if (Cache.clientesDTOCache == null) {
				Cache.clientesDTOCache = new TreeMap<String, ClienteDTO>();
			}

			ClienteDTO clienteDTO = new ClienteDTO();

			// Seta os dados do HTML no Objeto ClienteDTO
			Map<String, String> mapDadosDoCliente = getHeaderDadosDoCliente(doc);

			clienteDTO.setCpf("");
			if (mapDadosDoCliente.containsKey(Parametros.LABEL_COLABORADOR)) {
				clienteDTO.setColaborador(mapDadosDoCliente.get(Parametros.LABEL_COLABORADOR));
			}
			if (mapDadosDoCliente.containsKey(Parametros.LABEL_MATRICULA)) {
				clienteDTO.setMatricula(mapDadosDoCliente.get(Parametros.LABEL_MATRICULA));
			}
			if (mapDadosDoCliente.containsKey(Parametros.LABEL_MARGEM)) {
				clienteDTO.setMargem(mapDadosDoCliente.get(Parametros.LABEL_MARGEM));
			}
			if (mapDadosDoCliente.containsKey(Parametros.LABEL_INFO_EXTRA)) {
				String[] retornoSplit = mapDadosDoCliente.get(Parametros.LABEL_INFO_EXTRA).split("-");
				if (retornoSplit.length >= 1) {
					clienteDTO.setSecretaria(retornoSplit[0].trim());
				}
				if (retornoSplit.length >= 2) {
					if (retornoSplit[1].contains(Parametros.LABEL_DT_NASC)) {
						clienteDTO.setNascimento(retornoSplit[1].substring(10, 21).trim());
					}
				}

			}

			// Seta os dados do HTML no Objeto SolicitacaoDTO
			Element standardTable = doc.select("table.standardTable").first();

			// Monta corpo com os dados da tabela listagem de solicitações
			// ativas
			Element tbody = standardTable.select("tbody").first();
			Elements rowsTbody = tbody.select("tr");

			for (int i = 0; i < rowsTbody.size(); i++) {
				SolicitacaoDTO solicitacaoDTO = new SolicitacaoDTO();
				Element rowTbody = rowsTbody.get(i);
				Elements colsTbody = rowTbody.select("td");
				solicitacaoDTO.setCliente(clienteDTO);
				solicitacaoDTO.setBanco(colsTbody.get(2).text().trim());
				solicitacaoDTO.setPagas(colsTbody.get(8).text().trim());
				solicitacaoDTO.setParcelas(colsTbody.get(7).text().trim());
				solicitacaoDTO.setValorAutorizado(colsTbody.get(5).text().trim());

				clienteDTO.getSolicitacaes().add(solicitacaoDTO);
			}

			if (Cache.clientesDTOCache.containsKey(clienteDTO.getCpf()) == false && !clienteDTO.getCpf().isEmpty()) {
				Cache.clientesDTOCache.put(clienteDTO.getCpf(), clienteDTO);
			}
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

	}

	/**
	 * Classe que extrai os dados do cliente do header da tabela
	 * percorre o Header utilizando a classe table.headerTable
	 * 
	 * @param doc
	 * @return Map<String, String>
	 */
	public static Map<String, String> getHeaderDadosDoCliente(Document doc) {
		Map<String, String> map = new HashMap<String, String>();
		Element headerTable = doc.select("table.headerTable").get(0);
		Element table = headerTable.parent().select("table").get(2);
		Elements rows = table.select("tr");

		for (int i = 0; i < rows.size(); i++) {
			Element row = rows.get(i);
			Elements cols = row.select("td");

			String key = cols.get(0).text().trim();
			if (key.contains(":")) {
				key = key.substring(0, key.indexOf(":"));
			}
			String value = cols.get(1).text().trim();
			map.put(key, value);
		}
		return map;
	}

	/**
	 * Captura do HTML os dados do theady da table de solicitacoes
	 * @param doc
	 * @return Map<String, String>
	 */
	public static List<String> getTheadTableSolicitacao(Document doc) {
		List<String> listTheads = new ArrayList<String>();
		Element standardTable = doc.select("table.standardTable").first();
		
		// Monta cabeçalho da tabela listagem de solicitações ativas
		Element thead = standardTable.select("thead").first();		
		Element rowThead = thead.select("tr").get(1);
		Elements colsThead = rowThead.select("th");
		
		for (int i = 0; i < colsThead.size(); i++) {
			listTheads.add(colsThead.get(i).text().trim());
		}
			
		return listTheads;
	}
	
	/**
	 * Captura do HTML os dados do tbody da table de solicitacoes
	 *  
	 * @param doc
	 * @return
	 */
	public static List<String> getBodyTableSolicitacao(Document doc) {
		List<String> listTbody = new ArrayList<String>();
		Element standardTable = doc.select("table.standardTable").first();
		
		// Monta corpo com os dados da tabela listagem de solicitações ativas
		Element tbody = standardTable.select("tbody").first();
		Elements rowsTbody = tbody.select("tr");
			
		for (int i = 0; i < rowsTbody.size(); i++) {
			Element rowTbody = rowsTbody.get(i);
			Elements colsTbody = rowTbody.select("td");
			for (int y = 0; y < colsTbody.size(); y++) {
				listTbody.add(colsTbody.get(y).text());
			}
		}
			
		return listTbody;
	}
}

