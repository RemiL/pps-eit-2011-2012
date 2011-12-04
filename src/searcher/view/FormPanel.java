package searcher.view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Le panel avec le formulaire de recherche
 */
public class FormPanel extends JPanel {
	private static final long serialVersionUID = 5530102639867570180L;
	/** Le champ pur la requête */
	private JTextField field;
	/** Le bouton pour lancer la recherche */
	private JButton buttonSearch;

	/**
	 * Crée le panel
	 */
	public FormPanel() {
		super();
		this.setPreferredSize(new Dimension(800, 60));
		field = new JTextField();
		field.setPreferredSize(new Dimension(500, 40));
		buttonSearch = new JButton("Chercher");
		this.add(field);
		this.add(buttonSearch);
	}

	/**
	 * Retourne le bouton de recherche
	 * @return le bouton de recherche
	 */
	public JButton getButtonSearch() {
		return buttonSearch;
	}

	/**
	 * Retourne la requête
	 * @return la requête
	 */
	public String getRequest() {
		return field.getText();
	}
}
