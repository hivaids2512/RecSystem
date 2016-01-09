package hcmiu.edu.vn.recsystem.server;

import hcmiu.edu.vn.recsystem.client.ServiceClient;
import hcmiu.edu.vn.recsystem.facade.MovieFacade;
import hcmiu.edu.vn.recsystem.facade.UserFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

@Path("/")
public class Server {

	@POST
	@Path("/authenticate")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crunchifyREST(InputStream incomingData)
			throws JSONException {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				builder.append(line);
			}
			String data = builder.toString();
			JSONObject jsonObj = new JSONObject(data);
			String email = jsonObj.getString("email");
			String password = jsonObj.getString("password");
			String token = new UserFacade().authenticate(email, password);
			JSONObject responseObj = new JSONObject();
			if (token != null) {
				responseObj.put("authenticated", "true");
				responseObj.put("secureToken", token);
				return Response.status(200).entity(responseObj.toString())
						.build();
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
			return Response
					.status(200)
					.entity(new JSONObject().put("authenticated", "false")
							.toString()).build();
		}

		// System.out.println("Data Received: " + crunchifyBuilder.toString());

		// return HTTP response 200 in case of success
		return Response
				.status(200)
				.entity(new JSONObject().put("authenticated", "false")
						.toString()).build();
	}

	@GET
	@Path("/getmovies")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyRESTService(InputStream incomingData) {
		ArrayList<String> movieTitles = new MovieFacade().getRandommovies(25);
		// movieTitles.add("life of pi");
		// movieTitles.add("the social network");
		// movieTitles.add("the walking dead");
		// movieTitles.add("the avenger");
		// movieTitles.add("toy story");
		String result = new Gson().toJson(new ServiceClient()
				.getmovies(movieTitles));

		// return HTTP response 200 in case of success
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/getrec")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getrec(InputStream incomingData) throws IOException {
		ArrayList<String> movieTitles = new MovieFacade().getRecommeded(
				"2345678976543", 10);
		// movieTitles.add("life of pi");
		// movieTitles.add("the social network");
		// movieTitles.add("the walking dead");
		// movieTitles.add("the avenger");
		// movieTitles.add("toy story");
		String result = new Gson().toJson(new ServiceClient()
				.getmovies(movieTitles));

		// return HTTP response 200 in case of success
		return Response.ok(result, MediaType.APPLICATION_JSON).build();
	}

}