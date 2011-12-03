package searcher.extendedbooleanmodel;

import java.util.Set;

import index.Document;
import index.Index;
import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

public class NotQuery extends Query {
	private Query subQuery;

	public NotQuery(String query, Normalizer normalizer, boolean ignoreStopWords) throws InvalideQueryException,
			EmptyQueryException {
		subQuery = parse(query, normalizer, ignoreStopWords);
	}

	@Override
	public void getRelatedDocuments(Index index, Set<Document> documents) {
		// On ne fait rien, on considère que l'opérateur NOT ne peut pas ajouter
		// de documents
	}

	@Override
	public double calculateSimilarity(Index index, Document document) {
		return 1 - subQuery.calculateSimilarity(index, document);
	}
}
