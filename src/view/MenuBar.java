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

	private static final long serialVersionUID = -3175824100705406877L;
	private JMenu menuLoader, menuTypeRecherche;
	private JMenuItem menuOpen, menuLoad;
	private JRadioButtonMenuItem rbMenuVectBasic, rbMenuVectPrefix, rbMenuVectPrefixExclusion;
	private JFileChooser fileChooser;
	private String path;
	private SearcherType searcherType;

	public MenuBar() {
		super();
		menuLoader = new JMenu("Charger un moteur de recherche");

		fileChooser = new JFileChooser(".");
		fileChooser.setFileFilter(new IndexFilter());
		menuOpen = new JMenuItem("Choisir l'index");
		menuOpen.addActionListener(this);
		menuTypeRecherche = new JMenu("Type de recherche");

		ButtonGroup group = new ButtonGroup();
		rbMenuVectBasic = new JRadioButtonMenuItem("Modèle vectoriel basique");
		rbMenuVectBasic.setSelected(true);
		searcherType = SearcherType.VECT_BASIC;
		rbMenuVectBasic.addActionListener(this);
		group.add(rbMenuVectBasic);
		menuTypeRecherche.add(rbMenuVectBasic);

		rbMenuVectPrefix = new JRadioButtonMenuItem("Modèle vectoriel par préfixe");
		rbMenuVectPrefix.addActionListener(this);
		group.add(rbMenuVectPrefix);
		menuTypeRecherche.add(rbMenuVectPrefix);

		rbMenuVectPrefixExclusion = new JRadioButtonMenuItem("Modèle vectoriel par préfixe avec exclusion");
		rbMenuVectPrefixExclusion.addActionListener(this);
		group.add(rbMenuVectPrefixExclusion);
		menuTypeRecherche.add(rbMenuVectPrefixExclusion);

		menuLoad = new JMenuItem("Charger");
		menuLoad.setEnabled(false);

		menuLoader.add(menuOpen);
		menuLoader.add(menuTypeRecherche);
		menuLoader.add(menuLoad);
		this.add(menuLoader);
	}

	public JMenuItem getMenuLoad() {
		return menuLoad;
	}

	public String getIndexPath() {
		return path;
	}

	public SearcherType getSearcherType() {
		return searcherType;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == menuOpen) {
			int retval = fileChooser.showOpenDialog(MenuBar.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				path = fileChooser.getSelectedFile().getPath();
				menuLoad.setEnabled(true);
			}
		} else if (arg0.getSource() == rbMenuVectBasic) {
			searcherType = SearcherType.VECT_BASIC;
		} else if (arg0.getSource() == rbMenuVectPrefix) {
			searcherType = SearcherType.VECT_PREFIX;
		} else if (arg0.getSource() == rbMenuVectPrefixExclusion) {
			searcherType = SearcherType.VECT_PREFIX_EXCLUSION;
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
