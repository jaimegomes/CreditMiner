package br.com.bjjsolutions.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.com.bjjsolutions.model.Cliente;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Classe que serve para escrever no arquivo XML
 * 
 * @author Marcelo Lopes Nunes</br>
 *         bjjsolutions.com.br - 30/05/2016</br>
 *         <a href=malito:lopesnunnes@gmail.com>lopesnunnes@gmail.com</a>
 * 
 */
public class WriteFileXML {

    public static void gravaXMLListaProdutos(Map<String, Cliente> clientes, String localArquivo) {
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("clientes", List.class);
        File arquivo = new File(localArquivo + "/clientes.xml");
        FileOutputStream gravar;
        try {
            gravar = new FileOutputStream(arquivo);
            gravar.write(xStream.toXML(clientes).getBytes());
            gravar.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
