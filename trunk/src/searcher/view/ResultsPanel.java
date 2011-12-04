package searcher.view;

import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import searcher.Result;

/**
 * Un affichage de la liste des r�sultats sous forme de panel scrollable
 */
public class ResultsPanel extends JScrollPane {

	private static final long serialVersionUID = 2597612232158711073L;

	/**
	 * Cr�e une liste de r�sultat vide pouvant est sroller verticalement
	 */
	public ResultsPanel() {
		super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
	}

	/**
	 * Affiche la liste des r�ultats
	 * @param results la liste des r�sultats
	 */
	public void displayResults(LinkedList<Result> results) {
		JPanel pan = new JPanel();
		// Si la liste n'est pas vide, la liste est affich�e
		// Sinon un message est affich�
		if (results.size() != 0) {
			pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
			for (int i = 0; i < results.size(); i++) {
				pan.add(new ResultPanel(i + 1, results.get(i).getDocument().getTitle(), results.get(i).getDocument()
						.getUrl(), results.get(i).getDocument().getPath(), results.get(i).getPertinence()));
			}
		} else {
			pan.add(new JLabel("Aucun r�sultat"));
		}
		this.setViewportView(pan);
	}
}
