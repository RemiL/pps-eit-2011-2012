package index;

import java.util.HashMap;
import java.util.HashSet;
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

		int nbOcc = 1;
		if (liste.containsKey(document)) {
			nbOcc = liste.get(document).getNbOccurrences() + 1;
			liste.get(document).setNbOccurrences(nbOcc);
		} else {
			liste.put(document, new PairOccurrenceWeight(1, 0));
		}

		document.updateMaxTermFrequency(nbOcc);
	}

	@Override
	public int getNbDocumentsTerm(String term) {
		try {
			return index.get(term).size();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	@Override
	public int getNbOccurrencesTermDocument(String term, Document document) {
		try {
			return index.get(term).get(document).getNbOccurrences();
		} catch (NullPointerException e) {
			return 0;
		}
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
		try {
			return index.get(term).keySet();
		} catch (NullPointerException e) {
			return new HashSet<Document>();
		}
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

	@Override
	public void updateMinDocsCountByTerm() {
		minDocsCountByTerm = Integer.MAX_VALUE;

		for (HashMap<Document, PairOccurrenceWeight> v : index.values()) {
			if (minDocsCountByTerm > v.size()) {
				minDocsCountByTerm = v.size();
			}
		}
	}
}
