package searcher.view;

import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import searcher.Result;

public class ResultsPanel extends JScrollPane {

	private static final long serialVersionUID = 2597612232158711073L;

	public ResultsPanel() {
		super(VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER);
	}

	public void displayResults(LinkedList<Result> results) {
		JPanel pan = new JPanel();
		if (results.size() != 0) {
			pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
			for (int i = 0; i < results.size(); i++) {
				pan.add(new ResultPanel(i + 1, results.get(i).getDocument().getTitle(), results.get(i).getDocument()
						.getUrl(), results.get(i).getDocument().getPath(), results.get(i).getPertinence()));
			}
		} else {
			pan.add(new JLabel("Aucun résultat"));
		}
		this.setViewportView(pan);
	}
}
