package tools.weigher;

import index.Document;
import index.Index;

import java.util.List;

public class WeigherTfIdfLog implements Weigher {

	@Override
	public double calculateWeight(String term, Document document, Index index) {
		return logTf(index.getNbOccurrencesTermDocument(term, document)) * Math.log10((double) index.getNbDocuments() / index.getNbDocumentsTerm(term));
	}

	@Override
	public double calculateQueryWeight(String word, List<String> wordsQuery, Index index) {
		return Math.log10((double) (index.getNbDocuments() + 1) / (index.getNbDocumentsTerm(word) + 1));
	}
	
	protected double logTf(int tf) {
		if (tf == 0)
			return 0;
		else
			return Math.log10(tf) + 1;
	}
}
