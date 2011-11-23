package index;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * Un index abstrait
 */
public abstract class Index implements Serializable {

	private static final long serialVersionUID = 6230262995907224682L;
	/** Table de hashage qui à une url associe un document */
	protected HashMap<Integer, Document> listDocuments;

	/**
	 * Construit un Index vide
	 */
	protected Index() {
		listDocuments = new HashMap<Integer, Document>();
	}

	/**
	 * Ajoute un document à la liste de documents
	 * 
	 * @param document
	 *            le document à ajouter
	 */
	protected void addDocument(Document document) {
		listDocuments.put(document.getId(), document);
	}

	/**
	 * Retourne le document correspondant à l'URL fournie s'il existe, sinon
	 * retourne null.
	 * 
	 * @param idDocument
	 *            l'id du document à retourner
	 * @return le document correspondant à l'id fournie s'il existe, null sinon
	 */
	public Document getDocument(int idDocument) {
		return listDocuments.get(idDocument);
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
	 * @param idDocument
	 *            l'id du document
	 * @param weight
	 *            le poids du terme dans le document
	 */
	protected abstract void setWeight(String term, int idDocument, double weight);

	/**
	 * Retourne le nombre d'occurrences du terme dans le document
	 * 
	 * @param term
	 *            le terme voulu
	 * @param idDocument
	 *            l'id du document
	 */
	public abstract int getNbOccurrencesTermDocument(String term, int idDocument);

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
	 * @param idDocument
	 *            l'id du document
	 * @return le poids d'un terme dans un document
	 */
	public abstract double getWeight(String term, int idDocument);

	/**
	 * Returne un set des termes de l'index
	 * 
	 * @return un set des termes de l'index
	 */
	public abstract Set<String> getTermsIndex();

	/**
	 * Returne un set des id des documents d'un terme
	 * 
	 * @return un set des id des documents d'un terme
	 */
	public abstract Set<Integer> getDocumentsTerm(String term);
}
