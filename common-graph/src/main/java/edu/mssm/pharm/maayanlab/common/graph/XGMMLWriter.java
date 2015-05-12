package edu.mssm.pharm.maayanlab.common.graph;

import java.util.HashMap;

import edu.mssm.pharm.maayanlab.common.core.SimpleXMLWriter;

public class XGMMLWriter implements GraphWriter {

	protected SimpleXMLWriter sxw;
	private HashMap<String, Integer> nodeMap = new HashMap<String, Integer>();
	private int nodeCount = 0;
	
	public XGMMLWriter(String filename) {
		sxw = new SimpleXMLWriter(filename);
	}

	@Override
	public void open() {
		sxw.startElement("graph", "", 
				"label", "X2K Subnetwork",
				"xmlns:dc", "http://purl.org/dc/elements/1.1/",
				"xmlns:xlink", "http://www.w3.org/1999/xlink",
				"xmlns:rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
				"xmlns:cy", "http://www.cytoscape.org",
				"xmlns", "http://www.cs.rpi.edu/XGMML", 				
				"directed", "1");
		sxw.listElement("att", "", "name", "documentVersion", "value", "1.1");
		sxw.listElement("att", "", "type", "string", "name", "backgroundColor", "value", "#ffffff");
	}
	
	@Override
	public void close() {
		sxw.close();
	}
	
	@Override
	public void addNode(String label, String color, String shape, int size) {
		if (!nodeMap.containsKey(label))
			nodeMap.put(label, new Integer(nodeCount++));
		sxw.startElement("node", "", "label", label, "id", nodeMap.get(label).toString());
			sxw.listElement("att", "", "type", "string", "name", "node.fillColor", "value", color);
			sxw.listElement("att", "", "type", "string", "name", "node.shape", "value", shape);
			sxw.listElement("att", "", "type", "string", "name", "node.label", "value", label);
			sxw.listElement("graphics", "", "type", shape, "h", Integer.toString(size+20), "w", Integer.toString(size+20), "fill", color);			
		sxw.endElement();
		
	}
	
	@Override
	public void addEdge(String source, String target) {
		sxw.listElement("edge", "", "label", source + ":" + target, "source", nodeMap.get(source).toString(), "target", nodeMap.get(target).toString());
	}

}
