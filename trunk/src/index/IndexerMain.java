package index;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import tools.normalizer.FrenchTokenizer;
import tools.weigher.WeigherTfIdf;

/**
 * Index pour un répertoire entier
 */
public class IndexerMain {

	/**
	 * Index un répertoire entier
	 */
	public static void main(String[] args) {
		Indexer indexer = new TextIndexer();
		Index index = new IndexHash();

		try {
			// Crée l'index
			ArrayList<String> listFiles = createListFiles("corpus/texte");
			System.out.println("Nombre de fichiers à indexer : " + listFiles.size());
			long t1 = System.nanoTime();
			indexer.index(listFiles, "UTF-8", index, new FrenchTokenizer("frenchST.txt", "UTF-8"), true,
					new WeigherTfIdf());

			long t2 = System.nanoTime();
			System.out.println("Temps d'indexation : " + (t2 - t1) / 1000000.);

			// Sérialise l'index
			FileOutputStream fichier = new FileOutputStream("indexSansMotsVides.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(index);
			oos.close();

			long t3 = System.nanoTime();
			System.out.println("Temps de sérialisation : " + (t3 - t2) / 1000000.);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crée la liste des noms des fichiers d'un répertoire
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
