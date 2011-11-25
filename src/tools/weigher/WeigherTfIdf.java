package tools.weigher;

import java.util.ArrayList;
import java.util.Collections;

import index.Document;
import index.Index;

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
	public double calculateWeight(String word, ArrayList<String> wordsDoc, Index index) {
		return Collections.frequency(wordsDoc, word)
				* Math.log10(index.getNbDocuments() / index.getNbDocumentsTerm(word));
	}
}
