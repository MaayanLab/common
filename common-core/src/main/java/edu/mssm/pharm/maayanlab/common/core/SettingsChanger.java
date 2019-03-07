package edu.mssm.pharm.maayanlab.common.core;

public interface SettingsChanger {

	public final static String TRUE = "true";
	public final static String FALSE = "false";
	
	public void setSetting(String key, String value);
	
}
