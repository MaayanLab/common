package edu.mssm.pharm.maayanlab.common.web;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JSONifyTest extends TestCase {

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(JSONifyTest.class);
	}

	public void testAddArray() {
		int[] testArray = new int[5];
		// Fill test array from 0 to 4
		for (int i = 0; i < testArray.length; i++) {
			testArray[i] = i;
		}

		JSONify json = new JSONify();
		json.add("array", testArray);

		assertEquals("{\n\"array\": [0,1,2,3,4]\n}\n", json.toString());
	}

	public void testAddDictionary() {
		HashMap<String, Object> testDict = new HashMap<String, Object>();
		// Fill test dictionary
		testDict.put("foo", "bar");
		testDict.put("value", 0);

		JSONify json = new JSONify();
		json.add("dict", testDict);

		assertEquals("{\n\"dict\": {\"value\":0,\"foo\":\"bar\"}\n}\n",
				json.toString());
	}

	public void testAddRaw() {

		HashMap<String, Object> testDict = new HashMap<String, Object>();
		// Fill test dictionary
		testDict.put("foo", "bar");
		testDict.put("value", 0);

		String preconverted = new Gson().toJson(testDict).toString();
		JSONify json = new JSONify();
		json.addRaw("dict", preconverted);
		String preconvertedJSON = json.toString();

		json = new JSONify();
		json.add("dict", testDict);

		assertEquals(preconvertedJSON, json.toString());
	}
	
	public void testAdapter() {
		TestObject testObject = new TestObject(1, 2);
		
		JSONify json = new JSONify(new GsonBuilder().registerTypeAdapter(TestObject.class, new TestObjectAdapter()).create());
		json.add("addition", testObject);
		
		assertEquals("{\n\"addition\": {\"arg1\":1,\"arg2\":2,\"sum\":3}\n}\n", json.toString());
	}
	
	private class TestObject {
		
		public int addend1;
		public int addend2;
		
		public TestObject(int addend1, int addend2) {
			this.addend1 = addend1;
			this.addend2 = addend2;
		}
	}
	
	private class TestObjectAdapter implements JsonSerializer<TestObject> {

		@Override
		public JsonElement serialize(TestObject testObject, Type type, JsonSerializationContext jsc) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("arg1", testObject.addend1);
			jsonObject.addProperty("arg2", testObject.addend2);
			jsonObject.addProperty("sum", testObject.addend1 + testObject.addend2);
			return jsonObject;
		}
	}
}
