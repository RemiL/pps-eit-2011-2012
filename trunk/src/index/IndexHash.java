package index;

import java.util.HashMap;
import java.util.Set;

/**
 * Un index sous forme de table de hashage
 */
public class IndexHash extends Index {

	private static final long serialVersionUID = 6548144981533424715L;
	/** L'index */
	private HashMap<String, HashMap<Integer, PairOccurrenceWeight>> index;

	/**
	 * Construit un index vide
	 */
	public IndexHash() {
		super();
		index = new HashMap<String, HashMap<Integer, PairOccurrenceWeight>>();
	}

	@Override
	protected void addTerm(String term) {
		index.put(term, new HashMap<Integer, PairOccurrenceWeight>());
	}

	@Override
	public void addDocumentTerm(String term, Document document) {

		if (!index.containsKey(term))
			addTerm(term);
		if (!listDocuments.containsKey(document.getId()))
			addDocument(document);

		HashMap<Integer, PairOccurrenceWeight> liste = index.get(term);

		if (liste.containsKey(document.getId()))
			liste.get(document.getId()).setNbOccurrences(liste.get(document.getId()).getNbOccurrences() + 1);
		else
			liste.put(document.getId(), new PairOccurrenceWeight(1, 0));
	}

	@Override
	public int getNbDocumentsTerm(String term) {
		return index.get(term).size();
	}

	@Override
	public int getNbOccurrencesTermDocument(String term, int idDocument) {
		return index.get(term).get(idDocument).getNbOccurrences();
	}

	@Override
	public double getWeight(String term, int idDocument) {
		try {
			return index.get(term).get(idDocument).getWeight();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	@Override
	public void setWeight(String term, int idDocument, double weight) {
		index.get(term).get(idDocument).setWeight(weight);
	}

	@Override
	public Set<Integer> getDocumentsTerm(String term) {
		return index.get(term).keySet();
	}

	@Override
	public Set<String> getTermsIndex() {
		return index.keySet();
	}
}
