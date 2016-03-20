package hcmiu.edu.vn.recsystem.engine;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class model {

	private static model instance = null;
	private static Object mutex = new Object();
	private MysqlDataSource dataSource;
	private MySQLJDBCDataModel datamodel;
	private ReloadFromJDBCDataModel dm;

	private model() {
		dataSource = new MysqlDataSource();
		dataSource.setServerName("localhost");
		dataSource.setUser("root");
		dataSource.setPassword("@Quy@%!@!((#");
		dataSource.setDatabaseName("recommender");
		dataSource.setCachePreparedStatements(true);
		dataSource.setCachePrepStmts(true);
		dataSource.setCacheResultSetMetadata(true);
		dataSource.setAlwaysSendSetIsolation(false);
		dataSource.setElideSetAutoCommits(true);
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

	public static model getInstance() {
		if (instance == null) {
			synchronized (mutex) {
				if (instance == null)
					instance = new model();
			}
		}
		return instance;
	}
}