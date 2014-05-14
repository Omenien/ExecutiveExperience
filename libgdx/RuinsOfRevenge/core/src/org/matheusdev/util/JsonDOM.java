package org.matheusdev.util;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;

import java.util.ArrayList;

/**
 * @author matheusdev
 */
public class JsonDOM implements Serializable
{

	public static abstract class JsonElement
	{
		public abstract void toString(StringBuffer buffer);
	}

	public static class JsonArray extends JsonElement
	{
		public ArrayList<JsonObject> elements = new ArrayList<>();

		@Override
		public void toString(StringBuffer buffer)
		{
			buffer.append("[\n");
			for(JsonElement e : elements)
			{
				e.toString(buffer);
			}
			buffer.append("]\n");
		}
	}

	public static class JsonObject extends JsonElement
	{
		public OrderedMap<String, JsonElement> elements = new OrderedMap<>();
		public OrderedMap<String, String> values = new OrderedMap<>();

		@Override
		public void toString(StringBuffer buffer)
		{
			buffer.append("{\n");
			for(Entry<String, String> entry : values.entries())
			{
				buffer.append(entry.key).append(": ").append(entry.value).append('\n');
			}
			for(Entry<String, JsonElement> entry : elements.entries())
			{
				buffer.append(entry.key).append(": ");
				entry.value.toString(buffer);
			}
			buffer.append("}\n");
		}

		public boolean valuesContainAllKeys(String... keys)
		{
			for(String key : keys)
			{
				if(!values.containsKey(key))
				{
					return false;
				}
			}
			return true;
		}

		public String getValue(String value, String defaultValue)
		{
			String str = values.get(value);
			return str == null ? defaultValue : str;
		}

		public float getFloatValue(String value, float defaultValue)
		{
			String str = values.get(value);
			return str == null ? defaultValue : (float) Double.parseDouble(str);
		}

		public double getDoubleValue(String value, double defaultValue)
		{
			String str = values.get(value);
			return str == null ? defaultValue : Double.parseDouble(str);
		}

		public int getIntValue(String value, int defaultValue)
		{
			String str = values.get(value);
			return str == null ? defaultValue : (int) Double.parseDouble(str);
		}

		public long getLongValue(String value, long defaultValue)
		{
			String str = values.get(value);
			return str == null ? defaultValue : (long) Double.parseDouble(str);
		}

		public boolean getBoolValue(String value, boolean defaultValue)
		{
			String str = values.get(value);
			return str == null ? defaultValue : str.equalsIgnoreCase("true");
		}
	}

	private final JsonObject root;

	public JsonDOM()
	{
		root = new JsonObject();
	}

	public JsonObject getRoot()
	{
		return root;
	}

	@Override
	public void write(Json json)
	{
		writeJsonObject(root, json);
	}

	@Override
	public void read(Json json, JsonValue jsonData)
	{
		readJsonObject(root, jsonData);
	}

	public void read(Json json, OrderedMap<String, Object> jsonData)
	{
		readJsonObject(root, jsonData);
	}

	@SuppressWarnings("unchecked")
	public void readJsonObject(JsonObject element, OrderedMap<String, Object> jsonData)
	{
		Entries<String, Object> entries = jsonData.entries();
		for(Entry<String, Object> entry : entries)
		{
			if(entry.value instanceof OrderedMap)
			{
				JsonObject obj = new JsonObject();
				element.elements.put(entry.key, obj);

				// unchecked, but safe:
				readJsonObject(obj, (OrderedMap<String, Object>) entry.value);
			}
			else if(entry.value instanceof Array)
			{
				JsonArray arr = new JsonArray();
				element.elements.put(entry.key, arr);

				// unchecked, but safe:
				readJsonArray(arr, (Array<OrderedMap<String, Object>>) entry.value);
			}
			else
			{
				element.values.put(entry.key, entry.value.toString());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void readJsonObject(JsonObject element, JsonValue jsonData)
	{
		for (JsonValue entry = jsonData.child(); entry != null; entry = entry.next())
		{
			if(!entry.isArray())
			{
				System.out.println(entry.name() + " = " + entry.asString());

				element.values.put(entry.name(), entry.asString());
			}
			else
			{
				readJsonObject(element, entry.child());
			}
		}

//		while(iterator.hasNext())
//		{
//			JsonValue entry = iterator.next();
//
//			System.out.println("Name: " + entry.name() + " Class: " + entry.getClass().getName() + " " + entry.toString());
//
//			element.values.put(entry.name(), entry.toString());
//
//			/*if(entry.isArray())
//			{
//				JsonArray arr = new JsonArray();
//				element.elements.put(entry.name(), arr);
//				readJsonArray(arr, (Array<JsonValue>) entry.);
//			}      */
//		}
	}

	public void writeJsonObject(JsonObject element, Json json)
	{
		for(Entry<String, String> entry : element.values.entries())
		{
			json.writeValue(entry.key, entry.value);
		}
		for(Entry<String, JsonElement> entry : element.elements.entries())
		{
			if(entry.value instanceof JsonObject)
			{
				json.writeObjectStart(entry.key);
				writeJsonObject((JsonObject) entry.value, json);
				json.writeObjectEnd();
			}
			else if(entry.value instanceof JsonArray)
			{
				json.writeArrayStart(entry.key);
				writeJsonArray((JsonArray) entry.value, json);
				json.writeArrayEnd();
			}
		}
	}

	public void writeJsonArray(JsonArray array, Json json)
	{
		for(JsonObject obj : array.elements)
		{
			json.writeObjectStart();
			writeJsonObject(obj, json);
			json.writeObjectEnd();
		}
	}

	public void readJsonArray(JsonArray array, Array<OrderedMap<String, Object>> jsonArray)
	{
		for(OrderedMap<String, Object> jsonObject : jsonArray)
		{
			JsonObject obj = new JsonObject();
			array.elements.add(obj);
			readJsonObject(obj, jsonObject);
		}
	}

	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer("{\n");
		root.toString(buffer);
		buffer.append("\n}");
		return buffer.toString();
	}

}
