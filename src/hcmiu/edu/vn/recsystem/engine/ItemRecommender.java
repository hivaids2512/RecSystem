package hcmiu.edu.vn.recsystem.engine;

import hcmiu.edu.vn.recsystem.facade.MovieFacade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

public class ItemRecommender {

	public ArrayList<Integer> getRecommendation(ArrayList<Integer> movieIDs,
			int numberOfRec) throws IOException {
		ArrayList<Integer> recList = new ArrayList<Integer>();
		try {
			DataModel.init();
			/*
			 * MysqlDataSource dataSource = new MysqlDataSource();
			 * dataSource.setServerName("localhost");
			 * dataSource.setUser("root"); dataSource.setPassword("");
			 * dataSource.setDatabaseName("recommender"); MySQLJDBCDataModel
			 * datamodel = new MySQLJDBCDataModel(dataSource, "ratings",
			 * "UserId", "MovieId", "Rating", null); ReloadFromJDBCDataModel dm
			 * = new ReloadFromJDBCDataModel(datamodel);
			 */
			// DataModel dm = new FileDataModel(new File("data/rate.csv"));

			ItemSimilarity sim = new LogLikelihoodSimilarity(DataModel.dm);

			GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(
					DataModel.dm, sim);
			// int x=1;
			// for (LongPrimitiveIterator items = dm.getItemIDs();
			// items.hasNext();){
			// long itemID = items.nextLong();
			// System.out.println("sd");

			for (int id : movieIDs) {
				List<RecommendedItem> recommendations = recommender
						.mostSimilarItems(id, numberOfRec);
				for (RecommendedItem recommendedItem : recommendations) {
					recList.add((int) recommendedItem.getItemID());
					/*
					 * System.out.println("ItemID: " + "58" + " recommended: " +
					 * recommendedItem.getItemID() + " value: " +
					 * recommendedItem.getValue());
					 */
				}
			}

			// x++;
			// if(x==10) System.exit(1);
			// }

		} catch (TasteException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return recList;
	}

	public static void main(String arg[]) {
		ItemRecommender ItemRecommender = new ItemRecommender();
		try {
			ArrayList<Integer> s = new MovieFacade()
					.getRatedMovies("2345678976543");
			ArrayList<Integer> list = ItemRecommender.getRecommendation(s, 1);
			System.out.println("Sixe list " + list.size());
			System.out.println("S " + s.size());
			for (int id : list) {
				System.out.println(id);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
