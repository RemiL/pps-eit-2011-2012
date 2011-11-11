package tools.normalizer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe implémentant un normalisateur basé sur la segmentation des mots en
 * français.
 */
public class FrenchTokenizer extends Normalizer {
	/** L'automate de segmentation */
	private FrenchTokenizerAutomaton transducer;

	/**
	 * Construit un normalisateur de texte basé sur la segmentation des unités
	 * lexicales.
	 */
	public FrenchTokenizer() {
		super();
		this.transducer = new FrenchTokenizerAutomaton();
	}

	/**
	 * Construit un normalisateur de texte basé sur la segmentation des unités
	 * lexicales. La liste de mots vides est initialisée à partir du fichier
	 * fourni. Ce fichier doit contenir un mot vide par ligne.
	 * 
	 * @param stopWordFileName
	 *            le nom du fichier contenant les mots vides
	 * @param encoding
	 *            l'encodage de caractères utilisé pour le fichier contenant les
	 *            mots vides
	 * @throws IOException
	 *             si la lecture du fichier dont le nom est fourni échoue
	 */
	public FrenchTokenizer(String stopWordFileName, String encoding)
			throws IOException {
		super();
		this.transducer = new FrenchTokenizerAutomaton();
	}

	/**
	 * Renvoie la liste d'unités lexicales contenues dans le texte spécifié en
	 * appliquant une segmentation des mots (avec ou sans suppression des mots
	 * vides).
	 * 
	 * @param text
	 *            le texte à normaliser
	 * @param removeStopWords
	 *            indique si les mots vides doivent être supprimés par la
	 *            normalisation
	 * @return la liste d'unités lexicales contenues dans le texte spécifié en
	 *         appliquant une segmentation des mots
	 */
	public ArrayList<String> normalize(String text, boolean removeStopWords) {
		// On convertit la chaine de caractères en minuscule
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
	 * Ajoute le mot passé en paramètre à la liste fournie. Si les mots vides
	 * doivent être ignorés et si le mot fourni est un mot vide, il n'est pas
	 * ajouté et la liste n'est pas modifiée.
	 * 
	 * @param list
	 *            la liste à laquelle ajouter le mot
	 * @param token
	 *            le mot à ajouter
	 * @param ignoreStopWords
	 *            indique si les mots vides doivent être ignorés
	 */
	private void addToken(ArrayList<String> list, String token,
			boolean ignoreStopWords) {
		if (!ignoreStopWords || !isStopWord(token)) {
			list.add(token);
		}
	}
}
