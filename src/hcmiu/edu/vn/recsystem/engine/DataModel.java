package hcmiu.edu.vn.recsystem.engine;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataModel {

	private static DataModel instance = null;
	private static Object mutex = new Object();
	private MysqlDataSource dataSource;
	private MySQLJDBCDataModel datamodel;
	private ReloadFromJDBCDataModel dm;

	private DataModel() {
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

	protected ReloadFromJDBCDataModel getDataModel() {
		return dm;
	}

	public static DataModel getInstance() {
		if (instance == null) {
			synchronized (mutex) {
				if (instance == null)
					instance = new DataModel();
			}
		}
		return instance;
	}
}