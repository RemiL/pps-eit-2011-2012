package tools.ponderateur;

import java.util.ArrayList;
import java.util.Collections;

import index.Index;

/**
 * Un pondérateur utilisant la méthode tf.idf
 */
public class PonderateurTfIdf implements Ponderateur {

	@Override
	public double calculerPoids(String terme, String urlDocument, Index index) {
		return index.getNbOccurrencesTermeDocument(terme, urlDocument)
				* Math.log10(index.getNbDocuments() / index.getNbDocumentsTerme(terme));
	}

	@Override
	public double calculerPoids(String word, ArrayList<String> wordsDoc, Index index) {
		return Collections.frequency(wordsDoc, word)
				* Math.log10(index.getNbDocuments() / index.getNbDocumentsTerme(word));
	}
}
