package index;

import java.io.Serializable;

/**
 * Un document avec ses informations.
 */
public class Document implements Serializable {

	private static final long serialVersionUID = 2590642121208114450L;
	/** L'url du document. */
	private String url;
	/** Le chemin vers le fichier dans le r�pertoire corpus. */
	private String path;
	/** Le titre du document. */
	private String title;
	/** La norme du vecteur repr�sentant le document. */
	private double norm;
	/** Le plus grand nombre d'occurences pour un terme du document. */
	private int maxTermFrequency;

	/**
	 * Construit un document avec un titre et une url vides.
	 */
	protected Document() {
		// On n'a rien � faire.
	}

	/**
	 * Construit un document avec un titre et une url.
	 * 
	 * @param url
	 *            l'url du document
	 * @param title
	 *            le titre du document
	 */
	protected Document(String url, String path, String title) {
		this.url = url;
		this.path = path;
		this.title = title;
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
	 * Retourne le chemin du document
	 * 
	 * @return le chemin du document
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Modifie le chemin du document.
	 * 
	 * @param path
	 *            le nouveau chemin du document
	 */
	protected void setPath(String path) {
		this.path = path;
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
	 * Finalise le calcul de la norme en y appliquant une racine carr�e apr�s
	 * avoir additionn� tous les poids au carr�
	 */
	protected void finalizeNorm() {
		norm = Math.sqrt(norm);
	}

	/**
	 * Retourne le nombre maximal d'occurences d'un mot dans le document.
	 * 
	 * @return le nombre maximal d'occurences d'un mot dans le document.
	 */
	public int getMaxTermFrequency() {
		return maxTermFrequency;
	}

	/**
	 * Modifie le nombre maximal d'occurences d'un mot dans le document.
	 * 
	 * @param maxTermFrequency
	 *            le nombre maximal d'occurences d'un mot dans le document.
	 */
	public void setMaxTermFrequency(int maxTermFrequency) {
		this.maxTermFrequency = maxTermFrequency;
	}

	/**
	 * Met � jour le nombre maximal d'occurences d'un mot dans le document.
	 * 
	 * @param termFrequency
	 *            une nouvelle valeur d'occurences
	 */
	public void updateMaxTermFrequency(int termFrequency) {
		if (maxTermFrequency < termFrequency) {
			maxTermFrequency = termFrequency;
		}
	}
}
