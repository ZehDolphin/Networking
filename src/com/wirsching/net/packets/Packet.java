
package com.wirsching.net.packets;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;

public class Packet {

	/**
	 * This is a unique packet id. <br> 
	 */
	protected String id = "";
	
	protected String ip = "";
	
	protected int port = 0;
	
	/**
	 * This HashMap contains all of the data that should be sent by the packet. <br>
	 */
	protected HashMap<String, String> data = new HashMap<>();
	
	public Packet() {
		
	}
	
	public Packet sender(String ip, int port) {
		this.ip = ip;
		this.port = port;
		return this;
	}
	
	public Packet setID(String id) {
		this.id = id;
		return this;
	}
	
	public Packet setIP(String ip) {
		this.ip = ip;
		return this;
	}
	
	public Packet setPort(int port) {
		this.port = port;
		return this;
	}
	
	public String getID() {
		return id;
	}
	
	public String getIP() {
		return ip;
	}
	
	public int getPort() {
		return port;
	}

	public HashMap<String, String> getData() {
		return data;
	}
	
	public String getValue(String key) {
		return getData().get(key);
	}
	
	public void putData(String key, Object data) {
		getData().put(key, String.valueOf(data));
	}

	/**
	 * Take a string and parse it back to a packet.
	 */
	public static Packet parse(String data) {
		try {
			Packet p = new Packet();
			JSONObject obj = new JSONObject(data);
			p.id = obj.getString("id");
			p.ip = obj.getString("ip");
			p.port = Integer.parseInt(obj.getString("port"));
			JSONArray arr = obj.getJSONArray("data");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject o = arr.getJSONObject(i);
				p.putData(o.getString("key"), o.getString("value"));
			}
			return p;
		} catch (Exception e) {
			return new Packet().setID("SERVER_ERROR");
		}
	}
	
	@Override
	public String toString() {
		
		String string = "{\"id\": \"" + getID() + "\", \"ip\": \""+getIP()+"\", \"port\": \""+getPort()+"\"";
		
		
		string +=  ", \"data\": [";
		
		Set<Entry<String, String>> set = getData().entrySet();
		Object[] a = set.toArray();
		
		int index = 0;
		for (Object o : a) {
			@SuppressWarnings("unchecked")
			Entry<String, String> entry = (Entry<String, String>) o;

			string += "{\"key\": \"" + entry.getKey() + "\"";
			string += ", \"value\": \"" + entry.getValue() + "\"}";
			
			index++;
			if (index != a.length) string += ", ";
		}
		
		string += "]}";
		
		return string;
	}
	
}

