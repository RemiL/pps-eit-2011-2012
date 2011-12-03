package searcher;

import index.Index;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import tools.normalizer.FrenchTokenizer;
import tools.weigher.WeigherTfIdf;
import view.SearcherFrame;

public class SearcherMain implements ActionListener {

	private SearcherFrame searcherFrame;
	private String request;
	private Searcher searcher;

	public SearcherMain() {
		searcherFrame = new SearcherFrame();
		searcherFrame.getButtonLoad().addActionListener(this);
		searcherFrame.getButtonSearcher().addActionListener(this);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SearcherMain searcherMain = new SearcherMain();
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == searcherFrame.getButtonLoad()) {
			String indexPath = searcherFrame.getIndexPath();
			String searcherType = searcherFrame.getSearcherType();
			
			try {
				long t1 = System.nanoTime();
				// Chargement de l'index depuis un fichier
				Index index = Index.load(indexPath);
	
				long t2 = System.nanoTime();
				System.out.println("Temps de désérialisation : " + (t2 - t1) / 1000000.);
	
				// Création du searcher
				if(searcherType == "Modèle vectoriel basique")
					searcher = new SearcherVectorModel(new FrenchTokenizer("frenchST.txt", "UTF-8"), new WeigherTfIdf(),
						index);
				else if(searcherType == "Modèle vectoriel par préfixe")
					searcher = new SearcherVectorModelPrefix(new FrenchTokenizer("frenchST.txt", "UTF-8"), new WeigherTfIdf(),
						index);
	
				long t3 = System.nanoTime();
				System.out.println("Temps de création du searcher : " + (t3 - t2) / 1000000.);
				searcherFrame.displayForm();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (arg0.getSource() == searcherFrame.getButtonSearcher()) {
			request = searcherFrame.getRequest();
			if(!request.equals(""))
			{
				long t1 = System.nanoTime();
				LinkedList<Result> results = searcher.search(request, true, Searcher.ALL_RESULTS);
				long t2 = System.nanoTime();
				System.out.println("Temps de la recherche : " + (t2 - t1) / 1000000.);
				searcherFrame.displayResults(results);
			}
		}
	}
}
