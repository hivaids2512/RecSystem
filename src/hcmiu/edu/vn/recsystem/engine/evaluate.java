package hcmiu.edu.vn.recsystem.engine;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.RandomUtils;

public class evaluate {

	public static void main(String[] args) {

		try {
			RandomUtils.useTestSeed();
			DataModel model = new FileDataModel(new File("data/rate.csv"));
			RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();

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

			double score = evaluator.evaluate(builder, null, model, 0.7, 1.0);
			System.out.print(score);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
