package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;
import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

public class OrQuery extends AndOrQuery {

	public OrQuery(String query, Normalizer normalizer, boolean ignoreStopWords) throws InvalideQueryException {
		super(query, normalizer, ignoreStopWords);
	}

	@Override
	public double calculateSimilarity(Index index, Document document) {
		double sim = 0;

		for (Query subQuery : subQueries) {
			sim += Math.pow(subQuery.calculateSimilarity(index, document), 2);
		}

		sim = Math.sqrt(sim / subQueries.size());

		return sim;
	}
}
