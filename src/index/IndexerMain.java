package index;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import tools.normalizer.FrenchTokenizer;
import tools.weigher.WeigherTfIdf;

/**
 * Index pour un r�pertoire entier
 */
public class IndexerMain {

	/**
	 * Index un r�pertoire entier
	 */
	public static void main(String[] args) {
		Indexer indexer = new TextIndexer();
		Index index = new IndexHash();

		try {
			// Cr�e l'index
			ArrayList<String> listFiles = createListFiles("corpus/texte");
			System.out.println("Nombre de fichiers � indexer : " + listFiles.size());
			long t1 = System.nanoTime();
			indexer.index(listFiles, "UTF-8", index, new FrenchTokenizer("frenchST.txt", "UTF-8"), true,
					new WeigherTfIdf());

			long t2 = System.nanoTime();
			System.out.println("Temps d'indexation : " + (t2 - t1) / 1000000.);

			// S�rialise l'index
			FileOutputStream fichier = new FileOutputStream("indexSansMotsVides.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(index);
			oos.close();

			long t3 = System.nanoTime();
			System.out.println("Temps de s�rialisation : " + (t3 - t2) / 1000000.);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cr�e la liste des noms des fichiers d'un r�pertoire
	 * 
	 * @param dirName
	 *            le nom du r�pertoire
	 * @return la liste des noms des fichiers d'un r�pertoire
	 */
	private static ArrayList<String> createListFiles(String dirName) {
		ArrayList<String> listFiles = new ArrayList<String>();
		File dir = new File(dirName);

		// Si on a un r�pertoire, on le parcourt r�cursivement
		// Sinon si c'est un fichier, on l'ajoute � la liste
		if (dir.isDirectory()) {
			// La liste des fichiers du r�pertoire
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
