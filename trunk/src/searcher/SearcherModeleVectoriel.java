package searcher;

import java.util.LinkedList;

import index.Index;
import tools.normalizer.Normalizer;
import tools.ponderateur.Ponderateur;

/**
 * Un searcher qui utilise le modèle vectoriel pour trouver les documents les
 * plus pertinant
 */
public class SearcherModeleVectoriel extends Searcher {

	/** Le pondérateur pour traiter la requête */
	Ponderateur ponderateur;

	/**
	 * Construit un searcher utilisant le modèle vectoriel avec un normalizer et
	 * un pondérateur pour traiter la requête et un index à questionner
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requête
	 * @param ponderateur
	 *            le pondérateur pour traiter la requête
	 * @param index
	 *            l'index à questionner
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
