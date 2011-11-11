package tools.normalizer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe impl�mentant un normalisateur bas� sur la segmentation des mots en
 * fran�ais.
 */
public class FrenchTokenizer extends Normalizer {
	/** L'automate de segmentation */
	private FrenchTokenizerAutomaton transducer;

	/**
	 * Construit un normalisateur de texte bas� sur la segmentation des unit�s
	 * lexicales.
	 */
	public FrenchTokenizer() {
		super();
		this.transducer = new FrenchTokenizerAutomaton();
	}

	/**
	 * Construit un normalisateur de texte bas� sur la segmentation des unit�s
	 * lexicales. La liste de mots vides est initialis�e � partir du fichier
	 * fourni. Ce fichier doit contenir un mot vide par ligne.
	 * 
	 * @param stopWordFileName
	 *            le nom du fichier contenant les mots vides
	 * @param encoding
	 *            l'encodage de caract�res utilis� pour le fichier contenant les
	 *            mots vides
	 * @throws IOException
	 *             si la lecture du fichier dont le nom est fourni �choue
	 */
	public FrenchTokenizer(String stopWordFileName, String encoding)
			throws IOException {
		super();
		this.transducer = new FrenchTokenizerAutomaton();
	}

	/**
	 * Renvoie la liste d'unit�s lexicales contenues dans le texte sp�cifi� en
	 * appliquant une segmentation des mots (avec ou sans suppression des mots
	 * vides).
	 * 
	 * @param text
	 *            le texte � normaliser
	 * @param removeStopWords
	 *            indique si les mots vides doivent �tre supprim�s par la
	 *            normalisation
	 * @return la liste d'unit�s lexicales contenues dans le texte sp�cifi� en
	 *         appliquant une segmentation des mots
	 */
	public ArrayList<String> normalize(String text, boolean removeStopWords) {
		// On convertit la chaine de caract�res en minuscule
		text = text.toLowerCase();

		ArrayList<String> tokens = new ArrayList<String>();

		// Initialize the execution
		int begin = -1;
		transducer.reset();
		String word;
		// Run over the chars
		for (int i = 0; i < text.length(); i++) {
			switch (transducer.feedChar(text.charAt(i))) {
			case start_word:
				begin = i;
				break;
			case end_word:
				word = text.substring(begin, i);
				addToken(tokens, word, removeStopWords);
				begin = -1;
				break;
			case end_word_prev:
				word = text.substring(begin, i - 1);
				addToken(tokens, word, removeStopWords);
				break;
			case switch_word:
				word = text.substring(begin, i);
				addToken(tokens, word, removeStopWords);
				begin = i;
				break;
			case switch_word_prev:
				word = text.substring(begin, i - 1);
				addToken(tokens, word, removeStopWords);
				begin = i;
				break;
			case cancel_word:
				begin = -1;
				break;
			}
		}
		// Add the last one
		if (begin != -1) {
			word = text.substring(begin, text.length());
			addToken(tokens, word, removeStopWords);
		}

		return tokens;
	}

	/**
	 * Ajoute le mot pass� en param�tre � la liste fournie. Si les mots vides
	 * doivent �tre ignor�s et si le mot fourni est un mot vide, il n'est pas
	 * ajout� et la liste n'est pas modifi�e.
	 * 
	 * @param list
	 *            la liste � laquelle ajouter le mot
	 * @param token
	 *            le mot � ajouter
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent �tre ignor�s
	 */
	private void addToken(ArrayList<String> list, String token,
			boolean ignoreStopWords) {
		if (!ignoreStopWords || !isStopWord(token)) {
			list.add(token);
		}
	}
}
