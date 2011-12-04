package searcher.extendedbooleanmodel;

import java.util.Set;

import index.Document;
import index.Index;
import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

/**
 * Classe repr�sentant une requ�te NOT permettant d'inverser (au sens bool�en)
 * le r�sultat d'une sous-requ�te.
 */
public class NotQuery extends Query {
	/** La sous-requ�te � nier. */
	private Query subQuery;

	/**
	 * Construit une requ�te de n�gation � partir de la requ�te textuelle
	 * fournie en utilisant le normalisateur et l'index fournis et en ignorant
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
	public NotQuery(String query, Normalizer normalizer, boolean ignoreStopWords, Index index)
			throws InvalideQueryException, EmptyQueryException {
		subQuery = parse(query, normalizer, ignoreStopWords, index);
	}

	@Override
	public void getRelatedDocuments(Index index, Set<Document> documents) {
		// On ne fait rien, on consid�re que l'op�rateur NOT
		// ne peut pas ajouter de documents.
	}

	@Override
	public double calculateSimilarity(Index index, Document document) {
		return 1 - subQuery.calculateSimilarity(index, document);
	}
}
