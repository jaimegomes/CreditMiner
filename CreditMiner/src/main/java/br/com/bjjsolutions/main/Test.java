package br.com.bjjsolutions.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * pura viaje
 * @author marcelo
 *
 */
public class Test {

	public static void main(String[] args) {

		try {

			File input = new File("/home/marcelo/75147653953.html");
			Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
			System.out.println(doc.html());
			getDadosDoColaborador(doc);

			getDadosParcela(doc);
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}

	}

	/**
	 * 
	 * @param doc
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getDadosDoColaborador(Document doc) {
		Map<String, Object> map = new LinkedHashMap<>();
		// Class table.headerTable - referencia para encontrar os dados do colaborador
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

	public static Map<String, Object> getDadosParcela(Document doc) {
		Map<String, Object> map = new LinkedHashMap<>();
		Element standardTable = doc.select("table.standardTable").first();
		Elements theads = standardTable.parent().select("thead");
		
		for (int i = 0; i < theads.size(); i++) { 
			Element thead = theads.get(i);
			Element rowThead = thead.select("tr").get(1);
			Elements colsThead = rowThead.select("th");
			
			List<String> listTheads = new ArrayList<String>();
			for (int j = 0; j < colsThead.size(); j++) {
				listTheads.add(colsThead.get(j).text());
			}
			
			Element tbody = thead.parent().select("tbody").first();
			Elements rowsTbody = tbody.select("tr");
			
			for (int z = 0; z < rowsTbody.size(); z++) {
				Element rowTbody = rowsTbody.get(z);
				Elements colsTbody = rowTbody.select("td");
				for (int y = 0; y < listTheads.size(); y++) {
					map.put(listTheads.get(y), colsTbody.get(y).text());
				}
			}
			
		}
		return map;
	}
}

