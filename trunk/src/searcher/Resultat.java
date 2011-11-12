package searcher;

import index.Document;

/**
 * Un résultat d'une recherche. C'est un couple qui à un document associe une
 * valeur
 */
public class Resultat {

	/** Le document du résultat */
	Document document;
	/** La valeur du résultat */
	double valeur;

	/**
	 * Construit un résultat d'une recherche avec un document et une valeur
	 * 
	 * @param document
	 *            le document
	 * @param valeur
	 *            la valeur
	 */
	public Resultat(Document document, double valeur) {
		this.document = document;
		this.valeur = valeur;
	}

	/**
	 * Retourne le document
	 * 
	 * @return le document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * Retourne la valeur
	 * 
	 * @return la valeur
	 */
	public double getValeur() {
		return valeur;
	}
}
