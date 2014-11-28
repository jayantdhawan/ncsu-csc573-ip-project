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
				JSONArray json = new JSONArray.get(line);
				System.out.println("dpid " + json.get("dpid"));
			}
		}

		String urlParameters = "param1=a&param2=b&param3=c";
		URL url = new URL("http://example.com/index.php");
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
