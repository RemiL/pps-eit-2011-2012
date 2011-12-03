package searcher.vectormodel;

import index.Index;

import java.util.ArrayList;
import java.util.LinkedList;
import tools.normalizer.Normalizer;
import tools.weigher.Weigher;

/**
 * Un searcher qui utilise le mod�le vectoriel pour trouver les documents les
 * plus pertinant et qui supporte la recherche par pr�fixe si l'index le permet
 * (en utilisant le marqueur *).
 */
public class SearcherVectorModelPrefix extends SearcherVectorModel {

	/** Constante symbolisant le marqueur de pr�fixe. */
	public static final String PREFIX_MARK = "*";

	/**
	 * Construit un searcher utilisant le mod�le vectoriel avec un normalizer et
	 * un pond�rateur pour traiter la requ�te et un index � questionner. La
	 * recherche par pr�fixe est prise en charge si l'index le permet.
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requ�te.
	 * @param weigher
	 *            le pond�rateur pour traiter la requ�te.
	 * @param index
	 *            l'index � questionner.
	 */
	public SearcherVectorModelPrefix(Normalizer normalizer, Weigher weigher, Index index) {
		super(normalizer, weigher, index);
	}

	/**
	 * Pr�pare la requ�te en la normalisant et en rempla�ant les pr�fixes par
	 * les mots correspondant.
	 * 
	 * @param request
	 *            la requ�te � pr�parer.
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent �tre ignor�s ou non dans la
	 *            requ�te.
	 * @return la requ�te pr�par�e.
	 */
	protected LinkedList<String> prepareQuery(String request, boolean ignoreStopWords) {
		LinkedList<String> query = new LinkedList<String>();
		ArrayList<String> terms = normalizer.normalize(request, ignoreStopWords);

		for (int i = 1; i < terms.size(); i++) {
			// Si on a trouv� un marqueur de pr�fixe, on remplace
			// le terme pr�c�dent dans la requ�te par tous les
			// termes de l'index poss�dant ce pr�fixe.
			if (terms.get(i).equals(PREFIX_MARK)) {
				query.addAll(index.getTermsIndex(terms.get(i - 1)));
			} else if (!terms.get(i - 1).equals(PREFIX_MARK)) {
				// Sinon on ajoute simplement le terme pr�c�dent
				// si ce n'�tait pas un marqueur de pr�fixe.
				query.add(terms.get(i - 1));
			}
		}

		// On ajoute le dernier terme de la requ�te
		// si ce n'est pas un marqueur de pr�fixe.
		if (!terms.get(terms.size() - 1).equals(PREFIX_MARK)) {
			query.add(terms.get(terms.size() - 1));
		}

		return query;
	}
}
