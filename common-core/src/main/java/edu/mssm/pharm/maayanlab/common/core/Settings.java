package edu.mssm.pharm.maayanlab.common.core;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class Settings implements Serializable{
	
	private static final long serialVersionUID = -3535786835726140375L;
	private HashMap<String, String> settings = new HashMap<String, String>();
	

	public Settings(){}
	
	public Settings(Settings s){
		loadSettings(s);
	}
	
	public String get(String setting) {
		if (settings.containsKey(setting))
			return settings.get(setting);
		else
			throw new NullPointerException("Settings.get(" + setting +")");
	}
	
	public boolean getBoolean(String setting) {
		return Boolean.parseBoolean(get(setting));
	}
	
	public int getInt(String setting) {
		return Integer.parseInt(get(setting));
	}
	
	public void set(String setting, String value) {
		if(value == null)
			throw new NullPointerException("Settings.set(" + setting + ", null)");
		settings.put(setting, value);
	}
	
	public void set(String setting, boolean value) {
		settings.put(setting, Boolean.toString(value));
	}
	
	public void set(String setting, int value) {
		settings.put(setting, Integer.toString(value));
	}
	
	public void loadSettings() {
		if (new File("settings.ini").exists()) {
			ArrayList<String> settingsFile = FileUtils.readFile("settings.ini");
			
			for (String settingLine : settingsFile) {
				// skip line if blank or starts with #
				if (settingLine.isEmpty() || settingLine.charAt(0) == '#')
					continue;
				String[] settingPair = settingLine.split(":\\s*");
				settings.put(settingPair[0], settingPair[1]);
			}
		}
	}
	
	public void loadSettings(Settings externalSettings) {
		settings.putAll(externalSettings.settings);
	}
	
	public void dump() {
		System.out.println(Collections.singletonList(settings));
	}
}
