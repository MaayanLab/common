package edu.mssm.pharm.maayanlab.common.swing;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JTextArea;

public class UIUtils {

	public static ArrayList<String> getTextAreaText(String text) {
		
		String[] list = text.split("\n");
		ArrayList<String> convertedList = new ArrayList<String>();
		
		for (String item : list) 
			if (!item.isEmpty())
				convertedList.add(item);
		
		return convertedList;
	}
	
	public static ArrayList<String> getTextAreaText(JTextArea textArea) {
		
		return getTextAreaText(textArea.getText());
	}
	
	public static void setTextAreaText(JTextArea textArea, Collection<String> list) {
		
		StringBuilder outputText = new StringBuilder();
		
		for (String item : list)
			outputText.append(item).append('\n');
		
		textArea.setText(outputText.toString());
		
		textArea.setCaretPosition(0);
	}
	
	public static int getTextAreaTextLineCount(JTextArea textArea) {		
		String[] list = textArea.getText().split("\n");

		// Handle first line empty string case
		if (list.length == 1)
			return (list[0].isEmpty()) ? 0 : 1;
		else
			return list.length;
	}
	
	@Deprecated
	public static boolean isOnlyOneLine(JTextArea textArea) {
		return textArea.getText().indexOf("\n") == -1;
	}
}
