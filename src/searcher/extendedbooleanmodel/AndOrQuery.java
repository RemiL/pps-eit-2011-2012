package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;

import java.util.ArrayList;
import java.util.Set;

import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

/**
 * Classe abstraite représentant une requête AND ou OR composée d'une ou
 * plusieurs sous-requêtes.
 */
public abstract class AndOrQuery extends Query {
	/** La valeur de P pour le calcul de la P-norme. */
	public static final int P = 2;
	
	/** La liste des sous-requêtes composant la requête. */
	protected ArrayList<Query> subQueries;

	/**
	 * Construit une requête AND ou OR à partir de la requête textuelle fournie
	 * en utilisant le normalisateur et l'index fournis et en ignorant
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
	public AndOrQuery(String query, Normalizer normalizer, boolean ignoreStopWords, Index index)
			throws InvalideQueryException {
		subQueries = new ArrayList<Query>();

		// On analyse les sous-requêtes séparées par des ',' en ne
		// prenant pas en compte les sous-requêtes d'une sous-requête.
		Query subQuery;
		int start = 0;
		// Niveau d'imbrication de la sous-requête courant.
		int groupEndNeeded = 0;

		for (int i = 0; i < query.length(); i++) {
			switch (query.charAt(i)) {
			case ',':
				// On ne considère que les sous-requêtes
				// directes de notre requête de base.
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
		// On traite la dernière sous-requête.
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
		// Les documents concernés par la requête sont
		// tous ceux concernés par les sous-requêtes.
		for (Query subQuery : subQueries) {
			subQuery.getRelatedDocuments(index, documents);
		}
	}
}
