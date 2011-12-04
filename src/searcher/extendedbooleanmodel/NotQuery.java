package searcher.extendedbooleanmodel;

import java.util.Set;

import index.Document;
import index.Index;
import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

/**
 * Classe représentant une requête NOT permettant d'inverser (au sens booléen)
 * le résultat d'une sous-requête.
 */
public class NotQuery extends Query {
	/** La sous-requête à nier. */
	private Query subQuery;

	/**
	 * Construit une requête de négation à partir de la requête textuelle
	 * fournie en utilisant le normalisateur et l'index fournis et en ignorant
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
	public NotQuery(String query, Normalizer normalizer, boolean ignoreStopWords, Index index)
			throws InvalideQueryException, EmptyQueryException {
		subQuery = parse(query, normalizer, ignoreStopWords, index);
	}

	@Override
	public void getRelatedDocuments(Index index, Set<Document> documents) {
		// On ne fait rien, on considère que l'opérateur NOT
		// ne peut pas ajouter de documents.
	}

	@Override
	public double calculateSimilarity(Index index, Document document) {
		return 1 - subQuery.calculateSimilarity(index, document);
	}
}
