package index.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class IndexerFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 2587388120975844409L;
	private JButton buttonSave, buttonOpenStopWords, buttonOpenDirectory;
	private JFileChooser directoryChooser, stopWordsFileChooser;
	private JTextField textFieldStopWords, textFieldDirectory;
	private JComboBox listIndexerTypes, listWeigherTypes, listNormalizerTypes, listIndexTypes;

	public IndexerFrame() {
		super();
		this.setTitle("Indexer");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.getContentPane().setLayout(new BorderLayout());

		directoryChooser = new JFileChooser(".");
		stopWordsFileChooser = new JFileChooser(".");

		// Crée le premier formulaire pour la recherche du nombre de taxis
		JPanel labelPanel = new JPanel(new GridLayout(6, 1));
		JPanel fieldPanel = new JPanel(new GridLayout(6, 1));
		this.getContentPane().add(labelPanel, BorderLayout.WEST);
		this.getContentPane().add(fieldPanel, BorderLayout.CENTER);

		String[] indexerTypes = { "Indexer de texte" };
		String[] indexTypes = { "IndexHash", "IndexTree" };
		String[] weigherTypes = { "Tf.Idf", "Tf.Idf normalisé" };
		String[] normalizerTypes = { "Tokenizer", "Stemmer" };

		labelPanel.add(new JLabel("Répertoire à indexer : ", JLabel.RIGHT));
		labelPanel.add(new JLabel("Type d'indexer : ", JLabel.RIGHT));
		labelPanel.add(new JLabel("Type d'index : ", JLabel.RIGHT));
		labelPanel.add(new JLabel("Type de pondérateur : ", JLabel.RIGHT));
		labelPanel.add(new JLabel("Type de normaliseur : ", JLabel.RIGHT));
		labelPanel.add(new JLabel("Liste des mots vides (optionnel) : ", JLabel.RIGHT));

		textFieldDirectory = new JTextField();
		textFieldDirectory.setColumns(20);
		buttonOpenDirectory = new JButton("Ouvrir");
		buttonOpenDirectory.addActionListener(this);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(textFieldDirectory);
		p.add(buttonOpenDirectory);
		fieldPanel.add(p);

		listIndexerTypes = new JComboBox(indexerTypes);
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p2.add(listIndexerTypes);
		fieldPanel.add(p2);
		
		listIndexTypes = new JComboBox(indexTypes);
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p3.add(listIndexTypes);
		fieldPanel.add(p3);

		listWeigherTypes = new JComboBox(weigherTypes);
		JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p4.add(listWeigherTypes);
		fieldPanel.add(p4);
		
		listNormalizerTypes = new JComboBox(normalizerTypes);
		JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p5.add(listNormalizerTypes);
		fieldPanel.add(p5);

		textFieldStopWords = new JTextField();
		textFieldStopWords.setColumns(20);
		buttonOpenStopWords = new JButton("Ouvrir");
		buttonOpenStopWords.addActionListener(this);
		JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p6.add(textFieldStopWords);
		p6.add(buttonOpenStopWords);
		fieldPanel.add(p6);

		buttonSave = new JButton("Indexer");
		buttonSave.addActionListener(this);
		this.getContentPane().add(buttonSave, BorderLayout.SOUTH);
		this.pack();
	}

	public JButton getButtonSave() {
		return buttonSave;
	}

	public String getDirectoryPath() {
		return textFieldDirectory.getText();
	}
	
	public String getStopWordsPath() {
		return textFieldStopWords.getText();
	}
	
	public String getIndexerType()
	{
		return (String) listIndexerTypes.getSelectedItem();
	}
	
	public String getWeigherType()
	{
		return (String) listWeigherTypes.getSelectedItem();
	}

	public String getNormalizerType() {
		return (String) listNormalizerTypes.getSelectedItem();
	}

	public String getIndexType() {
		return (String) listIndexTypes.getSelectedItem();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonOpenStopWords) {
			int retval = stopWordsFileChooser.showOpenDialog(IndexerFrame.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				textFieldStopWords.setText(stopWordsFileChooser.getSelectedFile().getPath());
			}
		}
		if (e.getSource() == buttonOpenDirectory) {
			directoryChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int retval = directoryChooser.showOpenDialog(IndexerFrame.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				textFieldDirectory.setText(directoryChooser.getSelectedFile().getPath());
			}
		}
	}
}
