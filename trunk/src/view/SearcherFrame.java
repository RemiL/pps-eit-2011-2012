package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;

import searcher.Result;

public class SearcherFrame extends JFrame {
	private static final long serialVersionUID = -1317739677659226928L;

	LoaderPanel loaderPan;
	FormPanel formPan;
	ResultsPanel resultsPan;

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

		loaderPan = new LoaderPanel();
		formPan = new FormPanel();
		resultsPan = new ResultsPanel();

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(loaderPan, BorderLayout.CENTER);
		this.getContentPane().validate();
	}

	public JButton getButtonSearcher() {
		return formPan.getButtonSearch();
	}

	public JButton getButtonLoad() {
		return loaderPan.getButtonLoad();
	}

	public void displayForm() {
		this.getContentPane().remove(loaderPan);
		this.getContentPane().add(formPan, BorderLayout.NORTH);
		this.getContentPane().validate();
	}

	public void displayResults(LinkedList<Result> results) {
		this.remove(resultsPan);
		resultsPan.displayResults(results);
		this.getContentPane().add(resultsPan, BorderLayout.CENTER);
		this.getContentPane().validate();
	}

	public String getRequest() {
		return formPan.getRequest();
	}

	public String getIndexPath() {
		return loaderPan.getIndexPath();
	}

	public String getSearcherType() {
		return loaderPan.getSearcherType();
	}
}
