package searcher.vectormodel;

import index.Document;
import index.Index;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import tools.normalizer.Normalizer;
import tools.weigher.Weigher;

/**
 * Un searcher qui utilise le modèle vectoriel pour trouver les documents les
 * plus pertinant et qui supporte la recherche par préfixe si l'index le permet
 * (en utilisant le marqueur *). Il permet également d'exclure les documents
 * contenant certains termes (en utilisant le marqueur -).
 */
public class SearcherVectorModelPrefixWordsExclusion extends SearcherVectorModelPrefix {

	/** Constante symbolisant le marqueur d'exclusion de mots. */
	public static final String EXCLUSION_MARK = "-";

	/**
	 * Construit un searcher utilisant le modèle vectoriel avec un normalizer et
	 * un pondérateur pour traiter la requête et un index à questionner. La
	 * recherche par préfixe est prise en charge si l'index le permet et
	 * l'exclusion des documents contenant certains termes est également
	 * possible.
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requête.
	 * @param weigher
	 *            le pondérateur pour traiter la requête.
	 * @param index
	 *            l'index à questionner.
	 */
	public SearcherVectorModelPrefixWordsExclusion(Normalizer normalizer, Weigher weigher, Index index) {
		super(normalizer, weigher, index);
	}

	/**
	 * Prépare la requête en la normalisant et en remplaçant les préfixes par
	 * les mots correspondant.
	 * 
	 * @param request
	 *            la requête à préparer.
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent être ignorés ou non dans la
	 *            requête.
	 * @return la requête préparée.
	 */
	protected LinkedList<String> prepareQuery(String request, boolean ignoreStopWords) {
		LinkedList<String> query = new LinkedList<String>();
		ArrayList<String> terms = normalizer.normalize(request, ignoreStopWords);

		for (int i = 1; i < terms.size(); i++) {
			// Si on a trouvé un marqueur de préfixe, on remplace
			// le terme précédent dans la requête par tous les
			// termes de l'index possédant ce préfixe.
			if (terms.get(i).equals(PREFIX_MARK)) {
				if (i > 1 && terms.get(i-2).equals(EXCLUSION_MARK)) {
					for (String t : index.getTermsIndex(terms.get(i - 1))) {
						query.add(EXCLUSION_MARK);
						query.add(t);
					}
				} else {
					query.addAll(index.getTermsIndex(terms.get(i - 1)));
				}
			} else if (!terms.get(i - 1).equals(PREFIX_MARK)) {
				// Sinon on ajoute simplement le terme précédent
				// si ce n'était pas un marqueur de préfixe.
				query.add(terms.get(i - 1));
			}
		}

		// On ajoute le dernier terme de la requête
		// si ce n'est pas un marqueur de préfixe.
		if (!terms.get(terms.size() - 1).equals(PREFIX_MARK)) {
			query.add(terms.get(terms.size() - 1));
		}
System.out.println(query);
		return query;
	}
	
	/**
	 * Prépare la recherche en calculant le poid des termes de la requête et sa
	 * norme ainsi qu'en listant les documents concernés en excluant
	 * éventuellement certains termes préfixés par le marqueur d'exclusion.
	 * 
	 * @param wordsQuery
	 *            les termes composants la requête.
	 * @param weightsQuery
	 *            la liste à remplir des poids de la requête.
	 * @param docs
	 *            la liste à remplir des documents concernés par la requête.
	 * @return la norme de la requête.
	 */
	protected double setupSearch(List<String> wordsQuery, double[] weightsQuery, Set<Document> docs) {
		double normQuery = 0;

		boolean excludeNextTerm = false;
		HashSet<Document> excludedDocs = new HashSet<Document>();

		ListIterator<String> it = wordsQuery.listIterator();
		int i = 0;
		String term;

		while (it.hasNext()) {
			term = it.next();

			// Si le terme est un marqueur d'exclusion, on
			// marque le terme suivant comme devant être exclu.
			if (term.equals(EXCLUSION_MARK)) {
				excludeNextTerm = true;
				// On peut supprimer le marqueur de la requête.
				it.remove();
			} else if (!excludeNextTerm) {
				// Si le terme n'était précédé du marqueur d'exclusion,
				// on le traite normalement.

				// On calcule le poids du terme dans la requête
				weightsQuery[i] = weigher.calculateQueryWeight(term, wordsQuery, index);
				// On met à jour la norme.
				normQuery += weightsQuery[i] * weightsQuery[i];
				// On cherche dans l'index la liste des documents contenant
				// le terme et on les ajoute à la liste de tous les documents
				// concernés par la requête.
				docs.addAll(index.getDocumentsTerm(term));

				// Le prochaine terme n'est à priori pas exclu.
				excludeNextTerm = false;

				i++;
			} else { // Sinon on a un terme devant être exclu.
				// On peut le supprimer de la requête.
				it.remove();

				// On cherche dans l'index la liste des documents contenant
				// le terme et on les ajoute à la liste de tous les documents
				// devant être exclus de la requête.
				excludedDocs.addAll(index.getDocumentsTerm(term));

				// Le prochaine terme n'est à priori pas exclu.
				excludeNextTerm = false;
			}
		}

		// On supprime de la liste des documents concernés
		// par la requête tous ceux qui ont été exclus.
		docs.removeAll(excludedDocs);

		// Finalisation du calcul de la norme.
		return Math.sqrt(normQuery);
	}
}
