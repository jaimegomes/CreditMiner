package br.com.bjjsolutions.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import br.com.bjjsolutions.util.Util;

public class FactoryHTMLJsoup extends HTMLJsoup {

	private void travelFilesHTML() throws IOException {

		File file = new File(Util.getDirectorySO() + "/cpfs");
		File afile[] = file.listFiles();
		int i = 0;
		for (int j = afile.length; i < j; i++) {
			File files = afile[i];
			
			String html = readFile(files);
			
			createObjectRecordHTML(html, files.getName());
		}
	}

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
