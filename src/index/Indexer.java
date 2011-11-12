package index;

import java.io.IOException;
import java.util.ArrayList;

import tools.normalizer.Normalizer;
import tools.ponderateur.Ponderateur;

// TODO : transformer cette classe en interface ?
public abstract class Indexer {
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
	 * @param ponderateur
	 *            le pondérateur à utiliser
	 * @throws IOException
	 *             si la lecture d'un document échoue
	 */
	public abstract void index(ArrayList<String> docsList, String encoding,
			Index index, Normalizer normalizer, boolean removeStopWords,
			Ponderateur ponderateur) throws IOException;
}
