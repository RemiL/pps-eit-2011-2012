package index;

/**
 * Un couple nombre d'occurrences/poids
 */
public class CoupleOccurrencePoids {

	private int nbOccurrences;
	private double poids;

	/**
	 * Construit un couple nombre d'occurrences/poids avec les valeurs passées
	 * en paramètre
	 * 
	 * @param nbOccurrences
	 *            le nombre d'occurrences du terme dans le document
	 * @param poids
	 *            le poids du document
	 */
	protected CoupleOccurrencePoids(int nbOccurrences, double poids) {
		this.nbOccurrences = nbOccurrences;
		this.poids = poids;
	}

	/**
	 * Retourne le nombre d'occurrences du terme dans le document
	 * 
	 * @return le nombre d'occurrences
	 */
	public int getNbOccurrences() {
		return nbOccurrences;
	}

	/**
	 * Modifie le nombre d'occurrence du terme dans le document
	 * 
	 * @param nbOccurrences
	 *            le nouveau nombre d'occurrence du terme dans le document
	 */
	protected void setNbOccurrences(int nbOccurrences) {
		this.nbOccurrences = nbOccurrences;
	}

	/**
	 * Retourne le poids
	 * 
	 * @return le poids
	 */
	public double getPoids() {
		return poids;
	}

	/**
	 * Modifie le poids
	 * 
	 * @param poids
	 *            le nouveau poids
	 */
	protected void setPoids(double poids) {
		this.poids = poids;
	}
}
