package tools.ponderateur;

import index.Index;

/**
 * Un pond�rateur utilisant la m�thode tf.idf
 */
public class PonderateurTfIdf implements Ponderateur {

	@Override
	public double calculerPoids(String terme, String urlDocument, Index index) {
		return index.getNbOccurrencesTermeDocument(terme, urlDocument)
				* Math.log10(index.getNbDocuments() / index.getNbDocumentsTerme(terme));
	}
}
