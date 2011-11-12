package index;

/**
 * Un document avec ses informations
 */
public class Document {

	/** L'url du document */
	private String url;
	/** Le titre du document */
	private String titre;

	/**
	 * Construit un document avec un titre et une url
	 * 
	 * @param url
	 *            l'url du document
	 * @param title
	 *            le titre du document
	 */
	public Document(String url, String title) {
		this.url = url;
		this.titre = title;
	}

	/**
	 * Retourne l'url du document
	 * 
	 * @return l'url du document
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Modifie l'url du document
	 * 
	 * @param url
	 *            la nouvelle url du document
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Retourne le titre du document
	 * 
	 * @return le titre du document
	 */
	public String getTitre() {
		return titre;
	}

	/**
	 * Modifie le titre du document
	 * 
	 * @param titre
	 *            le nouveau titre du document
	 */
	public void setTitre(String title) {
		this.titre = title;
	}
}
