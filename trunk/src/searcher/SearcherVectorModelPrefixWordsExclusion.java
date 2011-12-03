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
 * Un searcher qui utilise le mod�le vectoriel pour trouver les documents les
 * plus pertinant et qui supporte la recherche par pr�fixe si l'index le permet
 * (en utilisant le marqueur *). Il permet �galement d'exclure les documents
 * contenant certains termes (en utilisant le marqueur -).
 */
public class SearcherVectorModelPrefixWordsExclusion extends SearcherVectorModelPrefix {

	/** Constante symbolisant le marqueur d'exclusion de mots. */
	public static final String EXCLUSION_MARK = "-";

	/**
	 * Construit un searcher utilisant le mod�le vectoriel avec un normalizer et
	 * un pond�rateur pour traiter la requ�te et un index � questionner. La
	 * recherche par pr�fixe est prise en charge si l'index le permet et
	 * l'exclusion des documents contenant certains termes est �galement
	 * possible.
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requ�te.
	 * @param weigher
	 *            le pond�rateur pour traiter la requ�te.
	 * @param index
	 *            l'index � questionner.
	 */
	public SearcherVectorModelPrefixWordsExclusion(Normalizer normalizer, Weigher weigher, Index index) {
		super(normalizer, weigher, index);
	}

	/**
	 * Pr�pare la recherche en calculant le poid des termes de la requ�te et sa
	 * norme ainsi qu'en listant les documents concern�s en excluant
	 * �ventuellement certains termes pr�fix�s par le marqueur d'exclusion.
	 * 
	 * @param wordsQuery
	 *            les termes composants la requ�te.
	 * @param weightsQuery
	 *            la liste � remplir des poids de la requ�te.
	 * @param docs
	 *            la liste � remplir des documents concern�s par la requ�te.
	 * @return la norme de la requ�te.
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
			// marque le terme suivant comme devant �tre exclu.
			if (term.equals(EXCLUSION_MARK)) {
				excludeNextTerm = true;
				// On peut supprimer le marqueur de la requ�te.
				it.remove();
			} else if (!excludeNextTerm) {
				// Si le terme n'�tait pr�c�d� du marqueur d'exclusion,
				// on le traite normalement.

				// On calcule le poids du terme dans la requ�te
				weightsQuery[i] = weigher.calculateQueryWeight(term, wordsQuery, index);
				// On met � jour la norme.
				normQuery += weightsQuery[i] * weightsQuery[i];
				// On cherche dans l'index la liste des documents contenant
				// le terme et on les ajoute � la liste de tous les documents
				// concern�s par la requ�te.
				docs.addAll(index.getDocumentsTerm(term));

				// Le prochaine terme n'est � priori pas exclu.
				excludeNextTerm = false;

				i++;
			} else { // Sinon on a un terme devant �tre exclu.
				// On peut le supprimer de la requ�te.
				it.remove();

				// On cherche dans l'index la liste des documents contenant
				// le terme et on les ajoute � la liste de tous les documents
				// devant �tre exclus de la requ�te.
				excludedDocs.addAll(index.getDocumentsTerm(term));

				// Le prochaine terme n'est � priori pas exclu.
				excludeNextTerm = false;
			}
		}

		// On supprime de la liste des documents concern�s
		// par la requ�te tous ceux qui ont �t� exclus.
		docs.removeAll(excludedDocs);

		// Finalisation du calcul de la norme.
		return Math.sqrt(normQuery);
	}
}
