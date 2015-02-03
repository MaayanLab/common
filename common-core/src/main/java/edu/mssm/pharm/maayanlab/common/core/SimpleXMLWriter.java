package edu.mssm.pharm.maayanlab.common.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


public class SimpleXMLWriter {
	
	private TransformerHandler xmlHandler;
	private TransformerHandler lastHandler;
	private StreamResult fileOutput;
	private AttributesImpl atts = new AttributesImpl();
	private Stack<String> nodes = new Stack<String>();
	private boolean started = false;

	public SimpleXMLWriter(String outputFile) {
		this(outputFile, "xml");
	}
	
	public SimpleXMLWriter(String outputFile, String method) {
		try {
			PrintWriter out = new PrintWriter(outputFile);
			fileOutput = new StreamResult(out);
			fileOutput.getWriter();
			SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
			
			xmlHandler = tf.newTransformerHandler();
			Transformer serializer = xmlHandler.getTransformer();
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // hack to get indenting working
			serializer.setOutputProperty(OutputKeys.METHOD, method);
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			xmlHandler.setResult(fileOutput);
			lastHandler = xmlHandler;
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void addTransform(String resourceFile, String method) throws SAXException {
		if (started)
			throw new SAXException("Must do addTransform before startElement.");
		
		try {
			SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
			TransformerHandler xsltHandler = tf.newTransformerHandler(new StreamSource(
					SimpleXMLWriter.class.getClassLoader().getResourceAsStream(resourceFile)));
			
			Transformer serializer = xsltHandler.getTransformer();
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // hack to get indenting working
			serializer.setOutputProperty(OutputKeys.METHOD, method);
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			lastHandler.setResult(new SAXResult(xsltHandler));
			xsltHandler.setResult(fileOutput);
			lastHandler = xsltHandler;
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void startPlainElement(String element) {
		startElement(element, "");
	}
	
	public void startElementWithAttributes(String element, String ... attributes) {
		startElement(element, "", attributes);
	}
	
	public void startElement(String element, String text, String ... attributes) {
		if (!started)
			start();
		
		try {
			if (attributes.length % 2 != 0)
				throw new SAXException("Incorrect length for field attributes.");
			
			for (int i = 0; i < attributes.length; i += 2) {
				atts.addAttribute("", attributes[i], attributes[i], "CDATA", attributes[i+1]);
			}
			
			xmlHandler.startElement("", element, element, atts);
			
			atts.clear();
			
			if(!text.equals(""))
				xmlHandler.characters(text.toCharArray(), 0, text.length());
			
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		nodes.add(element);
	}
	
	public void listElement(String element, String text, String ... attributes) {
		startElement(element, text, attributes);
		endElement();
	}
	
	protected void endElement(String element) {
		try {
			xmlHandler.endElement("", "", element);
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		nodes.pop();
	}
	
	public void endElement() {
		endElement(nodes.peek());
	}
	
	protected void start() {
		try {
			xmlHandler.startDocument();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		started = true;
	}
	
	public void close() {
		// Close all elements in case they get sloppy
		while (!nodes.isEmpty()) {
			endElement();
		}
		
		try {
			xmlHandler.endDocument();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		try {
			fileOutput.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
