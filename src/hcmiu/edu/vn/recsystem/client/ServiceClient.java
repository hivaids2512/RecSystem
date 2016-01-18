package hcmiu.edu.vn.recsystem.client;

import hcmiu.edu.vn.recsystem.entity.movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceClient {

	private static final String webServiceURI = "http://www.omdbapi.com";
	public ArrayList<movie> movies = new ArrayList<movie>();

	public ArrayList<movie> getmovies(ArrayList<String> movieTitles) {
		// ArrayList<movie> movies = new ArrayList<movie>();
		try {
			for (String title : movieTitles) {
				String realTitle = title.split("//--//")[0];
				String id = title.split("//--//")[1];
				String url = "http://www.omdbapi.com/?t="
						+ URLEncoder.encode(realTitle, "UTF-8");
				// movie movie = client(url);
				// movies.add(movie);
				MultiThread thread = new MultiThread(url, id);
				thread.start();
				thread.join();
			}

			// return movies;

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return movies;

		/*
		 * ClientConfig clientConfig = new ClientConfig(); Client client =
		 * ClientBuilder(); URI serviceURI =
		 * UriBuilder.fromUri(webServiceURI).queryParam
		 * ("t","toy story").build(); WebTarget webTarget =
		 * client.target(serviceURI);
		 * 
		 * // response System.out.println(webTarget.request()
		 * .accept(MediaType.APPLICATION_JSON
		 * ).get(Response.class).readEntity(String.class));
		 * 
		 * /* try {
		 * 
		 * Client client = Client.create(); WebResource webResource2 =
		 * client.resource("http://www.omdbapi.com/?t=Toy%20Story");
		 * ClientResponse response2 =
		 * webResource2.accept("application/json").get(ClientResponse.class); if
		 * (response2.getStatus() != 200) { throw new
		 * RuntimeException("Failed : HTTP error code : " +
		 * response2.getStatus()); }
		 * 
		 * String output2 = response2.getEntity(String.class);
		 * System.out.println(output2);
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */

	}

	/*
	 * public static void main(String arg[]){ ArrayList<String> movieTitles =
	 * new ArrayList<String>(); movieTitles.add("life of pi");
	 * movieTitles.add("the social network");
	 * movieTitles.add("the walking dead"); movieTitles.add("the avenger");
	 * 
	 * ArrayList<movie> movies = getmovies(movieTitles);
	 * 
	 * for(movie movie: movies){ System.out.println(movie.getTitle()); }
	 * 
	 * }
	 */

	public class MultiThread extends Thread {

		private String url = null;
		private String id = null;

		public MultiThread(String url, String id) {
			super("MultiServerThread");
			this.url = url;
			this.id = id;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			movie movie = new movie();
			try {
				// url =
				// "http://www.omdbapi.com/?t="+URLEncoder.encode("Life of pi",
				// "UTF-8");
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				InputStream is = httpEntity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "UTF-8"), 8);

				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();

				String json = sb.toString();
				JSONObject jsonObj = new JSONObject(json);
				if (jsonObj.getString("Title") != null) {
					movie = map(jsonObj);
					movies.add(movie);
				}

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
				movie.setId(id);
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

}
