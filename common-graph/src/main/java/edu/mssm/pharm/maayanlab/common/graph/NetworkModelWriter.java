package edu.mssm.pharm.maayanlab.common.graph;

import java.util.HashMap;
import java.util.HashSet;

import edu.mssm.pharm.maayanlab.common.graph.ShapeNode.Shape;

public class NetworkModelWriter {
	
	private HashMap<String, ShapeNode> network = new HashMap<String, ShapeNode>();
	private HashSet<String> visited;
	
	public void addNode(String id, String color, Shape shape) {
		if (!network.containsKey(id))
			network.put(id, new ShapeNode(id, color, shape));
	}
	
	public void addInteraction(String source, String target) {
		network.get(source).addNeighbor(target);
		network.get(target).addNeighbor(source);
	}
	
	public void writeGraph(GraphWriter gw) {
		visited = new HashSet<String>();
		for (String nodeName : network.keySet()) {
			ShapeNode node = network.get(nodeName);
			gw.addNode(node.getId(), node.getColor(), node.getShape(), node.getNeighbors().size());
		}
		for (String nodeName : network.keySet())
			visit(gw, network.get(nodeName));
	}
	
	private void visit(GraphWriter gw, ShapeNode node) {
		visited.add(node.getId());
		HashSet<String> neighbors = node.getNeighbors();
		
		for (String neighbor : neighbors)
			if (!visited.contains(neighbor))
				gw.addEdge(node.getId(), neighbor);						
	}
}
