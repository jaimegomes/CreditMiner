package br.com.mjsolutions.processing;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.mjsolutions.dto.ClienteDTO;
import br.com.mjsolutions.dto.SolicitacaoDTO;
import br.com.mjsolutions.util.Util;

/**
 * Classe que serve para extrair as informações do HTML
 * 
 * @author Marcelo Lopes Nunes</br> bjjsolutions.com.br - 23/06/2016</br> <a
 *         href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public class HTMLJsoup {

	/**
	 * Metodo responsavel por percorrer o html e extrair os dados setando no
	 * objeto DTO
	 * 
	 * @param html
	 * @param name
	 */
	public void createObjectRecordHTML(String html, String name) {
		try {
			Document doc = Jsoup.parse(html);

			// System.out.println(doc.html());

			if (Cache.clientesDTOCache == null) {
				Cache.clientesDTOCache = new LinkedHashMap<String, ClienteDTO>();
			}

			// Extrai os dados do header da tabela do HTML e seta no Objeto

			Map<String, String> mapDadosDoCliente = getHeaderDadosDoCliente(doc, name);

			if (mapDadosDoCliente.size() > 0) {

				ClienteDTO clienteDTO = new ClienteDTO();

				String names[] = name.split("-");
				clienteDTO.setCpf(names[0].substring(0, 11));

				if (mapDadosDoCliente.containsKey(Parametros.LABEL_COLABORADOR)) {
					clienteDTO.setColaborador(mapDadosDoCliente.get(Parametros.LABEL_COLABORADOR));
				}
				if (mapDadosDoCliente.containsKey(Parametros.LABEL_MATRICULA)) {
					clienteDTO.setMatricula(mapDadosDoCliente.get(Parametros.LABEL_MATRICULA));
				}
				if (mapDadosDoCliente.containsKey(Parametros.LABEL_MARGEM)) {
					clienteDTO.setMargem(mapDadosDoCliente.get(Parametros.LABEL_MARGEM));
				}
				if (mapDadosDoCliente.containsKey(Parametros.ULTIMA_FOLHA_MOVIMENTADA_DO_SERVIDOR)) {
					clienteDTO.setUltimaFolhaMovimentada(mapDadosDoCliente.get(Parametros.ULTIMA_FOLHA_MOVIMENTADA_DO_SERVIDOR));
				}
				if (mapDadosDoCliente.containsKey(Parametros.LABEL_INFO_EXTRA)) {
					String[] retornoSplit = mapDadosDoCliente.get(Parametros.LABEL_INFO_EXTRA).split("-");
					if (retornoSplit.length >= 1) {
						clienteDTO.setSecretaria(retornoSplit[0].trim());
					}
					if (retornoSplit.length >= 2) {
						if (retornoSplit[1].contains(Parametros.LABEL_DT_NASC)) {
							clienteDTO.setNascimento(retornoSplit[1].substring(10, 21).trim());
						} else if (retornoSplit[2].contains(Parametros.LABEL_DT_NASC)) {
							clienteDTO.setNascimento(retornoSplit[2].substring(10, 21).trim());
						}
					}
					if (retornoSplit.length >= 3) {
						if (retornoSplit[2].contains(Parametros.CARGO_FUNCAO)) {
							clienteDTO.setCargoFuncao(retornoSplit[2].substring(15).trim());
						} else if (retornoSplit[3].contains(Parametros.CARGO_FUNCAO)) {
							clienteDTO.setCargoFuncao(retornoSplit[3].substring(15).trim());
						}
					}
				}

				if (!name.contains("margem")) {
					// Extrai os dados do tbody da tabela listagem de
					// solicitações
					Element standardTable = doc.select("table.standardTable").first();

					// verifica se é listagem de Solicitações Ativas
					Element header = standardTable.select(".standardTable_Header").first();

					if (header.text().contains(Parametros.LISTAGEM_SOLICITACOES_ATIVAS)) {

						List<SolicitacaoDTO> listSolicitacoes = new ArrayList<SolicitacaoDTO>();

						Element tbody = standardTable.select("tbody").first();

						if (tbody.hasText()) {
							Elements rowsTbody = tbody.select("tr");

							for (int i = 0; i < rowsTbody.size(); i++) {
								// Extrai os dados do HTML e seta no Objeto

								Element rowTbody = rowsTbody.get(i);
								Elements colsTbody = rowTbody.select("td");

								SolicitacaoDTO solicitacaoDTO = new SolicitacaoDTO();
								solicitacaoDTO.setControleConsignataria(colsTbody.get(1).text().trim());
								solicitacaoDTO.setTipo(colsTbody.get(2).text().trim());
								solicitacaoDTO.setBanco(colsTbody.get(3).text().trim());
								solicitacaoDTO.setParcelas(colsTbody.get(8).text().trim());
								solicitacaoDTO.setPagas(colsTbody.get(9).text().trim());
								solicitacaoDTO.setValorAutorizado(colsTbody.get(6).text().trim());

								listSolicitacoes.add(solicitacaoDTO);
							}
							clienteDTO.setSolicitacaes(listSolicitacoes);
						}
					}
				}

				if (!clienteDTO.getMatricula().isEmpty()) {
					if (!Cache.clientesDTOCache.containsKey(clienteDTO.getMatricula())) {
						Cache.clientesDTOCache.put(clienteDTO.getMatricula(), clienteDTO);
					} else {
						ClienteDTO clienteDTOMerge = Cache.clientesDTOCache.get(clienteDTO.getMatricula());
						if (clienteDTO.getMargem() != null) {
							if (!clienteDTO.getMargem().equals("")) {
								clienteDTOMerge.setMargem(clienteDTO.getMargem());
							}
						}
						Cache.clientesDTOCache.put(clienteDTO.getMatricula(), clienteDTOMerge);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(Util.getProperty("prop.error.html.jsoup") + name + " " + e.getMessage());
		}
	}

	/**
	 * Classe que extrai os dados do cliente do header da tabela percorre o
	 * Header utilizando a classe table.headerTable
	 * 
	 * @param doc
	 * @return Map<String, String>
	 */
	private static Map<String, String> getHeaderDadosDoCliente(Document doc, String name) {
		Element headerTable = null;
		Element table = null;
		Element tableMovimento = null;
		Elements rows = null;
		Elements rowsMovimento = null;

		int verificaTamanhoHeaderTable = doc.select("table.headerTable").size();

		Map<String, String> map = new LinkedHashMap<String, String>();

		if (verificaTamanhoHeaderTable > 0) {
			headerTable = doc.select("table.headerTable").get(0);
			table = headerTable.parent().select("table").get(2);
			rows = table.select("tr");

			for (int i = 0; i < rows.size(); i++) {
				Element row = rows.get(i);
				Elements cols = row.select("td");

				String key = cols.get(0).text().trim();
				if (key.contains(":")) {
					key = key.substring(0, key.indexOf(":"));
				}
				if (key.equals("")) {
					key = Parametros.LABEL_INFO_EXTRA;
				}
				if (key.contains("Margem Dispo")) {
					key = Parametros.LABEL_MARGEM;
				}
				String value = cols.get(1).text().trim();
				map.put(key, value);
			}

			if (!name.contains("margem")) {
				tableMovimento = headerTable.parent().select("table").get(3);
				rowsMovimento = tableMovimento.select("tr");

				forMovimento: for (int i = 0; i < rowsMovimento.size(); i++) {
					Element row = rowsMovimento.get(i);
					Elements cols = row.select("td");

					String key = cols.get(0).text().trim();
					if (key.contains(Parametros.ULTIMA_FOLHA_MOVIMENTADA_DO_SERVIDOR)) {
						key = Parametros.ULTIMA_FOLHA_MOVIMENTADA_DO_SERVIDOR;
						String value = cols.get(1).text().trim();

						map.put(key, value);

						break forMovimento;
					}
				}
			}
		}

		return map;
	}

	/**
	 * Captura do HTML os dados do theady da table de solicitacoes
	 * 
	 * 
	 * @param doc
	 * @return Map<String, String>
	 */
	@Deprecated
	private static List<String> getTheadTableSolicitacao(Document doc) {
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
}
