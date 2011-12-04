package index;

import java.io.IOException;
import java.util.ArrayList;

import tools.normalizer.Normalizer;
import tools.weigher.Weigher;

/**
 * Interface d'un indexeur g�n�rique capable de remplir un index fourni sans en
 * connaitre l�impl�mentation exacte � partir d�une liste de documents �
 * indexer, d�un normalisateur et d�un pond�rateur et de l�information sur la
 * suppression ou non des mots vides et de l'encodage � utiliser
 * prioritairement.
 */
public interface Indexer {
	/**
	 * Indexe les documents dont la liste est fournie pour remplir l'index. Le
	 * normalisateur et le pond�rateur fournis sont utilis�s et les mots vides
	 * sont �ventuellement supprim�s si la demande en est faite.
	 * 
	 * @param docsList
	 *            la liste des documents � indexer
	 * @param encoding
	 *            l'encodage des documents � indexer
	 * @param index
	 *            l'index � remplir
	 * @param normalizer
	 *            le normalisateur � utiliser
	 * @param removeStopWords
	 *            indique si les mots vides doivent �tre supprim�s par la
	 *            normalisation
	 * @param weigher
	 *            le pond�rateur � utiliser
	 * @throws IOException
	 *             si la lecture d'un document �choue
	 */
	public abstract void index(ArrayList<String> docsList, String encoding, Index index, Normalizer normalizer,
			boolean removeStopWords, Weigher weigher) throws IOException;
}
