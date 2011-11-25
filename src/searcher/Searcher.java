package searcher;

import java.util.LinkedList;

import index.Index;
import tools.normalizer.Normalizer;

/**
 * Un searcher permet de faire des requ�tes sur l'index pour avoir une liste de
 * r�sultats
 */
public abstract class Searcher {

	/** Constante indiquant que tous les r�sultats doivent �tre retourn�s */
	public static final int ALL_RESULTS = -1;

	/** Le normalizer pour traiter la requ�te */
	protected Normalizer normalizer;
	/** L'index � questionner */
	protected Index index;

	/**
	 * Construit un searcher avec un normalizer pour traiter la requ�te et
	 * l'index � questionner
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requ�te
	 * @param index
	 *            l'index � questionner
	 */
	public Searcher(Normalizer normalizer, Index index) {
		this.normalizer = normalizer;
		this.index = index;
	}

	/**
	 * Lance une recherche dans l'index avec la requ�te en param�tre. Le
	 * r�sultat est retourn� sous forme d'une liste tri�e de documents du plus
	 * pertinant au moins pertinant. Le nombre de documents retourn� est
	 * param�trable.
	 * 
	 * @param request
	 *            le requ�te � traiter
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent �tre ignor�s
	 * @param nbResults
	 *            le nombre de r�sultats � retourner. ALL_RESULTS pour tous les r�sultats.
	 * @return une liste de documents tri�e du plus pertinant au moins pertinant
	 */
	public abstract LinkedList<Result> search(String request, boolean ignoreStopWords, int nbResults);

	/**
	 * Lance une recherche dans l'index avec la requ�te en param�tre. Le
	 * r�sultat est retourn� sous forme d'une liste tri�e de documents du plus
	 * pertinant au moins pertinant. L'ensemble des documents trouv�s est
	 * retourn�.
	 * 
	 * @param request
	 *            le requ�te � traiter
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent �tre ignor�s
	 * @return une liste de documents tri�e du plus pertinant au moins pertinant
	 */
	public LinkedList<Result> search(String request, boolean ignoreStopWords) {
		return search(request, ignoreStopWords, ALL_RESULTS);
	}
}
