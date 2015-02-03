package edu.mssm.pharm.maayanlab.common.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Settings {
	
	private HashMap<String, String> settings = new HashMap<String, String>();
	
	public String get(String setting) {
		if (settings.containsKey(setting))
			return settings.get(setting);
		else {
			System.err.println("Invalid setting: " + setting);
			return null;
		}
	}
	
	public boolean getBoolean(String setting) {
		return Boolean.parseBoolean(get(setting));
	}
	
	public int getInt(String setting) {
		return Integer.parseInt(get(setting));
	}
	
	public void set(String setting, String value) {
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
}
