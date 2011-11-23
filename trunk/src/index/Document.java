package index;

import java.io.Serializable;

/**
 * Un document avec ses informations.
 */
public class Document implements Serializable {

	private static final long serialVersionUID = 2590642121208114450L;
	/** L'url du document */
	private String url;
	/** Le titre du document */
	private String title;
	/** La norme du vecteur représentant le document */
	private double norm;
	/** L'id du document */
	private int id;
	/** L'id du prochain document */
	private static int currentId = 0;

	/**
	 * Construit un document avec un titre et une url vides.
	 */
	protected Document() {
		this.id = currentId;
		currentId++;
	}

	/**
	 * Construit un document avec un titre et une url.
	 * 
	 * @param url
	 *            l'url du document
	 * @param title
	 *            le titre du document
	 */
	protected Document(String url, String title) {
		this.url = url;
		this.title = title;
		this.id = currentId;
		currentId++;
	}

	/**
	 * Retourne l'id du document
	 * 
	 * @return l'id du document
	 */
	public int getId() {
		return id;
	}

	/**
	 * Retourne l'url du document.
	 * 
	 * @return l'url du document
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Modifie l'url du document.
	 * 
	 * @param url
	 *            la nouvelle url du document
	 */
	protected void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Retourne le titre du document.
	 * 
	 * @return le titre du document
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Modifie le titre du document.
	 * 
	 * @param title
	 *            le nouveau titre du document
	 */
	protected void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Retourne la norme du document.
	 * 
	 * @return la norme du document
	 */
	public double getNorm() {
		return norm;
	}

	/**
	 * Modifie la norme du document.
	 * 
	 * @param norm
	 *            la norme titre du document
	 */
	protected void setNorm(double norm) {
		this.norm = norm;
	}

	/**
	 * Ajoute un poids au document pour calculer la norme de celui-ci.
	 * 
	 * @param weight
	 *            le poids d'un terme du document
	 */
	protected void addWeight(double weight) {
		norm += Math.pow(weight, 2);
	}

	/**
	 * Finalise le calcul de la norme en y appliquant une racine carrée après
	 * avoir additionné tous les poids au carré
	 */
	protected void finalizeNorm() {
		norm = Math.sqrt(norm);
	}
}
