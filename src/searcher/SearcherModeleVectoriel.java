package searcher;

import java.util.LinkedList;

import index.Index;
import tools.normalizer.Normalizer;
import tools.ponderateur.Ponderateur;

/**
 * Un searcher qui utilise le mod�le vectoriel pour trouver les documents les
 * plus pertinant
 */
public class SearcherModeleVectoriel extends Searcher {

	/** Le pond�rateur pour traiter la requ�te */
	Ponderateur ponderateur;

	/**
	 * Construit un searcher utilisant le mod�le vectoriel avec un normalizer et
	 * un pond�rateur pour traiter la requ�te et un index � questionner
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requ�te
	 * @param ponderateur
	 *            le pond�rateur pour traiter la requ�te
	 * @param index
	 *            l'index � questionner
	 */
	public SearcherModeleVectoriel(Normalizer normalizer, Ponderateur ponderateur, Index index) {
		super(normalizer, index);
		this.ponderateur = ponderateur;
	}

	@Override
	public LinkedList<Resultat> search(String requete, int nbResultats) {

		return null;
	}

}
