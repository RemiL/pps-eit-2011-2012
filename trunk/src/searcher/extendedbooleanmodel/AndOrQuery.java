package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;

import java.util.ArrayList;
import java.util.Set;

import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

public abstract class AndOrQuery extends Query {
	protected ArrayList<Query> subQueries;

	public AndOrQuery(String query, Normalizer normalizer, boolean ignoreStopWords, Index index)
			throws InvalideQueryException {
		subQueries = new ArrayList<Query>();

		Query subQuery;
		int start = 0;
		int groupEndNeeded = 0;

		for (int i = 0; i < query.length(); i++) {
			switch (query.charAt(i)) {
			case ',':
				if (groupEndNeeded == 0) {
					try {
						subQuery = parse(query.substring(start, i), normalizer, ignoreStopWords, index);

						subQueries.add(subQuery);
					} catch (EmptyQueryException e) {
						// On ne fait rien pour l'instant, une sous-requête peut
						// disparaitre si la suppression des mots vides est
						// activée.
					}

					start = i + 1;
				}
				break;
			case '(':
				groupEndNeeded++;
				break;
			case ')':
				groupEndNeeded--;
				break;
			}
		}

		try {
			subQuery = parse(query.substring(start, query.length()), normalizer, ignoreStopWords, index);

			subQueries.add(subQuery);
		} catch (EmptyQueryException e) {
			// On ne fait rien pour l'instant, une sous-requête peut
			// disparaitre si la suppression des mots vides est
			// activée.
		}

		if (subQueries.isEmpty()) {
			throw new InvalideQueryException(query);
		}
	}

	@Override
	public void getRelatedDocuments(Index index, Set<Document> documents) {
		for (Query subQuery : subQueries) {
			subQuery.getRelatedDocuments(index, documents);
		}
	}
}
