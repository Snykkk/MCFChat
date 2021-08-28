package snykkk.mcfchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MCFUpdate {
	
	public static boolean checkUpdate() {
		try {
			String plugin = MCFMain.m.getDescription().getName();
			String query_result = web("https://mcfamily.vn/version/plugin.json", 500);
			
			
			JSONParser parser = new JSONParser();
	        JSONObject main = (JSONObject) parser.parse(query_result);
	        JSONObject sub = (JSONObject) main.get(plugin);
	        if (((String) sub.get("version")).contains(MCFMain.m.getDescription().getVersion())) {
	        	return true;
	        }
		} catch (IOException ioe) {}
		catch (ParseException parcsex) {}
		
		return false;
	}
	
	public static String version() {
		try {
			String plugin = MCFMain.m.getDescription().getName();
			String query_result = web("https://mcfamily.vn/version/plugin.json", 500);
			
			
			JSONParser parser = new JSONParser();
	        JSONObject main = (JSONObject) parser.parse(query_result);
	        JSONObject sub = (JSONObject) main.get(plugin);
	        if (!((String) sub.get("version")).contains(MCFMain.m.getDescription().getVersion())) {
	        	return ((String) sub.get("version"));
	        }
		} catch (IOException ioe) {}
		catch (ParseException parcsex) {}
		
		return MCFMain.m.getDescription().getVersion();
	}
	
	public static String web(String url, int timeout)
            throws MalformedURLException, IOException {
		StringBuilder response = new StringBuilder();
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        connection.setConnectTimeout(timeout);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("User-Agent", "Defiance-AntiBot.v1.2.0");
        connection.setRequestProperty("tag", "Defiance-AntiBot.v1.2.0");
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()))) {
            while ((url = in.readLine()) != null) {
                response.append(url);
            }
        }
        return response.toString();
	}

}
