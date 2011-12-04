package index;

import java.io.IOException;
import java.util.ArrayList;

import tools.normalizer.Normalizer;
import tools.weigher.Weigher;

/**
 * Interface d'un indexeur générique capable de remplir un index fourni sans en
 * connaitre l’implémentation exacte à partir d’une liste de documents à
 * indexer, d’un normalisateur et d’un pondérateur et de l’information sur la
 * suppression ou non des mots vides et de l'encodage à utiliser
 * prioritairement.
 */
public interface Indexer {
	/**
	 * Indexe les documents dont la liste est fournie pour remplir l'index. Le
	 * normalisateur et le pondérateur fournis sont utilisés et les mots vides
	 * sont éventuellement supprimés si la demande en est faite.
	 * 
	 * @param docsList
	 *            la liste des documents à indexer
	 * @param encoding
	 *            l'encodage des documents à indexer
	 * @param index
	 *            l'index à remplir
	 * @param normalizer
	 *            le normalisateur à utiliser
	 * @param removeStopWords
	 *            indique si les mots vides doivent être supprimés par la
	 *            normalisation
	 * @param weigher
	 *            le pondérateur à utiliser
	 * @throws IOException
	 *             si la lecture d'un document échoue
	 */
	public abstract void index(ArrayList<String> docsList, String encoding, Index index, Normalizer normalizer,
			boolean removeStopWords, Weigher weigher) throws IOException;
}
