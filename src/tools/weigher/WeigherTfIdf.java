package tools.weigher;

import index.Document;
import index.Index;

import java.util.List;

/**
 * Un pondérateur utilisant la méthode tf.idf.
 */
public class WeigherTfIdf implements Weigher {

	@Override
	public double calculateWeight(String term, Document document, Index index) {
		return index.getNbOccurrencesTermDocument(term, document)
				* Math.log10((double) index.getNbDocuments() / index.getNbDocumentsTerm(term));
	}

	@Override
	public double calculateQueryWeight(String word, List<String> wordsQuery, Index index) {
		return Math.log10((double) (index.getNbDocuments() + 1) / (index.getNbDocumentsTerm(word) + 1));
	}
}
