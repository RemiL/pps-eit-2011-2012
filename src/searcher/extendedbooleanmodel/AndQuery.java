package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;
import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

/**
 * Classe repr�sentant une requ�te AND capable de calculer sa similarit� avec un
 * document selon la formule propos�e par Gerard Salton, Edward Fox et Harry Wu
 * en 1983.
 */
public class AndQuery extends AndOrQuery {

	/**
	 * Construit une requ�te AND � partir de la requ�te textuelle fournie en
	 * utilisant le normalisateur et l'index fournis et en ignorant
	 * �ventuellement les mots vides.
	 * 
	 * @param query
	 *            la requ�te textuelle.
	 * @param normalizer
	 *            le normalisateur � utiliser.
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent �tre ignor�s ou non.
	 * @param index
	 *            l'index sur lequel portera la requ�te.
	 * @throws InvalideQueryException
	 *             si la requ�te est incorrectement construite.
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
