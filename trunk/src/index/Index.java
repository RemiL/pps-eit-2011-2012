package index;

import java.util.HashMap;
import java.util.Set;

/**
 * Un index abstrait
 */
public abstract class Index {

	/** Table de hashage qui à une url associe un document */
	protected HashMap<String, Document> listeDocuments;

	/**
	 * Construit un Index vide
	 */
	public Index() {
		listeDocuments = new HashMap<String, Document>();
	}

	/**
	 * Ajoute un document à la liste de documents
	 * 
	 * @param document
	 *            le document à ajouter
	 */
	protected void addDocument(Document document) {
		listeDocuments.put(document.getTitre(), document);
	}

	/**
	 * Retourne le document correspondant à l'URL fournie s'il existe, sinon
	 * retourne null.
	 * 
	 * @param urlDocument
	 *            l'URL du document à retourner
	 * @return le document correspondant à l'URL fournie s'il existe, null sinon
	 */
	public Document getDocument(String urlDocument) {
		return listeDocuments.get(urlDocument);
	}

	/**
	 * Retourne le nombre de documents dans l'index
	 * 
	 * @return le nombre de documents dans l'index
	 */
	public int getNbDocuments() {
		return listeDocuments.size();
	}

	/**
	 * Ajoute un terme à l'index
	 * 
	 * @param terme
	 *            le terme à ajouter à l'index
	 */
	protected abstract void addTerme(String terme);

	/**
	 * Ajoute un document à un terme. S'il existe déjà, son nombre d'occurrence
	 * est incrémenté. Sinon le document est créé
	 * 
	 * @param terme
	 *            le terme auquel ajouter le document
	 * @param document
	 *            le document à ajouter au terme
	 */
	public abstract void addDocumentTerme(String terme, Document document);

	/**
	 * Modifie le poids d'un terme d'un document
	 * 
	 * @param terme
	 *            le terme
	 * @param urlDocument
	 *            l'url du document
	 * @param poids
	 *            le poids du terme dans le document
	 */
	public abstract void setPoids(String terme, String urlDocument, double poids);

	/**
	 * Retourne le nombre d'occurrences du terme dans le document
	 * 
	 * @param terme
	 *            le terme voulu
	 * @param urlDocument
	 *            l'url du document
	 */
	public abstract int getNbOccurrencesTermeDocument(String terme, String urlDocument);

	/**
	 * Retourne le nombre de documents dans lesquels ce trouve le terme
	 * 
	 * @param terme
	 *            le terme
	 * @return le nombre de documents dans lesquels ce trouve le terme
	 */
	public abstract int getNbDocumentsTerme(String terme);

	/**
	 * Retourne le poids d'un terme dans un document, 0 si le terme n'existe pas
	 * dans l'index ou le document spécifié.
	 * 
	 * @param terme
	 *            le terme
	 * @param urlDocument
	 *            l'url du document
	 * @return le poids d'un terme dans un document
	 */
	public abstract double getPoids(String terme, String urlDocument);

	/**
	 * Returne un set des termes de l'index
	 * 
	 * @return un set des termes de l'index
	 */
	public abstract Set<String> getTermesIndex();

	/**
	 * Returne un set des documents de l'index
	 * 
	 * @return un set des documents de l'index
	 */
	public abstract Set<String> getDocumentsTerme(String terme);
}
