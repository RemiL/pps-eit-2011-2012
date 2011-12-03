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
	/** La liste des documents index�s. */
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
	 * Pr�pare l'index pour accueillir au moins le nombre de documents indiqu�s.
	 * 
	 * L'appel � cette m�thode est facultatif mais peut am�liorer les
	 * performances.
	 * 
	 * @param nbDocuments
	 *            le nombre de documents qui vont probablement �tre ajout�s �
	 *            l'index
	 */
	protected void prepareIndex(int nbDocuments) {
		listDocuments.ensureCapacity(nbDocuments);
	}

	/**
	 * Ajoute un document � la liste de documents.
	 * 
	 * @param document
	 *            le document � ajouter
	 */
	protected void addDocument(Document document) {
		listDocuments.add(document);
	}

	/**
	 * Retourne la liste des documents index�s.
	 * 
	 * @param idDocument
	 *            l'id du document � retourner
	 * @return le document correspondant � l'id fournie s'il existe, null sinon
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
	 * Ajoute un terme � l'index.
	 * 
	 * @param term
	 *            le terme � ajouter � l'index
	 */
	protected abstract void addTerm(String term);

	/**
	 * Ajoute un document � un terme. S'il existe d�j�, son nombre d'occurrence
	 * est incr�ment�. Sinon le document est cr��.
	 * 
	 * @param term
	 *            le terme auquel ajouter le document
	 * @param document
	 *            le document � ajouter au terme
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
	 * dans l'index ou le document sp�cifi�.
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
	 * Returne la liste des termes de l'index commen�ant par le pr�fixe fourni.
	 * 
	 * @return la liste des termes de l'index commen�ant par le pr�fixe fourni.
	 * @throws UnsupportedOperationException
	 *             si l'index ne supporte pas la recherche par pr�fixe.
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
	 * Met � jour le nombre minimal de documents par terme.
	 */
	public abstract void updateMinDocsCountByTerm();

	/**
	 * Charge un index depuis un fichier dont le nom est fourni. Cette m�thode
	 * retourne null si le fichier indiqu� ne correspondait pas � un index.
	 * 
	 * @param fileName
	 *            le nom du fichier depuis lequel l'index doit �tre charg�.
	 * @throws IOException
	 *             si la lecture du fichier a �chou�.
	 * @return l'index charg� depuis le fichier ou null si le fichier ne
	 *         correspondait pas � un index.
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
	 *            le nom du fichier dans lequel l'index doit �tre sauvegard�.
	 * @throws IOException
	 *             si l'�criture du fichier a �chou�.
	 */
	public void export(String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(this);
		oos.close();
	}
}
