package parser;


import graph.DirectedGraph;
import graph.Marker;
import graph.Node;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class for parsing XML files generated by VisualParadigm
 * 
 * @author Maciek
 * 
 */
public class VisualParadigmXmlParser extends AbstractParser {

	private DirectedGraph mGraph = new DirectedGraph();

	@Override
	public DirectedGraph parse(String filename) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db;
		try {
			XPathFactory xpathFactory = XPathFactory.newInstance();
			db = dbf.newDocumentBuilder();
			Document dom = db.parse(filename);

			Element projElement = dom.getDocumentElement();
			// there is exactly o
			Element modelsElement = (Element) projElement.getElementsByTagName("Models").item(0);

			// Every task is represented by node in graph
			NodeList tasks = modelsElement.getElementsByTagName("BPTask");
			parseTasks(tasks);

			// Parse gateways
			NodeList gateways = modelsElement.getElementsByTagName("BPGateway");
			parseGateways(gateways);
			
			
			
			// dziala na windowsowym, na linuksowym nie
			XPath xpath = xpathFactory.newXPath();
//			NodeList sequences = (NodeList) xpath.evaluate(
//					//This is madness!
//					"BPSequenceFlow", modelsElement,
//					XPathConstants.NODESET);
			NodeList sequences = (NodeList) xpath.evaluate(
					//This is madness!
					"ModelRelationshipContainer/ModelChildren/ModelRelationshipContainer/ModelChildren/BPSequenceFlow", modelsElement,
					XPathConstants.NODESET);
			
			// Every sequence is connection between nodes
//			NodeList sequences = modelsElement.getElementsByTagName("BPSequenceFlow");
			parseSequences(sequences);

			// TODO: more!

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mGraph;
	}

	void parseGateways (NodeList gateways){
		for (int i=0; i< gateways.getLength(); i++){
			Element el = (Element) gateways.item(i);
			final String id = el.getAttribute("Id");
			final String formula = el.getAttribute("Name");
			Node node = new Node (id, formula);
			
			// there is exactly o
			Element gatewayType = (Element)el.getElementsByTagName("GatewayType").item(0);
			
			if (gatewayType.getFirstChild().getNodeName().equals("BPGatewayDataBasedXOR") ||
					gatewayType.getFirstChild().getNodeName().equals("BPGatewayEventBasedXOR"))
				node.setMarker(Marker.EXCLUSIVE_CHOICE);
			if (gatewayType.getFirstChild().getNodeName().equals("BPGatewayDataBasedAND"))
				node.setMarker(Marker.PARALLEL_SPLIT);
			if (gatewayType.getFirstChild().getNodeName().equals("BPGatewayComplex"))
				node.setMarker(Marker.MULTI_CHOICE);
			
			System.out.println( gatewayType.getChildNodes());
				
//			BPGatewayDataBasedXOR
//			BPGatewayAND
//			BPGatewayComplex
//			BPGatewayEventBasedXOR 
			
			mGraph.addVertex(node);
			System.out.println("gateway id = " + id + "    formula: " + formula);
		}
		
	}
	
	void parseTasks(NodeList tasks) {
		for (int i = 0; i < tasks.getLength(); i++) {
			Element el = (Element) tasks.item(i);
			final String id = el.getAttribute("Id");
			final String formula = el.getAttribute("Name");
			mGraph.addVertex(new Node(id, formula));
			System.out.println("task id = " + id + "    formula: " + formula);
		}
	}

	private void parseSequences(NodeList sequences) {
		for (int i = 0; i < sequences.getLength(); i++) {			
			Element el = (Element) sequences.item(i);
			final String fromId = el.getAttribute("From");
			final String toId = el.getAttribute("To");
			System.out.println("Trying New connection: " + fromId + "--->" + toId);
			mGraph.addEdge(fromId + toId, fromId , toId);
//			System.out.println("New connection: " + fromId + "--->" + toId);
		}
	}
}
