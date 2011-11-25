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
 * Un searcher qui utilise le modèle vectoriel pour trouver les documents les
 * plus pertinant
 */
public class SearcherVectorModel extends Searcher {

	/** Le pondérateur pour traiter la requête */
	Weigher weigher;

	/**
	 * Construit un searcher utilisant le modèle vectoriel avec un normalizer et
	 * un pondérateur pour traiter la requête et un index à questionner
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requête
	 * @param weigher
	 *            le pondérateur pour traiter la requête
	 * @param index
	 *            l'index à questionner
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
			// On calcule le poids du terme dans la requête
			weightsQuery[i] = weigher.calculateWeight(wordsQuery.get(i), wordsQuery, index);
			// On met à jour la norme.
			normQuery += weightsQuery[i] * weightsQuery[i];
			// On cherche dans l'index la liste des documents contenant
			// le terme et on les ajoute à la liste de tous les documents
			// concernés par la requête.
			docs.addAll(index.getDocumentsTerm(wordsQuery.get(i)));
		}
		// Finalisation du calcul de la norme.
		normQuery = Math.sqrt(normQuery);

		double[] weightsDoc = new double[wordsQuery.size()];
		double similarity;

		// Pour chaque document contenant au moins un terme de la requête
		for (Document doc : docs) {
			// On cherche le poids de chacun des termes dans l'index
			for (int i = 0; i < weightsDoc.length; i++) {
				weightsDoc[i] = index.getWeight(wordsQuery.get(i), doc);
			}
			// On calcule la similarité cosinus entre les deux vecteurs
			similarity = cosinusSimilarity(weightsQuery, normQuery, weightsDoc, doc.getNorm());

			if (similarity != 0) {
				results.add(new Result(doc, similarity));
			}
		}

		// TODO : optimiser le code qui suit ?
		// On trie la liste des résultats.
		Collections.sort(results);
		// On ne conserve que le nombre désiré de résultats
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
