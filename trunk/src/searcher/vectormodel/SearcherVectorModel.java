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
 * Un searcher qui utilise le mod�le vectoriel pour trouver les documents les
 * plus pertinant
 */
public class SearcherVectorModel extends Searcher {

	/** Le pond�rateur pour traiter la requ�te. */
	protected Weigher weigher;

	/**
	 * Construit un searcher utilisant le mod�le vectoriel avec un normalizer et
	 * un pond�rateur pour traiter la requ�te et un index � questionner.
	 * 
	 * @param normalizer
	 *            le normalizer pour traiter la requ�te.
	 * @param weigher
	 *            le pond�rateur pour traiter la requ�te.
	 * @param index
	 *            l'index � questionner.
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

		// Pr�pare la recherche.
		double normQuery = setupSearch(wordsQuery, weightsQuery, docs);

		// Calcule les r�sultats pour chaque document.
		populateResults(results, wordsQuery, weightsQuery, normQuery, docs);

		// Trie la liste des r�sultats et on ne garde
		// que le nombre d�sir� de meilleurs r�sultats.
		sortResults(results, nbResultats);

		return results;
	}

	/**
	 * Pr�pare la requ�te (notamment en la normalisant).
	 * 
	 * @param request
	 *            la requ�te � pr�parer.
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent �tre ignor�s ou non dans la
	 *            requ�te.
	 * @return la requ�te pr�par�e.
	 */
	protected LinkedList<String> prepareQuery(String request, boolean ignoreStopWords) {
		return new LinkedList<String>(normalizer.normalize(request, ignoreStopWords));
	}

	/**
	 * Pr�pare la recherche en calculant le poid des termes de la requ�te et sa
	 * norme ainsi qu'en listant les documents concern�s.
	 * 
	 * @param wordsQuery
	 *            les termes composants la requ�te.
	 * @param weightsQuery
	 *            la liste � remplir des poids de la requ�te.
	 * @param docs
	 *            la liste � remplir des documents concern�s par la requ�te.
	 * @return la norme de la requ�te.
	 */
	protected double setupSearch(List<String> wordsQuery, double[] weightsQuery, Set<Document> docs) {
		double normQuery = 0;

		for (int i = 0; i < weightsQuery.length; i++) {
			// On calcule le poids du terme dans la requ�te
			weightsQuery[i] = weigher.calculateQueryWeight(wordsQuery.get(i), wordsQuery, index);
			// On met � jour la norme.
			normQuery += weightsQuery[i] * weightsQuery[i];
			// On cherche dans l'index la liste des documents contenant
			// le terme et on les ajoute � la liste de tous les documents
			// concern�s par la requ�te.
			docs.addAll(index.getDocumentsTerm(wordsQuery.get(i)));
		}

		// Finalisation du calcul de la norme.
		return Math.sqrt(normQuery);
	}

	/**
	 * Peuple la liste des r�sultats � partir de la liste des documents �
	 * consid�rer et de la requ�te.
	 * 
	 * @param results
	 *            la liste des r�sultats � peupler.
	 * @param wordsQuery
	 *            les termes composants la requ�te.
	 * @param weightsQuery
	 *            les poids des termes de la requ�te.
	 * @param normQuery
	 *            la norme de la requ�te.
	 * @param docs
	 *            la liste des documents � consid�rer.
	 */
	protected void populateResults(List<Result> results, List<String> wordsQuery, double[] weightsQuery,
			double normQuery, Set<Document> docs) {
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
	}

	/**
	 * Calcule la similarit� cosinus entre les deux vecteurs fournis.
	 * 
	 * @param weights1
	 *            le premier vecteur.
	 * @param norm1
	 *            la norme du premier vecteur.
	 * @param weights2
	 *            le deuxi�me vecteur.
	 * @param norm2
	 *            la norme du deuxi�me vecteur.
	 * @return la similarit� cosinus entre les deux vecteurs fournis.
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
	 * Trie la liste de r�sultat fournie par ordre de pertinance croissante en
	 * conservant uniquement le nombre de meilleurs r�sultats d�sir�s (qui peut
	 * �tre non limit� si la constante ALL_RESULTS est utilis�e).
	 * 
	 * @param results
	 *            la liste de r�sultat � trier.
	 * @param nbResultats
	 *            le nombre de meilleurs r�sultats � conserver (�ventuellement
	 *            ALL_RESULTS si on veut conserver tous les r�sultats).
	 */
	protected void sortResults(LinkedList<Result> results, int nbResultats) {
		// TODO : optimiser le code qui suit ?
		// On trie la liste des r�sultats.
		Collections.sort(results);
		// On ne conserve que le nombre d�sir� de r�sultats
		if (nbResultats != ALL_RESULTS && results.size() > nbResultats) {
			for (int i = results.size() - nbResultats; i > 0; i--) {
				results.removeLast();
			}
		}
	}
}
