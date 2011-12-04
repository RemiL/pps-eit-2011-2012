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
				if (i > 1 && terms.get(i-2).equals(EXCLUSION_MARK)) {
					for (String t : index.getTermsIndex(terms.get(i - 1))) {
						query.add(EXCLUSION_MARK);
						query.add(t);
					}
				} else {
					query.addAll(index.getTermsIndex(terms.get(i - 1)));
				}
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
System.out.println(query);
		return query;
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
