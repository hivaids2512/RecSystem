package hcmiu.edu.vn.recsystem.engine;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataModel {

	private static MysqlDataSource dataSource;
	private static MySQLJDBCDataModel datamodel;
	public static ReloadFromJDBCDataModel dm;

	public static void init() {
		dataSource = new MysqlDataSource();
		dataSource.setServerName("localhost");
		dataSource.setUser("root");
		dataSource.setPassword("");
		dataSource.setDatabaseName("recommender");
		datamodel = new MySQLJDBCDataModel(dataSource, "ratings", "UserId",
				"MovieId", "Rating", null);
		try {
			dm = new ReloadFromJDBCDataModel(datamodel);
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
