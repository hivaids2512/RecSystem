package hcmiu.edu.vn.recsystem.server;

import hcmiu.edu.vn.recsystem.entity.movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class MultiThread extends Thread {

	private String url = null;
	private movie movie = null;

	public MultiThread(String url) {
		super("MultiServerThread");
		this.url = url;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		movie = new movie();
		try {
			// url =
			// "http://www.omdbapi.com/?t="+URLEncoder.encode("Life of pi",
			// "UTF-8");
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			String json = sb.toString();
			JSONObject jsonObj = new JSONObject(json);
			movie = map(jsonObj);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private movie map(JSONObject jsonObj) {
		movie movie = new movie();
		try {
			movie.setTitle(jsonObj.getString("Title"));
			movie.setPoster(jsonObj.getString("Poster"));
			movie.setActor(jsonObj.getString("Actors"));
			movie.setDirector(jsonObj.getString("Director"));
			movie.setGenre(jsonObj.getString("Genre"));
			movie.setIMDBRating(jsonObj.getString("imdbRating"));
			movie.setPlot(jsonObj.getString("Plot"));
			movie.setYear(jsonObj.getString("Year"));
		} catch (JSONException e) {
			return null;
		}
		return movie;
	}

}
