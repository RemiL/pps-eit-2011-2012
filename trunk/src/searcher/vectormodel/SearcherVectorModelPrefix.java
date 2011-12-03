package searcher.vectormodel;

import index.Index;

import java.util.ArrayList;
import java.util.LinkedList;
import tools.normalizer.Normalizer;
import tools.weigher.Weigher;

/**
 * Un searcher qui utilise le modèle vectoriel pour trouver les documents les
 * plus pertinant et qui supporte la recherche par préfixe si l'index le permet
 * (en utilisant le marqueur *).
 */
public class SearcherVectorModelPrefix extends SearcherVectorModel {

	/** Constante symbolisant le marqueur de préfixe. */
	public static final String PREFIX_MARK = "*";

	/**
	 * Construit un searcher utilisant le modèle vectoriel avec un normalizer et
	 * un pondérateur pour traiter la requête et un index à questionner. La
	 * recherche par préfixe est prise en charge si l'index le permet.
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requête.
	 * @param weigher
	 *            le pondérateur pour traiter la requête.
	 * @param index
	 *            l'index à questionner.
	 */
	public SearcherVectorModelPrefix(Normalizer normalizer, Weigher weigher, Index index) {
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
				query.addAll(index.getTermsIndex(terms.get(i - 1)));
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

		return query;
	}
}
