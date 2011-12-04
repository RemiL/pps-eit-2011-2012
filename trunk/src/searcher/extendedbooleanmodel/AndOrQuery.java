package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;

import java.util.ArrayList;
import java.util.Set;

import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

/**
 * Classe abstraite repr�sentant une requ�te AND ou OR compos�e d'une ou
 * plusieurs sous-requ�tes.
 */
public abstract class AndOrQuery extends Query {
	/** La valeur de P pour le calcul de la P-norme. */
	public static final int P = 2;
	
	/** La liste des sous-requ�tes composant la requ�te. */
	protected ArrayList<Query> subQueries;

	/**
	 * Construit une requ�te AND ou OR � partir de la requ�te textuelle fournie
	 * en utilisant le normalisateur et l'index fournis et en ignorant
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
	public AndOrQuery(String query, Normalizer normalizer, boolean ignoreStopWords, Index index)
			throws InvalideQueryException {
		subQueries = new ArrayList<Query>();

		// On analyse les sous-requ�tes s�par�es par des ',' en ne
		// prenant pas en compte les sous-requ�tes d'une sous-requ�te.
		Query subQuery;
		int start = 0;
		// Niveau d'imbrication de la sous-requ�te courant.
		int groupEndNeeded = 0;

		for (int i = 0; i < query.length(); i++) {
			switch (query.charAt(i)) {
			case ',':
				// On ne consid�re que les sous-requ�tes
				// directes de notre requ�te de base.
				if (groupEndNeeded == 0) {
					try {
						subQuery = parse(query.substring(start, i), normalizer, ignoreStopWords, index);

						subQueries.add(subQuery);
					} catch (EmptyQueryException e) {
						// On ne fait rien pour l'instant, une sous-requ�te peut
						// disparaitre si la suppression des mots vides est
						// activ�e.
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
		// On traite la derni�re sous-requ�te.
		try {
			subQuery = parse(query.substring(start, query.length()), normalizer, ignoreStopWords, index);

			subQueries.add(subQuery);
		} catch (EmptyQueryException e) {
			// On ne fait rien pour l'instant, une sous-requ�te peut
			// disparaitre si la suppression des mots vides est
			// activ�e.
		}

		if (subQueries.isEmpty()) {
			throw new InvalideQueryException(query);
		}
	}

	@Override
	public void getRelatedDocuments(Index index, Set<Document> documents) {
		// Les documents concern�s par la requ�te sont
		// tous ceux concern�s par les sous-requ�tes.
		for (Query subQuery : subQueries) {
			subQuery.getRelatedDocuments(index, documents);
		}
	}
}
