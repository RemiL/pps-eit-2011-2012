package index;

import java.util.HashMap;
import java.util.Set;

/**
 * Un index abstrait
 */
public abstract class Index {

	/** Table de hashage qui à une url associe un document */
	protected HashMap<String, Document> listDocuments;

	/**
	 * Construit un Index vide
	 */
	protected Index() {
		listDocuments = new HashMap<String, Document>();
	}

	/**
	 * Ajoute un document à la liste de documents
	 * 
	 * @param document
	 *            le document à ajouter
	 */
	protected void addDocument(Document document) {
		listDocuments.put(document.getTitle(), document);
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
		return listDocuments.get(urlDocument);
	}

	/**
	 * Retourne le nombre de documents dans l'index
	 * 
	 * @return le nombre de documents dans l'index
	 */
	public int getNbDocuments() {
		return listDocuments.size();
	}

	/**
	 * Finalise la norme de tous les documents de l'index
	 */
	protected void finalizeNorm() {
		for (Document doc : listDocuments.values())
			doc.finalizeNorm();
	}

	/**
	 * Ajoute un terme à l'index
	 * 
	 * @param term
	 *            le terme à ajouter à l'index
	 */
	protected abstract void addTerm(String term);

	/**
	 * Ajoute un document à un terme. S'il existe déjà, son nombre d'occurrence
	 * est incrémenté. Sinon le document est créé
	 * 
	 * @param term
	 *            le terme auquel ajouter le document
	 * @param document
	 *            le document à ajouter au terme
	 */
	protected abstract void addDocumentTerm(String term, Document document);

	/**
	 * Modifie le poids d'un terme d'un document
	 * 
	 * @param term
	 *            le terme
	 * @param urlDocument
	 *            l'url du document
	 * @param weight
	 *            le poids du terme dans le document
	 */
	protected abstract void setWeight(String term, String urlDocument, double weight);

	/**
	 * Retourne le nombre d'occurrences du terme dans le document
	 * 
	 * @param term
	 *            le terme voulu
	 * @param urlDocument
	 *            l'url du document
	 */
	public abstract int getNbOccurrencesTermDocument(String term, String urlDocument);

	/**
	 * Retourne le nombre de documents dans lesquels ce trouve le terme
	 * 
	 * @param term
	 *            le terme
	 * @return le nombre de documents dans lesquels ce trouve le terme
	 */
	public abstract int getNbDocumentsTerm(String term);

	/**
	 * Retourne le poids d'un terme dans un document, 0 si le terme n'existe pas
	 * dans l'index ou le document spécifié.
	 * 
	 * @param term
	 *            le terme
	 * @param urlDocument
	 *            l'url du document
	 * @return le poids d'un terme dans un document
	 */
	public abstract double getWeight(String term, String urlDocument);

	/**
	 * Returne un set des termes de l'index
	 * 
	 * @return un set des termes de l'index
	 */
	public abstract Set<String> getTermsIndex();

	/**
	 * Returne un set des documents de l'index
	 * 
	 * @return un set des documents de l'index
	 */
	public abstract Set<String> getDocumentsTerm(String term);
}
