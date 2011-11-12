package index;

import java.util.HashMap;

public abstract class Index {
	
	/** Table de hashage qui � une url associe un document */
	protected HashMap<String, Document> listeDocuments;
	
	/**
	 * Construit un Index vide
	 */
	public Index()
	{
		listeDocuments = new HashMap<String, Document>();
	}
	
	/**
	 * Ajoute un document � la liste de documents
	 * @param document le document � ajouter
	 */
	protected void addDocument(Document document)
	{
		listeDocuments.put(document.getTitre(), document);
	}
	
	/** 
	 * Ajoute un terme � l'index
	 * @param terme le terme � ajouter � l'index
	 */
	protected abstract void addTerme(String terme);
	
	/**
	 * Ajoute un document � un terme. S'il existe d�j�, son nombre d'occurrence est incr�ment�. Sinon le document est cr��
	 * @param terme le terme auquel ajouter le document
	 * @param document le document � ajouter au terme
	 */
	public abstract void addDocumentTerme(String terme, Document document);
	
	/**
	 * Modifie le poids d'un terme d'un document
	 * @param terme le terme
	 * @param urlDocument l'url du document
	 */
	public abstract void setPoids(String terme, String urlDocument, int poids);
	
	/**
	 * Retourne le nombre d'occurrences du terme dans le document
	 * @param terme le terme voulu
	 * @param urlDocument l'url du document
	 */
	public abstract int getNbOccurrencesTermeDocument(String terme, String urlDocument);
	
	/**
	 * Retourne le nombre de documents dans lesquels ce trouve le terme
	 * @param terme le terme
	 * @return le nombre de documents dans lesquels ce trouve le terme
	 */
	public abstract int getNbDocumentsTerme(String terme);
	
	/**
	 * Retourne le poids d'un terme dans un document
	 * @param terme le terme
	 * @param urlDocument l'url du document
	 * @return le poids d'un terme dans un document
	 */
	public abstract double getPoids(String terme, String urlDocument);
}
