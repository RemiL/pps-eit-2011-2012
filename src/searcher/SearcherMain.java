package searcher;

import index.Index;

import java.io.IOException;

import tools.normalizer.FrenchTokenizer;
import tools.weigher.WeigherTfIdf;

public class SearcherMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			long t1 = System.nanoTime();
			// Chargement de l'index depuis un fichier
			Index index = Index.load("indexSansMotsVides2.ser");

			long t2 = System.nanoTime();
			System.out.println("Temps de désérialisation : " + (t2 - t1) / 1000000.);

			// Création du searcher
			Searcher searcher = new SearcherVectorModelPrefixWordsExclusion(
					new FrenchTokenizer("frenchST.txt", "UTF-8"), new WeigherTfIdf(), index);

			long t3 = System.nanoTime();
			System.out.println("Temps de création du searcher : " + (t3 - t2) / 1000000.);
			long t4 = System.nanoTime();

			for (Result res : searcher.search("Basket* encyclopédie guide -chaussure", true, Searcher.ALL_RESULTS)) {
				System.out.println(" - " + res.getDocument().getUrl() + " (" + res.getPertinence() + ")");
			}

			System.out.println("Temps de recherche : " + (t4 - t3) / 1000000.);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
