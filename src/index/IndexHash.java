package index;

import java.util.HashMap;

public class IndexHash extends Index {

	HashMap<String, HashMap<String, CoupleOccurrencePoids>> index;
	
	/**
	 * Construit un index vide
	 */
	public IndexHash()
	{
		super();
		index = new HashMap<String, HashMap<String, CoupleOccurrencePoids>>();
	}

	@Override
	protected void addTerme(String terme) {
		index.put(terme, new HashMap<String, CoupleOccurrencePoids>());
	}

	@Override
	public void addDocumentTerme(String terme, Document document) {
		
		if(!index.containsKey(terme))
			addTerme(terme);
		if(!listeDocuments.containsKey(document.getUrl()))
			addDocument(document);
			
		HashMap<String, CoupleOccurrencePoids> liste = index.get(terme);
			
		if(liste.containsKey(document.getUrl()))
			liste.get(document.getUrl()).setNbOccurrences(liste.get(document.getUrl()).getNbOccurrences() + 1);
		else
			liste.put(document.getUrl(), new CoupleOccurrencePoids(1, 0));		
	}

	@Override
	public int getNbDocumentsTerme(String terme) {
		return index.get(terme).size();
	}

	@Override
	public int getNbOccurrencesTermeDocument(String terme, String urlDocument) {
		return index.get(terme).get(urlDocument).getNbOccurrences();
	}

	@Override
	public double getPoids(String terme, String urlDocument) {
		return index.get(terme).get(urlDocument).getPoids();
	}

	@Override
	public void setPoids(String terme, String urlDocument, double poids) {
		index.get(terme).get(urlDocument).setPoids(poids);
	}
}
