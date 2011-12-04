package searcher;

import index.Index;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import searcher.extendedbooleanmodel.SearcherExtendBooleanModel;
import searcher.vectormodel.SearcherVectorModel;
import searcher.vectormodel.SearcherVectorModelPrefix;
import searcher.vectormodel.SearcherVectorModelPrefixWordsExclusion;
import tools.normalizer.FrenchStemmer;
import tools.normalizer.FrenchTokenizer;
import tools.normalizer.Normalizer;
import tools.weigher.WeigherTfIdf;

import searcher.view.SearcherFrame;
import searcher.view.MenuBar.NormalizerType;
import searcher.view.MenuBar.SearcherType;

/**
 * Une classe de test pour la recherche
 */
public class SearcherMain implements ActionListener {

	/** La fen�tre */
	private SearcherFrame searcherFrame;
	/** Le searcher */
	private Searcher searcher;
	/** L'index */
	private Index index;
	/** Le normaliseur */
	private Normalizer normalizer;
	/** Faut-il supprimer les mots vides */
	private boolean ignoreStopWords;

	/**
	 * Affiche une fen�tre pour le searcher
	 */
	public SearcherMain() {
		// Cr�e une fen�tre
		searcherFrame = new SearcherFrame();
		// Ecoute le bouton de recherche
		searcherFrame.getButtonSearcher().addActionListener(this);
		// Ecoute le bouton de chargement du searcher
		searcherFrame.getMenuLoad().addActionListener(this);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SearcherMain searcherMain = new SearcherMain();
	}

	public void actionPerformed(ActionEvent arg0) {
		// Si le bouton de chargement du searcher est cliqu�
		if (arg0.getSource() == searcherFrame.getMenuLoad()) {
			// Tous les param�tres sont lus
			String indexPath = searcherFrame.getIndexPath();
			String stopWordsPath = searcherFrame.getStopWordsPath();
			SearcherType searcherType = searcherFrame.getSearcherType();
			NormalizerType normalizerType = searcherFrame.getNormalizerType();

			// Si le fichier des mots vides est renseign�, les mots vides sont �
			// supprimer
			if (stopWordsPath.equals(""))
				ignoreStopWords = false;
			else
				ignoreStopWords = true;

			try {
				// Si le type de normaliseur ou le fichier des mots vides sont modifi�s
				if (searcherFrame.isModifiedNormalizer() || searcherFrame.isModifiedStopWords()) {
					// Cr�ation du normlizer
					switch (normalizerType) {
					case TOKENIZER:
						if (ignoreStopWords)
							normalizer = new FrenchTokenizer(stopWordsPath, "UTF-8");
						else
							normalizer = new FrenchTokenizer();
						break;
					case STEMMER:
						if (ignoreStopWords)
							normalizer = new FrenchStemmer(stopWordsPath, "UTF-8");
						else
							normalizer = new FrenchStemmer();
						break;
					}
				}

				// Si le fichier d'index est modifi�
				if (searcherFrame.isModifiedIndex()) {
					long t1 = System.nanoTime();
					// Chargement de l'index depuis un fichier
					index = Index.load(indexPath, indexPath.endsWith(".zindex"));

					long t2 = System.nanoTime();
					System.out.println("Temps de d�s�rialisation : " + (t2 - t1) / 1000000.);
					
					System.out.println("Nombre de termes index�s : " + index.getTermsIndex().size());
				}

				// Si un des param�tres est modifi�, il faut recr�er le searcher
				if (searcherFrame.isModifiedSearcher() || searcherFrame.isModifiedIndex()
						|| searcherFrame.isModifiedStopWords() || searcherFrame.isModifiedNormalizer()) {
					long t1 = System.nanoTime();
					// Cr�ation du searcher
					switch (searcherType) {
					case VECT_BASIC:
						searcher = new SearcherVectorModel(normalizer, new WeigherTfIdf(), index);
						break;
					case VECT_PREFIX:
						searcher = new SearcherVectorModelPrefix(normalizer, new WeigherTfIdf(), index);
						break;
					case VECT_PREFIX_EXCLUSION:
						searcher = new SearcherVectorModelPrefixWordsExclusion(normalizer, new WeigherTfIdf(), index);
						break;
					case EXTENDED_BOOLEAN:
						searcher = new SearcherExtendBooleanModel(normalizer, index);
						break;
					}
					long t2 = System.nanoTime();
					System.out.println("Temps de cr�ation du searcher : " + (t2 - t1) / 1000000.);
				}

				// Affiche le formulaire de recherche
				searcherFrame.displayForm();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Si le bouton de recherche est cliqu�
		else if (arg0.getSource() == searcherFrame.getButtonSearcher()) {
			// Lit la requ�te
			String request = searcherFrame.getRequest();
			if (!request.equals("")) {
				try {
					long t1 = System.nanoTime();
					// Lance la recherche
					LinkedList<Result> results = searcher.search(request, ignoreStopWords, Searcher.ALL_RESULTS);
					long t2 = System.nanoTime();
					System.out.println("Temps de la recherche : " + (t2 - t1) / 1000000.);
					// Affiche les r�sultats
					searcherFrame.displayResults(results);
				} catch (InvalideQueryException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
