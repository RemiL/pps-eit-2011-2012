package view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ResultPanel extends JPanel {
	private static final long serialVersionUID = 6866863524079372135L;

	public ResultPanel(String title, String url, String path, double pertinence) {
		super();
		this.setLayout(new GridLayout(4, 1));
		this.setBorder(new LineBorder(Color.BLACK));
		
		if(!title.equals(""))
			this.add(new JLabel(title));
		else
			this.add(new JLabel("PAS DE TITRE"));			
		this.add(new JLabel(url));
		this.add(new JLabel(path));
		this.add(new JLabel("Pertinence : " + pertinence));
	}
}
