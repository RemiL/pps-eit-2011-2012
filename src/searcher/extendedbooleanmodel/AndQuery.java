package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;
import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

/**
 * Classe représentant une requête AND capable de calculer sa similarité avec un
 * document selon la formule proposée par Gerard Salton, Edward Fox et Harry Wu
 * en 1983.
 */
public class AndQuery extends AndOrQuery {

	/**
	 * Construit une requête AND à partir de la requête textuelle fournie en
	 * utilisant le normalisateur et l'index fournis et en ignorant
	 * éventuellement les mots vides.
	 * 
	 * @param query
	 *            la requête textuelle.
	 * @param normalizer
	 *            le normalisateur à utiliser.
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent être ignorés ou non.
	 * @param index
	 *            l'index sur lequel portera la requête.
	 * @throws InvalideQueryException
	 *             si la requête est incorrectement construite.
	 */
	public AndQuery(String query, Normalizer normalizer, boolean ignoreStopWords, Index index)
			throws InvalideQueryException {
		super(query, normalizer, ignoreStopWords, index);
	}

	@Override
	public double calculateSimilarity(Index index, Document document) {
		double sim = 0;

		for (Query subQuery : subQueries) {
			sim += Math.pow(1 - subQuery.calculateSimilarity(index, document), P);
		}

		sim = 1 - Math.pow(sim / subQueries.size(), 1. / P);

		return sim;
	}
}
