package tools.normalizer;

import java.io.IOException;
import java.util.ArrayList;

import tools.org.tartarus.snowball.ext.frenchStemmer;

/**
 * Classe impl�mentant un normalisateur bas� sur la racinisation des mots en
 * fran�ais (cf le package SnowBall http://snowball.tartarus.org/download.php).
 */
public class FrenchStemmer extends FrenchTokenizer {
	private static short REPEAT = 1;
	/** Le raciniseur de base. */
	private frenchStemmer stemmer;

	/**
	 * Construit un normalisateur de texte bas� sur la racinisation des unit�s
	 * lexicales.
	 */
	public FrenchStemmer() {
		super();
		stemmer = new frenchStemmer();
	}

	/**
	 * Construit un normalisateur de texte bas� sur la racinisation des unit�s
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
	public FrenchStemmer(String stopWordFileName, String encoding) throws IOException {
		super(stopWordFileName, encoding);
		stemmer = new frenchStemmer();
	}

	/**
	 * Renvoie la liste d'unit�s lexicales contenues dans le texte sp�cifi� en
	 * appliquant une racinisation des mots (avec ou sans suppression des mots
	 * vides).
	 * 
	 * @param text
	 *            le texte � normaliser
	 * @param removeStopWords
	 *            indique si les mots vides doivent �tre supprim�s par la
	 *            normalisation
	 * @return la liste d'unit�s lexicales contenues dans le texte sp�cifi� en
	 *         appliquant une racinisation des mots
	 */
	public ArrayList<String> normalize(String text, boolean removeStopWords) {
		// On segmente le texte
		ArrayList<String> words = super.normalize(text, removeStopWords);

		// puis on racinise.
		ArrayList<String> result = new ArrayList<String>();
		for (String word : words) {
			stemmer.setCurrent(word);
			for (int i = REPEAT; i != 0; i--) {
				stemmer.stem();
			}
			result.add(stemmer.getCurrent());
		}

		return result;
	}
}
