package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;
import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

public class AndQuery extends AndOrQuery {

	public AndQuery(String query, Normalizer normalizer, boolean ignoreStopWords, Index index)
			throws InvalideQueryException {
		super(query, normalizer, ignoreStopWords, index);
	}

	@Override
	public double calculateSimilarity(Index index, Document document) {
		double sim = 0;

		for (Query subQuery : subQueries) {
			sim += Math.pow(1 - subQuery.calculateSimilarity(index, document), 2);
		}

		sim = 1 - Math.sqrt(sim / subQueries.size());

		return sim;
	}
}
