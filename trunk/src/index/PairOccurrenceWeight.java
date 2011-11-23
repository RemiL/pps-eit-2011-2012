package index;

import java.io.Serializable;

/**
 * Un couple nombre d'occurrences/poids.
 */
public class PairOccurrenceWeight implements Serializable {

	private static final long serialVersionUID = -7871879634276972901L;
	/** Le nombre d'occurrences du terme dans le document */
	private int nbOccurrences;
	/** Le poids du terme dans le document */
	private double weight;

	/**
	 * Construit un couple nombre d'occurrences/poids avec les valeurs passées
	 * en paramètre.
	 * 
	 * @param nbOccurrences
	 *            le nombre d'occurrences du terme dans le document
	 * @param weight
	 *            le poids du document
	 */
	protected PairOccurrenceWeight(int nbOccurrences, double weight) {
		this.nbOccurrences = nbOccurrences;
		this.weight = weight;
	}

	/**
	 * Retourne le nombre d'occurrences du terme dans le document.
	 * 
	 * @return le nombre d'occurrences
	 */
	public int getNbOccurrences() {
		return nbOccurrences;
	}

	/**
	 * Modifie le nombre d'occurrence du terme dans le document.
	 * 
	 * @param nbOccurrences
	 *            le nouveau nombre d'occurrence du terme dans le document
	 */
	protected void setNbOccurrences(int nbOccurrences) {
		this.nbOccurrences = nbOccurrences;
	}

	/**
	 * Retourne le poids.
	 * 
	 * @return le poids
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Modifie le poids.
	 * 
	 * @param weight
	 *            le nouveau poids
	 */
	protected void setWeight(double weight) {
		this.weight = weight;
	}
}
