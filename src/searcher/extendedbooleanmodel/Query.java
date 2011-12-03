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
	protected static final String EXCLUDE = "EXCLUDE\\(.+\\)";
	protected static final String PREFIX_MARK = "*";

	public abstract void getRelatedDocuments(Index index, Set<Document> documents);

	public abstract double calculateSimilarity(Index index, Document document);

	public static Query parse(String queryString, Normalizer normalizer, boolean ignoreStopWords)
			throws InvalideQueryException, EmptyQueryException {
		return parse(queryString, normalizer, ignoreStopWords, null);
	}

	public static Query parse(String queryString, Normalizer normalizer, boolean ignoreStopWords, Index index)
			throws InvalideQueryException, EmptyQueryException {
		Query query;

		queryString = queryString.trim();

		if (queryString.matches(AND)) {
			query = new AndQuery(queryString.substring(4, queryString.length() - 1), normalizer, ignoreStopWords, index);
		} else if (queryString.matches(OR)) {
			query = new OrQuery(queryString.substring(3, queryString.length() - 1), normalizer, ignoreStopWords, index);
		} else if (queryString.matches(NOT)) {
			query = new NotQuery(queryString.substring(4, queryString.length() - 1), normalizer, ignoreStopWords);
		} else if (index != null && queryString.endsWith(PREFIX_MARK)) {
			Set<String> terms = index.getTermsIndex(queryString.substring(0, queryString.length() - 1));

			StringBuilder replacementQuery = new StringBuilder("OR(");
			int i = 1;
			for (String term : terms) {
				replacementQuery.append(term);
				if (i < terms.size())
					replacementQuery.append(", ");
				i++;
			}
			replacementQuery.append(")");
			System.out.println(replacementQuery.toString());
			query = new OrQuery(replacementQuery.toString(), normalizer, ignoreStopWords, index);
		} else {
			query = new QueryTerm(queryString, normalizer, ignoreStopWords);
		}

		return query;
	}
}
