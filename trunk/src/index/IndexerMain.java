package index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import searcher.InvalideQueryException;
import searcher.Result;
import searcher.Searcher;
import searcher.extendedbooleanmodel.SearcherExtendBooleanModel;
import tools.normalizer.FrenchTokenizer;
import tools.weigher.WeigherTfIdfNorm;

/**
 * Classe de test pour l'indexation.
 */
public class IndexerMain {

	public static void main(String[] args) {
		Indexer indexer = new TextIndexer();
		Index index = new IndexTree();

		try {
			// Crée l'index
			ArrayList<String> listFiles = createListFiles("corpus/texte");
			System.out.println("Nombre de fichiers à indexer : " + listFiles.size());
			long t1 = System.nanoTime();
			indexer.index(listFiles, "UTF-8", index, new FrenchTokenizer("frenchST.txt", "UTF-8"), true,
					new WeigherTfIdfNorm());

			long t2 = System.nanoTime();
			System.out.println("Temps d'indexation : " + (t2 - t1) / 1000000.);

			// Exporte l'index
			// index.export("indexSansMotsVides3.ser");
			long t3 = System.nanoTime();
			System.out.println("Temps de sérialisation : " + (t3 - t2) / 1000000.);

			Searcher searcher = new SearcherExtendBooleanModel(new FrenchTokenizer("frenchST.txt", "UTF-8"), index);
			for (Result r : searcher.search("AND(OR(basketball, encyclopédie, guide), NOT(xxx))", true)) {
				System.out.println(r.getDocument().getTitle() + " : " + r.getPertinence());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalideQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Crée la liste des noms des fichiers d'un répertoire.
	 * 
	 * @param dirName
	 *            le nom du répertoire
	 * @return la liste des noms des fichiers d'un répertoire
	 */
	private static ArrayList<String> createListFiles(String dirName) {
		ArrayList<String> listFiles = new ArrayList<String>();
		File dir = new File(dirName);

		// Si on a un répertoire, on le parcourt récursivement
		// Sinon si c'est un fichier, on l'ajoute à la liste
		if (dir.isDirectory()) {
			// La liste des fichiers du répertoire
			File[] list = dir.listFiles();
			if (list != null) {
				// Pour chaque fichier, on essaie de le parcourt
				for (int i = 0; i < list.length; i++) {
					listFiles.addAll(createListFiles(list[i].getPath()));
				}
			}
		} else
			listFiles.add(dirName);

		return listFiles;
	}
}
