package edu.mssm.pharm.maayanlab.common.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.http.Part;

public class PartReader {

	public static ArrayList<String> readLines(Part chunk) throws IOException {
		ArrayList<String> inputList = new ArrayList<String>();
		
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(chunk.getInputStream()));
		String line = null;
		while ((line = fileReader.readLine()) != null)
			inputList.add(line.trim());
		fileReader.close();
		
		return inputList;
	}

	public static ArrayList<String> readTokens(Part chunk) throws IOException {
		ArrayList<String> inputList = new ArrayList<String>();
		
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(chunk.getInputStream()));
		String line = null;
		while ((line = fileReader.readLine()) != null) {
			String[] elements = line.split("\\s");
			for (int i = 0; i < elements.length; i++) {
				String element = elements[i].trim();
				if (!element.isEmpty())
					inputList.add(element);
			}
		}
		fileReader.close();
		
		return inputList;
	}
	
	public static String readString(Part chunk) throws IOException {
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(chunk.getInputStream()));
		String line = fileReader.readLine();
		fileReader.close();
		return line;
	}
	
	public static int readInteger(Part chunk) throws IOException {
		return Integer.parseInt(readString(chunk));
	}
	
	public static double readDouble(Part chunk) throws IOException {
		return Double.parseDouble(readString(chunk));
	}	
}
