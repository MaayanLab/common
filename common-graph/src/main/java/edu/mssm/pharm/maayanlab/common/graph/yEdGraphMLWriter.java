package edu.mssm.pharm.maayanlab.common.graph;

import edu.mssm.pharm.maayanlab.common.core.SimpleXMLWriter;

public class yEdGraphMLWriter implements GraphWriter {
	
	protected SimpleXMLWriter sxw;
	
	public yEdGraphMLWriter(String filename) {
		sxw = new SimpleXMLWriter(filename);
	}

	@Override
	public void open() {
		sxw.startElement("graphml", "", 
				"xmlns", "http://graphml.graphdrawing.org/xmlns",
				"xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance",
				"xsi:schemaLocation", "http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd",
				"xmlns:y", "http://www.yworks.com/xml/graphml");
		sxw.listElement("key", "", "id", "d0", "for", "node", "yfiles.type", "nodegraphics");
		sxw.listElement("key", "", "id", "d1", "for", "edge", "yfiles.type", "edgegraphics");
		sxw.startElement("graph", "", "id", "G", "edgedefault", "undirected");	
	}
	
	@Override
	public void addNode(String id, String color, String shape, int size) {
		sxw.startElement("node", "", "id", id);
			sxw.startElement("data", "", "key", "d0");
				sxw.startElement("y:ShapeNode", "");
					sxw.listElement("y:Geometry", "", "height", Integer.toString(size+20), "width", Integer.toString(size+20));
					sxw.listElement("y:Fill", "", "color", color);
					sxw.listElement("y:NodeLabel", id);
					sxw.listElement("y:Shape", "", "type", shape);
				sxw.endElement();
			sxw.endElement();
		sxw.endElement();
	}
	
	@Override
	public void addEdge(String source, String target) {
		sxw.startElement("edge", "", "id", source + ":" + target, "source", source, "target", target);
			sxw.startElement("data", "", "key", "d1");
				sxw.startElement("y:PolyLineEdge", "");
					sxw.listElement("y:Arrows", "", "source", "none", "target", "none");
				sxw.endElement();
			sxw.endElement();
		sxw.endElement();
	}
	
	@Override
	public void close() {
		sxw.close();		
	}
	
}
