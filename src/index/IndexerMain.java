package index;

import index.view.IndexerFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import tools.normalizer.FrenchStemmer;
import tools.normalizer.FrenchTokenizer;
import tools.normalizer.Normalizer;
import tools.weigher.Weigher;
import tools.weigher.WeigherTfIdf;
import tools.weigher.WeigherTfIdfNorm;

/**
 * Classe de test pour l'indexation.
 */
public class IndexerMain implements ActionListener {

	/** La fen�tre */
	private IndexerFrame indexerFrame;
	/** L'indexer */
	private Indexer indexer;
	/** L'index */
	private Index index;
	/** Le pond�rateur */
	private Weigher weigher;
	/** La boite de dialogue pour choisir le chemin de la sauvegarde de l'index */
	private JFileChooser saveFileChooser;
	/** Le nomaliseur */
	private Normalizer normalizer;

	/**
	 * Affiche une fen�tre pour indexer
	 */
	public IndexerMain() {
		// Cr�e une fen�tre
		indexerFrame = new IndexerFrame();
		// Ecoute le bouton Indexer
		indexerFrame.getButtonSave().addActionListener(this);
		// Positionne la boite de dialogue dans le r�pertoire courant
		saveFileChooser = new JFileChooser(".");
		// Ajoute le filtre de fichier � la boite de dialogue
		saveFileChooser.setFileFilter(new IndexFilter());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IndexerMain indexerMain = new IndexerMain();
	}

	/**
	 * Cr�e la liste des noms des fichiers d'un r�pertoire.
	 * 
	 * @param dirName
	 *            le nom du r�pertoire
	 * @return la liste des noms des fichiers d'un r�pertoire
	 */
	private ArrayList<String> createListFiles(String dirName) {
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Le bouton Indexer est cliqu�
		if (arg0.getSource() == indexerFrame.getButtonSave()) {
			// On ouvre la boite de dialogue pour le choix du chemin de la
			// sauvegarde
			int retval = saveFileChooser.showSaveDialog(indexerFrame);
			// Si un fichier est choisi
			if (retval == JFileChooser.APPROVE_OPTION) {
				// Tous les param�tres sont lus
				String directoryPath = indexerFrame.getDirectoryPath();
				String stopWordsPath = indexerFrame.getStopWordsPath();
				String indexerType = indexerFrame.getIndexerType();
				String indexType = indexerFrame.getIndexType();
				String weigherType = indexerFrame.getWeigherType();
				String normalizerType = indexerFrame.getNormalizerType();
				String savePath = saveFileChooser.getSelectedFile().getPath();
				boolean useStopWords = false;

				// Si l'extention du fichier de sauvegarde n'est pas .index ou
				// .zindex, on ajoute l'extention .index par d�faut
				if (!savePath.endsWith(".index") && !savePath.endsWith(".zindex"))
					savePath += ".index";

				try {
					// Cr�e l'indexer
					if (indexerType.equals("Indexer de texte")) {
						indexer = new TextIndexer();
					}

					// Cr�e l'index
					if (indexType.equals("IndexHash")) {
						index = new IndexHash();
					} else if (indexType.equals("IndexTree")) {
						index = new IndexTree();
					}

					// Cr�e le pond�rateur
					if (weigherType.equals("Tf.Idf")) {
						weigher = new WeigherTfIdf();
					} else if (weigherType.equals("Tf.Idf normalis�")) {
						weigher = new WeigherTfIdfNorm();
					}

					// cr�e le normaliseur
					if (normalizerType.equals("Tokenizer")) {
						// S'il y a une liste de mots vides
						if (!stopWordsPath.equals("")) {
							normalizer = new FrenchTokenizer("frenchST.txt", "UTF-8");
							useStopWords = true;
						} else
							// S'il n'y a pas de mots vides
							normalizer = new FrenchTokenizer();
					} else if (normalizerType.equals("Stemmer")) {
						// S'il y a une liste de mots vides
						if (!stopWordsPath.equals("")) {
							normalizer = new FrenchStemmer("frenchST.txt", "UTF-8");
							useStopWords = true;
						} else
							// S'il n'y a pas de mots vides
							normalizer = new FrenchStemmer();
					}

					// Cr�e la liste des fichiers � indexer
					ArrayList<String> listFiles = createListFiles(directoryPath);
					System.out.println("Nombre de fichiers � indexer : " + listFiles.size());

					long t1 = System.nanoTime();

					// Remplie l'index
					indexer.index(listFiles, "UTF-8", index, normalizer, useStopWords, weigher);

					long t2 = System.nanoTime();
					System.out.println("Temps d'indexation : " + (t2 - t1) / 1000000.);

					// Exporte l'index
					index.export(savePath, savePath.endsWith(".zindex"));

					long t3 = System.nanoTime();
					System.out.println("Temps de s�rialisation : " + (t3 - t2) / 1000000.);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Un filtre pour les fichiers .index et .zindex
	class IndexFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".index")
					|| f.getName().toLowerCase().endsWith(".zindex");
		}

		@Override
		public String getDescription() {
			return "Fichiers d'index (.index, .zindex)";
		}
	}
}
