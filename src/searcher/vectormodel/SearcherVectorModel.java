package searcher.vectormodel;

import index.Document;
import index.Index;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import searcher.Result;
import searcher.Searcher;
import tools.normalizer.Normalizer;
import tools.weigher.Weigher;

/**
 * Un searcher qui utilise le modèle vectoriel pour trouver les documents les
 * plus pertinant
 */
public class SearcherVectorModel extends Searcher {

	/** Le pondérateur pour traiter la requête. */
	protected Weigher weigher;

	/**
	 * Construit un searcher utilisant le modèle vectoriel avec un normalizer et
	 * un pondérateur pour traiter la requête et un index à questionner.
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requête.
	 * @param weigher
	 *            le pondérateur pour traiter la requête.
	 * @param index
	 *            l'index à questionner.
	 */
	public SearcherVectorModel(Normalizer normalizer, Weigher weigher, Index index) {
		super(normalizer, index);
		this.weigher = weigher;
	}

	@Override
	public LinkedList<Result> search(String request, boolean ignoreStopWords, int nbResultats) {
		LinkedList<Result> results = new LinkedList<Result>();

		LinkedList<String> wordsQuery = prepareQuery(request, ignoreStopWords);
		double[] weightsQuery = new double[wordsQuery.size()];
		HashSet<Document> docs = new HashSet<Document>();

		// Prépare la recherche.
		double normQuery = setupSearch(wordsQuery, weightsQuery, docs);

		// Calcule les résultats pour chaque document.
		populateResults(results, wordsQuery, weightsQuery, normQuery, docs);

		// Trie la liste des résultats et on ne garde
		// que le nombre désiré de meilleurs résultats.
		sortResults(results, nbResultats);

		return results;
	}

	/**
	 * Prépare la requête (notamment en la normalisant).
	 * 
	 * @param request
	 *            la requête à préparer.
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent être ignorés ou non dans la
	 *            requête.
	 * @return la requête préparée.
	 */
	protected LinkedList<String> prepareQuery(String request, boolean ignoreStopWords) {
		return new LinkedList<String>(normalizer.normalize(request, ignoreStopWords));
	}

	/**
	 * Prépare la recherche en calculant le poid des termes de la requête et sa
	 * norme ainsi qu'en listant les documents concernés.
	 * 
	 * @param wordsQuery
	 *            les termes composants la requête.
	 * @param weightsQuery
	 *            la liste à remplir des poids de la requête.
	 * @param docs
	 *            la liste à remplir des documents concernés par la requête.
	 * @return la norme de la requête.
	 */
	protected double setupSearch(List<String> wordsQuery, double[] weightsQuery, Set<Document> docs) {
		double normQuery = 0;

		for (int i = 0; i < weightsQuery.length; i++) {
			// On calcule le poids du terme dans la requête
			weightsQuery[i] = weigher.calculateQueryWeight(wordsQuery.get(i), wordsQuery, index);
			// On met à jour la norme.
			normQuery += weightsQuery[i] * weightsQuery[i];
			// On cherche dans l'index la liste des documents contenant
			// le terme et on les ajoute à la liste de tous les documents
			// concernés par la requête.
			docs.addAll(index.getDocumentsTerm(wordsQuery.get(i)));
		}

		// Finalisation du calcul de la norme.
		return Math.sqrt(normQuery);
	}

	/**
	 * Peuple la liste des résultats à partir de la liste des documents à
	 * considérer et de la requête.
	 * 
	 * @param results
	 *            la liste des résultats à peupler.
	 * @param wordsQuery
	 *            les termes composants la requête.
	 * @param weightsQuery
	 *            les poids des termes de la requête.
	 * @param normQuery
	 *            la norme de la requête.
	 * @param docs
	 *            la liste des documents à considérer.
	 */
	protected void populateResults(List<Result> results, List<String> wordsQuery, double[] weightsQuery,
			double normQuery, Set<Document> docs) {
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
	}

	/**
	 * Calcule la similarité cosinus entre les deux vecteurs fournis.
	 * 
	 * @param weights1
	 *            le premier vecteur.
	 * @param norm1
	 *            la norme du premier vecteur.
	 * @param weights2
	 *            le deuxième vecteur.
	 * @param norm2
	 *            la norme du deuxième vecteur.
	 * @return la similarité cosinus entre les deux vecteurs fournis.
	 */
	protected double cosinusSimilarity(double[] weights1, double norm1, double[] weights2, double norm2) {
		double sim = 0;

		for (int i = 0; i < weights1.length && i < weights2.length; i++) {
			sim += weights1[i] * weights2[i];
		}

		sim /= norm1 * norm2;

		return sim;
	}

	/**
	 * Trie la liste de résultat fournie par ordre de pertinance croissante en
	 * conservant uniquement le nombre de meilleurs résultats désirés (qui peut
	 * être non limité si la constante ALL_RESULTS est utilisée).
	 * 
	 * @param results
	 *            la liste de résultat à trier.
	 * @param nbResultats
	 *            le nombre de meilleurs résultats à conserver (éventuellement
	 *            ALL_RESULTS si on veut conserver tous les résultats).
	 */
	protected void sortResults(LinkedList<Result> results, int nbResultats) {
		// TODO : optimiser le code qui suit ?
		// On trie la liste des résultats.
		Collections.sort(results);
		// On ne conserve que le nombre désiré de résultats
		if (nbResultats != ALL_RESULTS && results.size() > nbResultats) {
			for (int i = results.size() - nbResultats; i > 0; i--) {
				results.removeLast();
			}
		}
	}
}
