package edu.mssm.pharm.maayanlab.common.swing;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class SystemClipboard {
	
	public static String paste() {

		String text = null;
		
		Transferable clipboardData = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		
		try {
			if (clipboardData != null && clipboardData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				text = (String) clipboardData.getTransferData(DataFlavor.stringFlavor);
			}				
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return text;
	}
	
	public static void copy(String text) {
		
		StringSelection stringSelection = new StringSelection(text);
		
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		
	}
	
}
