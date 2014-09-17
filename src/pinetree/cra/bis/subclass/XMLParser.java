package pinetree.cra.bis.subclass;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser {
	
	public static XmlResponse parsingResponse(String xmlString){
		XmlResponse response = new XmlResponse();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		//Log.i("DebugPrint",xmlString);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xmlString)));

			NodeList nodeList = document.getElementsByTagName("response");
			
			Node node = nodeList.item(0).getFirstChild();
			if(!node.getTextContent().equals("0")){
				response.setError(true);
			}else{
				response.setError(false);
				response.setMessage(node.getNextSibling().getTextContent());
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	public XMLParser(String xmlString){
		
	}
}
