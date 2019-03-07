package edu.mssm.pharm.maayanlab.common.graph;

import java.util.HashSet;

public class ShapeNode {
	
	public enum Shape {
		ELLIPSE, ROUNDRECTANGLE, RECTANGLE;
	};

	private String id;
	private String color;
	private Shape shape;
	private HashSet<String> neighbors = new HashSet<String>();
	
	public ShapeNode(String id, String color, Shape shape) {
		this.id = id;
		this.color = color;
		this.shape = shape;
	}

	public String getId() {
		return id;
	}
	
	public String getColor() {
		return color;
	}

	public String getShape() {
		return shape.toString().toLowerCase();
	}

	public void addNeighbor(String id) {
		neighbors.add(id);
	}
	
	public HashSet<String> getNeighbors() {
		return neighbors;
	}
	
}
