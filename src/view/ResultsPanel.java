package view;

import java.util.LinkedList;

import javax.swing.BoxLayout;
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
		pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
		for (Result result : results) {
			pan.add(new ResultPanel(result.getDocument().getTitle(), result.getDocument().getUrl(), result.getDocument().getPath(), result.getPertinence()));
		}
		this.setViewportView(pan);
	}
}
