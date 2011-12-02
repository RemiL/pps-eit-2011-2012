package index;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;

/**
 * Classe représentant un index basé sur un arbre-B.
 */
public class IndexTree extends Index {

	private static final long serialVersionUID = 6548144981533424715L;
	/** L'index */
	private TreeMap<String, HashMap<Document, PairOccurrenceWeight>> index;

	/**
	 * Construit un index vide basé sur un arbre-B.
	 */
	public IndexTree() {
		super();
		index = new TreeMap<String, HashMap<Document, PairOccurrenceWeight>>();
	}

	@Override
	protected void addTerm(String term) {
		index.put(term, new HashMap<Document, PairOccurrenceWeight>());
	}

	@Override
	public void addDocumentTerm(String term, Document document) {

		if (!index.containsKey(term))
			addTerm(term);
		/*
		 * TODO : faire quelque chose ?! if
		 * (!listDocuments.containsKey(document.getId())) addDocument(document);
		 */

		HashMap<Document, PairOccurrenceWeight> liste = index.get(term);

		if (liste.containsKey(document))
			liste.get(document).setNbOccurrences(liste.get(document).getNbOccurrences() + 1);
		else
			liste.put(document, new PairOccurrenceWeight(1, 0));
	}

	@Override
	public int getNbDocumentsTerm(String term) {
		return index.get(term).size();
	}

	@Override
	public int getNbOccurrencesTermDocument(String term, Document document) {
		return index.get(term).get(document).getNbOccurrences();
	}

	@Override
	public double getWeight(String term, Document document) {
		try {
			return index.get(term).get(document).getWeight();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	@Override
	public void setWeight(String term, Document document, double weight) {
		index.get(term).get(document).setWeight(weight);
	}

	@Override
	public Set<Document> getDocumentsTerm(String term) {
		return index.get(term).keySet();
	}

	@Override
	public Set<String> getTermsIndex() {
		return index.keySet();
	}

	@Override
	public Set<String> getTermsIndex(String prefix) throws UnsupportedOperationException {
		// On cherche la lettre suivant la dernière lettre du préfixe
		char nextLetter = (char) (prefix.charAt(prefix.length() - 1) + 1);
		// pour en déduire la borne de fin des termes à retourner.
		String end = prefix.substring(0, prefix.length() - 1) + nextLetter;

		return index.subMap(prefix, true, end, false).keySet();
	}
}
