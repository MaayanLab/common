package edu.mssm.pharm.maayanlab.common.graph;

import java.util.HashSet;

import edu.mssm.pharm.maayanlab.common.core.SimpleXMLWriter;

public class GraphMLWriter {
	
	protected SimpleXMLWriter sxw;
	private HashSet<String> nodeSet = new HashSet<String>();
	private HashSet<String> edgeSet = new HashSet<String>();
	
	public GraphMLWriter(String filename) {
		sxw = new SimpleXMLWriter(filename);
	}
	
	public void open() {
		sxw.startElement("graphml", "", 
				"xmlns", "http://graphml.graphdrawing.org/xmlns",
				"xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance",
				"xsi:schemaLocation", "http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd");
	}
	
	public void addNodeKey(String id, String attrName, String attrType) {
		addKey(id, "node", attrName, attrType);
	}
	
	public void addNodeKey(String id, String attrName, String attrType, String defaultAttr) {
		addKey(id, "node", attrName, attrType, defaultAttr);
	}
	
	public void addEdgeKey(String id, String attrName, String attrType) {
		addKey(id, "edge", attrName, attrType);
	}
	
	public void addEdgeKey(String id, String attrName, String attrType, String defaultAttr) {
		addKey(id, "edge", attrName, attrType, defaultAttr);
	}
	
	public void addKey(String id, String whatFor, String attrName, String attrType) {
		sxw.listElement("key", "", "id", id, "for", whatFor, "attr.name", attrName, "attr.type", attrType);
	}
	
	public void addKey(String id, String whatFor, String attrName, String attrType, String defaultAttr) {
		sxw.startElement("key", "", "id", id, "for", whatFor, "attr.name", attrName, "attr.type", attrType);
			sxw.listElement("default", defaultAttr);
		sxw.endElement();
	}
	
	public void startGraph(String id, String edgedefault) {
		sxw.startElement("graph", "", "id", id, "edgedefault", edgedefault);
	}
	
	public void addDefaultNode(String id) {
		if (!nodeSet.contains(id)) {
			sxw.listElement("node", "", "id", id);
			nodeSet.add(id);
		}
	}
	
	public void addNode(String id, String attr, String attrValue) {
		if (!nodeSet.contains(id)) {
			sxw.startElement("node", "", "id", id);
				sxw.listElement("data", attrValue, "key", attr);
			sxw.endElement();
			nodeSet.add(id);
		}
	}
	
	public void addDefaultEdge(String id, String source, String target) {
		if (!edgeSet.contains(id)) {
			sxw.listElement("edge", "", "id", id, "source", source, "target", target);
			edgeSet.add(id);
		}
	}
	
	public void addEdge(String id, String source, String target, String attr, String attrValue) {
		if (!edgeSet.contains(id)) {
			sxw.startElement("edge", "", "id", id, "source", source, "target", target);
				sxw.listElement("data", attrValue, "key", attr);
			sxw.endElement();
			edgeSet.add(id);
		}
	}
	
	public void close() {
		sxw.close();
	}
	
}
