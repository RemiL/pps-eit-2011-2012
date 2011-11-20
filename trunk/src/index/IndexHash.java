package index;

import java.util.HashMap;
import java.util.Set;

/**
 * Un index sous forme de table de hashage
 */
public class IndexHash extends Index {

	HashMap<String, HashMap<String, CoupleOccurrenceWeight>> index;

	/**
	 * Construit un index vide
	 */
	public IndexHash() {
		super();
		index = new HashMap<String, HashMap<String, CoupleOccurrenceWeight>>();
	}

	@Override
	protected void addTerm(String term) {
		index.put(term, new HashMap<String, CoupleOccurrenceWeight>());
	}

	@Override
	public void addDocumentTerm(String term, Document document) {

		if (!index.containsKey(term))
			addTerm(term);
		if (!listDocuments.containsKey(document.getUrl()))
			addDocument(document);

		HashMap<String, CoupleOccurrenceWeight> liste = index.get(term);

		if (liste.containsKey(document.getUrl()))
			liste.get(document.getUrl()).setNbOccurrences(liste.get(document.getUrl()).getNbOccurrences() + 1);
		else
			liste.put(document.getUrl(), new CoupleOccurrenceWeight(1, 0));
	}

	@Override
	public int getNbDocumentsTerm(String term) {
		return index.get(term).size();
	}

	@Override
	public int getNbOccurrencesTermDocument(String term, String urlDocument) {
		return index.get(term).get(urlDocument).getNbOccurrences();
	}

	@Override
	public double getWeight(String term, String urlDocument) {
		try {
			return index.get(term).get(urlDocument).getWeight();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	@Override
	public void setWeight(String term, String urlDocument, double weight) {
		index.get(term).get(urlDocument).setWeight(weight);
	}

	@Override
	public Set<String> getDocumentsTerm(String term) {
		return index.get(term).keySet();
	}

	@Override
	public Set<String> getTermsIndex() {
		return index.keySet();
	}
}
