package tools.ponderateur;

import java.util.ArrayList;
import java.util.Collections;

import index.Index;

/**
 * Un pondérateur utilisant la méthode tf.idf
 */
public class PonderateurTfIdf implements Ponderateur {

	@Override
	public double calculateWeight(String term, String urlDocument, Index index) {
		return index.getNbOccurrencesTermDocument(term, urlDocument)
				* Math.log10(index.getNbDocuments() / index.getNbDocumentsTerm(term));
	}

	@Override
	public double calculateWeight(String word, ArrayList<String> wordsDoc, Index index) {
		return Collections.frequency(wordsDoc, word)
				* Math.log10(index.getNbDocuments() / index.getNbDocumentsTerm(word));
	}
}
