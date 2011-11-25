package searcher;

import index.Index;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import tools.normalizer.FrenchTokenizer;
import tools.weigher.WeigherTfIdf;

public class SearcherMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			long t1 = System.nanoTime();
			// Lecture de l'index depuis un fichier
			FileInputStream fis = new FileInputStream("indexSansMotsVides.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Index index = (Index) ois.readObject();

			long t2 = System.nanoTime();
			System.out.println("Temps de désérialisation :" + (t2 - t1) / 1000000.);

			// Création du searcher
			Searcher searcher = new SearcherVectorModel(new FrenchTokenizer("frenchST.txt", "UTF-8"),
					new WeigherTfIdf(), index);

			long t3 = System.nanoTime();
			System.out.println("Temps de création du searcher :" + (t3 - t2) / 1000000.);

			for (Result res : searcher.search("Basketball encyclopédie guide", true, Searcher.ALL_RESULTS)) {
				System.out.println(" - " + res.getDocument().getUrl() + " (" + res.getPertinence() + ")");
			}

			long t4 = System.nanoTime();
			System.out.println("Temps de recherche :" + (t4 - t3) / 1000000.);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
