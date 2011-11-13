package searcher;

import index.Document;

/**
 * Un r�sultat d'une recherche. Il s'agit d'un couple qui � un document associe
 * une valeur de pertinence. Un r�sultat est comparable � un autre en fonction
 * de leur valeur respective de pertinence.
 */
public class Resultat implements Comparable<Resultat> {

	/** Le document du r�sultat */
	Document document;
	/** La pertinence du r�sultat */
	double pertinence;

	/**
	 * Construit un r�sultat d'une recherche avec un document et une pertinence
	 * 
	 * @param document
	 *            le document
	 * @param pertinence
	 *            la pertinence
	 */
	public Resultat(Document document, double pertinence) {
		this.document = document;
		this.pertinence = pertinence;
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
	 * Retourne la pertinence
	 * 
	 * @return la pertinence
	 */
	public double getPertinence() {
		return pertinence;
	}

	@Override
	public int compareTo(Resultat resultat) {
		int res = 0;

		if (pertinence < resultat.pertinence) {
			res = -1;
		} else if (pertinence > resultat.pertinence) {
			res = 1;
		}

		return res;
	}
}
