package edu.mssm.pharm.maayanlab.common.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

public class FileUtils {

	public static ArrayList<String> readFile(File file) {
		String s;
		ArrayList<String> fileLines = new ArrayList<String>();
		
		BufferedReader in = null;
		
		try {
			in = new BufferedReader(new FileReader(file));
			
			while ((s = in.readLine()) != null){
				fileLines.add(s);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Unable to find file: " + file.getAbsolutePath());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Unable to read file: " +  file.getAbsolutePath());
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.err.println("Unable to close file: " + file.getAbsolutePath());
				e.printStackTrace();
			}
		}
		
		return fileLines;
	}
	
	public static ArrayList<String> readFile(String filename) {
		String s;
		ArrayList<String> fileLines = new ArrayList<String>();
		
		BufferedReader in = null;
		
		try {
			in = new BufferedReader(new FileReader(filename));
			
			while ((s = in.readLine()) != null){
				fileLines.add(s.trim());
			}
		} catch (FileNotFoundException e) {
			System.err.println("Unable to find file: " + filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Unable to read file: " +  filename);
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.err.println("Unable to close file: " + filename);
				e.printStackTrace();
			}
		}
		
		return fileLines;
	}
	
	public static URL getFileURL(String fileName){
		return FileUtils.class.getClassLoader().getResource(fileName);
	}
	
	// Expensive, don't use often
	public static ArrayList<String> readResource(String resource) {
		
		InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(resource);
		
		if (is == null) {
			// fall back to reading file outside of jar
			return FileUtils.readFile(resource);
		}
		else {
			String s;
			ArrayList<String> fileLines = new ArrayList<String>();
			
			BufferedReader in = null;
			
			try {
				in = new BufferedReader(new InputStreamReader(is));
				while ((s = in.readLine()) != null){
					fileLines.add(s.trim());
				}
			} catch (IOException e) {
				System.err.println("Unable to read file: " +  resource);
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					System.err.println("Unable to close file: " + resource);
					e.printStackTrace();
				}
			}
			
			return fileLines;
		}
	}
	
	public static boolean resourceExists(String resource) {
		return FileUtils.class.getClassLoader().getResource(resource) != null;
	}
	
	@Deprecated
	public static ArrayList<String> readResource(Object o, String resource) {
		
		InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(resource);
		
		if (is == null) {
			// fall back to reading file outside of jar
			return FileUtils.readFile(resource);
		}
		else {
			String s;
			ArrayList<String> fileLines = new ArrayList<String>();
			
			BufferedReader in = null;
			
			try {
				in = new BufferedReader(new InputStreamReader(is));
				while ((s = in.readLine()) != null){
					fileLines.add(s.trim());
				}
			} catch (IOException e) {
				System.err.println("Unable to read file: " +  resource);
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					System.err.println("Unable to close file: " + resource);
					e.printStackTrace();
				}
			}
			
			return fileLines;
		}
	}
	
	public static void write(PrintWriter writer, String header, Collection<?> outputCollection) {
		if (!header.isEmpty())
			writer.println(header);
		for (Object o : outputCollection)
			writer.println(o.toString());
		writer.close();
	}
	
	public static void writeStream(OutputStream out, String header, Collection<?> outputCollection) {
		PrintWriter writer = new PrintWriter(out);
		write(writer, header, outputCollection);
	}
	
	public static void writeStream(OutputStream out, Collection<?> outputCollection) {
		writeStream(out, "", outputCollection);
	}
	
	// Write a collection using each item's toString() method with a header to a file
	public static void writeFile(String filename, String header, Collection<?> outputCollection) {
		try {
			PrintWriter writer = new PrintWriter(new File(filename));
			write(writer, header, outputCollection);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeFile(String filename, Collection<?> outputCollection) {
		writeFile(filename, "", outputCollection);
	}
	
	public static void writeString(String filename, String str) {
		try {
			PrintWriter writer = new PrintWriter(new File(filename));
			writer.println(str);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// Given "filename.ext", strips the ".ext" and gives only "filename"
	public static String stripFileExtension(String filename) {
		int index = filename.lastIndexOf(".");
		
		return (index > 0) ? filename.substring(0, index) : filename; 
	}
	
	// Returns -1 if list is empty
	// Returns message and offset for the offending item on the list and the index
	public static boolean validateList(Collection<String> testList) throws ParseException {
		if (testList.isEmpty())
			throw new ParseException("", -1);
		
		Pattern p = Pattern.compile("[\\w\\-@./]+");
		
		int i = 0;
		for (String item : testList) {
			if (!p.matcher(item).matches())
				throw new ParseException(item, i);
			i++;
		}
		return true;
	}
	
}
