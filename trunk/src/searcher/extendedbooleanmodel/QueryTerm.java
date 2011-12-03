package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;

import java.util.ArrayList;
import java.util.Set;

import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

public class QueryTerm extends Query {

	private String term;

	public QueryTerm(String query, Normalizer normalizer, boolean ignoreStopWords) throws InvalideQueryException,
			EmptyQueryException {
		ArrayList<String> terms = normalizer.normalize(query, ignoreStopWords);

		if (terms.size() == 1) {
			term = terms.get(0);
		} else if (terms.isEmpty()) {
			throw new EmptyQueryException();
		} else {
			throw new InvalideQueryException(query);
		}
	}

	@Override
	public void getRelatedDocuments(Index index, Set<Document> documents) {
		documents.addAll(index.getDocumentsTerm(term));
	}

	@Override
	public double calculateSimilarity(Index index, Document document) {
		return index.getWeight(term, document);
	}
}
