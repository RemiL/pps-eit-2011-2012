package tools.weigher;

import index.Document;
import index.Index;

import java.util.Collections;
import java.util.List;

public class WeigherTfIdfLog implements Weigher {

	@Override
	public double calculateWeight(String term, Document document, Index index) {
		double logTf;
		if (index.getNbOccurrencesTermDocument(term, document) == 0)
			logTf = 0;
		else
			logTf = Math.log10(index.getNbOccurrencesTermDocument(term, document)) + 1;
		return logTf * Math.log10((double) index.getNbDocuments() / index.getNbDocumentsTerm(term));
	}

	@Override
	public double calculateQueryWeight(String word, List<String> wordsQuery, Index index) {
		double logTf;
		if (Collections.frequency(wordsQuery, word) == 0)
			logTf = 0;
		else
			logTf = Math.log10(Collections.frequency(wordsQuery, word)) + 1;
		return logTf * Math.log10((double) (index.getNbDocuments() + 1) / (index.getNbDocumentsTerm(word) + 1));
	}
}
