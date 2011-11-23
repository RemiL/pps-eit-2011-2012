package tools.normalizer;

import java.io.IOException;
import java.util.ArrayList;

import tools.org.tartarus.snowball.ext.frenchStemmer;

/**
 * Classe implémentant un normalisateur basé sur la racinisation des mots en
 * français (cf le package SnowBall http://snowball.tartarus.org/download.php).
 */
public class FrenchStemmer extends FrenchTokenizer {
	private static short REPEAT = 1;
	/** Le raciniseur de base. */
	private frenchStemmer stemmer;

	/**
	 * Construit un normalisateur de texte basé sur la racinisation des unités
	 * lexicales.
	 */
	public FrenchStemmer() {
		super();
		stemmer = new frenchStemmer();
	}

	/**
	 * Construit un normalisateur de texte basé sur la racinisation des unités
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
	public FrenchStemmer(String stopWordFileName, String encoding) throws IOException {
		super(stopWordFileName, encoding);
		stemmer = new frenchStemmer();
	}

	/**
	 * Renvoie la liste d'unités lexicales contenues dans le texte spécifié en
	 * appliquant une racinisation des mots (avec ou sans suppression des mots
	 * vides).
	 * 
	 * @param text
	 *            le texte à normaliser
	 * @param removeStopWords
	 *            indique si les mots vides doivent être supprimés par la
	 *            normalisation
	 * @return la liste d'unités lexicales contenues dans le texte spécifié en
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
