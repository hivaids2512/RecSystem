package hcmiu.edu.vn.recsystem.facade;

import hcmiu.edu.vn.recsystem.security.securityManager;
import hcmiu.edu.vn.recsystem.server.MySqlConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserFacade {

	private MySqlConnectionManager manager;

	public UserFacade() {
		manager = new MySqlConnectionManager();
	}

	public String authenticate(String email, String password) {
		Connection connection = null;
		try {
			connection = manager.getDBConnection();

			String sql = "SELECT * FROM users where Email = ? and Password = ?";

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, password);

			ResultSet result = stmt.executeQuery();
			if (result.first()) {
				return result.getString("Token");
			}
			stmt.close();
			connection.close();
		} catch (Exception ex) {
			Logger.getLogger(MySqlConnectionManager.class.getName()).log(
					Level.SEVERE, null, ex);
			return null;
		}
		return null;
	}

	public String register(hcmiu.edu.vn.recsystem.entity.user user) {
		Connection connection = null;
		String token = new securityManager().generateToken();
		try {
			connection = manager.getDBConnection();

			String sql = "insert into users values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, user.getId());
			stmt.setString(2, user.getGender());
			stmt.setInt(3, user.getAge());
			stmt.setString(4, user.getOccupation());
			stmt.setString(5, user.getOccupation());
			stmt.setString(6, user.getZipCode());
			stmt.setString(7, user.getEmail());
			stmt.setString(8, user.getFirstname());
			stmt.setString(9, user.getLastname());
			stmt.setString(10, token);

			stmt.executeUpdate();

			stmt.close();
			connection.close();

		} catch (Exception ex) {
			Logger.getLogger(MySqlConnectionManager.class.getName()).log(
					Level.SEVERE, null, ex);
			return null;
		}
		return token;
	}

}
