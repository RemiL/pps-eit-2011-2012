package searcher;

import index.Index;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import tools.normalizer.FrenchStemmer;
import tools.normalizer.FrenchTokenizer;
import tools.normalizer.Normalizer;
import tools.weigher.WeigherTfIdf;
import tools.weigher.WeigherTfIdfNorm;
import view.SearcherFrame;
import view.MenuBar.NormalizerType;
import view.MenuBar.SearcherType;


public class SearcherMain implements ActionListener {

	private SearcherFrame searcherFrame;
	private Searcher searcher;
	private Index index;
	private Normalizer normalizer;

	public SearcherMain() {
		searcherFrame = new SearcherFrame();
		searcherFrame.getButtonSearcher().addActionListener(this);
		searcherFrame.getMenuLoad().addActionListener(this);
		
		normalizer = null;
		index = null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SearcherMain searcherMain = new SearcherMain();
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == searcherFrame.getMenuLoad()) {
			String indexPath = searcherFrame.getIndexPath();
			String stopWordsPath = searcherFrame.getStopWordsPath();
			SearcherType searcherType = searcherFrame.getSearcherType();
			NormalizerType normalizerType = searcherFrame.getNormalizerType();

			try {
				if(searcherFrame.isModifiedNormalizer() || searcherFrame.isModifiedStopWords())
				{
					// Création du normlizer
					switch (normalizerType) {
					case TOKENIZER:
						normalizer = new FrenchTokenizer(stopWordsPath, "UTF-8");
						break;
					case STEMMER:
						normalizer = new FrenchStemmer(stopWordsPath, "UTF-8");
						break;
					}
				}
				
				if(searcherFrame.isModifiedIndex())
				{
					long t1 = System.nanoTime();
					// Chargement de l'index depuis un fichier
					index = Index.load(indexPath);
	
					long t2 = System.nanoTime();
					System.out.println("Temps de désérialisation : " + (t2 - t1) / 1000000.);
				}

				if(searcherFrame.isModifiedSearcher() || searcherFrame.isModifiedIndex() || searcherFrame.isModifiedStopWords() || searcherFrame.isModifiedNormalizer())
				{
					long t1 = System.nanoTime();
					// Création du searcher
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
					}
					long t2 = System.nanoTime();
					System.out.println("Temps de création du searcher : " + (t2 - t1) / 1000000.);
				}

				searcherFrame.displayForm();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (arg0.getSource() == searcherFrame.getButtonSearcher()) {
			String request = searcherFrame.getRequest();
			if (!request.equals("")) {
				long t1 = System.nanoTime();
				LinkedList<Result> results = searcher.search(request, true, Searcher.ALL_RESULTS);
				long t2 = System.nanoTime();
				System.out.println("Temps de la recherche : " + (t2 - t1) / 1000000.);
				searcherFrame.displayResults(results);
			}
		}
	}
}
