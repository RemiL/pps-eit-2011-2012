package index;

import java.io.IOException;
import java.util.ArrayList;

import tools.normalizer.Normalizer;
import tools.ponderateur.Ponderateur;

// TODO : transformer cette classe en interface ?
public abstract class Indexer {
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
	 * @param ponderateur
	 *            le pond�rateur � utiliser
	 * @throws IOException
	 *             si la lecture d'un document �choue
	 */
	public abstract void index(ArrayList<String> docsList, String encoding,
			Index index, Normalizer normalizer, boolean removeStopWords,
			Ponderateur ponderateur) throws IOException;
}
