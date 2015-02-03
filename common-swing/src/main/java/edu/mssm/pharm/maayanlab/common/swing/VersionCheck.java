package edu.mssm.pharm.maayanlab.common.swing;

import java.awt.HeadlessException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class VersionCheck {
	
	static Logger log = Logger.getLogger(VersionCheck.class.getSimpleName());
	
	public static void main(String[] args) {
		jvm();
	}
	
	public static void jvm() {
		if (Integer.parseInt(System.getProperty("java.version").substring(2, 3)) < 6) {
			try {
				JOptionPane.showMessageDialog(null, "This application requires at least Java 6.", "Version error", JOptionPane.ERROR_MESSAGE);				
			} catch (HeadlessException e) {
				log.info("Running in headless mode.");
				log.severe("This application requires at least Java 6.");
			}
			finally {
				System.exit(-1);
			}
		}
	}
	
}
