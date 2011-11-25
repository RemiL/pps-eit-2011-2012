package searcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.HashSet;

import index.Document;
import index.Index;
import tools.normalizer.Normalizer;
import tools.weigher.Weigher;

/**
 * Un searcher qui utilise le mod�le vectoriel pour trouver les documents les
 * plus pertinant
 */
public class SearcherVectorModel extends Searcher {

	/** Le pond�rateur pour traiter la requ�te */
	Weigher weigher;

	/**
	 * Construit un searcher utilisant le mod�le vectoriel avec un normalizer et
	 * un pond�rateur pour traiter la requ�te et un index � questionner
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requ�te
	 * @param weigher
	 *            le pond�rateur pour traiter la requ�te
	 * @param index
	 *            l'index � questionner
	 */
	public SearcherVectorModel(Normalizer normalizer, Weigher weigher, Index index) {
		super(normalizer, index);
		this.weigher = weigher;
	}

	@Override
	public LinkedList<Result> search(String request, boolean ignoreStopWords, int nbResultats) {
		LinkedList<Result> results = new LinkedList<Result>();

		ArrayList<String> wordsQuery = normalizer.normalize(request, ignoreStopWords);
		double[] weightsQuery = new double[wordsQuery.size()];
		double normQuery = 0;
		HashSet<Document> docs = new HashSet<Document>();

		for (int i = 0; i < weightsQuery.length; i++) {
			// On calcule le poids du terme dans la requ�te
			weightsQuery[i] = weigher.calculateWeight(wordsQuery.get(i), wordsQuery, index);
			// On met � jour la norme.
			normQuery += weightsQuery[i] * weightsQuery[i];
			// On cherche dans l'index la liste des documents contenant
			// le terme et on les ajoute � la liste de tous les documents
			// concern�s par la requ�te.
			docs.addAll(index.getDocumentsTerm(wordsQuery.get(i)));
		}
		// Finalisation du calcul de la norme.
		normQuery = Math.sqrt(normQuery);

		double[] weightsDoc = new double[wordsQuery.size()];
		double similarity;

		// Pour chaque document contenant au moins un terme de la requ�te
		for (Document doc : docs) {
			// On cherche le poids de chacun des termes dans l'index
			for (int i = 0; i < weightsDoc.length; i++) {
				weightsDoc[i] = index.getWeight(wordsQuery.get(i), doc);
			}
			// On calcule la similarit� cosinus entre les deux vecteurs
			similarity = cosinusSimilarity(weightsQuery, normQuery, weightsDoc, doc.getNorm());

			if (similarity != 0) {
				results.add(new Result(doc, similarity));
			}
		}

		// TODO : optimiser le code qui suit ?
		// On trie la liste des r�sultats.
		Collections.sort(results);
		// On ne conserve que le nombre d�sir� de r�sultats
		if (nbResultats != ALL_RESULTS && results.size() > nbResultats) {
			for (int i = results.size() - nbResultats; i > 0; i--) {
				results.removeFirst();
			}
		}

		return results;
	}

	private double cosinusSimilarity(double[] weights1, double norm1, double[] weights2, double norm2) {
		double sim = 0;

		for (int i = 0; i < weights1.length; i++) {
			sim += weights1[i] * weights2[i];
		}

		sim /= norm1 * norm2;

		return sim;
	}
}
