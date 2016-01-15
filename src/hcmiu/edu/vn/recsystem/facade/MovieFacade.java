package hcmiu.edu.vn.recsystem.facade;

import hcmiu.edu.vn.recsystem.engine.ItemRecommender;
import hcmiu.edu.vn.recsystem.server.MySqlConnectionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MovieFacade {

	private MySqlConnectionManager manager;

	public MovieFacade() {
		manager = new MySqlConnectionManager();
	}

	public ArrayList<String> getRandommovies(int size) {
		Connection connection = null;
		ArrayList<String> movieTitles = new ArrayList<String>();
		try {
			connection = manager.getDBConnection();

			String sql = "SELECT * FROM movies ORDER BY RAND() LIMIT ?";

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, size);

			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				String titleReal = "";
				String title = result.getString("Title");
				String parts[] = title.split(" ");
				for (int i = 0; i < parts.length - 1; i++) {
					if (parts[i].startsWith("(")) {
						break;
					}
					if (parts[i].equals("The") && i != 0) {
						break;
					}
					titleReal = titleReal + " " + parts[i];
				}
				movieTitles.add(titleReal);
			}
			stmt.close();
			connection.close();
		} catch (Exception ex) {
			Logger.getLogger(MySqlConnectionManager.class.getName()).log(
					Level.SEVERE, null, ex);
			return null;
		}
		return movieTitles;
	}

	public ArrayList<Integer> getRatedMovies(String userToken) {
		Connection connection = null;
		ArrayList<Integer> movieIDs = new ArrayList<Integer>();
		try {
			connection = manager.getDBConnection();

			String sql = "select * from movies, ratings, users where users.UserID = ratings.UserID and movies.MovieID = ratings.MovieID and users.Token = ? ";

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, userToken);

			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				int id = result.getInt("MovieID");
				movieIDs.add(id);
			}
			stmt.close();
			connection.close();
		} catch (Exception ex) {
			Logger.getLogger(MySqlConnectionManager.class.getName()).log(
					Level.SEVERE, null, ex);
			return null;
		}
		return movieIDs;
	}

	public ArrayList<String> getRecommeded(String Token, int numOfRec)
			throws IOException {
		Connection connection = null;
		int count = 0;
		ArrayList<String> movieTitles = new ArrayList<String>();
		ArrayList<Integer> ids = this.getRatedMovies(Token);
		ArrayList<Integer> movieIDs = new ItemRecommender().getRecommendation(
				ids, 1);
		connection = manager.getDBConnection();
		for (int id : movieIDs) {
			try {
				count++;
				if (count == numOfRec) {
					break;
				}
				String sql = "SELECT * FROM movies where MovieID = ?";

				PreparedStatement stmt = connection.prepareStatement(sql);
				stmt.setInt(1, id);
				ResultSet result = stmt.executeQuery();
				while (result.next()) {
					String titleReal = "";
					String title = result.getString("Title");
					String parts[] = title.split(" ");
					for (int i = 0; i < parts.length - 1; i++) {
						if (parts[i].startsWith("(")) {
							break;
						}
						if (parts[i].equals("The") && i != 0) {
							break;
						}
						titleReal = titleReal + " " + parts[i];
					}
					movieTitles.add(titleReal);
				}
				stmt.close();

			} catch (Exception ex) {
				Logger.getLogger(MySqlConnectionManager.class.getName()).log(
						Level.SEVERE, null, ex);
				return null;
			}
		}
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieTitles;
	}

	public boolean rating(int userId, int rating, int movieId) {
		Connection connection = null;
		ArrayList<String> movieTitles = new ArrayList<String>();
		try {
			connection = manager.getDBConnection();

			String sql = "insert into ratings values(? , ? , ?);";

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, userId);
			stmt.setInt(2, movieId);
			stmt.setInt(3, rating);

			stmt.executeUpdate();

			stmt.close();
			connection.close();
		} catch (Exception ex) {
			Logger.getLogger(MySqlConnectionManager.class.getName()).log(
					Level.SEVERE, null, ex);
			return false;
		}
		return true;
	}

	/*
	 * public static void main(String arg[]) throws IOException {
	 * System.out.println("start"); ArrayList<String> movieIDs = new
	 * MovieFacade() .getRecommeded("2345678976543"); System.out.println("asd");
	 * for (String id : movieIDs) { System.out.println(id); } }
	 */

}
