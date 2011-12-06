package tools.weigher;

import index.Document;
import index.Index;

import java.util.Collections;
import java.util.List;

public class WeigherTfIdfLogNorm extends WeigherTfIdfLog {

	@Override
	public double calculateWeight(String term, Document document, Index index) {
		return super.calculateWeight(term, document, index)
				/ (logTf(document.getMaxTermFrequency()) * Math.log10((double) index.getNbDocuments()
						/ index.getMinDocsCountByTerm()));
	}

	@Override
	public double calculateQueryWeight(String word, List<String> wordsQuery, Index index) {
		int queryTermFrequency, queryMaxTermFrequency = 1;
		for (String term : wordsQuery) {
			queryTermFrequency = Collections.frequency(wordsQuery, term);
			if (queryMaxTermFrequency < queryTermFrequency) {
				queryMaxTermFrequency = queryTermFrequency;
			}
		}

		return super.calculateQueryWeight(word, wordsQuery, index)
				/ (queryMaxTermFrequency * Math.log10((double) (index.getNbDocuments() + 1)
						/ index.getMinDocsCountByTerm()));
	}
}
