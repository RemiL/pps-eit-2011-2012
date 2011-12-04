package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;

import java.util.ArrayList;
import java.util.Set;

import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

/**
 * Classe repr�sentant un terme d'une requ�te.
 */
public class QueryTerm extends Query {
	/** Le terme. */
	private String term;

	/**
	 * Construit un terme � partir de la requ�te textuelle fournie en utilisant
	 * le normalisateur fourni et en ignorant �ventuellement les mots vides.
	 * 
	 * @param query
	 *            la requ�te textuelle.
	 * @param normalizer
	 *            le normalisateur � utiliser.
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent �tre ignor�s ou non.
	 * @throws InvalideQueryException
	 *             si la requ�te est incorrectement construite.
	 */
	public QueryTerm(String query, Normalizer normalizer, boolean ignoreStopWords) throws InvalideQueryException,
			EmptyQueryException {
		ArrayList<String> terms = normalizer.normalize(query, ignoreStopWords);

		if (terms.size() == 1) {
			term = terms.get(0);
		} else if (terms.isEmpty()) {
			throw new EmptyQueryException();
		} else {
			throw new InvalideQueryException(query);
		}
	}

	@Override
	public void getRelatedDocuments(Index index, Set<Document> documents) {
		// Les documents concern�s sont tous ceux contenant le terme.
		documents.addAll(index.getDocumentsTerm(term));
	}

	@Override
	public double calculateSimilarity(Index index, Document document) {
		// Un terme s'�value par son poids dans le document.
		return index.getWeight(term, document);
	}
}
