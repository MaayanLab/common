package edu.mssm.pharm.maayanlab.common.graph;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class PajekNETWriter extends PrintWriter implements GraphWriter {

	private HashMap<String, Integer> nodeMap = new HashMap<String, Integer>();
	private StringBuilder vertices = new StringBuilder();
	private StringBuilder edges = new StringBuilder();
	
	private int nodeCount = 1;
	
	public PajekNETWriter(String fileName) throws FileNotFoundException {
		super(fileName);
	}

	@Override
	public void open() {
	}
	
	@Override
	public void close() {
		super.println("*Vertices " + nodeMap.size());
		super.print(vertices.toString());
		super.println("*arcs");
		super.print(edges.toString());
		super.close();
	}

	@Override
	public void addNode(String label, String color, String shape, int size) {
		if (!nodeMap.containsKey(label))
			nodeMap.put(label, nodeCount++);
		vertices.append(nodeMap.get(label).toString()).append(" \"").append(label).append("\"").append("0.0 0.0 0.0").append("ic").append(convertHexColorToString(color)).append("\n");
	}

	@Override
	public void addEdge(String source, String target) {
		edges.append(nodeMap.get(source)).append(" ").append(nodeMap.get(target)).append("\n");
	}
	
	private String convertHexColorToString(String hexColor) {
		int intValue = Integer.parseInt(hexColor.substring(1), 16);	// Removes the #
		
		if (intValue == (Color.PINK.getRGB() & 0x00ffffff))
			return "Pink";
		else if (intValue == ( Color.RED.getRGB() & 0x00ffffff))
			return "Red";		
		else if (intValue == (Color.ORANGE.getRGB() & 0x00ffffff))
			return "Orange";
		else if (intValue == (Color.YELLOW.getRGB() & 0x00ffffff))
			return "Yellow";
		else if (intValue == (Color.GREEN.getRGB() & 0x00ffffff))
			return "Green";					
		else if (intValue == (Color.CYAN.getRGB() & 0x00ffffff))
			return "Cyan";
		else if (intValue == (Color.BLUE.getRGB() & 0x00ffffff))
			return "Blue";		
		else if (intValue == (Color.MAGENTA.getRGB() & 0x00ffffff))
			return "Magenta";
		else if (intValue == (Color.BLACK.getRGB() & 0x00ffffff))
			return "Black";
		else if (intValue == (Color.DARK_GRAY.getRGB() & 0x00ffffff))
			return "Gray75";
		else if (intValue == (Color.GRAY.getRGB() & 0x00ffffff))
			return "Gray";
		else if (intValue == (Color.LIGHT_GRAY.getRGB() & 0x00ffffff))
			return "Gray25";
		else if (intValue == (Color.WHITE.getRGB() & 0x00ffffff))
			return "White";
		else
			return findNearestColor(intValue);
	}
	
	private String findNearestColor(int intValue) {
		int red = intValue >> 16;
		int green = (intValue & 0x00ff00) >> 8;
		int blue = intValue & 0x0000ff;		
		int threeBit = (red > 127 ? 4 : 0) + (green > 127 ? 2 : 0) + (blue > 127 ? 1 : 0);
		
		switch (threeBit) {
		case 0: return "Black"; 
		case 1: return "Blue"; 
		case 2: return "Green"; 
		case 3: return "Cyan";
		case 4: return "Red";
		case 5: return "Magneta";
		case 6: return "Yellow";
		case 7: return "White";
		default: return "Black";
		}
	}
	
}
