package hcmiu.edu.vn.recsystem.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class ItemRecommender {

	public ArrayList<Integer> getRecommendation(ArrayList<Integer> movieIDs,
			int numberOfRec) throws IOException {
		ArrayList<Integer> recList = new ArrayList<Integer>();
		try {
			/*
			 * MysqlDataSource dataSource = new MysqlDataSource();
			 * dataSource.setServerName("localhost");
			 * dataSource.setUser("root"); dataSource.setPassword("");
			 * dataSource.setDatabaseName("recommender"); MySQLJDBCDataModel
			 * datamodel = new MySQLJDBCDataModel(dataSource, "ratings",
			 * "UserId", "MovieId", "Rating", null); ReloadFromJDBCDataModel dm
			 * = new ReloadFromJDBCDataModel(datamodel);
			 */
			model model = null;
			model = model.getInstance();
			ReloadFromJDBCDataModel dm = model.getDataModel();
			dm.refresh(null);

			DataModel dmm = dm.getDelegateInMemory();
			// DataModel dm = new FileDataModel(new File("data/rate.csv"));

			ItemSimilarity sim = new LogLikelihoodSimilarity(dmm);

			GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(
					dmm, sim);

			for (int id : movieIDs) {
				List<RecommendedItem> recommendations = recommender
						.mostSimilarItems(id, numberOfRec);
				for (RecommendedItem recommendedItem : recommendations) {
					recList.add((int) recommendedItem.getItemID());
				}
			}
		} catch (TasteException e) {
			return null;
		}
		return recList;
	}

}
