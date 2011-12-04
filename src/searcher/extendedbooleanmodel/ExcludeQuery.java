package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;

import java.util.ArrayList;
import java.util.Set;

import searcher.InvalideQueryException;
import tools.normalizer.Normalizer;

/**
 * Classe repr�sentant une requ�te permettant d'exclure strictement de la liste
 * de r�sultats les documents contenant certains termes.
 */
public class ExcludeQuery extends Query {
	/** La liste des termes exclus. */
	private ArrayList<String> terms;

	/**
	 * Construit une requ�te d'exclusion � partir de la requ�te textuelle
	 * fournie en utilisant le normalisateur fourni et en ignorant
	 * �ventuellement les mots vides.
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
	public ExcludeQuery(String query, Normalizer normalizer, boolean ignoreStopWords) throws InvalideQueryException {
		terms = new ArrayList<String>();

		query = query.trim();
		if (query.matches(EXCLUDE)) {
			query = query.substring(8, query.length() - 1);

			for (String term : query.split(",")) {
				terms.addAll(normalizer.normalize(term, ignoreStopWords));
			}
		} else {
			throw new InvalideQueryException(query);
		}
	}

	@Override
	public void getRelatedDocuments(Index index, Set<Document> documents) {
		// Les documents concern�s par l'exclusion sont tous
		// ceux contenant ou moins un des termes exclus.
		for (String term : terms) {
			documents.addAll(index.getDocumentsTerm(term));
		}
	}

	@Override
	public double calculateSimilarity(Index index, Document document) {
		// Normalement jamais utilis�e.
		return 0;
	}
}
