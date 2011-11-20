package index;

/**
 * Un couple nombre d'occurrences/poids
 */
public class CoupleOccurrenceWeight {

	private int nbOccurrences;
	private double weight;

	/**
	 * Construit un couple nombre d'occurrences/poids avec les valeurs passées
	 * en paramètre
	 * 
	 * @param nbOccurrences
	 *            le nombre d'occurrences du terme dans le document
	 * @param weight
	 *            le poids du document
	 */
	protected CoupleOccurrenceWeight(int nbOccurrences, double weight) {
		this.nbOccurrences = nbOccurrences;
		this.weight = weight;
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
	public double getWeight() {
		return weight;
	}

	/**
	 * Modifie le poids
	 * 
	 * @param weight
	 *            le nouveau poids
	 */
	protected void setWeight(double weight) {
		this.weight = weight;
	}
}
