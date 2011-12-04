package searcher.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;

import searcher.Result;
import searcher.view.MenuBar.NormalizerType;
import searcher.view.MenuBar.SearcherType;

/**
 * Un affichage pour le searcher
 */
public class SearcherFrame extends JFrame {
	private static final long serialVersionUID = -1317739677659226928L;

	/** Le formulaire de recherche */
	FormPanel formPan;
	/** La liste des résultats */
	ResultsPanel resultsPan;
	/** La barre de menu */
	MenuBar menuBar;

	/**
	 * Construit une nouvelle fenêtre de taille 800x600
	 */
	public SearcherFrame() {
		super();
		this.setTitle("Moteur de recherche");
		this.setSize(800, 600);
		this.setMinimumSize(new Dimension(800, 600));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		formPan = new FormPanel();
		resultsPan = new ResultsPanel();
		menuBar = new MenuBar();

		// Ajoute la barre de menu
		this.setJMenuBar(menuBar);
		this.validate();
	}

	/**
	 * Retourne le bouton de recherche
	 * 
	 * @return le bouton de recherche
	 */
	public JButton getButtonSearcher() {
		return formPan.getButtonSearch();
	}

	/**
	 * Affiche le formulaire de recherche
	 */
	public void displayForm() {
		this.getContentPane().removeAll();
		this.getContentPane().add(formPan, BorderLayout.CENTER);
		this.getContentPane().validate();
	}

	/**
	 * Affiche la liste des résultats
	 * 
	 * @param results
	 *            la liste des résultats
	 */
	public void displayResults(LinkedList<Result> results) {
		this.getContentPane().removeAll();
		resultsPan.displayResults(results);
		this.getContentPane().add(formPan, BorderLayout.NORTH);
		this.getContentPane().add(resultsPan, BorderLayout.CENTER);
		this.getContentPane().validate();
	}

	/**
	 * Retourne la requête
	 * 
	 * @return la requête
	 */
	public String getRequest() {
		return formPan.getRequest();
	}

	/**
	 * Retourne le chemin de l'index
	 * 
	 * @return le chemin de l'index
	 */
	public String getIndexPath() {
		return menuBar.getIndexPath();
	}

	/**
	 * Retourne le type de searher
	 * 
	 * @return le type de searher
	 */
	public SearcherType getSearcherType() {
		return menuBar.getSearcherType();
	}

	/**
	 * Retourne le bouton de chargement du searcher
	 * 
	 * @return le bouton de chargement du searcher
	 */
	public AbstractButton getMenuLoad() {
		return menuBar.getMenuLoad();
	}

	/**
	 * Retourne le chemin du fichier des mots vides
	 * 
	 * @return le chemin du fichier des mots vides
	 */
	public String getStopWordsPath() {
		return menuBar.getStopWordsPath();
	}

	/**
	 * Retourne le type de normaliseur
	 * 
	 * @return le type de normalizer
	 */
	public NormalizerType getNormalizerType() {
		return menuBar.getNormalizerType();
	}

	/**
	 * Retourne si le type de searcher a été modifié
	 * 
	 * @return true si le type de searcher est modifié, false sinon
	 */
	public boolean isModifiedSearcher() {
		return menuBar.isModifiedSearcher();
	}

	/**
	 * Retourne si le type de normaliseur a été modifié
	 * 
	 * @return true si le type de normaliseur est modifié, false sinon
	 */
	public boolean isModifiedNormalizer() {
		return menuBar.isModifiedNormalizer();
	}

	/**
	 * Retourne si l'index a été modifié
	 * 
	 * @return true si l'index est modifié, false sinon
	 */
	public boolean isModifiedIndex() {
		return menuBar.isModifiedIndex();
	}

	/**
	 * Retourne si la liste de mots vides a été modifiée
	 * 
	 * @return true si la liste de mots vides est modifiée, false sinon
	 */
	public boolean isModifiedStopWords() {
		return menuBar.isModifiedStopWords();
	}
}
