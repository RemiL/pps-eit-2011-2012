package tools.ponderateur;

import java.util.ArrayList;

import index.Index;

/**
 * Un pond�rateur permet de donner un poids � un couple terme/document de
 * l'index
 */
public interface Ponderateur {

	/**
	 * Calcule le poids d'un terme dans un document
	 * 
	 * @param terme
	 *            le terme
	 * @param urlDocument
	 *            l'url du document
	 * @param index
	 *            l'index � consid�rer
	 * @return le poids du terme dans le document
	 */
	public double calculerPoids(String terme, String urlDocument, Index index);

	/**
	 * Calcule le poids d'un terme dans un document
	 * 
	 * @param word
	 *            le terme
	 * @param wordsDoc
	 *            la liste des termes du document
	 * @param index
	 *            l'index � consid�rer
	 * @return le poids du terme dans le document
	 */
	public double calculerPoids(String word, ArrayList<String> wordsDoc, Index index);
}
