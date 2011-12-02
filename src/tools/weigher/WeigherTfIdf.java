package tools.weigher;

import index.Document;
import index.Index;

import java.util.Collections;
import java.util.List;

/**
 * Un pondérateur utilisant la méthode tf.idf.
 */
public class WeigherTfIdf implements Weigher {

	@Override
	public double calculateWeight(String term, Document document, Index index) {
		return index.getNbOccurrencesTermDocument(term, document)
				* Math.log10(index.getNbDocuments() / index.getNbDocumentsTerm(term));
	}

	@Override
	public double calculateWeight(String word, List<String> wordsDoc, Index index) {
		return Collections.frequency(wordsDoc, word)
				* Math.log10(index.getNbDocuments() / index.getNbDocumentsTerm(word));
	}
}
