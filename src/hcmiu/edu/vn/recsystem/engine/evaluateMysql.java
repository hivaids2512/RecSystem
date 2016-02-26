package hcmiu.edu.vn.recsystem.engine;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.RandomUtils;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class evaluateMysql {

	public static void main(String[] args) {

		try {
			RandomUtils.useTestSeed();

			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setServerName("localhost");
			dataSource.setUser("root");
			dataSource.setPassword("");
			dataSource.setDatabaseName("recommender");
			dataSource.setCachePreparedStatements(true);
			dataSource.setCachePrepStmts(true);
			dataSource.setCacheResultSetMetadata(true);
			dataSource.setAlwaysSendSetIsolation(false);
			dataSource.setElideSetAutoCommits(true);
			MySQLJDBCDataModel datamodel = new MySQLJDBCDataModel(dataSource,
					"ratings", "UserId", "MovieId", "Rating", null);
			ReloadFromJDBCDataModel model = new ReloadFromJDBCDataModel(
					datamodel);

			RecommenderBuilder builder = new RecommenderBuilder() {

				@Override
				public Recommender buildRecommender(DataModel model)
						throws TasteException {
					ItemSimilarity sim = new LogLikelihoodSimilarity(model);

					GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(
							model, sim);
					return recommender;
				}

			};

			RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
			double score = evaluator.evaluate(builder, null, model, 0.7, 1.0);
			System.out.print(score);

		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
