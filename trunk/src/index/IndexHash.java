package index;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe repr�sentant un index bas� sur une table de hachage.
 */
public class IndexHash extends Index {

	private static final long serialVersionUID = 6548144981533424715L;
	/** L'index */
	private HashMap<String, HashMap<Document, PairOccurrenceWeight>> index;

	/**
	 * Construit un index vide bas� sur une table de hachage.
	 */
	public IndexHash() {
		super();
		index = new HashMap<String, HashMap<Document, PairOccurrenceWeight>>();
	}

	@Override
	protected void addTerm(String term) {
		index.put(term, new HashMap<Document, PairOccurrenceWeight>());
	}

	@Override
	public void addDocumentTerm(String term, Document document) {

		if (!index.containsKey(term)) {
			addTerm(term);
		}
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
		// IndexHash ne supporte pas les recherches par pr�fixe.
		throw new UnsupportedOperationException("La recherche par pr�fixe n'est pas support�e par IndexHash");
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
