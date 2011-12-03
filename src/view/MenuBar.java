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
		VECT_BASIC, VECT_PREFIX, VECT_PREFIX_EXCLUSION, EXTENDED_BOOLEAN
	};

	public enum NormalizerType {
		TOKENIZER, STEMMER
	};

	private static final long serialVersionUID = -3175824100705406877L;
	private JMenu menuLoader, menuSearcherType, menuNormalizerType;
	private JMenuItem menuOpenIndex, menuOpenStopWords, menuLoad;
	private JRadioButtonMenuItem rbMenuVectBasic, rbMenuVectPrefix, rbMenuVectPrefixExclusion, rbMenuExtendedBoolean;
	private JRadioButtonMenuItem rbMenuTokenizer, rbMenuStemmer;
	private JFileChooser indexFileChooser, stopWordsFileChooser;
	private String indexPath, stopWordsPath;
	private SearcherType searcherType;
	private NormalizerType normalizerType;
	private boolean isModifiedIndex;
	private boolean isModifiedStopWords;
	private boolean isModifiedSearcher;
	private boolean isModifiedNormalizer;

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

		rbMenuExtendedBoolean = new JRadioButtonMenuItem("Modèle booléen étendu");
		rbMenuExtendedBoolean.addActionListener(this);
		groupSearcherType.add(rbMenuExtendedBoolean);
		menuSearcherType.add(rbMenuExtendedBoolean);

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
		menuLoad.addActionListener(this);

		menuLoader.add(menuOpenIndex);
		menuLoader.add(menuOpenStopWords);
		menuLoader.add(menuSearcherType);
		menuLoader.add(menuNormalizerType);
		menuLoader.add(menuLoad);
		this.add(menuLoader);

		indexPath = "";
		stopWordsPath = "";

		isModifiedIndex = false;
		isModifiedNormalizer = false;
		isModifiedSearcher = false;
		isModifiedStopWords = false;
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

	public boolean isModifiedIndex() {
		return isModifiedIndex;
	}

	public boolean isModifiedStopWords() {
		return isModifiedStopWords;
	}

	public boolean isModifiedSearcher() {
		return isModifiedSearcher;
	}

	public boolean isModifiedNormalizer() {
		return isModifiedNormalizer;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == menuOpenIndex) {
			int retval = indexFileChooser.showOpenDialog(MenuBar.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				indexPath = indexFileChooser.getSelectedFile().getPath();
				isModifiedIndex = true;
				if (!stopWordsPath.equals(""))
					menuLoad.setEnabled(true);
			}
		} else if (arg0.getSource() == menuOpenStopWords) {
			int retval = stopWordsFileChooser.showOpenDialog(MenuBar.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				stopWordsPath = stopWordsFileChooser.getSelectedFile().getPath();
				isModifiedStopWords = true;
				if (!indexPath.equals(""))
					menuLoad.setEnabled(true);
			}
		} else if (arg0.getSource() == menuLoad) {
			isModifiedIndex = false;
			isModifiedNormalizer = false;
			isModifiedSearcher = false;
			isModifiedStopWords = false;
		} else if (arg0.getSource() == rbMenuVectBasic) {
			if (searcherType != SearcherType.VECT_BASIC)
				isModifiedSearcher = true;
			searcherType = SearcherType.VECT_BASIC;
		} else if (arg0.getSource() == rbMenuVectPrefix) {
			if (searcherType != SearcherType.VECT_PREFIX)
				isModifiedSearcher = true;
			searcherType = SearcherType.VECT_PREFIX;
		} else if (arg0.getSource() == rbMenuVectPrefixExclusion) {
			if (searcherType != SearcherType.VECT_PREFIX_EXCLUSION)
				isModifiedSearcher = true;
			searcherType = SearcherType.VECT_PREFIX_EXCLUSION;
		} else if (arg0.getSource() == rbMenuExtendedBoolean) {
			if (searcherType != SearcherType.EXTENDED_BOOLEAN)
				isModifiedSearcher = true;
			searcherType = SearcherType.EXTENDED_BOOLEAN;
		} else if (arg0.getSource() == rbMenuTokenizer) {
			if (normalizerType != NormalizerType.TOKENIZER)
				isModifiedNormalizer = true;
			normalizerType = NormalizerType.TOKENIZER;
		} else if (arg0.getSource() == rbMenuStemmer) {
			if (normalizerType != NormalizerType.STEMMER)
				isModifiedNormalizer = true;
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
