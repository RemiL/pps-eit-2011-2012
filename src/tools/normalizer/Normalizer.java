package tools.normalizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Classe d�finissant un normalisateur abstrait de texte capable de g�rer la
 * suppression des mots vides.
 */
public abstract class Normalizer {
	/** Liste des mots vides */
	private HashSet<String> stopWords;

	/**
	 * Construit un normalisateur de texte dont la liste de mots vides est vide.
	 */
	public Normalizer() {
		stopWords = new HashSet<String>();
	}

	/**
	 * Construit un normalisateur de texte dont la liste de mots vides est
	 * initialis�e � partir du fichier fourni. Ce fichier doit contenir un mot
	 * vide par ligne.
	 * 
	 * @param stopWordFileName
	 *            le nom du fichier contenant les mots vides
	 * @param encoding
	 *            l'encodage de caract�res utilis� pour le fichier contenant les
	 *            mots vides
	 * @throws IOException
	 *             si la lecture du fichier dont le nom est fourni �choue
	 */
	public Normalizer(String stopWordFileName, String encoding) throws IOException {
		stopWords = new HashSet<String>();

		// lecture du fichier texte
		InputStream ips = new FileInputStream(stopWordFileName);
		InputStreamReader ipsr = new InputStreamReader(ips, encoding);
		BufferedReader br = new BufferedReader(ipsr);
		String line;
		while ((line = br.readLine()) != null) {
			this.stopWords.add(line);
		}
		br.close();
		ipsr.close();
		ips.close();
	}

	/**
	 * Teste si un mot est un mot vide.
	 * 
	 * @param word
	 *            le mot � tester
	 * @return vrai si et seulement si le mot fourni est un mot vide, sinon faux
	 */
	protected boolean isStopWord(String word) {
		return stopWords.contains(word);
	}

	/**
	 * Renvoie la liste d'unit�s lexicales contenues dans le texte sp�cifi� en
	 * appliquant une normalisation.
	 * 
	 * @param text
	 *            le texte � normaliser
	 * @param removeStopWords
	 *            indique si les mots vides doivent �tre supprim�s par la
	 *            normalisation
	 * @return la liste d'unit�s lexicales contenues dans le texte sp�cifi� en
	 *         appliquant une normalisation
	 */
	public abstract ArrayList<String> normalize(String text, boolean removeStopWords);
}
