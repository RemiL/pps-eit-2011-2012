package searcher;

import java.util.LinkedList;

import index.Index;
import tools.normalizer.Normalizer;

/**
 * Un searcher permet de faire des requêtes sur l'index pour avoir une liste de
 * résultats
 */
public abstract class Searcher {

	/** Le normalizer pour traiter la requête */
	Normalizer normalizer;
	/** L'index à questionner */
	Index index;

	/**
	 * Construit un searcher avec un normalizer pour traiter la requête et
	 * l'index à questionner
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requête
	 * @param index
	 *            l'index à questionner
	 */
	public Searcher(Normalizer normalizer, Index index) {
		this.normalizer = normalizer;
		this.index = index;
	}

	/**
	 * Lance une recherche dans l'index avec la requête en paramètre. Le
	 * résultat est retourné sous forme d'une liste triée de documents du plus
	 * pertinant au moins pertinant. Le nombr ede documents dans la liste est
	 * paramétrable
	 * 
	 * @param request
	 *            le requête à traiter
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent être ignorés
	 * @param nbResults
	 *            le nombre de résultats à retourner. -1 pour tous les résultats
	 * @return une liste de documents triée du plus pertinant au moins pertiant
	 */
	public abstract LinkedList<Result> search(String request, boolean ignoreStopWords, int nbResults);
}
