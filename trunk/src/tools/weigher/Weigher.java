package tools.weigher;

import index.Document;
import index.Index;

import java.util.List;

/**
 * Un pondérateur permet de donner un poids à un couple terme/document de
 * l'index.
 */
public interface Weigher {

	/**
	 * Calcule le poids d'un terme dans un document.
	 * 
	 * @param term
	 *            le terme
	 * @param document
	 *            le document
	 * @param index
	 *            l'index à considérer
	 * @return le poids du terme dans le document
	 */
	public double calculateWeight(String term, Document document, Index index);

	/**
	 * Calcule le poids d'un terme dans une requête.
	 * 
	 * @param word
	 *            le terme
	 * @param wordsQuery
	 *            la liste des termes de la requête
	 * @param index
	 *            l'index à considérer
	 * @return le poids du terme dans le document
	 */
	public double calculateQueryWeight(String word, List<String> wordsQuery, Index index);
}
