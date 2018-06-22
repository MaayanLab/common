package edu.mssm.pharm.maayanlab.common.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

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
		String line = fileReader.readLine();
		while (line != null) {
			StringTokenizer tk = new StringTokenizer(line);
			String token = tk.nextToken();
			while (token != null) {
				inputList.add(token.trim());
				token = tk.nextToken();
			}
			line = fileReader.readLine();
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
