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
 * Un searcher qui utilise le mod�le bool�en �tendu pour trouver les documents
 * les plus pertinant. Les requ�tes doivent �tre �crites de la fa�on suivante :
 * <ul>
 * <li>AND(req1, req2, ..., reqN)</li>
 * <li>OR(req1, req2, ..., reqN)</li>
 * <li>NOT(req1)</li>
 * o� req sont des termes ou des sous-requ�tes.
 * </ul>
 */
public class SearcherExtendBooleanModel extends Searcher {
	/**
	 * Construit un searcher utilisant le mod�le bool�en �tendu avec un
	 * normalizer pour traiter la requ�te et un index � questionner.
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requ�te.
	 * @param index
	 *            l'index � questionner.
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

		// Trie la liste des r�sultats et on ne garde
		// que le nombre d�sir� de meilleurs r�sultats.
		sortResults(results, nbResultats);

		return results;
	}

	/**
	 * Trie la liste de r�sultat fournie par ordre de pertinance croissante en
	 * conservant uniquement le nombre de meilleurs r�sultats d�sir�s (qui peut
	 * �tre non limit� si la constante ALL_RESULTS est utilis�e).
	 * 
	 * @param results
	 *            la liste de r�sultat � trier.
	 * @param nbResultats
	 *            le nombre de meilleurs r�sultats � conserver (�ventuellement
	 *            ALL_RESULTS si on veut conserver tous les r�sultats).
	 */
	protected void sortResults(LinkedList<Result> results, int nbResultats) {
		// TODO : optimiser le code qui suit ?
		// On trie la liste des r�sultats.
		Collections.sort(results);
		// On ne conserve que le nombre d�sir� de r�sultats
		if (nbResultats != ALL_RESULTS && results.size() > nbResultats) {
			for (int i = results.size() - nbResultats; i > 0; i--) {
				results.removeLast();
			}
		}
	}
}
