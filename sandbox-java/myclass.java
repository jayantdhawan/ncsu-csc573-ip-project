import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import org.json.*;

public class myclass{
	public static void main(String args[]) throws UnsupportedEncodingException, IOException{
		String myString;

		URL url = new URL("http://127.0.0.1:8080/wm/core/controller/switches/json");
		//HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) 
		{
			for (String line; (line = reader.readLine()) != null;) 
			{
				System.out.println(line + "\n");
				JSONObject json = new JSONObject(line);
				System.out.println("dpid " + json.get("dpid"));
			}
		}

		URL url = new URL("http://127.0.0.1:8080/wm/staticflowentrypusher/json");
		String urlParameters = "{\"switch\":\"00:00:00:00:00:00:00:01\",\"name\":\"flow-mod-1\",\"cookie\":\"0\",\"priority\":\"32768\",\"ingress-port\":\"1\",\"active\":\"true\",\"actions\":\"output=3\"}";

		URLConnection conn = url.openConnection();

		conn.setDoOutput(true);

		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		writer.write(urlParameters);
		writer.flush();

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		writer.close();
		reader.close();

		/*
		try (
		{
			
		connection.setDoOutput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod("PUSH");
		connection.setRequestProperty("Content-Type", "text/plain");
		connection.setRequestProperty("charset", "utf-8");
		connection.connect();

		myString = new JSONObject().put("JSON", "Hello, World!").toString();

		System.out.println(myString);
		*/
	}
}
