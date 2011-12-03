package searcher.extendedbooleanmodel;

import java.util.Set;

import index.Document;
import index.Index;
import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

public abstract class Query {
	protected static final String AND = "AND\\(.+\\)";
	protected static final String OR = "OR\\(.+\\)";
	protected static final String NOT = "NOT\\(.+\\)";
	
	public abstract void getRelatedDocuments(Index index, Set<Document> documents);

	public abstract double calculateSimilarity(Index index, Document document);

	public static Query parse(String queryString, Normalizer normalizer, boolean ignoreStopWords)
			throws InvalideQueryException, EmptyQueryException {
		Query query;
		
		queryString = queryString.trim();

		if (queryString.matches(AND)) {
			query = new AndQuery(queryString.substring(4, queryString.length() - 1), normalizer, ignoreStopWords);
		} else if (queryString.matches(OR)) {
			query = new OrQuery(queryString.substring(3, queryString.length() - 1), normalizer, ignoreStopWords);
		} else if (queryString.matches(NOT)) {
			query = new NotQuery(queryString.substring(4, queryString.length() - 1), normalizer, ignoreStopWords);
		} else {
			query = new QueryTerm(queryString, normalizer, ignoreStopWords);
		}

		return query;
	}
}
