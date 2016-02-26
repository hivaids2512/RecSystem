package hcmiu.edu.vn.recsystem.facade;

import hcmiu.edu.vn.recsystem.engine.ItemRecommender;
import hcmiu.edu.vn.recsystem.entity.movie;
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

	// This method is used to get random movies
	public ArrayList<movie> getRandommovies(int size) {
		Connection connection = null;
		ArrayList<movie> movies = new ArrayList<movie>();
		try {
			connection = manager.getDBConnection();

			String sql = "SELECT * FROM movies ORDER BY RAND() LIMIT ?";

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, size);

			ResultSet result = stmt.executeQuery();

			while (result.next()) {

				String sql2 = "SELECT * FROM moviedata where MovieID = ?";
				PreparedStatement stmt2 = connection.prepareStatement(sql2);
				stmt2.setInt(1, result.getInt("MovieID"));
				ResultSet result2 = stmt2.executeQuery();
				if (result2.next()) {
					movie movie = new movie();
					movie.setId(result2.getString("MovieID"));
					movie.setTitle(result2.getString("Title"));
					movie.setYear(result2.getString("Year"));
					movie.setRated(result2.getString("Rated"));
					movie.setReleased(result2.getString("Released"));
					movie.setRuntime(result2.getString("Runtime"));
					movie.setGenre(result2.getString("Genre"));
					movie.setDirector(result2.getString("Director"));
					movie.setWriter(result2.getString("Writer"));
					movie.setActors(result2.getString("Actors"));
					movie.setPlot(result2.getString("Plot"));
					movie.setLanguage(result2.getString("Language"));
					movie.setCountry(result2.getString("Country"));
					movie.setAwards(result2.getString("Awards"));
					movie.setPoster(result2.getString("Poster"));
					movie.setMetascore(result2.getString("Metascore"));
					movie.setImdbRating(result2.getString("imdbRating"));
					movie.setImdbVotes(result2.getString("imdbVotes"));
					movie.setImdbID(result2.getString("imdbID"));
					movie.setType(result2.getString("Type"));
					movies.add(movie);
				}
			}

			/*
			 * while (result.next()) { String titleReal = ""; String title =
			 * result.getString("Title"); int movieId =
			 * result.getInt("MovieID"); String parts[] = title.split(" "); for
			 * (int i = 0; i < parts.length - 1; i++) { if
			 * (parts[i].startsWith("(")) { break; } if (parts[i].equals("The")
			 * && i != 0) { break; } titleReal = titleReal + " " + parts[i]; }
			 * movieTitles.add(titleReal + "//--//" + movieId); }
			 */
			stmt.close();
			connection.close();
		} catch (Exception ex) {
			Logger.getLogger(MySqlConnectionManager.class.getName()).log(
					Level.SEVERE, null, ex);
			return null;
		}
		return movies;
	}

	// This method is used to get list of movie id that user has been rated
	public ArrayList<Integer> getRatedMovies(String userToken) {
		Connection connection = null;
		ArrayList<Integer> movieIDs = new ArrayList<Integer>();
		try {
			connection = manager.getDBConnection();

			String sql = "select * from ratings, users where users.UserID = ratings.UserID and users.Token = ? ";

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

	// For each movie id that user has been rated, this method will calculate
	// the most similarity for that movies
	public ArrayList<movie> getRecommeded(String Token, int numOfRec)
			throws IOException {
		Connection connection = null;
		int count = 0;
		ArrayList<movie> movies = new ArrayList<movie>();
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
				String sql = "SELECT * FROM moviedata where MovieID = ?";
				PreparedStatement stmt = connection.prepareStatement(sql);
				stmt.setInt(1, id);
				ResultSet result = stmt.executeQuery();
				if (result.next()) {
					movie movie = new movie();
					movie.setId(result.getString("MovieID"));
					movie.setTitle(result.getString("Title"));
					movie.setYear(result.getString("Year"));
					movie.setRated(result.getString("Rated"));
					movie.setReleased(result.getString("Released"));
					movie.setRuntime(result.getString("Runtime"));
					movie.setGenre(result.getString("Genre"));
					movie.setDirector(result.getString("Director"));
					movie.setWriter(result.getString("Writer"));
					movie.setActors(result.getString("Actors"));
					movie.setPlot(result.getString("Plot"));
					movie.setLanguage(result.getString("Language"));
					movie.setCountry(result.getString("Country"));
					movie.setAwards(result.getString("Awards"));
					movie.setPoster(result.getString("Poster"));
					movie.setMetascore(result.getString("Metascore"));
					movie.setImdbRating(result.getString("imdbRating"));
					movie.setImdbVotes(result.getString("imdbVotes"));
					movie.setImdbID(result.getString("imdbID"));
					movie.setType(result.getString("Type"));
					movies.add(movie);
				}
				result.close();
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

		return movies;
	}

	// This method is used for add rating a movie
	public boolean rating(int userId, int rating, int movieId) {
		Connection connection = null;
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
			System.out.println(ex);
			Logger.getLogger(MySqlConnectionManager.class.getName()).log(
					Level.SEVERE, null, ex);
			return false;
		}
		return true;
	}
}
