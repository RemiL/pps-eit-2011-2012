package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;

import searcher.Result;
import view.MenuBar.NormalizerType;
import view.MenuBar.SearcherType;

public class SearcherFrame extends JFrame {
	private static final long serialVersionUID = -1317739677659226928L;

	FormPanel formPan;
	ResultsPanel resultsPan;
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

		this.setJMenuBar(menuBar);
		this.validate();
	}

	public JButton getButtonSearcher() {
		return formPan.getButtonSearch();
	}

	public void displayForm() {
		this.getContentPane().removeAll();
		this.getContentPane().add(formPan, BorderLayout.CENTER);
		this.getContentPane().validate();
	}

	public void displayResults(LinkedList<Result> results) {
		this.getContentPane().removeAll();
		resultsPan.displayResults(results);
		this.getContentPane().add(formPan, BorderLayout.NORTH);
		this.getContentPane().add(resultsPan, BorderLayout.CENTER);
		this.getContentPane().validate();
	}

	public String getRequest() {
		return formPan.getRequest();
	}

	public String getIndexPath() {
		return menuBar.getIndexPath();
	}

	public SearcherType getSearcherType() {
		return menuBar.getSearcherType();
	}

	public AbstractButton getMenuLoad() {
		return menuBar.getMenuLoad();
	}

	public String getStopWordsPath() {
		return menuBar.getStopWordsPath();
	}

	public NormalizerType getNormalizerType() {
		return menuBar.getNormalizerType();
	}

	public boolean isModifiedSearcher() {
		return menuBar.isModifiedSearcher();
	}
	
	public boolean isModifiedNormalizer() {
		return menuBar.isModifiedNormalizer();
	}
	
	public boolean isModifiedIndex() {
		return menuBar.isModifiedIndex();
	}
	
	public boolean isModifiedStopWords() {
		return menuBar.isModifiedStopWords();
	}
}
