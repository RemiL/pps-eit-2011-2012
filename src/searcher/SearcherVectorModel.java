package searcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.HashSet;

import index.Index;
import tools.normalizer.Normalizer;
import tools.ponderateur.Ponderateur;

/**
 * Un searcher qui utilise le modèle vectoriel pour trouver les documents les
 * plus pertinant
 */
public class SearcherVectorModel extends Searcher {

	/** Le pondérateur pour traiter la requête */
	Ponderateur ponderateur;

	/**
	 * Construit un searcher utilisant le modèle vectoriel avec un normalizer et
	 * un pondérateur pour traiter la requête et un index à questionner
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requête
	 * @param ponderateur
	 *            le pondérateur pour traiter la requête
	 * @param index
	 *            l'index à questionner
	 */
	public SearcherVectorModel(Normalizer normalizer, Ponderateur ponderateur, Index index) {
		super(normalizer, index);
		this.ponderateur = ponderateur;
	}

	@Override
	public LinkedList<Result> search(String request, boolean ignoreStopWords, int nbResultats) {
		LinkedList<Result> results = new LinkedList<Result>();

		ArrayList<String> wordsReq = normalizer.normalize(request, ignoreStopWords);
		double[] weightsReq = new double[wordsReq.size()];
		HashSet<String> docs = new HashSet<String>();

		for (int i = 0; i < weightsReq.length; i++) {
			// On calcule le poids du terme dans la requête
			weightsReq[i] = ponderateur.calculateWeight(wordsReq.get(i), wordsReq, index);
			// On cherche dans l'index la liste des documents contenant
			// le terme et on les ajoute à la liste de tous les documents
			// concernés par la requête.
			docs.addAll(index.getDocumentsTerm(wordsReq.get(i)));
		}

		double[] weightsDoc = new double[wordsReq.size()];
		double similarity;

		// Pour chaque document contenant au moins un terme de la requête
		for (String doc : docs) {
			// On cherche le poids de chacun des termes dans l'index
			for (int i = 0; i < weightsDoc.length; i++) {
				weightsDoc[i] = index.getWeight(wordsReq.get(i), doc);
			}
			// On calcule la similarité cosinus entre les deux vecteurs
			similarity = cosinusSimilarity(weightsReq, weightsReq);

			if (similarity != 0) {
				results.add(new Result(index.getDocument(doc), similarity));
			}
		}
		
		// TODO : optimiser le code qui suit ?
		// On trie la liste des résultats.
		Collections.sort(results);
		// On ne conserve que le nombre désiré de résultats
		if (nbResultats != -1 && results.size() > nbResultats) {
			for (int i = results.size() - nbResultats; i > 0; i--) {
				results.removeFirst();
			}
		}

		return results;
	}

	private double cosinusSimilarity(double[] weights1, double[] weights2) {
		double sim = 0;
		double sumSquare1 = 0;
		double sumSquare2 = 0;

		for (int i = 0; i < weights1.length; i++) {
			sim += weights1[i] * weights2[i];
			sumSquare1 += weights1[i] * weights1[i];
			sumSquare2 += weights2[i] * weights2[i];
		}

		sim /= Math.sqrt(sumSquare1 * sumSquare2);

		return sim;
	}
}
