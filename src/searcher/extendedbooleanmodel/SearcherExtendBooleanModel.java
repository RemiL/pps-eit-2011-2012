package searcher.extendedbooleanmodel;

import index.Document;
import index.Index;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import searcher.InvalideQueryException;
import searcher.Result;
import searcher.Searcher;
import tools.normalizer.Normalizer;

/**
 * Un searcher qui utilise le modèle booléen étendu pour trouver les documents
 * les plus pertinant. Les requêtes doivent être écrites de la façon suivante :
 * <ul>
 * <li>AND(req1, req2, ..., reqN)</li>
 * <li>OR(req1, req2, ..., reqN)</li>
 * <li>NOT(req1)</li>
 * où req sont des termes ou des sous-requêtes.
 * </ul>
 */
public class SearcherExtendBooleanModel extends Searcher {
	/**
	 * Construit un searcher utilisant le modèle booléen étendu avec un
	 * normalizer pour traiter la requête et un index à questionner.
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requête.
	 * @param index
	 *            l'index à questionner.
	 */
	public SearcherExtendBooleanModel(Normalizer normalizer, Index index) {
		super(normalizer, index);
	}

	@Override
	public LinkedList<Result> search(String request, boolean ignoreStopWords, int nbResultats)
			throws InvalideQueryException {
		LinkedList<Result> results = new LinkedList<Result>();
		Set<Document> documents = new HashSet<Document>();

		String requests[] = request.split("\\|");

		Query query;
		try {
			query = Query.parse(requests[0], normalizer, ignoreStopWords, index);
		} catch (EmptyQueryException e) {
			throw new InvalideQueryException(request);
		}

		query.getRelatedDocuments(index, documents);

		if (requests.length > 1) {
			Query excludeQuery = new ExcludeQuery(requests[1], normalizer, ignoreStopWords);
			Set<Document> excludedDocuments = new HashSet<Document>();
			excludeQuery.getRelatedDocuments(index, excludedDocuments);

			documents.removeAll(excludedDocuments);
		}

		double similarity;
		for (Document doc : documents) {
			similarity = query.calculateSimilarity(index, doc);

			if (similarity != 0) {
				results.add(new Result(doc, similarity));
			}
		}

		// Trie la liste des résultats et on ne garde
		// que le nombre désiré de meilleurs résultats.
		sortResults(results, nbResultats);

		return results;
	}

	/**
	 * Trie la liste de résultat fournie par ordre de pertinance croissante en
	 * conservant uniquement le nombre de meilleurs résultats désirés (qui peut
	 * être non limité si la constante ALL_RESULTS est utilisée).
	 * 
	 * @param results
	 *            la liste de résultat à trier.
	 * @param nbResultats
	 *            le nombre de meilleurs résultats à conserver (éventuellement
	 *            ALL_RESULTS si on veut conserver tous les résultats).
	 */
	protected void sortResults(LinkedList<Result> results, int nbResultats) {
		// TODO : optimiser le code qui suit ?
		// On trie la liste des résultats.
		Collections.sort(results);
		// On ne conserve que le nombre désiré de résultats
		if (nbResultats != ALL_RESULTS && results.size() > nbResultats) {
			for (int i = results.size() - nbResultats; i > 0; i--) {
				results.removeLast();
			}
		}
	}
}
