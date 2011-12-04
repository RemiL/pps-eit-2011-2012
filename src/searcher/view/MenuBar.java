package searcher.view;

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

/**
 * Une barre de menu
 */
public class MenuBar extends JMenuBar implements ActionListener {

	/** La liste des types de searcher */
	public enum SearcherType {
		VECT_BASIC, VECT_PREFIX, VECT_PREFIX_EXCLUSION, EXTENDED_BOOLEAN
	};

	/** La liste des types de normaliseur */
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

	/** Crée une barre de menu */
	public MenuBar() {
		super();
		// Le menu principal
		menuLoader = new JMenu("Charger un moteur de recherche");

		// Un sous menu pour choisir l'index
		// Ouvre une boite de dialogue pour parcourir les fichiers d'index
		indexFileChooser = new JFileChooser(".");
		indexFileChooser.setFileFilter(new IndexFilter());
		menuOpenIndex = new JMenuItem("Choisir l'index");
		menuOpenIndex.addActionListener(this);

		// Un sous menu pour ouvrir le fichier de mots vides
		// Ouvre une boite de dialogue pour parcourir les fichiers de mots vides
		stopWordsFileChooser = new JFileChooser(".");
		menuOpenStopWords = new JMenuItem("Choisir les mots vides");
		menuOpenStopWords.addActionListener(this);

		// Un sous menu pour choisir le type de recherche
		// Par défaut c'est une recherche par le modèle vectoriel basique
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

		// Un sous menu pour choisir le type de normaliseur
		// Par défaut c'est un tokenizer
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

		// Le sous menu pour charger le searcher
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

		isModifiedIndex = true;
		isModifiedNormalizer = true;
		isModifiedSearcher = true;
		isModifiedStopWords = true;
	}

	/**
	 * Retourne le bouton pour charger le searcher
	 * 
	 * @return le bouton pour charger le searcher
	 */
	public JMenuItem getMenuLoad() {
		return menuLoad;
	}

	/**
	 * Retourne le chemin de l'index
	 * 
	 * @return le chemin de l'index
	 */
	public String getIndexPath() {
		return indexPath;
	}

	/**
	 * Retourne le chemin du fichier des mots vides
	 * 
	 * @return le bouton pour charger le searcher
	 */
	public String getStopWordsPath() {
		return stopWordsPath;
	}

	/**
	 * Retourne le type de searcher
	 * 
	 * @return le type de searcher
	 */
	public SearcherType getSearcherType() {
		return searcherType;
	}

	/**
	 * Retourne le type de normaliseur
	 * 
	 * @return le type de normaliseur
	 */
	public NormalizerType getNormalizerType() {
		return normalizerType;
	}

	/**
	 * Retourne si l'index a été modifié
	 * 
	 * @return true si l'index est modifié, false sinon
	 */
	public boolean isModifiedIndex() {
		return isModifiedIndex;
	}

	/**
	 * Retourne si la liste de mots vides a été modifiée
	 * 
	 * @return true si la liste de mots vides est modifiée, false sinon
	 */
	public boolean isModifiedStopWords() {
		return isModifiedStopWords;
	}

	/**
	 * Retourne si le type de searcher a été modifié
	 * 
	 * @return true si le type de searcher est modifié, false sinon
	 */
	public boolean isModifiedSearcher() {
		return isModifiedSearcher;
	}

	/**
	 * Retourne si le type de normaliseur a été modifié
	 * 
	 * @return true si le type de normaliseur est modifié, false sinon
	 */
	public boolean isModifiedNormalizer() {
		return isModifiedNormalizer;
	}

	public void actionPerformed(ActionEvent arg0) {
		// Si le menu de selection de l'index est cliqué
		if (arg0.getSource() == menuOpenIndex) {
			int retval = indexFileChooser.showOpenDialog(MenuBar.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				indexPath = indexFileChooser.getSelectedFile().getPath();
				isModifiedIndex = true;
				menuLoad.setEnabled(true);
			}
		}
		// Si le menu de selection du fichier de mots vides est cliqué
		else if (arg0.getSource() == menuOpenStopWords) {
			int retval = stopWordsFileChooser.showOpenDialog(MenuBar.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				stopWordsPath = stopWordsFileChooser.getSelectedFile().getPath();
				isModifiedStopWords = true;
			}
		}
		// Si le menu de chargement du searcher est cliqué
		else if (arg0.getSource() == menuLoad) {
			isModifiedIndex = false;
			isModifiedNormalizer = false;
			isModifiedSearcher = false;
			isModifiedStopWords = false;
		}
		// Si un type de searcher est sélectionné
		else if (arg0.getSource() == rbMenuVectBasic) {
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
		}
		// Si un type de normaliseur est sélectionné
		else if (arg0.getSource() == rbMenuTokenizer) {
			if (normalizerType != NormalizerType.TOKENIZER)
				isModifiedNormalizer = true;
			normalizerType = NormalizerType.TOKENIZER;
		} else if (arg0.getSource() == rbMenuStemmer) {
			if (normalizerType != NormalizerType.STEMMER)
				isModifiedNormalizer = true;
			normalizerType = NormalizerType.STEMMER;
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
