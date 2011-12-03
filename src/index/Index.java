package index;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * Un index abstrait
 */
public abstract class Index implements Serializable {

	private static final long serialVersionUID = 6230262995907224682L;
	/** La liste des documents indexés. */
	private ArrayList<Document> listDocuments;
	/** Le nombre minimal de documents par terme. */
	protected int minDocsCountByTerm;

	/**
	 * Construit un Index vide.
	 */
	protected Index() {
		listDocuments = new ArrayList<Document>();
	}

	/**
	 * Prépare l'index pour accueillir au moins le nombre de documents indiqués.
	 * 
	 * L'appel à cette méthode est facultatif mais peut améliorer les
	 * performances.
	 * 
	 * @param nbDocuments
	 *            le nombre de documents qui vont probablement être ajoutés à
	 *            l'index
	 */
	protected void prepareIndex(int nbDocuments) {
		listDocuments.ensureCapacity(nbDocuments);
	}

	/**
	 * Ajoute un document à la liste de documents.
	 * 
	 * @param document
	 *            le document à ajouter
	 */
	protected void addDocument(Document document) {
		listDocuments.add(document);
	}

	/**
	 * Retourne la liste des documents indexés.
	 * 
	 * @param idDocument
	 *            l'id du document à retourner
	 * @return le document correspondant à l'id fournie s'il existe, null sinon
	 */
	public Document getDocument(int idDocument) {
		return listDocuments.get(idDocument);
	}

	/**
	 * Retourne le nombre de documents dans l'index.
	 * 
	 * @return le nombre de documents dans l'index
	 */
	public int getNbDocuments() {
		return listDocuments.size();
	}

	/**
	 * Finalise la norme de tous les documents de l'index.
	 */
	protected void finalizeNorm() {
		for (Document doc : listDocuments)
			doc.finalizeNorm();
	}

	/**
	 * Ajoute un terme à l'index.
	 * 
	 * @param term
	 *            le terme à ajouter à l'index
	 */
	protected abstract void addTerm(String term);

	/**
	 * Ajoute un document à un terme. S'il existe déjà, son nombre d'occurrence
	 * est incrémenté. Sinon le document est créé.
	 * 
	 * @param term
	 *            le terme auquel ajouter le document
	 * @param document
	 *            le document à ajouter au terme
	 */
	protected abstract void addDocumentTerm(String term, Document document);

	/**
	 * Modifie le poids d'un terme d'un document.
	 * 
	 * @param term
	 *            le terme
	 * @param document
	 *            le document
	 * @param weight
	 *            le poids du terme dans le document
	 */
	protected abstract void setWeight(String term, Document document, double weight);

	/**
	 * Retourne le nombre d'occurrences du terme dans le document.
	 * 
	 * @param term
	 *            le terme voulu
	 * @param document
	 *            le document
	 */
	public abstract int getNbOccurrencesTermDocument(String term, Document document);

	/**
	 * Retourne le nombre de documents dans lesquels se trouve le terme.
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
	 * @param document
	 *            le document
	 * @return le poids d'un terme dans un document
	 */
	public abstract double getWeight(String term, Document document);

	/**
	 * Returne la liste des termes de l'index.
	 * 
	 * @return la liste des termes de l'index
	 */
	public abstract Set<String> getTermsIndex();

	/**
	 * Returne la liste des termes de l'index commençant par le préfixe fourni.
	 * 
	 * @return la liste des termes de l'index commençant par le préfixe fourni.
	 * @throws UnsupportedOperationException
	 *             si l'index ne supporte pas la recherche par préfixe.
	 */
	public abstract Set<String> getTermsIndex(String prefix) throws UnsupportedOperationException;

	/**
	 * Returne la liste des documents contenant un certain terme.
	 * 
	 * @return la liste des documents contenant un certain terme.
	 */
	public abstract Set<Document> getDocumentsTerm(String term);

	/**
	 * Retourne le nombre minimal de documents par terme.
	 * 
	 * @return le nombre minimal de documents par terme.
	 */
	public int getMinDocsCountByTerm() {
		return minDocsCountByTerm;
	}

	/**
	 * Met à jour le nombre minimal de documents par terme.
	 */
	public abstract void updateMinDocsCountByTerm();

	/**
	 * Charge un index depuis un fichier dont le nom est fourni. Cette méthode
	 * retourne null si le fichier indiqué ne correspondait pas à un index.
	 * 
	 * @param fileName
	 *            le nom du fichier depuis lequel l'index doit être chargé.
	 * @throws IOException
	 *             si la lecture du fichier a échoué.
	 * @return l'index chargé depuis le fichier ou null si le fichier ne
	 *         correspondait pas à un index.
	 */
	public static Index load(String fileName) throws IOException {
		FileInputStream fis = new FileInputStream(fileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ObjectInputStream ois = new ObjectInputStream(bis);

		try {
			return (Index) ois.readObject();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * Exporte l'index dans un fichier dont le nom est fourni.
	 * 
	 * @param fileName
	 *            le nom du fichier dans lequel l'index doit être sauvegardé.
	 * @throws IOException
	 *             si l'écriture du fichier a échoué.
	 */
	public void export(String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(this);
		oos.close();
	}
}
