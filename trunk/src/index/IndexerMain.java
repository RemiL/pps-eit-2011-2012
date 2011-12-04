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

	private IndexerFrame indexerFrame;
	private Indexer indexer;
	private Index index;
	private Weigher weigher;
	private JFileChooser saveFileChooser;
	private Normalizer normalizer;

	public IndexerMain() {
		indexerFrame = new IndexerFrame();
		indexerFrame.getButtonSave().addActionListener(this);
		saveFileChooser = new JFileChooser(".");
		saveFileChooser.setFileFilter(new IndexFilter());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IndexerMain indexerMain = new IndexerMain();
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == indexerFrame.getButtonSave()) {
			int retval = saveFileChooser.showSaveDialog(indexerFrame);
			if (retval == JFileChooser.APPROVE_OPTION) {
				String directoryPath = indexerFrame.getDirectoryPath();
				String stopWordsPath = indexerFrame.getStopWordsPath();
				String indexerType = indexerFrame.getIndexerType();
				String indexType = indexerFrame.getIndexType();
				String weigherType = indexerFrame.getWeigherType();
				String normalizerType = indexerFrame.getNormalizerType();
				String savePath = saveFileChooser.getSelectedFile().getPath();
				boolean useStopWords = false;

				if (!savePath.endsWith(".index") && !savePath.endsWith(".zindex"))
					savePath += ".index";

				try {
					// Crée l'index
					if (indexerType.equals("Indexer de texte")) {
						indexer = new TextIndexer();
					}

					if (indexType.equals("IndexHash")) {
						index = new IndexHash();
					} else if (indexType.equals("IndexTree")) {
						index = new IndexTree();
					}

					if (weigherType.equals("Tf.Idf")) {
						weigher = new WeigherTfIdf();
					} else if (weigherType.equals("Tf.Idf normalisé")) {
						weigher = new WeigherTfIdfNorm();
					}

					if (normalizerType.equals("Tokenizer")) {
						if (!stopWordsPath.equals("")) {
							normalizer = new FrenchTokenizer("frenchST.txt", "UTF-8");
							useStopWords = true;
						} else
							normalizer = new FrenchTokenizer();
					} else if (normalizerType.equals("Stemmer")) {
						if (!stopWordsPath.equals("")) {
							normalizer = new FrenchStemmer("frenchST.txt", "UTF-8");
							useStopWords = true;
						} else
							normalizer = new FrenchStemmer();
					}

					ArrayList<String> listFiles = createListFiles(directoryPath);
					System.out.println("Nombre de fichiers à indexer : " + listFiles.size());
					long t1 = System.nanoTime();
					indexer.index(listFiles, "UTF-8", index, normalizer, useStopWords, weigher);

					long t2 = System.nanoTime();
					System.out.println("Temps d'indexation : " + (t2 - t1) / 1000000.);

					// Exporte l'index
					index.export(savePath, savePath.endsWith(".zindex"));

					long t3 = System.nanoTime();
					System.out.println("Temps de sérialisation : " + (t3 - t2) / 1000000.);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

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
