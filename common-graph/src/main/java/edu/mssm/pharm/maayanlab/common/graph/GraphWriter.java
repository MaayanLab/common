package edu.mssm.pharm.maayanlab.common.graph;

public interface GraphWriter {
	
	public void open();
	
	public void close();
	
	public void addNode(String id, String color, String shape, int size);
	
	public void addEdge(String source, String target);
	
}
