package searcher;

import java.util.LinkedList;

import index.Index;
import tools.normalizer.Normalizer;

/**
 * Un searcher permet de faire des requêtes sur l'index pour avoir une liste de
 * résultats
 */
public abstract class Searcher {

	/** Constante indiquant que tous les résultats doivent être retournés */
	public static final int ALL_RESULTS = -1;

	/** Le normalizer pour traiter la requête */
	protected Normalizer normalizer;
	/** L'index à questionner */
	protected Index index;

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
	 * pertinant au moins pertinant. Le nombre de documents retourné est
	 * paramétrable.
	 * 
	 * @param request
	 *            le requête à traiter
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent être ignorés
	 * @param nbResults
	 *            le nombre de résultats à retourner. ALL_RESULTS pour tous les résultats.
	 * @return une liste de documents triée du plus pertinant au moins pertinant
	 */
	public abstract LinkedList<Result> search(String request, boolean ignoreStopWords, int nbResults);

	/**
	 * Lance une recherche dans l'index avec la requête en paramètre. Le
	 * résultat est retourné sous forme d'une liste triée de documents du plus
	 * pertinant au moins pertinant. L'ensemble des documents trouvés est
	 * retourné.
	 * 
	 * @param request
	 *            le requête à traiter
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent être ignorés
	 * @return une liste de documents triée du plus pertinant au moins pertinant
	 */
	public LinkedList<Result> search(String request, boolean ignoreStopWords) {
		return search(request, ignoreStopWords, ALL_RESULTS);
	}
}
