package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;

import java.util.ArrayList;
import java.util.Set;

import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

public class ExcludeQuery extends Query {
	private ArrayList<String> terms;

	public ExcludeQuery(String query, Normalizer normalizer, boolean ignoreStopWords) throws InvalideQueryException {
		terms = new ArrayList<String>();

		query = query.trim();
		if (query.matches(EXCLUDE)) {
			query = query.substring(8, query.length() - 1);
	
			for (String term : query.split(",")) {
				terms.addAll(normalizer.normalize(term, ignoreStopWords));
			}
		} else {
			throw new InvalideQueryException(query);
		}
	}

	@Override
	public void getRelatedDocuments(Index index, Set<Document> documents) {
		for (String term : terms) {
			documents.addAll(index.getDocumentsTerm(term));
		}
	}

	@Override
	public double calculateSimilarity(Index index, Document document) {
		return 0;
	}
}
