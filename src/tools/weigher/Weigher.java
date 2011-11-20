package tools.weigher;

import java.util.ArrayList;

import index.Index;

/**
 * Un pond�rateur permet de donner un poids � un couple terme/document de
 * l'index.
 */
public interface Weigher {

	/**
	 * Calcule le poids d'un terme dans un document.
	 * 
	 * @param term
	 *            le terme
	 * @param urlDocument
	 *            l'url du document
	 * @param index
	 *            l'index � consid�rer
	 * @return le poids du terme dans le document
	 */
	public double calculateWeight(String term, String urlDocument, Index index);

	/**
	 * Calcule le poids d'un terme dans un document.
	 * 
	 * @param word
	 *            le terme
	 * @param wordsDoc
	 *            la liste des termes du document
	 * @param index
	 *            l'index � consid�rer
	 * @return le poids du terme dans le document
	 */
	public double calculateWeight(String word, ArrayList<String> wordsDoc, Index index);
}
