package searcher;

import index.Document;
import index.Index;

import java.util.HashSet;
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
