package view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FormPanel extends JPanel {
	private static final long serialVersionUID = 5530102639867570180L;
	private JTextField field;
	private JButton buttonSearch;

	public FormPanel() {
		super();
		this.setPreferredSize(new Dimension(800, 60));
		field = new JTextField();
		field.setPreferredSize(new Dimension(500, 40));
		buttonSearch = new JButton("Chercher");
		this.add(field);
		this.add(buttonSearch);
	}

	public JButton getButtonSearch() {
		return buttonSearch;
	}

	public String getRequest() {
		return field.getText();
	}
}
