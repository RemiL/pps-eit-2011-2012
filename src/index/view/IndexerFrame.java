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

/**
 * Un affichage pour l'indexer
 */
public class IndexerFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 2587388120975844409L;

	private JButton buttonSave, buttonOpenStopWords, buttonOpenDirectory;
	private JFileChooser directoryChooser, stopWordsFileChooser;
	private JTextField textFieldStopWords, textFieldDirectory;
	private JComboBox listIndexerTypes, listWeigherTypes, listNormalizerTypes, listIndexTypes;

	/**
	 * Cr�e une fen�tre pour l'indexer
	 */
	public IndexerFrame() {
		super();
		this.setTitle("Indexer");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		this.getContentPane().setLayout(new BorderLayout());

		// Positionne le choix du r�pertoire � indexer et du fichier des mots
		// vides sur le r�pertoire courant
		directoryChooser = new JFileChooser(".");
		stopWordsFileChooser = new JFileChooser(".");

		// Cr�e le formulaire pour param�trer l'indexer
		JPanel labelPanel = new JPanel(new GridLayout(6, 1));
		JPanel fieldPanel = new JPanel(new GridLayout(6, 1));
		this.getContentPane().add(labelPanel, BorderLayout.WEST);
		this.getContentPane().add(fieldPanel, BorderLayout.CENTER);

		String[] indexerTypes = { "Indexer de texte" };
		String[] indexTypes = { "IndexHash", "IndexTree" };
		String[] weigherTypes = { "Tf.Idf", "Tf.Idf normalis�" };
		String[] normalizerTypes = { "Tokenizer", "Stemmer" };

		labelPanel.add(new JLabel("R�pertoire � indexer : ", JLabel.RIGHT));
		labelPanel.add(new JLabel("Type d'indexer : ", JLabel.RIGHT));
		labelPanel.add(new JLabel("Type d'index : ", JLabel.RIGHT));
		labelPanel.add(new JLabel("Type de pond�rateur : ", JLabel.RIGHT));
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

	/**
	 * Retourne le bouton de sauvegarde
	 * @return le bouton de sauvegarde
	 */
	public JButton getButtonSave() {
		return buttonSave;
	}

	/**
	 * Retourne le chemin du r�pertoire � indexer
	 * @return le chemin du r�pertoire � indexer
	 */
	public String getDirectoryPath() {
		return textFieldDirectory.getText();
	}

	/**
	 * Retourne le chemin du fichier des mots vides
	 * @return le chemin du fichier des mots vides
	 */
	public String getStopWordsPath() {
		return textFieldStopWords.getText();
	}

	/**
	 * Retourne le type d'indexer
	 * @return le type d'indexer
	 */
	public String getIndexerType() {
		return (String) listIndexerTypes.getSelectedItem();
	}

	/**
	 * Retourne le type de pond�rateur
	 * @return le type de pond�rateur
	 */
	public String getWeigherType() {
		return (String) listWeigherTypes.getSelectedItem();
	}

	/**
	 * Retourne le type de normaliseur
	 * @return le type de normaliseur
	 */
	public String getNormalizerType() {
		return (String) listNormalizerTypes.getSelectedItem();
	}

	/**
	 * Retourne le type d'index
	 * @return le type d'index
	 */
	public String getIndexType() {
		return (String) listIndexTypes.getSelectedItem();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Si le bouton pour ouvrir le fichier des mots vides est cliqu�
		if (e.getSource() == buttonOpenStopWords) {
			// Ouvre une boite de dialogue pour choisir le fichier
			int retval = stopWordsFileChooser.showOpenDialog(IndexerFrame.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				// Ecrit le chemin du fichier dans la zone de texte correspondante
				textFieldStopWords.setText(stopWordsFileChooser.getSelectedFile().getPath());
			}
		}
		// Si le bouton pour ouvrir le r�pertoire � indexer est cliqu�
		if (e.getSource() == buttonOpenDirectory) {
			// Ouvre une boite de dialogue pour choisir le fichier ou le r�pertoire
			directoryChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int retval = directoryChooser.showOpenDialog(IndexerFrame.this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				// Ecrit le chemin du fichier dans la zone de texte correspondante
				textFieldDirectory.setText(directoryChooser.getSelectedFile().getPath());
			}
		}
	}
}
