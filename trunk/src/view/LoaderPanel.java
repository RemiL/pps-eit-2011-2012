package view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoaderPanel extends JPanel {
	private static final long serialVersionUID = 2982062364608671224L;
	JTextField indexField;
	JComboBox listSearchers;
	JButton buttonLoad;
	
	
	public LoaderPanel()
	{
		String[] searchers = { "Modèle vectoriel basique", "Modèle vectoriel par préfixe" };
		indexField = new JTextField(20);
		listSearchers = new JComboBox(searchers);
		buttonLoad = new JButton("Charger");
		this.add(new JLabel("Index : "));
		this.add(indexField);
		this.add(listSearchers);
		this.add(buttonLoad);
	}

	public JButton getButtonLoad() {
		return buttonLoad;
	}
	
	public String getSearcherType() {
		return (String) listSearchers.getSelectedItem();
	}
	
	public String getIndexPath() {
		return indexField.getText();
	}
}
