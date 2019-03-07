package edu.mssm.pharm.maayanlab.common.web;

import java.io.PrintWriter;

import com.google.gson.Gson;

public class JSONify {
	
	private static Gson defaultConverter = new Gson();
	private Gson converter;
	private StringBuilder json = new StringBuilder();
	
	public JSONify() {
		this.converter = defaultConverter;
		json.append("{\n");
	}
	
	public JSONify(Gson converter) {
		this.converter = converter;
		json.append("{\n");
	}
	
	public void add(String field, Object value) {
		json.append("\"").append(field).append("\": ").append(converter.toJson(value)).append(",\n");
	}
	
	public void addRaw(String field, String value) {
		json.append("\"").append(field).append("\": ").append(value).append(",\n");
	}
	
	private void end() {
		int lastCommaIndex = json.lastIndexOf(",");
		if (lastCommaIndex != -1)
			json.deleteCharAt(lastCommaIndex);
		json.append("}\n");
	}
	
	public void write(PrintWriter out) {
		end();
		out.print(json);
		out.close();
	}
	
	@Override
	public String toString() {
		end();
		return json.toString();
	}
}
