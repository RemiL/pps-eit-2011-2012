package searcher;

import java.util.LinkedList;

import index.Index;
import tools.normalizer.Normalizer;

/**
 * Un searcher permet de faire des requ�tes sur l'index pour avoir une liste de
 * r�sultats
 */
public abstract class Searcher {

	/** Le normalizer pour traiter la requ�te */
	Normalizer normalizer;
	/** L'index � questionner */
	Index index;

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
	 * pertinant au moins pertinant. Le nombr ede documents dans la liste est
	 * param�trable
	 * 
	 * @param request
	 *            le requ�te � traiter
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent �tre ignor�s
	 * @param nbResults
	 *            le nombre de r�sultats � retourner. -1 pour tous les r�sultats
	 * @return une liste de documents tri�e du plus pertinant au moins pertiant
	 */
	public abstract LinkedList<Result> search(String request, boolean ignoreStopWords, int nbResults);
}
