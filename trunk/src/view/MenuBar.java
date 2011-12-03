package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.filechooser.FileFilter;

public class MenuBar extends JMenuBar implements ActionListener {

	public enum SearcherType {
		VECT_BASIC, VECT_PREFIX, VECT_PREFIX_EXCLUSION
	};

	public enum NormalizerType {
		TOKENIZER, STEMMER
	};

	private static final long serialVersionUID = -3175824100705406877L;
	private JMenu menuLoader, menuSearcherType, menuNormalizerType;
	private JMenuItem menuOpenIndex, menuOpenStopWords, menuLoad;
	private JRadioButtonMenuItem rbMenuVectBasic, rbMenuVectPrefix, rbMenuVectPrefixExclusion;
	private JRadioButtonMenuItem rbMenuTokenizer, rbMenuStemmer;
	private JFileChooser indexFileChooser, stopWordsFileChooser;
	private String indexPath, stopWordsPath;
	private SearcherType searcherType;
	private NormalizerType normalizerType;

	public MenuBar() {
		super();
		menuLoader = new JMenu("Charger un moteur de recherche");

		indexFileChooser = new JFileChooser(".");
		indexFileChooser.setFileFilter(new IndexFilter());
		menuOpenIndex = new JMenuItem("Choisir l'index");
		menuOpenIndex.addActionListener(this);

		stopWordsFileChooser = new JFileChooser(".");
		menuOpenStopWords = new JMenuItem("Choisir les mots vides");
		menuOpenStopWords.addActionListener(this);

		menuSearcherType = new JMenu("Type de recherche");
		ButtonGroup groupSearcherType = new ButtonGroup();
		rbMenuVectBasic = new JRadioButtonMenuItem("Modèle vectoriel basique");
		rbMenuVectBasic.setSelected(true);
		searcherType = SearcherType.VECT_BASIC;
		rbMenuVectBasic.addActionListener(this);
		groupSearcherType.add(rbMenuVectBasic);
		menuSearcherType.add(rbMenuVectBasic);

		rbMenuVectPrefix = new JRadioButtonMenuItem("Modèle vectoriel par préfixe");
		rbMenuVectPrefix.addActionListener(this);
		groupSearcherType.add(rbMenuVectPrefix);
		menuSearcherType.add(rbMenuVectPrefix);

		rbMenuVectPrefixExclusion = new JRadioButtonMenuItem("Modèle vectoriel par préfixe avec exclusion");
		rbMenuVectPrefixExclusion.addActionListener(this);
		groupSearcherType.add(rbMenuVectPrefixExclusion);
		menuSearcherType.add(rbMenuVectPrefixExclusion);

		menuNormalizerType = new JMenu("Type de normaliseur");
		ButtonGroup groupNormalizerType = new ButtonGroup();
		rbMenuTokenizer = new JRadioButtonMenuItem("Tokenizer");
		rbMenuTokenizer.setSelected(true);
		normalizerType = NormalizerType.TOKENIZER;
		rbMenuTokenizer.addActionListener(this);
		groupNormalizerType.add(rbMenuTokenizer);
		menuNormalizerType.add(rbMenuTokenizer);

		rbMenuStemmer = new JRadioButtonMenuItem("Stemmer");
		rbMenuStemmer.addActionListener(this);
		groupNormalizerType.add(rbMenuStemmer);
		menuNormalizerType.add(rbMenuStemmer);

		menuLoad = new JMenuItem("Charger");
		menuLoad.setEnabled(false);

		menuLoader.add(menuOpenIndex);
		menuLoader.add(menuOpenStopWords);
		menuLoader.add(menuSearcherType);
		menuLoader.add(menuNormalizerType);
		menuLoader.add(menuLoad);
		this.add(menuLoader);

		indexPath = "";
		stopWordsPath = "";
	}

	public JMenuItem getMenuLoad() {
		return menuLoad;
	}

	public String getIndexPath() {
		return indexPath;
	}

	public String getStopWordsPath() {
		return stopWordsPath;
	}

	public SearcherType getSearcherType() {
		return searcherType;
	}

	public NormalizerType getNormalizerType() {
		return normalizerType;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == menuOpenIndex) {
			int retval = indexFileChooser.showOpenDialog(MenuBar.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				indexPath = indexFileChooser.getSelectedFile().getPath();
				if (!stopWordsPath.equals(""))
					menuLoad.setEnabled(true);
			}
		} else if (arg0.getSource() == menuOpenStopWords) {
			int retval = stopWordsFileChooser.showOpenDialog(MenuBar.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				stopWordsPath = stopWordsFileChooser.getSelectedFile().getPath();
				if (!indexPath.equals(""))
					menuLoad.setEnabled(true);
			}
		} else if (arg0.getSource() == rbMenuVectBasic) {
			searcherType = SearcherType.VECT_BASIC;
		} else if (arg0.getSource() == rbMenuVectPrefix) {
			searcherType = SearcherType.VECT_PREFIX;
		} else if (arg0.getSource() == rbMenuVectPrefixExclusion) {
			searcherType = SearcherType.VECT_PREFIX_EXCLUSION;
		} else if (arg0.getSource() == rbMenuTokenizer) {
			normalizerType = NormalizerType.TOKENIZER;
		} else if (arg0.getSource() == rbMenuStemmer) {
			normalizerType = NormalizerType.STEMMER;
		}
	}

	class IndexFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.isDirectory() || f.getName().toLowerCase().endsWith(".ser");
		}

		@Override
		public String getDescription() {
			return "Fichiers .ser";
		}

	}
}
